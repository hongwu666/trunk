package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;

public class UserOnlineLog implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private int logId;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 登录时间
	 */
	private Date loginTime;

	/**
	 * 登出时间
	 */
	private Date logoutTime;

	/**
	 * 用户IP
	 */
	private String userIp;

	/**
	 * 用户登出时的等级
	 */
	private int level;
	
	/**
	 * 用户登出时的新手引导步骤
	 */
	private int guideStep;
	
	public int getLogId() {
		return logId;
	}

	public void setLogId(int logId) {
		this.logId = logId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Date getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getGuideStep() {
		return guideStep;
	}

	public void setGuideStep(int guideStep) {
		this.guideStep = guideStep;
	}
}
