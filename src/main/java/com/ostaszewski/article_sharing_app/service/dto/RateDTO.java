package com.ostaszewski.article_sharing_app.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Rate entity.
 */
@Getter
@Setter
public class RateDTO implements Serializable {

    private Long id;

    private Integer value;

    private Long articleId;

    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RateDTO rateDTO = (RateDTO) o;
        if (rateDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rateDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RateDTO{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", article=" + getArticleId() +
            ", user=" + getUserId() +
            "}";
    }
}
