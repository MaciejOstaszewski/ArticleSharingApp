package com.ostaszewski.article_sharing_app.web.rest.errors;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
