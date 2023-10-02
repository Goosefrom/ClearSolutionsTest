package com.goose.clearsolutionstest.exception;

import lombok.Getter;

@Getter
public enum ErrorType {
    INTERNAL_ERROR(500),
    NOT_FOUND(404),
    ALREADY_OCCUPIED(400);
    private final int httpError;

    ErrorType(int httpError) {this.httpError = httpError;}

}
