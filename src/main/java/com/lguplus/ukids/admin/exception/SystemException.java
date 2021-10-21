package com.lguplus.ukids.admin.exception;

import com.lguplus.ukids.admin.constants.StatusCodeConstants;

public class SystemException extends Exception {
    private static final long serialVersionUID = 1L;
    private String message;
    private String statusCode;
    private Throwable throwable;

    public SystemException(final String message, final Throwable throwable) {
        this.message = message;
        this.throwable = throwable;
    }

    public SystemException(final Throwable throwable) {
        this.throwable = throwable;
    }

    public SystemException(final String message) {
        this(message, StatusCodeConstants.INTERNAL_SERVER_ERROR);
    }

    public SystemException(final String message, final String statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return this.message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }
}
