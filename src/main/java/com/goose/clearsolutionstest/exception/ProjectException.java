package com.goose.clearsolutionstest.exception;

import lombok.Getter;

@Getter
public class ProjectException extends RuntimeException{
    private final ErrorType errorType;

    public ProjectException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }
}
