package com.lguplus.ukids.admin.utility;

import java.security.spec.KeySpec;
import java.util.Map;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.http.HttpServletRequest;

import com.lguplus.ukids.admin.constants.CommonConstants;
import com.lguplus.ukids.admin.constants.HttpHeaderConstants;
import com.lguplus.ukids.admin.constants.StatusCodeConstants;
import com.lguplus.ukids.admin.exception.SystemException;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.util.Date;
import java.text.SimpleDateFormat;

public final class ValidateUtility {

    private ValidateUtility() {
    }

    public static Boolean invalidDateFormat(final String uploadFileName) {
        return !Pattern.compile("^[^_]+_{1}[0-9]{12}[^_]+$").matcher(uploadFileName).find();
    }

    public static Boolean checkYyyymm(final String yyyymm) {
        String regExp = "(19|20)\\d{2}(0[1-9]|1[012])";
        return yyyymm.matches(regExp);
    }

    public static String checkFromToYyyymm(final String startYyyymm, final String endYyyymm) {
        String yyyymm = new SimpleDateFormat("yyyyMM", Locale.KOREAN).format(new Date());
        if (!checkYyyymm(startYyyymm) || !checkYyyymm(endYyyymm)) {
            return StatusCodeConstants.INVALID_FORMAT;
        } else if (Integer.parseInt(startYyyymm) > Integer.parseInt(endYyyymm)) {
            return StatusCodeConstants.INVALID_DATE_FROM_TO;
        } else if (Integer.parseInt(endYyyymm) > Integer.parseInt(yyyymm)) {
            return StatusCodeConstants.INVALID_FUTURE_DATE;
        } else {
            return StatusCodeConstants.OK;
        }
    }

    public static String encryptSha(final String email, final String password) throws SystemException {
        String encrypted;
        String salt = email + email.length();
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), CommonConstants.ITERATION_COUNT,
                    CommonConstants.KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            encrypted = new String(Base64Coder.encode(hash));
        } catch (Exception exception) {
            throw new SystemException("Can not generated sha-512 string", exception);
        }
        return encrypted;
    }

    public static boolean checkEmail(final String email) {
        boolean result = false;
        String regExp = "^[a-zA-Z0-9.%+-]+@hanwha\\.com$";
        result = email.matches(regExp);
        return result;
    }

    public static String getRequestIp() throws Exception {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = "";
        String xFowardedIp = req.getHeader(HttpHeaderConstants.X_FORWARDED_FOR);
        if (xFowardedIp != null && !xFowardedIp.isEmpty()) {
            ip = xFowardedIp;
        } else {
            ip = req.getRemoteAddr();
        }
        return ip;
    }

    public static boolean checkPhoneNumber(final String phoneNumber) {
        String regExp = "01([0|1|6|7|8|9])([0-9]{7,8})";
        return phoneNumber.matches(regExp);
    }

    public static boolean isEmpty(final Object obj) {
        if (obj == null) {
            return true;
        }
        if ((obj instanceof String) && (((String) obj).trim().length() == 0)) {
            return true;
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        }
        if (obj instanceof List) {
            return ((List<?>) obj).isEmpty();
        }
        if (obj instanceof Object[]) {
            return (((Object[]) obj).length == 0);
        }
        return false;
    }


}
