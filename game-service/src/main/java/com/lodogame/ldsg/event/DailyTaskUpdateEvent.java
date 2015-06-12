package com.lodogame.ldsg.event;

/**
*
* <br>==========================
* <br> 公司：木屋网络
* <br> 开发：onedear
* <br> 版本：1.0
* <br> 创建时间：Oct 29, 2014 2:48:49 PM
* <br>==========================
*/
public class DailyTaskUpdateEvent extends BaseEvent {
	/**
	 * 
	 * @param userId
	 * @param taskId
	 * @param type 	1  新增
	 * 				2. 更新
	 * 				3. 删除
	 */
	public DailyTaskUpdateEvent(String userId, int taskId, int finishTimes) {
		this.userId = userId;
		this.data.put("taskId", taskId);
		this.data.put("finishTimes", finishTimes);
	}
}
