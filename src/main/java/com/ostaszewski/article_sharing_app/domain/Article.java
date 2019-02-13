package com.ostaszewski.article_sharing_app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Article.
 */
@Getter
@Setter
@Entity
@Table(name = "article")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "article")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "title", length = 300, nullable = false)
    private String title;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "modification_date")
    private Instant modificationDate;

    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    @NotNull
    @Column(name = "image_url", nullable = false)
    private String imageURL;

    @Column(name = "views")
    private Integer views;

    private Boolean active;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User user;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Category category;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "article_tag",
               joinColumns = @JoinColumn(name = "articles_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "tags_id", referencedColumnName = "id"))
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "article_interest",
               joinColumns = @JoinColumn(name = "articles_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "interests_id", referencedColumnName = "id"))
    private Set<Interest> interests = new HashSet<>();

    public Article title(String title) {
        this.title = title;
        return this;
    }

    public Article creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public Article modificationDate(Instant modificationDate) {
        this.modificationDate = modificationDate;
        return this;
    }

    public Article content(String content) {
        this.content = content;
        return this;
    }

    public Article imageURL(String imageURL) {
        this.imageURL = imageURL;
        return this;
    }

    public Article views(Integer views) {
        this.views = views;
        return this;
    }

    public Article user(User user) {
        this.user = user;
        return this;
    }

    public Article category(Category category) {
        this.category = category;
        return this;
    }

    public Article tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Article addTag(Tag tag) {
        this.tags.add(tag);
        return this;
    }

    public Article removeTag(Tag tag) {
        this.tags.remove(tag);
        return this;
    }

    public Article interests(Set<Interest> interests) {
        this.interests = interests;
        return this;
    }

    public Article addInterest(Interest interest) {
        this.interests.add(interest);
        return this;
    }

    public Article removeInterest(Interest interest) {
        this.interests.remove(interest);
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
        Article article = (Article) o;
        if (article.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), article.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Article{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", modificationDate='" + getModificationDate() + "'" +
            ", content='" + getContent() + "'" +
            ", imageURL='" + getImageURL() + "'" +
            ", views=" + getViews() +
            "}";
    }
}
