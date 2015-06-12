package com.lodogame.model;

import java.io.Serializable;

/**
*
* <br>==========================
* <br> 公司：木屋网络
* <br> 开发：onedear
* <br> 版本：1.0
* <br> 创建时间：Oct 29, 2014 2:49:51 PM
* <br>==========================
*/
public class UserDailyTask implements Serializable {

	public final static int STATUS_UNFINISH = 0;
	public final static int STATUS_FINISH_NO_REWARD = 1;
	public final static int STATUS_FINISH = 2;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 系统任务ID
	 */
	private int taskId;
	
	/**
	 * 任务类型
	 */
	private int taskType;

	/**
	 * 已经完成次数
	 */
	private int finishTimes;

	/**
	 * 更新时间
	 */
	private long updatedTime;

	/**
	 * 任务状态
	 */
	private int status;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public int getFinishTimes() {
		return finishTimes;
	}

	public void setFinishTimes(int finishTimes) {
		this.finishTimes = finishTimes;
	}

	public long getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(long updatedTime) {
		this.updatedTime = updatedTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
