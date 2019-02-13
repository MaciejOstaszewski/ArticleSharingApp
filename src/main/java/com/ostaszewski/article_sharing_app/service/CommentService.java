package com.ostaszewski.article_sharing_app.service;

import com.ostaszewski.article_sharing_app.domain.Comment;
import com.ostaszewski.article_sharing_app.domain.User;
import com.ostaszewski.article_sharing_app.repository.CommentRepository;
import com.ostaszewski.article_sharing_app.repository.UserRepository;
import com.ostaszewski.article_sharing_app.repository.search.CommentSearchRepository;
import com.ostaszewski.article_sharing_app.service.dto.CommentDTO;
import com.ostaszewski.article_sharing_app.service.dto.CommentWithUserDTO;
import com.ostaszewski.article_sharing_app.service.mapper.CommentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Comment.
 */
@Service
@Transactional
public class CommentService {

    private final Logger log = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    private final CommentSearchRepository commentSearchRepository;

    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper, CommentSearchRepository commentSearchRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.commentSearchRepository = commentSearchRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save a comment.
     *
     * @param commentDTO the entity to save
     * @return the persisted entity
     */
    public CommentDTO save(CommentDTO commentDTO) {
        log.debug("Request to save Comment : {}", commentDTO);
        Comment comment = commentMapper.toEntity(commentDTO);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<User> optionalUser = userRepository.findOneByLogin(currentPrincipalName);
        User user = null;
        if (optionalUser.isPresent()) {
             user = optionalUser.get();
        }

        comment.setUser(user);
        comment.setCreationDate(Instant.now());
        comment.setModificationDate(Instant.now());
        comment = commentRepository.save(comment);
        CommentDTO result = commentMapper.toDto(comment);
        commentSearchRepository.save(comment);
        return result;
    }

    /**
     * Get all the comments.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<CommentDTO> findAll() {
        log.debug("Request to get all Comments");
        return commentRepository.findAll().stream()
            .map(commentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public List<CommentDTO> findAllArticleComments(Long id) {
        return commentRepository.findAllByArticleId(id).stream()
            .map(commentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public List<CommentWithUserDTO> findArticleComments(Long id) {
        return commentRepository.findAllArticleComments(id);
    }

    /**
     * Get one comment by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<CommentDTO> findOne(Long id) {
        log.debug("Request to get Comment : {}", id);
        return commentRepository.findById(id)
            .map(commentMapper::toDto);
    }

    /**
     * Delete the comment by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Comment : {}", id);
        commentRepository.deleteById(id);
        commentSearchRepository.deleteById(id);
    }

    /**
     * Search for the comment corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<CommentDTO> search(String query) {
        log.debug("Request to search Comments for query {}", query);
        return StreamSupport
            .stream(commentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(commentMapper::toDto)
            .collect(Collectors.toList());
    }
}
