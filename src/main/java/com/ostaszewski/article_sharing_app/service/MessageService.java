package com.ostaszewski.article_sharing_app.service;

import com.ostaszewski.article_sharing_app.domain.Message;
import com.ostaszewski.article_sharing_app.repository.MessageRepository;
import com.ostaszewski.article_sharing_app.repository.search.MessageSearchRepository;
import com.ostaszewski.article_sharing_app.service.dto.MessageDTO;
import com.ostaszewski.article_sharing_app.service.mapper.MessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Message.
 */
@Service
@Transactional
public class MessageService {

    private final Logger log = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository messageRepository;

    private final MessageMapper messageMapper;

    private final MessageSearchRepository messageSearchRepository;

    public MessageService(MessageRepository messageRepository, MessageMapper messageMapper, MessageSearchRepository messageSearchRepository) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.messageSearchRepository = messageSearchRepository;
    }

    /**
     * Save a message.
     *
     * @param messageDTO the entity to save
     * @return the persisted entity
     */
    public MessageDTO save(MessageDTO messageDTO) {
        log.debug("Request to save Message : {}", messageDTO);
        Message message = messageMapper.toEntity(messageDTO);
        message = messageRepository.save(message);
        MessageDTO result = messageMapper.toDto(message);
        messageSearchRepository.save(message);
        return result;
    }

    /**
     * Get all the messages.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<MessageDTO> findAll() {
        log.debug("Request to get all Messages");
        return messageRepository.findAll().stream()
            .map(messageMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one message by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<MessageDTO> findOne(Long id) {
        log.debug("Request to get Message : {}", id);
        return messageRepository.findById(id)
            .map(messageMapper::toDto);
    }

    /**
     * Delete the message by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Message : {}", id);
        messageRepository.deleteById(id);
        messageSearchRepository.deleteById(id);
    }

    /**
     * Search for the message corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<MessageDTO> search(String query) {
        log.debug("Request to search Messages for query {}", query);
        return StreamSupport
            .stream(messageSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(messageMapper::toDto)
            .collect(Collectors.toList());
    }
}
