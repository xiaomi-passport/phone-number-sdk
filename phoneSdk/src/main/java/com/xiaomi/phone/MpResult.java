package com.xiaomi.phone;

public class MpResult {
    int mResponse;
    String mMessage;

    public MpResult(Error response, String message) {
        this(response.code, message);
    }

    public MpResult(int response, String message) {
        mResponse = response;
        if (message == null || message.trim().length() == 0) {
            mMessage = Error.getResponseDesc(response);
        }
        else {
            mMessage = message + " (response: " + Error.getResponseDesc(response) + ")";
        }
    }
    public int getResponse() { return mResponse; }
    public String getMessage() { return mMessage; }
    public boolean isSuccess() { return mResponse == Error.OK.code; }
    public boolean isFailure() { return !isSuccess(); }
    public String toString() { return "IabResult: " + getMessage(); }
}
