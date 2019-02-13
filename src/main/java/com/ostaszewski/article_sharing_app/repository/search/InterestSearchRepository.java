package com.ostaszewski.article_sharing_app.repository.search;

import com.ostaszewski.article_sharing_app.domain.Interest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Interest entity.
 */
public interface InterestSearchRepository extends ElasticsearchRepository<Interest, Long> {
}
