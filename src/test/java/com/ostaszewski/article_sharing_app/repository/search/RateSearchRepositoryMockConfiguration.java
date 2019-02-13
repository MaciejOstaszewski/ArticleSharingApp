package com.ostaszewski.article_sharing_app.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of RateSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class RateSearchRepositoryMockConfiguration {

    @MockBean
    private RateSearchRepository mockRateSearchRepository;

}
