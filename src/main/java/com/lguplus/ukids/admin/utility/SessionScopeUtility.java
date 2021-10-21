package com.lguplus.ukids.admin.utility;

import com.lguplus.ukids.admin.dto.SessionDto;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public final class SessionScopeUtility {
    public static final String SESSION_USER = "sessionUser";

    private SessionScopeUtility() {
        // 유틸클래스 선언 방지
    }

    public static SessionDto getAttribute(final String name) throws Exception {
        return (SessionDto) RequestContextHolder.getRequestAttributes().getAttribute(name,
                RequestAttributes.SCOPE_SESSION);
    }

    public static void setAttribute(final String name, final SessionDto session) throws Exception {
        RequestContextHolder.getRequestAttributes().setAttribute(name, session, RequestAttributes.SCOPE_SESSION);
    }

    public static void removeAttribute(final String name) throws Exception {
        RequestContextHolder.getRequestAttributes().removeAttribute(name, RequestAttributes.SCOPE_SESSION);
    }
}
