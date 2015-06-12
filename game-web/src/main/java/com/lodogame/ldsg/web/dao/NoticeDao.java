package com.lodogame.ldsg.web.dao;

import com.lodogame.ldsg.web.model.Notice;

public interface NoticeDao {
	/**
	 * 根据serverId和渠道获取公告
	 * 
	 * @param serverId
	 * @param partnerId
	 * @return
	 */
	public Notice getNotice(String serverId, String partnerId);

	/**
	 * 更新公告
	 * 
	 * @param notice
	 * @return
	 */
	boolean updateNotice(Notice notice);
}
