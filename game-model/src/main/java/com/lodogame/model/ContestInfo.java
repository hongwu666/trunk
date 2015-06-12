package com.lodogame.model;

import java.util.Date;

public class ContestInfo {

	private int week;

	private int status;

	private Date nextStatusTime;

	private Date createdTime;

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getNextStatusTime() {
		return nextStatusTime;
	}

	public void setNextStatusTime(Date nextStatusTime) {
		this.nextStatusTime = nextStatusTime;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

}
