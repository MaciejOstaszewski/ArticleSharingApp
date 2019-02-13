package com.ostaszewski.article_sharing_app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ostaszewski.article_sharing_app.domain.Category;
import com.ostaszewski.article_sharing_app.repository.CategoryRepository;
import com.ostaszewski.article_sharing_app.repository.search.CategorySearchRepository;
import com.ostaszewski.article_sharing_app.web.rest.errors.BadRequestAlertException;
import com.ostaszewski.article_sharing_app.web.rest.util.HeaderUtil;
import com.ostaszewski.article_sharing_app.service.dto.CategoryDTO;
import com.ostaszewski.article_sharing_app.service.mapper.CategoryMapper;
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

@RestController
@RequestMapping("/api")
public class CategoryResource {

    private final Logger log = LoggerFactory.getLogger(CategoryResource.class);

    private static final String ENTITY_NAME = "category";

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private final CategorySearchRepository categorySearchRepository;

    public CategoryResource(CategoryRepository categoryRepository, CategoryMapper categoryMapper, CategorySearchRepository categorySearchRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.categorySearchRepository = categorySearchRepository;
    }


    @PostMapping("/categories")
    @Timed
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) throws URISyntaxException {
        log.debug("REST request to save Category : {}", categoryDTO);
        if (categoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new category cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Category category = categoryMapper.toEntity(categoryDTO);
        category = categoryRepository.save(category);
        CategoryDTO result = categoryMapper.toDto(category);
        categorySearchRepository.save(category);
        return ResponseEntity.created(new URI("/api/categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    @PutMapping("/categories")
    @Timed
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO) throws URISyntaxException {
        log.debug("REST request to update Category : {}", categoryDTO);
        if (categoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Category category = categoryMapper.toEntity(categoryDTO);
        category = categoryRepository.save(category);
        CategoryDTO result = categoryMapper.toDto(category);
        categorySearchRepository.save(category);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, categoryDTO.getId().toString()))
            .body(result);
    }


    @GetMapping("/categories")
    @Timed
    public List<CategoryDTO> getAllCategories() {
        log.debug("REST request to get all Categories");
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toDto(categories);
    }


    @GetMapping("/categories/{id}")
    @Timed
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long id) {
        log.debug("REST request to get Category : {}", id);
        Optional<CategoryDTO> categoryDTO = categoryRepository.findById(id)
            .map(categoryMapper::toDto);
        return ResponseUtil.wrapOrNotFound(categoryDTO);
    }


    @DeleteMapping("/categories/{id}")
    @Timed
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.debug("REST request to delete Category : {}", id);

        categoryRepository.deleteById(id);
        categorySearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    @GetMapping("/_search/categories")
    @Timed
    public List<CategoryDTO> searchCategories(@RequestParam String query) {
        log.debug("REST request to search Categories for query {}", query);
        return StreamSupport
            .stream(categorySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(categoryMapper::toDto)
            .collect(Collectors.toList());
    }

}
