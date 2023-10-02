package com.goose.clearsolutionstest.exception;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public class ResponseStatusExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProjectException.class)
    protected ResponseEntity<Object> handleProjectException(ProjectException e) {
        e.printStackTrace();
        return ResponseEntity.status(e.getErrorType().getHttpError())
                .contentType(MediaType.APPLICATION_JSON)
                .body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleRuntimeException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500)
                .contentType(MediaType.APPLICATION_JSON)
                .body("Internal error");
    }
}
