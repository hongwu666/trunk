package com.lodogame.model;

import java.util.Date;

/**
 * 
 * 
 * @author shixiangwen
 * 
 */
public class ContestUser {

	private String userId;

	private String username;

	private int deadRound;

	private int ind;

	private int arrayFinish;

	private int selectTeam;

	private int isEmpty;

	private int roundWin;

	private int level;

	private int imgId;

	private int vip;

	private String modelId;

	private Date createdTime;

	private Date updatedTime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getDeadRound() {
		return deadRound;
	}

	public void setDeadRound(int deadRound) {
		this.deadRound = deadRound;
	}

	public int getInd() {
		return ind;
	}

	public void setInd(int ind) {
		this.ind = ind;
	}

	public int getArrayFinish() {
		return arrayFinish;
	}

	public void setArrayFinish(int arrayFinish) {
		this.arrayFinish = arrayFinish;
	}

	public int getSelectTeam() {
		return selectTeam;
	}

	public void setSelectTeam(int selectTeam) {
		this.selectTeam = selectTeam;
	}

	public int getRoundWin() {
		return roundWin;
	}

	public void setRoundWin(int roundWin) {
		this.roundWin = roundWin;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public int getIsEmpty() {
		return isEmpty;
	}

	public void setIsEmpty(int isEmpty) {
		this.isEmpty = isEmpty;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

}
