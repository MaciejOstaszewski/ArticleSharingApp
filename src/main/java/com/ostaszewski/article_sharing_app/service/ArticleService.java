package com.ostaszewski.article_sharing_app.service;

import com.ostaszewski.article_sharing_app.domain.Article;
import com.ostaszewski.article_sharing_app.domain.Interest;
import com.ostaszewski.article_sharing_app.domain.Rate;
import com.ostaszewski.article_sharing_app.domain.User;
import com.ostaszewski.article_sharing_app.repository.ArticleRepository;
import com.ostaszewski.article_sharing_app.repository.RateRepository;
import com.ostaszewski.article_sharing_app.repository.UserRepository;
import com.ostaszewski.article_sharing_app.repository.search.ArticleSearchRepository;
import com.ostaszewski.article_sharing_app.security.SecurityUtils;
import com.ostaszewski.article_sharing_app.service.dto.ArticleDTO;
import com.ostaszewski.article_sharing_app.service.dto.AverageRatingDTO;
import com.ostaszewski.article_sharing_app.service.dto.RateDTO;
import com.ostaszewski.article_sharing_app.service.mapper.ArticleMapper;
import com.ostaszewski.article_sharing_app.service.mapper.RateMapper;
import com.ostaszewski.article_sharing_app.service.util.ImageUtil;
import com.ostaszewski.article_sharing_app.web.rest.errors.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Article.
 */

@RequiredArgsConstructor
@Service
@Transactional
public class ArticleService {

    private final Logger log = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleRepository articleRepository;

    private final ArticleMapper articleMapper;

    private final ArticleSearchRepository articleSearchRepository;

    private final RateRepository rateRepository;

    private final RateMapper rateMapper;

    private final UserRepository userRepository;

    /**
     * Save a article.
     *
     * @param articleDTO the entity to save
     * @return the persisted entity
     */
    public ArticleDTO save(ArticleDTO articleDTO) {
        log.debug("Request to save Article : {}", articleDTO);
        Article article = articleMapper.toEntity(articleDTO);
        article.setImageURL(ImageUtil.prepareImage(article.getImageURL()));
        article.setCreationDate(Instant.now());
        article.setModificationDate(Instant.now());
        article.setUser(userRepository.findOneByLogin(
            SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new EntityNotFoundException("Unauthenticated")))
                .orElseThrow(() -> new EntityNotFoundException("No such user")));
        article.setViews(0);
        article.setActive(false);
        article = articleRepository.save(article);
        ArticleDTO result = articleMapper.toDto(article);

        return result;
    }

    /**
     * Get all the articles.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ArticleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Articles");
        return articleRepository.findAll(pageable)
            .map(articleMapper::toDto);
    }

    /**
     * Get all the Article with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<ArticleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return articleRepository.findAllWithEagerRelationships(pageable).map(articleMapper::toDto);
    }


    /**
     * Get one article by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ArticleDTO> findOne(Long id) {
        log.debug("Request to get Article : {}", id);
        return articleRepository.findOneWithEagerRelationships(id)
            .map(articleMapper::toDto);
    }

    /**
     * Delete the article by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Article : {}", id);
        articleRepository.findById(id).ifPresent(article -> {
            articleRepository.deleteById(id);
            articleSearchRepository.deleteById(id);
        });
    }

    /**
     * Search for the article corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ArticleDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Articles for query {}", query);
        return articleSearchRepository.search(queryStringQuery(query), pageable)
            .map(articleMapper::toDto);
    }

    public void updateViews(Long id) {
        articleRepository.findById(id).ifPresent(article -> {
            article.setViews(article.getViews() + 1);
            articleRepository.save(article);
        });
    }

    public void updateModificationDate(Long id) {
        articleRepository.findById(id).ifPresent(article -> {
            article.setModificationDate(Instant.now());
            articleRepository.save(article);
        });

    }

    public void addRating(RateDTO rateDTO) {
        Rate rate = rateMapper.toEntity(rateDTO);
        Article article = articleRepository.findById(rateDTO.getArticleId()).orElseThrow(() -> new EntityNotFoundException("No such article"));
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).orElseThrow(() -> new EntityNotFoundException("No such user"));
        rate.setUser(user);
        rate.setArticle(article);
        rateRepository.save(rate);
    }

    public Optional<RateDTO> getUserRating(Long articleId, String userLogin) {
        return rateRepository.findByArticleIdAndUserLogin(articleId, userLogin)
            .map(rateMapper::toDto);
    }

    public Optional<AverageRatingDTO> getAverageRate(Long articleId) {
        return rateRepository.getAverageArticleRating(articleId);
    }


    @Transactional(readOnly = true)
    public Page<ArticleDTO> findAll(Pageable pageable, Boolean active) {
        log.debug("Request to get all Articles");
        return articleRepository.findAllByActiveOrderByModificationDateDesc(pageable, active).map(articleMapper::toDto);

    }


    public Page<ArticleDTO> findAllWithEagerRelationships(Pageable pageable, Boolean active) {
        return articleRepository.findAllWithEagerRelationships(pageable, active).map(articleMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ArticleDTO> findAllByCategory(Pageable pageable, Boolean active, Long id) {
        log.debug("Request to get all Articles");
        return articleRepository.findAllByCategory(pageable, active, id).map(articleMapper::toDto);

    }


    public Page<ArticleDTO> findAllWithEagerRelationshipsByCategory(Pageable pageable, Boolean active, Long id) {
        return articleRepository.findAllWithEagerRelationshipsByCategory(pageable, active, id).map(articleMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ArticleDTO> findAllByTag(Pageable pageable, Boolean active, Long id) {
        log.debug("Request to get all Articles");
        return articleRepository.findAllByTag(pageable, active, id).map(articleMapper::toDto);

    }


    public Page<ArticleDTO> findAllWithEagerRelationshipsByTag(Pageable pageable, Boolean active, Long id) {
        return articleRepository.findAllWithEagerRelationshipsByTag(pageable, active, id).map(articleMapper::toDto);
    }

    @Transactional
    public void activateArticle(Long id) {
        if (articleRepository.findById(id).isPresent()) {
         //   articleRepository.activateArticle(id);
            Article article = articleRepository.getOne(id);
            article.setActive(true);
            articleRepository.save(article);
            articleSearchRepository.save(article);
        } else throw new EntityNotFoundException("No such article");

    }

    @Transactional(readOnly = true)
    public List<ArticleDTO> getSuggestedArticles(Long id) {
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).orElseThrow(() -> new EntityNotFoundException("No such user"));
        return articleMapper.toDto(articleRepository.findSuggestedArticles(user.getInterests(), id)
            .stream().limit(9).collect(Collectors.toList()));
    }

}
