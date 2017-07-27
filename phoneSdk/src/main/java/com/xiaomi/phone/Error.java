package com.xiaomi.phone;

public enum Error {
    UNKNOW(-1),
    OK(0),
    UNACTIVATED(1),
    DEV_ROOTED(2),
    PARAM_INVALID(3),
    NO_TE(4),
    TE_SIGN_FAILED(5),
    NOT_IN_SERVICE(6),
    INTERRUPTED(7),
    LOCAL_NOT_MATCH(8),
    NO_PERMISSION(9),
    NOT_SUPPORTED(10),
    CANCELED(11);

    public final int code;
    Error(int code) {
        this.code = code;
    }

    static String getResponseDesc(int code) {
        for (Error error:Error.values()) {
            if (error.code == code) {
                return error.toString();
            }
        }
        return "UNKNOW";
    }
}
