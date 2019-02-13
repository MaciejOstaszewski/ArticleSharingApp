package com.ostaszewski.article_sharing_app.service.mapper;

import com.ostaszewski.article_sharing_app.domain.*;
import com.ostaszewski.article_sharing_app.service.dto.CommentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Comment and its DTO CommentDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, ArticleMapper.class})
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "article.id", target = "articleId")
    CommentDTO toDto(Comment comment);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "articleId", target = "article")
    Comment toEntity(CommentDTO commentDTO);

    default Comment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setId(id);
        return comment;
    }
}
