package com.ostaszewski.article_sharing_app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ostaszewski.article_sharing_app.security.AuthoritiesConstants;
import com.ostaszewski.article_sharing_app.service.ArticleService;
import com.ostaszewski.article_sharing_app.service.dto.AverageRatingDTO;
import com.ostaszewski.article_sharing_app.service.dto.RateDTO;
import com.ostaszewski.article_sharing_app.web.rest.errors.BadRequestAlertException;
import com.ostaszewski.article_sharing_app.web.rest.util.HeaderUtil;
import com.ostaszewski.article_sharing_app.web.rest.util.PaginationUtil;
import com.ostaszewski.article_sharing_app.service.dto.ArticleDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Article.
 */
@RestController
@RequestMapping("/api")
public class ArticleResource {

    private final Logger log = LoggerFactory.getLogger(ArticleResource.class);

    private static final String ENTITY_NAME = "article";

    private final ArticleService articleService;

    public ArticleResource(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * POST  /articles : Create a new article.
     *
     * @param articleDTO the articleDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new articleDTO, or with status 400 (Bad Request) if the article has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/articles")
    @Timed
    public ResponseEntity<ArticleDTO> createArticle(@Valid @RequestBody ArticleDTO articleDTO) throws URISyntaxException {
        log.debug("REST request to save Article : {}", articleDTO);
        if (articleDTO.getId() != null) {
            throw new BadRequestAlertException("A new article cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ArticleDTO result = articleService.save(articleDTO);
        return ResponseEntity.created(new URI("/api/articles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /articles : Updates an existing article.
     *
     * @param articleDTO the articleDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated articleDTO,
     * or with status 400 (Bad Request) if the articleDTO is not valid,
     * or with status 500 (Internal Server Error) if the articleDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/articles")
    @Timed
    public ResponseEntity<ArticleDTO> updateArticle(@Valid @RequestBody ArticleDTO articleDTO) throws URISyntaxException {
        log.debug("REST request to update Article : {}", articleDTO);
        if (articleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ArticleDTO result = articleService.save(articleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, articleDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /articles : get all the articles.
     *
     * @param pageable  the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of articles in body
     */
    @GetMapping("/articles")
    @Timed
    public ResponseEntity<List<ArticleDTO>> getAllArticles(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Articles");
        Page<ArticleDTO> page;
        if (eagerload) {
            page = articleService.findAllWithEagerRelationships(pageable);
        } else {
            page = articleService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/articles?eagerload=%b", eagerload));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/articles/all/{active}")
    @Timed
    public ResponseEntity<List<ArticleDTO>> getArticleList(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload, @PathVariable Boolean active) {
        log.debug("REST request to get a page of Articles");
        Page<ArticleDTO> page;
        if (eagerload) {
            page = articleService.findAllWithEagerRelationships(pageable, active);
        } else {
            page = articleService.findAll(pageable, active);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/articles?eagerload=%b", eagerload));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/articles/category/{categoryId}/{active}")
    @Timed
    public ResponseEntity<List<ArticleDTO>> getArticleListByCategory(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload,
                                                                     @PathVariable Boolean active, @PathVariable Long categoryId) {
        log.debug("REST request to get a page of Articles by category");
        Page<ArticleDTO> page;
        if (eagerload) {
            page = articleService.findAllWithEagerRelationshipsByCategory(pageable, active, categoryId);
        } else {
            page = articleService.findAllByCategory(pageable, active, categoryId);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/articles?eagerload=%b", eagerload));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/articles/tag/{tagId}/{active}")
    @Timed
    public ResponseEntity<List<ArticleDTO>> getArticleListByTag(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload,
                                                                @PathVariable Boolean active, @PathVariable Long tagId) {
        log.debug("REST request to get a page of Articles by tag");
        Page<ArticleDTO> page;
        if (eagerload) {
            page = articleService.findAllWithEagerRelationshipsByTag(pageable, active, tagId);
        } else {
            page = articleService.findAllByTag(pageable, active, tagId);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/articles?eagerload=%b", eagerload));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * GET  /articles/:id : get the "id" article.
     *
     * @param id the id of the articleDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the articleDTO, or with status 404 (Not Found)
     */
    @GetMapping("/articles/{id}")
    @Timed
    public ResponseEntity<ArticleDTO> getArticle(@PathVariable Long id) {
        log.debug("REST request to get Article : {}", id);
        Optional<ArticleDTO> articleDTO = articleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(articleDTO);
    }

    /**
     * DELETE  /articles/:id : delete the "id" article.
     *
     * @param id the id of the articleDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/articles/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        log.debug("REST request to delete Article : {}", id);
        articleService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
        //return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/articles?query=:query : search for the article corresponding
     * to the query.
     *
     * @param query    the query of the article search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/articles")
    @Timed
    public ResponseEntity<List<ArticleDTO>> searchArticles(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Articles for query {}", query);
        Page<ArticleDTO> page = articleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/articles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/articles/{id}/views")
    public void updateArticleViews(@PathVariable Long id) {
        articleService.updateViews(id);
    }

    @GetMapping("/articles/{id}/modification-date")
    public void updateArticleModificationDate(@PathVariable Long id) {
        articleService.updateModificationDate(id);
    }

    @PostMapping("/articles/rate")
    @Timed
    public ResponseEntity<Void> addRating(@Valid @RequestBody RateDTO rateDTO) {
        log.debug("REST request to add article rating");
        articleService.addRating(rateDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/articles/rate/{articleId}/{userLogin}")
    @Timed
    public ResponseEntity<RateDTO> getRating(@PathVariable Long articleId, @PathVariable String userLogin) {
        log.debug("REST request to get rating article: {}", articleId);
        return ResponseUtil.wrapOrNotFound(articleService.getUserRating(articleId, userLogin));
    }


    @GetMapping("/articles/rate/average/{articleId}")
    @Timed
    public ResponseEntity<AverageRatingDTO> getAverageRating(@PathVariable Long articleId) {
        log.debug("REST request to get average rating article: {}", articleId);
        return ResponseUtil.wrapOrNotFound(articleService.getAverageRate(articleId));
    }

    @GetMapping("/articles/activate/{articleId}")
    public ResponseEntity<Void> activateArticle(@PathVariable Long articleId) {
        log.debug("REST request to activate article: {}", articleId);
        articleService.activateArticle(articleId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/articles/suggested/{articleId}")
    @Timed
    public ResponseEntity<List<ArticleDTO>> getSuggestedArticles(@PathVariable Long articleId) {
        log.debug("REST request to get suggested articles");
        return new ResponseEntity<>(articleService.getSuggestedArticles(articleId), HttpStatus.OK);
    }
}
