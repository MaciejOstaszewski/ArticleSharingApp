package com.ostaszewski.article_sharing_app.service;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ostaszewski.article_sharing_app.domain.*;
import com.ostaszewski.article_sharing_app.repository.*;
import com.ostaszewski.article_sharing_app.repository.search.*;
import org.elasticsearch.ResourceAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.ManyToMany;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ElasticsearchIndexService {

    private static final Lock reindexLock = new ReentrantLock();

    private final Logger log = LoggerFactory.getLogger(ElasticsearchIndexService.class);

    private final ArticleRepository articleRepository;

    private final ArticleSearchRepository articleSearchRepository;

    private final CategoryRepository categoryRepository;

    private final CategorySearchRepository categorySearchRepository;

    private final CommentRepository commentRepository;

    private final CommentSearchRepository commentSearchRepository;

    private final InterestRepository interestRepository;

    private final InterestSearchRepository interestSearchRepository;

    private final MessageRepository messageRepository;

    private final MessageSearchRepository messageSearchRepository;

    private final RateRepository rateRepository;

    private final RateSearchRepository rateSearchRepository;

    private final TagRepository tagRepository;

    private final TagSearchRepository tagSearchRepository;

    private final UserRepository userRepository;

    private final UserSearchRepository userSearchRepository;

    private final ElasticsearchTemplate elasticsearchTemplate;

    public ElasticsearchIndexService(
        UserRepository userRepository,
        UserSearchRepository userSearchRepository,
        ArticleRepository articleRepository,
        ArticleSearchRepository articleSearchRepository,
        CategoryRepository categoryRepository,
        CategorySearchRepository categorySearchRepository,
        CommentRepository commentRepository,
        CommentSearchRepository commentSearchRepository,
        InterestRepository interestRepository,
        InterestSearchRepository interestSearchRepository,
        MessageRepository messageRepository,
        MessageSearchRepository messageSearchRepository,
        RateRepository rateRepository,
        RateSearchRepository rateSearchRepository,
        TagRepository tagRepository,
        TagSearchRepository tagSearchRepository,
        ElasticsearchTemplate elasticsearchTemplate) {
        this.userRepository = userRepository;
        this.userSearchRepository = userSearchRepository;
        this.articleRepository = articleRepository;
        this.articleSearchRepository = articleSearchRepository;
        this.categoryRepository = categoryRepository;
        this.categorySearchRepository = categorySearchRepository;
        this.commentRepository = commentRepository;
        this.commentSearchRepository = commentSearchRepository;
        this.interestRepository = interestRepository;
        this.interestSearchRepository = interestSearchRepository;
        this.messageRepository = messageRepository;
        this.messageSearchRepository = messageSearchRepository;
        this.rateRepository = rateRepository;
        this.rateSearchRepository = rateSearchRepository;
        this.tagRepository = tagRepository;
        this.tagSearchRepository = tagSearchRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Async
    @Timed
    public void reindexAll() {
        if (reindexLock.tryLock()) {
            try {
                reindexForClass(Article.class, articleRepository, articleSearchRepository);
                reindexForClass(Category.class, categoryRepository, categorySearchRepository);
                reindexForClass(Comment.class, commentRepository, commentSearchRepository);
                reindexForClass(Interest.class, interestRepository, interestSearchRepository);
                reindexForClass(Message.class, messageRepository, messageSearchRepository);
                reindexForClass(Rate.class, rateRepository, rateSearchRepository);
                reindexForClass(Tag.class, tagRepository, tagSearchRepository);
                reindexForClass(User.class, userRepository, userSearchRepository);

                log.info("Elasticsearch: Successfully performed reindexing");
            } finally {
                reindexLock.unlock();
            }
        } else {
            log.info("Elasticsearch: concurrent reindexing attempt");
        }
    }

    @SuppressWarnings("unchecked")
    private <T, ID extends Serializable> void reindexForClass(Class<T> entityClass, JpaRepository<T, ID> jpaRepository,
                                                              ElasticsearchRepository<T, ID> elasticsearchRepository) {
        elasticsearchTemplate.deleteIndex(entityClass);
        try {
            elasticsearchTemplate.createIndex(entityClass);
        } catch (ResourceAlreadyExistsException e) {
            // Do nothing. Index was already concurrently recreated by some other service.
        }
        elasticsearchTemplate.putMapping(entityClass);
        if (jpaRepository.count() > 0) {
            // if a JHipster entity field is the owner side of a many-to-many relationship, it should be loaded manually
            List<Method> relationshipGetters = Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> field.getType().equals(Set.class))
                .filter(field -> field.getAnnotation(ManyToMany.class) != null)
                .filter(field -> field.getAnnotation(ManyToMany.class).mappedBy().isEmpty())
                .filter(field -> field.getAnnotation(JsonIgnore.class) == null)
                .map(field -> {
                    try {
                        return new PropertyDescriptor(field.getName(), entityClass).getReadMethod();
                    } catch (IntrospectionException e) {
                        log.error("Error retrieving getter for class {}, field {}. Field will NOT be indexed",
                            entityClass.getSimpleName(), field.getName(), e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            int size = 100;
            for (int i = 0; i <= jpaRepository.count() / size; i++) {
                Pageable page = new PageRequest(i, size);
                log.info("Indexing page {} of {}, size {}", i, jpaRepository.count() / size, size);
                Page<T> results = jpaRepository.findAll(page);
                results.map(result -> {
                    // if there are any relationships to load, do it now
                    relationshipGetters.forEach(method -> {
                        try {
                            // eagerly load the relationship set
                            ((Set) method.invoke(result)).size();
                        } catch (Exception ex) {
                            log.error(ex.getMessage());
                        }
                    });
                    return result;
                });
                elasticsearchRepository.saveAll(results.getContent());
            }
        }
        log.info("Elasticsearch: Indexed all rows for {}", entityClass.getSimpleName());
    }
}
