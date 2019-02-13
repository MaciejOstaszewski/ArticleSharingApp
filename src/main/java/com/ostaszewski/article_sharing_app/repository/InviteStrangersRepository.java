package com.ostaszewski.article_sharing_app.repository;

import com.ostaszewski.article_sharing_app.domain.InvitesFromStrangers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface InviteStrangersRepository extends JpaRepository<InvitesFromStrangers, Long> {
}
