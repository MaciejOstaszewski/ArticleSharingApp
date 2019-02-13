package com.ostaszewski.article_sharing_app.repository.search;

import com.ostaszewski.article_sharing_app.domain.Message;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Message entity.
 */
public interface MessageSearchRepository extends ElasticsearchRepository<Message, Long> {
}
