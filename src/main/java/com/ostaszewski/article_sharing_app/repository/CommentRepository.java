package com.ostaszewski.article_sharing_app.repository;

import com.ostaszewski.article_sharing_app.domain.Comment;
import com.ostaszewski.article_sharing_app.service.dto.CommentWithUserDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Comment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select comment from Comment comment where comment.user.login = ?#{principal.username}")
    List<Comment> findByUserIsCurrentUser();

    List<Comment> findAllByArticleId(Long id);


    @Query("SELECT DISTINCT NEW com.ostaszewski.article_sharing_app.service.dto.CommentWithUserDTO(c.id, c.content, c.creationDate, c.modificationDate, u.login, u.avatar) " +
        "FROM com.ostaszewski.article_sharing_app.domain.Comment c " +
        "INNER JOIN com.ostaszewski.article_sharing_app.domain.User u ON c.user.id = u.id " +
        "WHERE c.article.id = ?1")
    List<CommentWithUserDTO> findAllArticleComments(@Param("articleId") Long articleId);
}
