package com.ostaszewski.article_sharing_app.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.*;

/**
 * A DTO for the Article entity.
 */
@Getter
@Setter
public class ArticleDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 300)
    private String title;

    private Instant creationDate;

    private Instant modificationDate;

    @NotNull
    private String content;

    @NotNull
    private String imageURL;

    private Integer views;

    private Boolean active;

    private Long userId;

    private String userLogin;

    private Long categoryId;

    private String categoryName;

    private Set<TagDTO> tags = new HashSet<>();

    private Set<InterestDTO> interests = new HashSet<>();

    private Set<CommentDTO> comments = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ArticleDTO articleDTO = (ArticleDTO) o;
        if (articleDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), articleDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ArticleDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", modificationDate='" + getModificationDate() + "'" +
            ", content='" + getContent() + "'" +
            ", imageURL='" + getImageURL() + "'" +
            ", views=" + getViews() +
            ", active=" + getActive() +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            ", category=" + getCategoryId() +
            ", categoryName=" + getCategoryName() +
            "}";
    }
}
