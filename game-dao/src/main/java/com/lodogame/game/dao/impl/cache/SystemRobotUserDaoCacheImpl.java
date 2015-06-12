package com.lodogame.game.dao.impl.cache;

import com.lodogame.game.dao.SystemRobotUserDao;
import com.lodogame.model.SystemRobotUser;

public class SystemRobotUserDaoCacheImpl extends BaseSystemCacheDao<SystemRobotUser> implements SystemRobotUserDao {

	@Override
	public SystemRobotUser get(int level) {
		return this.get(String.valueOf(level));
	}

}
