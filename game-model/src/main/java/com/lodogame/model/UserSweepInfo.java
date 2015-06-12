/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 * Since 2008 - 2013
 */

package com.lodogame.model;

public class UserSweepInfo implements java.io.Serializable {
	private static final long serialVersionUID = 1;
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	private java.lang.String userId;
	private java.util.Date createdTime;
	private java.util.Date endTime;
	private java.util.Date updatedTime;
	//columns END

	public UserSweepInfo(){
	}

	public UserSweepInfo(
		java.lang.String userId,
		java.lang.Integer groupForcesId,
		java.util.Date createdTime
	){
		this.userId = userId;
		this.createdTime = createdTime;
	}

	public void setUserId(java.lang.String value) {
		this.userId = value;
	}
	
	public java.lang.String getUserId() {
		return this.userId;
	}


	public void setCreatedTime(java.util.Date value) {
		this.createdTime = value;
	}
	
	public java.util.Date getCreatedTime() {
		return this.createdTime;
	}

	public void setEndTime(java.util.Date value) {
		this.endTime = value;
	}
	
	public java.util.Date getEndTime() {
		return this.endTime;
	}
	public void setUpdatedTime(java.util.Date value) {
		this.updatedTime = value;
	}
	
	public java.util.Date getUpdatedTime() {
		return this.updatedTime;
	}
}

