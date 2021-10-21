package com.lguplus.ukids.admin.utility;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.http.HttpServletRequest;

import com.lguplus.ukids.admin.constants.CommonConstants;
import com.lguplus.ukids.admin.constants.HttpHeaderConstants;
import com.lguplus.ukids.admin.exception.SystemException;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public final class CommonUtility {
    private CommonUtility() {
    }

    public static String encryptSha(final String employeeNumber, final String password) throws SystemException {
        String encrypted;
        String salt = employeeNumber + employeeNumber.length();
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

    /**
     * email 뒤 3자리를 masking 함. ex) abcdefg@naver.com ->
     * abcd&#42;&#42;&#42;@naver.com
     *
     * @param emailAddress email 주소. ex) abcdefg@naver.com
     * @return null or abcd&#42;&#42;&#42;@naver.com
     */
    public static String maskEmailAddress(final String emailAddress) {
        if (emailAddress == null) {
            return null;
        }

        String ret;

        String[] splittedEmailAddress = emailAddress.split("@");
        String maskedEmailId;

        if (splittedEmailAddress[0].length() > 3) {
            maskedEmailId = splittedEmailAddress[0].substring(0, splittedEmailAddress[0].length() - 3) + "***";
        } else {
            maskedEmailId = getStars(splittedEmailAddress[0].length());
        }

        if (splittedEmailAddress.length > 1) {
            StringBuilder sb = new StringBuilder(maskedEmailId);

            for (int i = 1; i < splittedEmailAddress.length; i++) {
                sb.append('@');
                sb.append(splittedEmailAddress[i]);
            }

            ret = sb.toString();
        } else {
            ret = maskedEmailId;
        }

        return ret;
    }

    /**
     * 전화번호 뒤 4자리를 masking 함. ex) 010-1234-5678 -> 010-1234-&#42;&#42;&#42;&#42;
     *
     * @param phoneNumber 전화번호. ex) 010-1234-5678
     * @return null or 010-1234-&#42;&#42;&#42;&#42;
     */
    public static String maskPhoneNumber(final String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }

        String maskedPhoneNumber;

        if (phoneNumber.length() > 4) {
            maskedPhoneNumber = phoneNumber.substring(0, phoneNumber.length() - 4) + "****";
        } else {
            maskedPhoneNumber = getStars(phoneNumber.length());
        }

        return maskedPhoneNumber;
    }

    /**
     * 전화번호에 hyphen 넣어줌. ex) 01012345678 -> 010-1234-5678
     *
     * @param phoneNumber 10~11 자리 전화번호. ex) 01012345678
     * @return null or 010-1234-5678
     */
    public static String hyphenPhoneNumber(final String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }

        if (phoneNumber.contains("-")) {
            return phoneNumber;
        }

        if (phoneNumber.length() != 10 && phoneNumber.length() != 11) {
            return phoneNumber;
        }

        StringBuilder sb = new StringBuilder();

        sb.append(phoneNumber.substring(0, 3));
        sb.append('-');

        if (phoneNumber.length() == 10) {
            sb.append(phoneNumber.substring(3, 6));
        } else {
            sb.append(phoneNumber.substring(3, 7));
        }

        sb.append('-');
        sb.append(phoneNumber.substring(phoneNumber.length() - 4, phoneNumber.length()));

        return sb.toString();
    }

    /**
     * @param size more than 0
     * @return random string, or null
     */
    public static String generateRandomString(final int size) {
        String DEFAULT_ALLOWED_CHARS = CommonConstants.ALPHABET_CHARSET + CommonConstants.NUMERIC_CHARSET
                + CommonConstants.SPECIAL_CHARSET;
        return generateRandomString(size, DEFAULT_ALLOWED_CHARS);
    }

    /**
     * @param size         more than 0
     * @param allowedChars not empty
     * @return random string, or null
     */
    public static String generateRandomString(final int size, final String allowedChars) {
        if (size < 1 || ValidateUtility.isEmpty(allowedChars)) {
            return null;
        }

        Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < size; i++) {
            int randomIndex = random.nextInt(allowedChars.length());
            sb.append(allowedChars.charAt(randomIndex));
        }

        return sb.toString();
    }

    private static String getStars(final int length) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append('*');
        }

        return sb.toString();
    }

    public static String[] splitPhoneNumber(final String phoneNumber) {
        if (phoneNumber == null || phoneNumber.equals("")) {
            return new String[] { "", "", "" };
        }

        Pattern telPattern = Pattern.compile("^(01\\d{1}|02|0505|0502|0506|0\\d{1,2})-?(\\d{3,4})-?(\\d{4})");
        Matcher matcher = telPattern.matcher(phoneNumber);
        if (matcher.matches()) {
            return new String[] { matcher.group(1), matcher.group(2), matcher.group(3) };
        } else {
            return new String[] { "", "", "" };
        }
    }
}
