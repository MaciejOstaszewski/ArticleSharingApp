package com.ostaszewski.article_sharing_app.service.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Interest entity.
 */
@Getter
@Setter
public class InterestDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InterestDTO interestDTO = (InterestDTO) o;
        if (interestDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), interestDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InterestDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
