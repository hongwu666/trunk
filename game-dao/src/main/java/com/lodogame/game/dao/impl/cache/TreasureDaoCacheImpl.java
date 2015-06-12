package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.TreasureDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.game.dao.impl.mysql.TreasureDaoMysqlImpl;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.model.TreasureConfigInfo;
import com.lodogame.model.TreasureUserInfo;

public class TreasureDaoCacheImpl implements TreasureDao, ClearCacheOnLoginOut {

	private Map<Integer, TreasureConfigInfo> config = new HashMap<Integer, TreasureConfigInfo>(); // grade-config

	private Map<String, List<TreasureUserInfo>> userInfo = new ConcurrentHashMap<String, List<TreasureUserInfo>>(); // userid-userinfo

	private int maxConfig = -1;

	private TreasureDaoMysqlImpl sqlDaoImpl;

	public void setSqlDaoImpl(TreasureDaoMysqlImpl sqlDaoImpl) {
		this.sqlDaoImpl = sqlDaoImpl;
	}

	public void init() {
		List<TreasureConfigInfo> cInfo = sqlDaoImpl.getConfig();
		for (TreasureConfigInfo temp : cInfo) {
			if (temp.getGrade() > maxConfig)
				maxConfig = temp.getGrade();
			config.put(temp.getGrade(), temp);
		}
	}

	public List<TreasureConfigInfo> getConfig() {
		return null;
	}

	public List<TreasureUserInfo> getUserInfo(String userId, String date) {
		List<TreasureUserInfo> uInfo = null;
		if (!userInfo.containsKey(userId)) {
			uInfo = sqlDaoImpl.getUserInfo(userId, date);
			if (uInfo == null)
				uInfo = new ArrayList<TreasureUserInfo>();
			userInfo.put(userId, uInfo);
		} else {
			uInfo = userInfo.get(userId);
		}
		return uInfo;
	}

	public void updateUserInfo(TreasureUserInfo info) {
		sqlDaoImpl.updateUserInfo(info);
	}

	public void insertUserInfo(TreasureUserInfo info) {
		getUserInfo(info.getUserId(), DateUtils.getDate(new Date())).add(info);
		sqlDaoImpl.insertUserInfo(info);
	}

	public TreasureConfigInfo getConfigByGrade(int grade) {
		if (grade > maxConfig)
			grade = maxConfig;
		return config.get(grade);
	}

	public void replace() {
		userInfo.clear();
	}

	public void clearOnLoginOut(String userId) throws Exception {
		userInfo.remove(userId);
	}
}
