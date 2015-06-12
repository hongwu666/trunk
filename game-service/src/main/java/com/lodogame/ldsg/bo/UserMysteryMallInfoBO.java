package com.lodogame.ldsg.bo;

import java.util.Date;

public class UserMysteryMallInfoBO {

	public int normalTimes;

	public int times;

	public Date lastRefreshTime;

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public Date getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime(Date lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

	public int getNormalTimes() {
		return normalTimes;
	}

	public void setNormalTimes(int normalTimes) {
		this.normalTimes = normalTimes;
	}

}
