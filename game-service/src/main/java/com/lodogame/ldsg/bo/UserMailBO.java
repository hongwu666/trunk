package com.lodogame.ldsg.bo;

import java.util.Date;
import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserMailBO {

	@Mapper(name = "mid")
	private int userMailId;

	/**
	 * 如果是个人邮件，这个字段表示邮件发送方的用户id
	 */
	@Mapper(name = "fuid")
	private String friendUserId;
	
	/**
	 * 1 个人 2系统
	 */
	@Mapper(name = "tp")
	private int type;

	/**
	 * 0表示普通个人邮件，1表示好友申请邮件
	 */
	@Mapper(name = "ipm")
	private int isApproveMail;
	
	@Mapper(name = "tt")
	private String title;
	
	/**
	 * 0 未读 1 已读 2邮件已删除删除， 3，同意好友申请,
	 */
	@Mapper(name = "st")
	private int status;

	/**
	 * 0 未领取 1 已经领取。
	 * 如果是好友申请邮件，0表示用户没有点击“同意”或者“不同意”，1表示用户点击了“同意”或者“不同意
	 */
	@Mapper(name = "rst")
	private int receiveStatus;

	@Mapper(name = "content")
	private String content;

	@Mapper(name = "ct")
	private Date createdTime;

	@Mapper(name = "et")
	private Date expiredTime;

	@Mapper(name = "tls")
	private List<DropToolBO> dropToolBOList;

	public int getUserMailId() {
		return userMailId;
	}

	public void setUserMailId(int userMailId) {
		this.userMailId = userMailId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 0 未读 1 已读 2邮件已删除删除， 3，同意好友申请,
	 * @return
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 0 未读 1 已读 2邮件已删除删除， 3，同意好友申请,
	 * @param status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	public int getReceiveStatus() {
		return receiveStatus;
	}

	public void setReceiveStatus(int receiveStatus) {
		this.receiveStatus = receiveStatus;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Date expiredTime) {
		this.expiredTime = expiredTime;
	}

	public List<DropToolBO> getDropToolBOList() {
		return dropToolBOList;
	}

	public void setDropToolBOList(List<DropToolBO> dropToolBOList) {
		this.dropToolBOList = dropToolBOList;
	}

	public String getFriendUserId() {
		return friendUserId;
	}

	public void setFriendUserId(String friendUserId) {
		this.friendUserId = friendUserId;
	}

	public int getIsApproveMail() {
		return isApproveMail;
	}

	public void setIsApproveMail(int isApproveMail) {
		this.isApproveMail = isApproveMail;
	}

}
