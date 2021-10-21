package com.lguplus.ukids.admin.exception;

public class BusinessException extends Exception {
    private static final long serialVersionUID = 1L;
    private String message;
    private String statusCode;

    public BusinessException(final String message, final String statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return this.message;
    }

    public String getStatusCode() {
        return statusCode;
    }
}
