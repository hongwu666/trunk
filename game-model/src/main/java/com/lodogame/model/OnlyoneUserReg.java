package com.lodogame.model;

import java.util.Date;

public class OnlyoneUserReg implements Cloneable {

	private String userId;

	private String username;

	/**
	 * 状态
	 */
	private int status;

	/**
	 * 总的次数
	 */
	private int totalCount;

	/**
	 * 积分累积次数
	 */
	private int pointCount;

	/**
	 * 胜利总次数
	 */
	private int winCount;

	/**
	 * 连胜次数
	 */
	private int winTimes;

	/**
	 * 精力
	 */
	private int vigour;

	/**
	 * 积分
	 */
	private double point;

	/**
	 * 增加精力时间
	 */
	private Date addVigourTime;

	/**
	 * cd结束时间
	 */
	private Date endCdTime = new Date();

	/**
	 * 是否机器人
	 */
	private int isRobot = 0;

	/**
	 * 排名
	 */
	private int rank;

	/**
	 * 创建时间
	 */
	private Date createdTime;

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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getWinCount() {
		return winCount;
	}

	public void setWinCount(int winCount) {
		this.winCount = winCount;
	}

	public int getWinTimes() {
		return winTimes;
	}

	public void setWinTimes(int winTimes) {
		this.winTimes = winTimes;
	}

	public int getVigour() {
		return vigour;
	}

	public void setVigour(int vigour) {
		this.vigour = vigour;
	}

	public Date getAddVigourTime() {
		return addVigourTime;
	}

	public void setAddVigourTime(Date addVigourTime) {
		this.addVigourTime = addVigourTime;
	}

	public double getPoint() {
		return point;
	}

	public void setPoint(double point) {
		this.point = point;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getEndCdTime() {
		return endCdTime;
	}

	public void setEndCdTime(Date endCdTime) {
		this.endCdTime = endCdTime;
	}

	public int getIsRobot() {
		return isRobot;
	}

	public void setIsRobot(int isRobot) {
		this.isRobot = isRobot;
	}

	public int getPointCount() {
		return pointCount;
	}

	public void setPointCount(int pointCount) {
		this.pointCount = pointCount;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	@Override
	public Object clone() {
		OnlyoneUserReg reg = new OnlyoneUserReg();
		reg.setUserId(userId);
		reg.setPoint(point);
		reg.setAddVigourTime(addVigourTime);
		reg.setStatus(status);
		reg.setTotalCount(totalCount);
		reg.setUsername(username);
		reg.setVigour(vigour);
		reg.setWinCount(winCount);
		reg.setWinTimes(winTimes);
		return reg;
	}

}
