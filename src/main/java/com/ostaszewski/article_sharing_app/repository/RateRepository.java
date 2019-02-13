package com.ostaszewski.article_sharing_app.repository;

import com.ostaszewski.article_sharing_app.domain.Rate;
import com.ostaszewski.article_sharing_app.service.dto.AverageRatingDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the Rate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {

//    @Query("SELECT r FROM Rate r WHERE r.article.id = ?1 AND r.user.login = ?2")
//    Optional<Rate> findByArticleIdAndUserLogin(@Param("articleId") Long articleId, @Param("userLogin") String userLogin);
    Optional<Rate> findByArticleIdAndUserLogin(Long articleId, String userLogin);


    @Query(
        "SELECT NEW com.ostaszewski.article_sharing_app.service.dto.AverageRatingDTO(avg(r.value), count(r.id)) " +
            "FROM com.ostaszewski.article_sharing_app.domain.Rate r " +
            "WHERE r.article.id = ?1"
    )
    Optional<AverageRatingDTO> getAverageArticleRating(@Param("articleId") Long articleId);
}
