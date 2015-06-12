package com.lodogame.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserLoginLog {

	private String userId;
	
	/**
	 * 连续第几天登录
	 */
	private int day;
	
	private String lastLoginDate;
	
	private Date updatedTime;
	
	/**
	 * 七天登陆奖励领取记录
	 */
	private String recStatus;
	
	/**
	 * 是否领取了当天的奖励，0没有，1领取了
	 */
	private int recToday;

	public UserLoginLog(String userId) {
		super();
		this.userId = userId;
		this.day = 1;

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		this.lastLoginDate = format.format(now);
		this.updatedTime = now;
		this.recStatus = "0,";

	}
	
	public UserLoginLog() {
		
	}

	public int getRecToday() {
		return recToday;
	}

	public void setRecToday(int recToday) {
		this.recToday = recToday;
	}

	public String getRecStatus() {
		return recStatus;
	}

	public void setRecStatus(String recStatus) {
		this.recStatus = recStatus;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(String lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	
	
	
}
