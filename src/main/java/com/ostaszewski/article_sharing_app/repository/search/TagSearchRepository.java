package com.ostaszewski.article_sharing_app.repository.search;

import com.ostaszewski.article_sharing_app.domain.Tag;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Tag entity.
 */
public interface TagSearchRepository extends ElasticsearchRepository<Tag, Long> {
}
