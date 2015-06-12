package com.lodogame.ldsg.bo;

import java.util.Date;
import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;
import com.lodogame.ldsg.helper.DropDescHelper;
import com.lodogame.model.EmpireHistory;
@Compress
public class EmpireHistoryBO {
	
	/**
	 * 敌方id
	 */
	@Mapper(name = "uid")
	private String targetUserId;
	/**
	 * 敌方用户名
	 */
	@Mapper(name = "uname")
	private String targetUsername;
	/**
	 * 状态，1防守成功，0失败
	 */
	@Mapper(name = "st")
	private int state;
	/**
	 * 被掠夺的资源
	 */
	@Mapper(name = "tools")
	private List<DropDescBO> toolIds;
	/**
	 * 创建时间
	 */
	@Mapper(name = "time")
	private Date createdTime;
	public String getTargetUserId() {
		return targetUserId;
	}
	public void setTargetUserId(String targetUserId) {
		this.targetUserId = targetUserId;
	}
	public String getTargetUsername() {
		return targetUsername;
	}
	public void setTargetUsername(String targetUsername) {
		this.targetUsername = targetUsername;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public List<DropDescBO> getToolIds() {
		return toolIds;
	}
	public void setToolIds(List<DropDescBO> toolIds) {
		this.toolIds = toolIds;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public EmpireHistoryBO(EmpireHistory empireHistory) {
		this.targetUserId=empireHistory.getTargetUserId();
		this.targetUsername=empireHistory.getTargetUsername();
		this.toolIds=DropDescHelper.parseDropTool(empireHistory.getToolIds());
		this.state=empireHistory.getState();
		this.createdTime=empireHistory.getCreatedTime();
	}
	
}
