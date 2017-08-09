package com.xiaomi.phone;

public enum Error {
    UNKNOW(-1),
    OK(0),
    PARAM_INVALID(1),
    NO_PERMISSION(2),
    NOT_SUPPORTED(3),
    IO_EXCEPTION(4),
    UNACTIVATEDSIM(5),
    CANCELED(6);

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
