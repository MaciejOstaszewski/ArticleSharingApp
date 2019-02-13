package com.ostaszewski.article_sharing_app.repository.search;

import com.ostaszewski.article_sharing_app.domain.Comment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Comment entity.
 */
public interface CommentSearchRepository extends ElasticsearchRepository<Comment, Long> {
}
