package com.tp.bsserver.po;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/12/5.
 */
public class Album implements Serializable {
    private int aid; //相册编号
    private String aname; //相册名字
    private String aphoto;//相册封面

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getAname() {
        return aname;
    }

    public void setAname(String aname) {
        this.aname = aname;
    }

    public String getAphoto() {
        return aphoto;
    }

    public void setAphoto(String aphoto) {
        this.aphoto = aphoto;
    }
}
