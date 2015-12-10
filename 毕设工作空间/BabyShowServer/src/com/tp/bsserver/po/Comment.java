package com.tp.bsserver.po;

import java.io.Serializable;
import java.sql.Date;

public class Comment implements Serializable {
	private Integer comid;
	private Integer uid;
	private Integer nid;
	private String content;
	private Date date;

	public Integer getComid() {
		return comid;
	}

	public void setComid(Integer comid) {
		this.comid = comid;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getNid() {
		return nid;
	}

	public void setNid(Integer nid) {
		this.nid = nid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
