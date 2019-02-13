package com.ostaszewski.article_sharing_app.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the Message entity.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageDTO implements Serializable {

    private Long id;

    @NotNull
    private String content;

    private Instant creationDate;

    private Long receiverId;

    private Boolean read;


}
