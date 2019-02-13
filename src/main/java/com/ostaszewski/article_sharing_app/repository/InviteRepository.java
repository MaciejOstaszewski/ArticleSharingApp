package com.ostaszewski.article_sharing_app.repository;

import com.ostaszewski.article_sharing_app.domain.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface InviteRepository extends JpaRepository<Invite, Long> {
}
