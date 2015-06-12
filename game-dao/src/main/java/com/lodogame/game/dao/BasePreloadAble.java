package com.lodogame.game.dao;

import org.apache.log4j.Logger;

import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;

public abstract class BasePreloadAble implements PreloadAble, ReloadAble {

	private static final Logger logger = Logger.getLogger(BasePreloadAble.class);

	@Override
	public void initCache() {

		logger.debug("start to init cache data[" + this.getClass().getSimpleName() + "]");
		long start = System.currentTimeMillis();

		initData();

		long end = System.currentTimeMillis();
		logger.debug("finish init cache data[" + this.getClass().getSimpleName() + "], time[" + (end - start) + "]");

	}

	@Override
	public void reload() {
		initCache();
	}

	@Override
	public void init() {
		initCache();
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

	abstract protected void initData();
}
