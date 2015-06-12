package com.lodogame.game.dao.impl.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.MeridianDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.game.dao.impl.mysql.MeridianDaoMysqlImpl;
import com.lodogame.model.MeridianUser;
import com.lodogame.model.MeridianUserInfo;

public class MeridianDaoCacheImpl implements MeridianDao, ClearCacheOnLoginOut {

	private MeridianDaoMysqlImpl meridianDaoMysqlImpl;

	public void setMeridianDaoMysqlImpl(MeridianDaoMysqlImpl meridianDaoMysqlImpl) {
		this.meridianDaoMysqlImpl = meridianDaoMysqlImpl;
	}

	private Map<String, MeridianUser> userInfo = new ConcurrentHashMap<String, MeridianUser>();

	public MeridianUser getUserMeridian(String userId) {
		MeridianUser user = userInfo.get(userId);
		if (user == null) {
			user = new MeridianUser(getMeridianUserInfo(userId), userId);
			userInfo.put(userId, user);
		}
		return user;
	}

	public List<MeridianUserInfo> getMeridianUserInfo(String userId) {
		return meridianDaoMysqlImpl.getMeridianUserInfo(userId);
	}

	public void insertMeridianUserInfo(MeridianUserInfo info) {
		getUserMeridian(info.getUserId()).addMeridian(info);
		meridianDaoMysqlImpl.insertMeridianUserInfo(info);
	}

	public void updateMeridianUserInfo(MeridianUserInfo info) {
		meridianDaoMysqlImpl.updateMeridianUserInfo(info);
	}

	public void clearOnLoginOut(String userId) throws Exception {
		userInfo.remove(userId);
	}

	public void init() {

	}

}
