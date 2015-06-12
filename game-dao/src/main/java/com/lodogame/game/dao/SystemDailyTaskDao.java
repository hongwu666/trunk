package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemDailyTask;

/**
 * 
 * <br>=
 * ========================= <br>
 * 公司：木屋网络 <br>
 * 开发：onedear <br>
 * 版本：1.0 <br>
 * 创建时间：Oct 29, 2014 2:49:41 PM <br>=
 * =========================
 */
public interface SystemDailyTaskDao {

	public SystemDailyTask get(int taskId);

	public List<SystemDailyTask> getList();
}
