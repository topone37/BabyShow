package com.tp.bsserver.po;


import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/15.
 */
public class Zan implements Serializable {
	private Integer zid;
	private Integer uid;
	private Integer nid;

	public Integer getZid() {
		return zid;
	}

	public void setZid(Integer zid) {
		this.zid = zid;
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
