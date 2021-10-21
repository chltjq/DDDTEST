package com.lguplus.ukids.admin.constants;

public final class CommonConstants {
    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";

    public static final String PATH_DELIMITER = "/";
    public static final String YES_FLAG = "Y";
    public static final String NO_FLAG = "N";

    public static final int ITERATION_COUNT = 10005;
    public static final int KEY_LENGTH = 512;

    public static final String PW_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{10,20}$";
    public static final String ENV_CODE_PRD = "prd";
    public static final String ENV_CODE_STG = "stg";
    public static final String ENV_CODE_DEV = "dev";
    public static final String ENV_CODE_LOC = "loc";
    public static final String ENV_CODE_DEFAULT = "default";

    public static final String ALPHABET_CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static final String NUMERIC_CHARSET = "0123456789";
    public static final String SPECIAL_CHARSET = "!@#$%^&";
}
