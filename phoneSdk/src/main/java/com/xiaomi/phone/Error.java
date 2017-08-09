package com.xiaomi.phone;

public enum Error {
    UNKNOW(-1),
    OK(0),
    PARAM_INVALID(1),
    NOT_SUPPORTED(2),
    IO_EXCEPTION(3),
    UNACTIVATEDSIM(4),
    CANCELED(5);

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
