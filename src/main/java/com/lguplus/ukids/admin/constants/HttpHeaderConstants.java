package com.lguplus.ukids.admin.constants;

import org.apache.http.HttpHeaders;
import org.springframework.http.MediaType;

public final class HttpHeaderConstants {
    public static final String DATE_KEY = HttpHeaders.DATE;
    public static final String ACCEPT_KEY = HttpHeaders.ACCEPT;
    public static final String ACCEPT_VAL = MediaType.APPLICATION_JSON_VALUE;
    public static final String ACCEPT_CHARSET_KEY = HttpHeaders.ACCEPT_CHARSET;
    public static final String ACCEPT_CHARSET_VAL = "UTF-8";
    public static final String CONTENTS_TYPE_KEY = HttpHeaders.CONTENT_TYPE;
    public static final String CONTENTS_TYPE_JSON = "application/json";
    public static final String CONTENTS_TYPE_MULTIPART = "multipart/form-data";
    public static final String AUTHORIZATION_KEY = HttpHeaders.AUTHORIZATION;

    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    public static final String X_SESSION_ID = "x-session-id";
    public static final String X_MEMBER_ID = "x-member-id";
    public static final String X_CORRELATION_ID = "x-correlation-id";
    public static final String X_AUTHORIZED_KEY = "x-authorized-key";
}
