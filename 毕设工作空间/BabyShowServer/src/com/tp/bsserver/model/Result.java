package com.tp.bsserver.model;

/**
 * Created by Administrator on 2015/11/7.
 */
public class Result {
    private int code;
    private String userId;
    private String token;

    public Result() {

    }

    public Result(int code, String userId, String token) {
        this.code = code;
        this.userId = userId;
        this.token = token;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
