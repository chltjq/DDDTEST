package com.lguplus.ukids.admin.exception;

import org.springframework.http.HttpStatus;

public class RestApiException extends Exception {
    private static final long serialVersionUID = 1L;
    private String message;
    private HttpStatus httpStatus;

    public RestApiException(final String message, final HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return this.message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
