package com.ostaszewski.article_sharing_app.repository.search;

import com.ostaszewski.article_sharing_app.domain.Rate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Rate entity.
 */
public interface RateSearchRepository extends ElasticsearchRepository<Rate, Long> {
}
