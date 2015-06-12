package com.lodogame.ldsg.bo;

import java.io.Serializable;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserForcesBO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;

	private int sceneId;

	@Mapper(name = "fid")
	private int groupId;

	@Mapper(name = "stat")
	private int status;

	@Mapper(name = "ts")
	private int times;

	@Mapper(name = "ps")
	private int passStar;

	@Mapper(name = "frsm")
	private int resetMoney;

	public int getPassStar() {
		return passStar;
	}

	public void setPassStar(int passStar) {
		this.passStar = passStar;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getResetMoney() {
		return resetMoney;
	}

	public void setResetMoney(int resetMoney) {
		this.resetMoney = resetMoney;
	}

}
