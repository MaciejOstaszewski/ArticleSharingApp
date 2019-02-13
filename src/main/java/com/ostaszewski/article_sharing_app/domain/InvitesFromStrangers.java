package com.ostaszewski.article_sharing_app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "invites_from_strangers")
@Document(indexName = "invites_from_strangers")
public class InvitesFromStrangers {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;


    @ManyToOne
    @JsonIgnoreProperties("")
    private User friend;

    private Boolean read;

    public InvitesFromStrangers(User friend) {
        this.friend = friend;
        this.read = false;
    }

}
