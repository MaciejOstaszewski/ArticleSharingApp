package com.ostaszewski.article_sharing_app.web.rest;

import com.ostaszewski.article_sharing_app.ArticleSharingApp;

import com.ostaszewski.article_sharing_app.domain.Interest;
import com.ostaszewski.article_sharing_app.repository.InterestRepository;
import com.ostaszewski.article_sharing_app.repository.search.InterestSearchRepository;
import com.ostaszewski.article_sharing_app.service.dto.InterestDTO;
import com.ostaszewski.article_sharing_app.service.mapper.InterestMapper;
import com.ostaszewski.article_sharing_app.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.ostaszewski.article_sharing_app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InterestResource REST controller.
 *
 * @see InterestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArticleSharingApp.class)
public class InterestResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private InterestRepository interestRepository;


    @Autowired
    private InterestMapper interestMapper;

    /**
     * This repository is mocked in the com.ostaszewski.article_sharing_app.repository.search test package.
     *
     * @see com.ostaszewski.article_sharing_app.repository.search.InterestSearchRepositoryMockConfiguration
     */
    @Autowired
    private InterestSearchRepository mockInterestSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInterestMockMvc;

    private Interest interest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InterestResource interestResource = new InterestResource(interestRepository, interestMapper, mockInterestSearchRepository);
        this.restInterestMockMvc = MockMvcBuilders.standaloneSetup(interestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Interest createEntity(EntityManager em) {
        Interest interest = new Interest()
            .name(DEFAULT_NAME);
        return interest;
    }

    @Before
    public void initTest() {
        interest = createEntity(em);
    }

    @Test
    @Transactional
    public void createInterest() throws Exception {
        int databaseSizeBeforeCreate = interestRepository.findAll().size();

        // Create the Interest
        InterestDTO interestDTO = interestMapper.toDto(interest);
        restInterestMockMvc.perform(post("/api/interests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interestDTO)))
            .andExpect(status().isCreated());

        // Validate the Interest in the database
        List<Interest> interestList = interestRepository.findAll();
        assertThat(interestList).hasSize(databaseSizeBeforeCreate + 1);
        Interest testInterest = interestList.get(interestList.size() - 1);
        assertThat(testInterest.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Interest in Elasticsearch
        verify(mockInterestSearchRepository, times(1)).save(testInterest);
    }

    @Test
    @Transactional
    public void createInterestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = interestRepository.findAll().size();

        // Create the Interest with an existing ID
        interest.setId(1L);
        InterestDTO interestDTO = interestMapper.toDto(interest);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInterestMockMvc.perform(post("/api/interests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Interest in the database
        List<Interest> interestList = interestRepository.findAll();
        assertThat(interestList).hasSize(databaseSizeBeforeCreate);

        // Validate the Interest in Elasticsearch
        verify(mockInterestSearchRepository, times(0)).save(interest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = interestRepository.findAll().size();
        // set the field null
        interest.setName(null);

        // Create the Interest, which fails.
        InterestDTO interestDTO = interestMapper.toDto(interest);

        restInterestMockMvc.perform(post("/api/interests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interestDTO)))
            .andExpect(status().isBadRequest());

        List<Interest> interestList = interestRepository.findAll();
        assertThat(interestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInterests() throws Exception {
        // Initialize the database
        interestRepository.saveAndFlush(interest);

        // Get all the interestList
        restInterestMockMvc.perform(get("/api/interests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interest.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    

    @Test
    @Transactional
    public void getInterest() throws Exception {
        // Initialize the database
        interestRepository.saveAndFlush(interest);

        // Get the interest
        restInterestMockMvc.perform(get("/api/interests/{id}", interest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(interest.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingInterest() throws Exception {
        // Get the interest
        restInterestMockMvc.perform(get("/api/interests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInterest() throws Exception {
        // Initialize the database
        interestRepository.saveAndFlush(interest);

        int databaseSizeBeforeUpdate = interestRepository.findAll().size();

        // Update the interest
        Interest updatedInterest = interestRepository.findById(interest.getId()).get();
        // Disconnect from session so that the updates on updatedInterest are not directly saved in db
        em.detach(updatedInterest);
        updatedInterest
            .name(UPDATED_NAME);
        InterestDTO interestDTO = interestMapper.toDto(updatedInterest);

        restInterestMockMvc.perform(put("/api/interests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interestDTO)))
            .andExpect(status().isOk());

        // Validate the Interest in the database
        List<Interest> interestList = interestRepository.findAll();
        assertThat(interestList).hasSize(databaseSizeBeforeUpdate);
        Interest testInterest = interestList.get(interestList.size() - 1);
        assertThat(testInterest.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Interest in Elasticsearch
        verify(mockInterestSearchRepository, times(1)).save(testInterest);
    }

    @Test
    @Transactional
    public void updateNonExistingInterest() throws Exception {
        int databaseSizeBeforeUpdate = interestRepository.findAll().size();

        // Create the Interest
        InterestDTO interestDTO = interestMapper.toDto(interest);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInterestMockMvc.perform(put("/api/interests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Interest in the database
        List<Interest> interestList = interestRepository.findAll();
        assertThat(interestList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Interest in Elasticsearch
        verify(mockInterestSearchRepository, times(0)).save(interest);
    }

    @Test
    @Transactional
    public void deleteInterest() throws Exception {
        // Initialize the database
        interestRepository.saveAndFlush(interest);

        int databaseSizeBeforeDelete = interestRepository.findAll().size();

        // Get the interest
        restInterestMockMvc.perform(delete("/api/interests/{id}", interest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Interest> interestList = interestRepository.findAll();
        assertThat(interestList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Interest in Elasticsearch
        verify(mockInterestSearchRepository, times(1)).deleteById(interest.getId());
    }

    @Test
    @Transactional
    public void searchInterest() throws Exception {
        // Initialize the database
        interestRepository.saveAndFlush(interest);
        when(mockInterestSearchRepository.search(queryStringQuery("id:" + interest.getId())))
            .thenReturn(Collections.singletonList(interest));
        // Search the interest
        restInterestMockMvc.perform(get("/api/_search/interests?query=id:" + interest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interest.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Interest.class);
        Interest interest1 = new Interest();
        interest1.setId(1L);
        Interest interest2 = new Interest();
        interest2.setId(interest1.getId());
        assertThat(interest1).isEqualTo(interest2);
        interest2.setId(2L);
        assertThat(interest1).isNotEqualTo(interest2);
        interest1.setId(null);
        assertThat(interest1).isNotEqualTo(interest2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InterestDTO.class);
        InterestDTO interestDTO1 = new InterestDTO();
        interestDTO1.setId(1L);
        InterestDTO interestDTO2 = new InterestDTO();
        assertThat(interestDTO1).isNotEqualTo(interestDTO2);
        interestDTO2.setId(interestDTO1.getId());
        assertThat(interestDTO1).isEqualTo(interestDTO2);
        interestDTO2.setId(2L);
        assertThat(interestDTO1).isNotEqualTo(interestDTO2);
        interestDTO1.setId(null);
        assertThat(interestDTO1).isNotEqualTo(interestDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(interestMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(interestMapper.fromId(null)).isNull();
    }
}
