package com.ostaszewski.article_sharing_app.service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentWithUserDTO implements Serializable {

    private Long id;

    private String content;

    private Instant creationDate;

    private Instant modificationDate;

    private String userNick;

    private String userAvatar;


}
