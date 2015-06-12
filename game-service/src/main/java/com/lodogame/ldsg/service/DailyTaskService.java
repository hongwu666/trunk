package com.lodogame.ldsg.service;

import java.util.List;

import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.UserDailyTaskBO;

/**
*
* <br>==========================
* <br> 公司：木屋网络
* <br> 开发：onedear
* <br> 版本：1.0
* <br> 创建时间：Oct 28, 2014 6:40:24 PM
* <br>==========================
*/
public interface DailyTaskService {

	/**
	 * 获取用户任务
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserDailyTaskBO> getUserDailyTaskList(String userId);

	/**
	 * 领取任务
	 * 
	 * @param userId
	 * @param taskId
	 * @return
	 */
	public CommonDropBO receive(String userId, int taskId);

	/**
	 * 获取用户单个任务
	 * 
	 * @param userId
	 * @param taskId
	 * @return
	 */
	public UserDailyTaskBO get(String userId, int taskId);

	/**
	 * 修改日常任务进度
	 * @param userId 
	 * @param type 对应 ActivityDailyTask 的各个静态变量, 从1-19
	 * @param incr 这次增加的值
	 */
	public void sendUpdateDailyTaskEvent(String userId, int type, int incr);
	/**
	 * 通过登记增加任务
	 * @param userId
	 * @param lv
	 */
	public void addTaskByLv(String userId, int lv);

	public void initDailyTask(String userId);

	public void updateTask(String userId, int taskId, int incr);
	
}
