package com.tp.bsclient.po;


import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/15.
 */
public class Images implements Serializable {
	private Integer imgid;
	private Integer nid;
	private String imgname;

	public Integer getImgid() {
		return imgid;
	}

	public void setImgid(Integer imgid) {
		this.imgid = imgid;
	}

	public Integer getNid() {
		return nid;
	}

	public void setNid(Integer nid) {
		this.nid = nid;
	}

	public String getImgname() {
		return imgname;
	}

	public void setImgname(String imgname) {
		this.imgname = imgname;
	}

}
