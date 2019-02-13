package com.ostaszewski.article_sharing_app.service.mapper;

import com.ostaszewski.article_sharing_app.domain.*;
import com.ostaszewski.article_sharing_app.service.dto.InterestDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Interest and its DTO InterestDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface InterestMapper extends EntityMapper<InterestDTO, Interest> {



    default Interest fromId(Long id) {
        if (id == null) {
            return null;
        }
        Interest interest = new Interest();
        interest.setId(id);
        return interest;
    }
}
