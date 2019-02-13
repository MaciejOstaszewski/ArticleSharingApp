package com.ostaszewski.article_sharing_app.service.mapper;

import com.ostaszewski.article_sharing_app.domain.*;
import com.ostaszewski.article_sharing_app.service.dto.RateDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Rate and its DTO RateDTO.
 */
@Mapper(componentModel = "spring", uses = {ArticleMapper.class, UserMapper.class})
public interface RateMapper extends EntityMapper<RateDTO, Rate> {

    @Mapping(source = "article.id", target = "articleId")
    @Mapping(source = "user.id", target = "userId")
    RateDTO toDto(Rate rate);

    @Mapping(source = "articleId", target = "article")
    @Mapping(source = "userId", target = "user")
    Rate toEntity(RateDTO rateDTO);

    default Rate fromId(Long id) {
        if (id == null) {
            return null;
        }
        Rate rate = new Rate();
        rate.setId(id);
        return rate;
    }
}
