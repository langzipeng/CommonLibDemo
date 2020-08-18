package com.fast.common.net.exception;

/**
 * 业务级别异常、错误
 */
public class ApiException extends IllegalArgumentException{

    private int code;

    private String massage;

    public ApiException(int code, String msg) {
        super(msg);
        this.code = code;
        this.massage = msg;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public int getCode() {
        return code;
    }
}
