package com.ostaszewski.article_sharing_app.repository.search;

import com.ostaszewski.article_sharing_app.domain.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Article entity.
 */
public interface ArticleSearchRepository extends ElasticsearchRepository<Article, Long> {
}
