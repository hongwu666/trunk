package com.lodogame.game.dao;

import java.util.Collection;

import com.lodogame.model.DailyTask;

/**
*
* <br>==========================
* <br> 公司：木屋网络
* <br> 开发：onedear
* <br> 版本：1.0
* <br> 创建时间：Oct 29, 2014 2:49:41 PM
* <br>==========================
*/
public interface ActivityDailyTaskDao {

	public Collection<DailyTask> getActivityDailyTaskList();

	public DailyTask getActivityDailyTask(int taskId);
}
