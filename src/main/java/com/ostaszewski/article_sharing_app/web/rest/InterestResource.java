package com.ostaszewski.article_sharing_app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ostaszewski.article_sharing_app.domain.Interest;
import com.ostaszewski.article_sharing_app.repository.InterestRepository;
import com.ostaszewski.article_sharing_app.repository.search.InterestSearchRepository;
import com.ostaszewski.article_sharing_app.web.rest.errors.BadRequestAlertException;
import com.ostaszewski.article_sharing_app.web.rest.util.HeaderUtil;
import com.ostaszewski.article_sharing_app.service.dto.InterestDTO;
import com.ostaszewski.article_sharing_app.service.mapper.InterestMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Interest.
 */
@RestController
@RequestMapping("/api")
public class InterestResource {

    private final Logger log = LoggerFactory.getLogger(InterestResource.class);

    private static final String ENTITY_NAME = "interest";

    private final InterestRepository interestRepository;

    private final InterestMapper interestMapper;

    private final InterestSearchRepository interestSearchRepository;

    public InterestResource(InterestRepository interestRepository, InterestMapper interestMapper, InterestSearchRepository interestSearchRepository) {
        this.interestRepository = interestRepository;
        this.interestMapper = interestMapper;
        this.interestSearchRepository = interestSearchRepository;
    }

    /**
     * POST  /interests : Create a new interest.
     *
     * @param interestDTO the interestDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new interestDTO, or with status 400 (Bad Request) if the interest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/interests")
    @Timed
    public ResponseEntity<InterestDTO> createInterest(@Valid @RequestBody InterestDTO interestDTO) throws URISyntaxException {
        log.debug("REST request to save Interest : {}", interestDTO);
        if (interestDTO.getId() != null) {
            throw new BadRequestAlertException("A new interest cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Interest interest = interestMapper.toEntity(interestDTO);
        interest = interestRepository.save(interest);
        InterestDTO result = interestMapper.toDto(interest);
        interestSearchRepository.save(interest);
        return ResponseEntity.created(new URI("/api/interests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /interests : Updates an existing interest.
     *
     * @param interestDTO the interestDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated interestDTO,
     * or with status 400 (Bad Request) if the interestDTO is not valid,
     * or with status 500 (Internal Server Error) if the interestDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/interests")
    @Timed
    public ResponseEntity<InterestDTO> updateInterest(@Valid @RequestBody InterestDTO interestDTO) throws URISyntaxException {
        log.debug("REST request to update Interest : {}", interestDTO);
        if (interestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Interest interest = interestMapper.toEntity(interestDTO);
        interest = interestRepository.save(interest);
        InterestDTO result = interestMapper.toDto(interest);
        interestSearchRepository.save(interest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, interestDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /interests : get all the interests.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of interests in body
     */
    @GetMapping("/interests")
    @Timed
    public List<InterestDTO> getAllInterests() {
        log.debug("REST request to get all Interests");
        List<Interest> interests = interestRepository.findAll();
        return interestMapper.toDto(interests);
    }

    /**
     * GET  /interests/:id : get the "id" interest.
     *
     * @param id the id of the interestDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the interestDTO, or with status 404 (Not Found)
     */
    @GetMapping("/interests/{id}")
    @Timed
    public ResponseEntity<InterestDTO> getInterest(@PathVariable Long id) {
        log.debug("REST request to get Interest : {}", id);
        Optional<InterestDTO> interestDTO = interestRepository.findById(id)
            .map(interestMapper::toDto);
        return ResponseUtil.wrapOrNotFound(interestDTO);
    }

    /**
     * DELETE  /interests/:id : delete the "id" interest.
     *
     * @param id the id of the interestDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/interests/{id}")
    @Timed
    public ResponseEntity<Void> deleteInterest(@PathVariable Long id) {
        log.debug("REST request to delete Interest : {}", id);

        interestRepository.deleteById(id);
        interestSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/interests?query=:query : search for the interest corresponding
     * to the query.
     *
     * @param query the query of the interest search
     * @return the result of the search
     */
    @GetMapping("/_search/interests")
    @Timed
    public List<InterestDTO> searchInterests(@RequestParam String query) {
        log.debug("REST request to search Interests for query {}", query);
        return StreamSupport
            .stream(interestSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(interestMapper::toDto)
            .collect(Collectors.toList());
    }

}
