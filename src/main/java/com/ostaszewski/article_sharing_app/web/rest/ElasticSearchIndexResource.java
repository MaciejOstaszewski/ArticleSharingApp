package com.ostaszewski.article_sharing_app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ostaszewski.article_sharing_app.security.AuthoritiesConstants;
import com.ostaszewski.article_sharing_app.security.SecurityUtils;
import com.ostaszewski.article_sharing_app.service.ElasticsearchIndexService;
import com.ostaszewski.article_sharing_app.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

/**
 * REST controller for managing Elasticsearch index.
 */
@RestController
@RequestMapping("/api")
public class ElasticSearchIndexResource {

    private final Logger log = LoggerFactory.getLogger(ElasticSearchIndexResource.class);

    private final ElasticsearchIndexService elasticsearchIndexService;

    public ElasticSearchIndexResource(ElasticsearchIndexService elasticsearchIndexService) {
        this.elasticsearchIndexService = elasticsearchIndexService;
    }

    /**
     * POST  /elasticsearch/index -> Reindex all Elasticsearch documents
     */
    @PostMapping("/elasticsearch/index")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> reindexAll() throws URISyntaxException {
        log.info("REST request to reindex Elasticsearch by user : {}", SecurityUtils.getCurrentUserLogin());
        elasticsearchIndexService.reindexAll();
        return ResponseEntity.accepted()
            .headers(HeaderUtil.createAlert("elasticsearch.reindex.accepted", null))
            .build();
    }
}
