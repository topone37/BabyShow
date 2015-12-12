package com.tp.bsclient.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/12/8.
 */
public class News implements Serializable {


    private int nid;//动态ID
    private String head;//头像
    private String nickname;//昵称
    private String intro;//简介
    private String content;//内容
    private String date; //日期
    private int colNum;//收藏数
    private boolean colStatue;//收藏数
    private int zanNum;//点赞数
    private boolean zanStatue;//点赞状态
    private int comNum;//评论数量
    private List<String> imgs;

    public int getColNum() {
        return colNum;
    }

    public void setColNum(int colNum) {
        this.colNum = colNum;
    }

    public boolean isColStatue() {
        return colStatue;
    }

    public void setColStatue(boolean colStatue) {
        this.colStatue = colStatue;
    }

    public int getZanNum() {
        return zanNum;
    }

    public void setZanNum(int zanNum) {
        this.zanNum = zanNum;
    }

    public boolean isZanStatue() {
        return zanStatue;
    }

    public void setZanStatue(boolean zanStatue) {
        this.zanStatue = zanStatue;
    }

    public int getComNum() {
        return comNum;
    }

    public void setComNum(int comNum) {
        this.comNum = comNum;
    }

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
