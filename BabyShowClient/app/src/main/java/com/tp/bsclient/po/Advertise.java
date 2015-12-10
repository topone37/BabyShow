package com.tp.bsclient.po;

/**
 * Created by Joney on 2015/9/29.
 * 轮播图实体类
 */
public class Advertise {
    private String id;//binner ID
    private String url;//活动图片地址
    private String contentUrl;// 活动主页详情地址


    public Advertise() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
}
