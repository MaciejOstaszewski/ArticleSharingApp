package com.ostaszewski.article_sharing_app.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserMessageDTO implements Serializable, Comparable<UserMessageDTO> {

    private Long id;

    private String content;

    private Long ownerId;

    private Instant creationDate;

    private Boolean read;


    @Override
    public int compareTo(UserMessageDTO o) {
        return this.creationDate.compareTo(o.getCreationDate());
    }
}
