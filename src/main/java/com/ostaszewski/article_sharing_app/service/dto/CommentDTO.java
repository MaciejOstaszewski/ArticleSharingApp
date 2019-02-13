package com.ostaszewski.article_sharing_app.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Comment entity.
 */
@Setter
@Getter
public class CommentDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 1000)
    private String content;

    private Instant creationDate;

    private Instant modificationDate;

    private Long userId;

    private String userLogin;

    private Long articleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CommentDTO commentDTO = (CommentDTO) o;
        if (commentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), commentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", modificationDate='" + getModificationDate() + "'" +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            ", article=" + getArticleId() +
            "}";
    }
}
