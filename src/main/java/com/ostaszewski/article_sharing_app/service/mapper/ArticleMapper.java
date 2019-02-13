package com.ostaszewski.article_sharing_app.service.mapper;

import com.ostaszewski.article_sharing_app.domain.*;
import com.ostaszewski.article_sharing_app.service.dto.ArticleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Article and its DTO ArticleDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, CategoryMapper.class, TagMapper.class, InterestMapper.class})
public interface ArticleMapper extends EntityMapper<ArticleDTO, Article> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    ArticleDTO toDto(Article article);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "categoryId", target = "category")
    Article toEntity(ArticleDTO articleDTO);

    default Article fromId(Long id) {
        if (id == null) {
            return null;
        }
        Article article = new Article();
        article.setId(id);
        return article;
    }
}
