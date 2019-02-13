package com.ostaszewski.article_sharing_app.repository;

import com.ostaszewski.article_sharing_app.domain.User;

import com.ostaszewski.article_sharing_app.service.dto.MessageDTO;
import com.ostaszewski.article_sharing_app.service.dto.UserMessageDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.time.Instant;

/**
 * Spring Data JPA repository for the User entity.
 */
@Repository
public interface    UserRepository extends JpaRepository<User, Long> {

    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmailIgnoreCase(String email);

    Optional<User> findOneByLogin(String login);

    Optional<User> findById(Long id);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesById(Long id);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<User> findOneWithAuthoritiesByLogin(@Param("login") String login);

    @EntityGraph(attributePaths = "authorities")
    @Query("SELECT u FROM com.ostaszewski.article_sharing_app.domain.User u WHERE LOWER(u.login) LIKE ?1")
    Optional<User> findOneWithAuthoritiesByLoginToAuthenticate(@Param("login") String login);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<User> findOneWithAuthoritiesByEmail(String email);

    Page<User> findAllByLoginNot(Pageable pageable, String login);

    @Query(
        value = "SELECT u FROM jhi_user u, invites i, user_invites ui WHERE invites.friend_id = ?1 AND ui.invite_id = i.id",
    nativeQuery = true)
    List<User> findAllUserInvitesFromOthers(@Param("id") Long id);


    @Query(
        "SELECT DISTINCT NEW com.ostaszewski.article_sharing_app.service.dto.MessageDTO(m.id, m.content, m.creationDate, m.receiver.id, m.read) " +
        "FROM com.ostaszewski.article_sharing_app.domain.User u " +
        "INNER JOIN u.userMessages m " +
        "WHERE u.id = ?1 " +
        "AND m.receiver.id = ?2"
    )
    List<MessageDTO> findUserMessages(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

}
