package com.lodogame.ldsg.web.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.web.dao.NoticeDao;
import com.lodogame.ldsg.web.dao.PackageInfoDao;
import com.lodogame.ldsg.web.model.Notice;
import com.lodogame.ldsg.web.service.WebApiService;

public class WebApiServiceImpl implements WebApiService {

	public static Logger LOG = Logger.getLogger(WebApiServiceImpl.class);

	@Autowired
	protected PackageInfoDao packageInfoDao;

	@Autowired
	protected NoticeDao noticeDao;

	@Override
	public Notice getNotice(String serverId, String partnerId) {
		return noticeDao.getNotice(serverId, partnerId);
	}

	@Override
	public Notice updateNotice(String serverId, int isEnable, String title, String content, String partnerId) {
		Notice notice = new Notice();
		notice.setServerId(serverId);
		notice.setTitle(title);
		notice.setContent(content);
		notice.setIsEnable(isEnable);
		notice.setPartnerId(partnerId);
		noticeDao.updateNotice(notice);
		return notice;
	}

}
