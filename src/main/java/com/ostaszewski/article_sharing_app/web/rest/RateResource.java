package com.ostaszewski.article_sharing_app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ostaszewski.article_sharing_app.domain.Rate;
import com.ostaszewski.article_sharing_app.repository.RateRepository;
import com.ostaszewski.article_sharing_app.repository.search.RateSearchRepository;
import com.ostaszewski.article_sharing_app.web.rest.errors.BadRequestAlertException;
import com.ostaszewski.article_sharing_app.web.rest.util.HeaderUtil;
import com.ostaszewski.article_sharing_app.service.dto.RateDTO;
import com.ostaszewski.article_sharing_app.service.mapper.RateMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Rate.
 */
@RestController
@RequestMapping("/api")
public class RateResource {

    private final Logger log = LoggerFactory.getLogger(RateResource.class);

    private static final String ENTITY_NAME = "rate";

    private final RateRepository rateRepository;

    private final RateMapper rateMapper;

    private final RateSearchRepository rateSearchRepository;

    public RateResource(RateRepository rateRepository, RateMapper rateMapper, RateSearchRepository rateSearchRepository) {
        this.rateRepository = rateRepository;
        this.rateMapper = rateMapper;
        this.rateSearchRepository = rateSearchRepository;
    }

    /**
     * POST  /rates : Create a new rate.
     *
     * @param rateDTO the rateDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rateDTO, or with status 400 (Bad Request) if the rate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rates")
    @Timed
    public ResponseEntity<RateDTO> createRate(@RequestBody RateDTO rateDTO) throws URISyntaxException {
        log.debug("REST request to save Rate : {}", rateDTO);
        if (rateDTO.getId() != null) {
            throw new BadRequestAlertException("A new rate cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Rate rate = rateMapper.toEntity(rateDTO);
        rate = rateRepository.save(rate);
        RateDTO result = rateMapper.toDto(rate);
        rateSearchRepository.save(rate);
        return ResponseEntity.created(new URI("/api/rates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rates : Updates an existing rate.
     *
     * @param rateDTO the rateDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rateDTO,
     * or with status 400 (Bad Request) if the rateDTO is not valid,
     * or with status 500 (Internal Server Error) if the rateDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rates")
    @Timed
    public ResponseEntity<RateDTO> updateRate(@RequestBody RateDTO rateDTO) throws URISyntaxException {
        log.debug("REST request to update Rate : {}", rateDTO);
        if (rateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Rate rate = rateMapper.toEntity(rateDTO);
        rate = rateRepository.save(rate);
        RateDTO result = rateMapper.toDto(rate);
        rateSearchRepository.save(rate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rateDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rates : get all the rates.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of rates in body
     */
    @GetMapping("/rates")
    @Timed
    public List<RateDTO> getAllRates() {
        log.debug("REST request to get all Rates");
        List<Rate> rates = rateRepository.findAll();
        return rateMapper.toDto(rates);
    }

    /**
     * GET  /rates/:id : get the "id" rate.
     *
     * @param id the id of the rateDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rateDTO, or with status 404 (Not Found)
     */
    @GetMapping("/rates/{id}")
    @Timed
    public ResponseEntity<RateDTO> getRate(@PathVariable Long id) {
        log.debug("REST request to get Rate : {}", id);
        Optional<RateDTO> rateDTO = rateRepository.findById(id)
            .map(rateMapper::toDto);
        return ResponseUtil.wrapOrNotFound(rateDTO);
    }

    /**
     * DELETE  /rates/:id : delete the "id" rate.
     *
     * @param id the id of the rateDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rates/{id}")
    @Timed
    public ResponseEntity<Void> deleteRate(@PathVariable Long id) {
        log.debug("REST request to delete Rate : {}", id);

        rateRepository.deleteById(id);
        rateSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/rates?query=:query : search for the rate corresponding
     * to the query.
     *
     * @param query the query of the rate search
     * @return the result of the search
     */
    @GetMapping("/_search/rates")
    @Timed
    public List<RateDTO> searchRates(@RequestParam String query) {
        log.debug("REST request to search Rates for query {}", query);
        return StreamSupport
            .stream(rateSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(rateMapper::toDto)
            .collect(Collectors.toList());
    }

}
