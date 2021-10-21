package com.lguplus.ukids.admin.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lguplus.ukids.admin.constants.HttpHeaderConstants;
import com.lguplus.ukids.admin.constants.StatusCodeConstants;
import com.lguplus.ukids.admin.dto.SessionDto;
import com.lguplus.ukids.admin.exception.BusinessException;
import com.lguplus.ukids.admin.exception.SystemException;
import com.lguplus.ukids.admin.utility.SessionScopeUtility;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
    private static final String HTTP_METHOD_OPTIONS = "OPTIONS";
    private static final String HTTP_METHOD_POST = "POST";
    private static final String MESSAGE_SESSION_EXPIRED = "Session Expired";
    private static final String SESSION_URI = "/v1/session";

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {

        if (SESSION_URI.equals(request.getRequestURI()) && HTTP_METHOD_POST.equals(request.getMethod())) {
            try {
                return super.preHandle(request, response, handler);
            } catch (Exception ex) {
                throw new SystemException(ex.getMessage(), StatusCodeConstants.FAIL);
            }
        } else if (!HTTP_METHOD_OPTIONS.equals(request.getMethod())) {

            String sessionId = request.getHeader(HttpHeaderConstants.X_SESSION_ID);
            String memberId = request.getHeader(HttpHeaderConstants.X_MEMBER_ID);

            if (StringUtils.isEmpty(sessionId) || StringUtils.isEmpty(memberId)) {
                throw new BusinessException(MESSAGE_SESSION_EXPIRED, StatusCodeConstants.SESSION_EXPIRE);
            }

            // 필요하면 세션 검증
            SessionDto sessionUser = SessionDto.builder().sessionId(sessionId).memberId(memberId).build();
            if (sessionUser == null ) {
                throw new BusinessException(MESSAGE_SESSION_EXPIRED, StatusCodeConstants.SESSION_EXPIRE);
            } else {
                SessionScopeUtility.setAttribute(SessionScopeUtility.SESSION_USER, sessionUser);
            }
        }

        try {
            return super.preHandle(request, response, handler);
        } catch (Exception ex) {
            throw new SystemException(ex.getMessage(), StatusCodeConstants.FAIL);
        }
    }
}
