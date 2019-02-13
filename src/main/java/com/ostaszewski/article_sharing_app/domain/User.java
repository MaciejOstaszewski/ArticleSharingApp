package com.ostaszewski.article_sharing_app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ostaszewski.article_sharing_app.config.Constants;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A user.
 */
@Getter
@Setter
@Entity
@Table(name = "jhi_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "user")
public class User extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash", length = 60, nullable = false)
    private String password;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    private String email;

    @NotNull
    @Column(nullable = false)
    private boolean activated = false;

    @Size(min = 2, max = 6)
    @Column(name = "lang_key", length = 6)
    private String langKey;

//    @Size(max = 256)
//    @Column(name = "image_url", length = 256)
//    private String imageUrl;

    @JsonIgnore
    private String avatar;


    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    @JsonIgnore
    private String resetKey;

    @Column(name = "reset_date")
    private Instant resetDate = null;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "jhi_user_authority",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Authority> authorities = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "user_interest",
        joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "interests_id", referencedColumnName = "id"))
    private Set<Interest> interests = new HashSet<>();

    @JsonIgnore
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "user_friend",
        joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "friends_id", referencedColumnName = "id"))
    private Set<User> friends = new HashSet<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_invites",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "invite_id", referencedColumnName = "id"))
    private Set<Invite> invites = new HashSet<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_invites_from_strangers",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "invite_id", referencedColumnName = "id"))
    private Set<InvitesFromStrangers> invitesFromStrangers = new HashSet<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_messages",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "message_id", referencedColumnName = "id"))
    private Set<Message> userMessages = new HashSet<>();

    public User friends(Set<User> friends) {
        this.friends = friends;
        return this;
    }

    public User addFriend(User user) {
        this.friends.add(user);
        return this;
    }

    public User removeFriend(User user) {
        this.friends.remove(user);
        return this;
    }

    public User invites(Set<Invite> invites) {
        this.invites = invites;
        return this;
    }

    public User addInvite(Invite invite) {
        this.invites.add(invite);
        return this;
    }

    public User removeInvite(Invite invite) {
        this.invites.remove(invite);
        return this;
    }

    public User invitesUser(Set<InvitesFromStrangers> invitesFromStrangers) {
        this.invitesFromStrangers = invitesFromStrangers;
        return this;
    }

    public User addInviteUser(InvitesFromStrangers invitesFromStrangers) {
        this.invitesFromStrangers.add(invitesFromStrangers);
        return this;
    }

    public User removeInviteUser(InvitesFromStrangers invite) {
        this.invitesFromStrangers.remove(invite);
        return this;
    }

    public User messages(Set<Message> messages) {
        this.userMessages = messages;
        return this;
    }

    public User addMessages(Message message) {
        this.userMessages.add(message);
        return this;
    }

    public User removeMessages(Message message) {
        this.userMessages.remove(message);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;
        return !(user.getId() == null || getId() == null) && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "User{" +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
//            ", imageUrl='" + imageUrl + '\'' +
            ", activated='" + activated + '\'' +
            ", langKey='" + langKey + '\'' +
            ", activationKey='" + activationKey + '\'' +
            "}";
    }
}
