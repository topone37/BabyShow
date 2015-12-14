package com.tp.bsserver.model;

/**
 * Created by Administrator on 2015/12/14.
 */
public class StatueInfo {
    private int code;
    private int status; // 1在线 0离线


    @Override
    public String toString() {
        return "StatueInfo{" +
                "code=" + code +
                ", status=" + status +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
