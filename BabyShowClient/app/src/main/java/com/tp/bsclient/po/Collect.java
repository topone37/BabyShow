package com.tp.bsclient.po;


import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/15.
 */
public class Collect implements Serializable{
	private Integer colid;
	private Integer uid;
	private Integer nid;

	public Integer getColid() {
		return colid;
	}

	public void setColid(Integer colid) {
		this.colid = colid;
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

}
