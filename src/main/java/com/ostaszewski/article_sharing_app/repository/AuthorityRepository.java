package com.ostaszewski.article_sharing_app.repository;

import com.ostaszewski.article_sharing_app.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
