package com.ostaszewski.article_sharing_app.repository;

import com.ostaszewski.article_sharing_app.domain.Interest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Interest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {

}
