package com.tp.bsserver.po;


import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/15.
 */
public class Friends implements Serializable {
	private Integer fid;
	private Integer uid1;
	private Integer uid2;

	public Integer getFid() {
		return fid;
	}

	public void setFid(Integer fid) {
		this.fid = fid;
	}

	public Integer getUid1() {
		return uid1;
	}

	public void setUid1(Integer uid1) {
		this.uid1 = uid1;
	}

	public Integer getUid2() {
		return uid2;
	}

	public void setUid2(Integer uid2) {
		this.uid2 = uid2;
	}

}
