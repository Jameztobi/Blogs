package com.example.Blogs.Exception;

import org.springframework.http.HttpStatus;

public class ApiException {
    private final String error;
    private final HttpStatus httpStatus;

    public ApiException(String error, HttpStatus httpStatus) {
        this.error = error;
        this.httpStatus = httpStatus;
    }

    public String getError() {
        return error;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
