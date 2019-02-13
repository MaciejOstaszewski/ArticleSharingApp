package com.ostaszewski.article_sharing_app.repository;

import com.ostaszewski.article_sharing_app.domain.Article;
import com.ostaszewski.article_sharing_app.domain.Interest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Spring Data  repository for the Article entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findById(Long id);

    @Query("select article from Article article where article.user.login = ?#{principal.username}")
    List<Article> findByUserIsCurrentUser();

    @Query(value = "select distinct article from Article article left join fetch article.tags left join fetch article.interests",
        countQuery = "select count(distinct article) from Article article")
    Page<Article> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct article from Article article left join fetch article.tags left join fetch article.interests")
    List<Article> findAllWithEagerRelationships();

    @Query("select article from Article article left join fetch article.tags left join fetch article.interests where article.id =:id")
    Optional<Article> findOneWithEagerRelationships(@Param("id") Long id);

    @Query(value = "select distinct article from Article article left join fetch article.tags left join fetch article.interests where article.active = :active",
        countQuery = "select count(distinct article) from Article article where article.active = :active")
    Page<Article> findAllWithEagerRelationships(Pageable pageable, @Param("active") Boolean active);

    Page<Article> findAllByActiveOrderByModificationDateDesc(Pageable pageable, Boolean active);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Article a SET a.active = true WHERE a.id = ?1")
    void activateArticle(@Param("id") Long id);

    @Query(value = "select distinct article from Article article join article.tags join article.interests where article.active = :active " +
        "and article.category.id = :id ")
    Page<Article> findAllByCategory(Pageable pageable, @Param("active") Boolean active, @Param("id") Long id);

    @Query(value = "select distinct article from Article article left join fetch article.tags  left join fetch article.interests where article.active = :active " +
        "and article.category.id = :id ",
        countQuery = "select count(distinct article) from Article article where article.active = :active")
    Page<Article> findAllWithEagerRelationshipsByCategory(Pageable pageable, @Param("active") Boolean active, @Param("id") Long id);

    @Query(value = "select distinct article from Article article left join fetch article.tags tags left join fetch article.interests where article.active = :active " +
        "and tags.id = :id ",
        countQuery = "select count(distinct article) from Article article where article.active = :active")
    Page<Article> findAllWithEagerRelationshipsByTag(Pageable pageable, @Param("active") Boolean active, @Param("id") Long id);

    @Query(value = "select distinct article from Article article join article.tags tags join article.interests where article.active = :active " +
        "and tags.id = :id ")
    Page<Article> findAllByTag(Pageable pageable, @Param("active") Boolean active, @Param("id") Long id);


    @Query(value = "select distinct article from Article article join article.tags tags join article.interests interests where article.active = true " +
        "and interests in (:interests) and article.id <> :id order by article.views desc ")
    List<Article> findSuggestedArticles(@Param("interests") Set<Interest> interests, @Param("id") Long id);

}
