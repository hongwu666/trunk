package com.lodogame.model;

import java.util.Date;

public class UserPersonalMail {

	
	private int id;
	private String userId;
	
	/**
	 * 邮件发送方的用户id
	 */
	private String friendUserId;
	
	private String title;
	
	private String content;
	
	/**
	 * 0表示普通个人邮件，1表示好友申请邮件
	 */
	private int type;
	
	/**
	 * 0 未读 1 已读 2邮件已删除删除， 3，同意好友申请,
	 */
	private int status;
	
	/**
	 * 如果是好友申请邮件，0表示用户没有点击“同意”或者“不同意”，1表示用户点击了“同意”或者“不同意
	 */
	private int isProcessed;
	
	private Date createdTime = new Date();

	/**
	 * 如果是好友申请邮件，0表示用户没有点击“同意”或者“不同意”，1表示用户点击了“同意”或者“不同意
	 */
	public int getIsProcessed() {
		return isProcessed;
	}

	/**
	 * 如果是好友申请邮件，0表示用户没有点击“同意”或者“不同意”，1表示用户点击了“同意”或者“不同意
	 */
	public void setIsProcessed(int isProcessed) {
		this.isProcessed = isProcessed;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 0 未读 1 已读 2邮件已删除删除， 3，同意好友申请,
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 0 未读 1 已读 2邮件已删除删除， 3，同意好友申请,
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 邮件发送方的用户id
	 */
	public String getFriendUserId() {
		return friendUserId;
	}

	/**
	 * 邮件发送方的用户id
	 */
	public void setFriendUserId(String friendUserId) {
		this.friendUserId = friendUserId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 0表示普通个人邮件，1表示好友申请邮件
	 */
	public int getType() {
		return type;
	}

	/**
	 * 0表示普通个人邮件，1表示好友申请邮件
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 0表示未阅读，1表示已经阅读, 2同意好友申请
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 0表示未阅读，1表示已经阅读, 2同意好友申请
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
	
}
