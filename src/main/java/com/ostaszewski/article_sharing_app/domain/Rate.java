package com.ostaszewski.article_sharing_app.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Rate.
 */
@Setter
@Getter
@Entity
@Table(name = "rate")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "rate")
public class Rate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "jhi_value")
    private Integer value;

    @OneToOne
    @JoinColumn(unique = true)
    private Article article;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;


    public Rate value(Integer value) {
        this.value = value;
        return this;
    }

    public Rate article(Article article) {
        this.article = article;
        return this;
    }


    public Rate user(User user) {
        this.user = user;
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
        Rate rate = (Rate) o;
        if (rate.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rate.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Rate{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            "}";
    }
}
