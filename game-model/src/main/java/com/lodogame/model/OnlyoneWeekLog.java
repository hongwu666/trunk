package com.lodogame.model;

import java.util.Date;

public class OnlyoneWeekLog {

	private int week;

	private String Date;

	private Date createdTime;

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

}
