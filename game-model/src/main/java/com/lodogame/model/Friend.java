package com.lodogame.model;

import java.util.Date;

/**
 * 好友
 * 
 * 每个对象描述一组好友对应关系
 * userIdA和userIdB 分别表示一组好友关系中的两个玩家
 * @author chengevo
 *
 */
public class Friend {

	private String userIdA;
	private String userIdB;
	private Date createdTime;

	public Friend() {
		
	}
	
	public Friend(String userIdA, String userIdB) {
		super();
		this.userIdA = userIdA;
		this.userIdB = userIdB;
		this.createdTime = new Date();
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getUserIdA() {
		return userIdA;
	}

	public void setUserIdA(String userIdA) {
		this.userIdA = userIdA;
	}

	public String getUserIdB() {
		return userIdB;
	}

	public void setUserIdB(String userIdB) {
		this.userIdB = userIdB;
	}
	
	
}
