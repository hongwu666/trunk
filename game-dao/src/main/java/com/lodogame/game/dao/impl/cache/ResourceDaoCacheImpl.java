package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.math.RandomUtils;

import com.lodogame.game.dao.ResourceDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.game.dao.impl.mysql.ResourceDaoMysqlImpl;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.model.ResourceGk;
import com.lodogame.model.ResourceGkConfig;
import com.lodogame.model.ResourceGkPkLog;
import com.lodogame.model.ResourceGkStart;
import com.lodogame.model.ResourceNum;
import com.lodogame.model.ResourceStartConfig;
import com.lodogame.model.ResourceUserInfo;

public class ResourceDaoCacheImpl implements ResourceDao, ClearCacheOnLoginOut {

	private Map<Integer, List<ResourceStartConfig>> startConfig = new HashMap<Integer, List<ResourceStartConfig>>(); // startNum..{start}
																														// 刷星
	private Map<Integer, Map<Integer, Map<Integer, Map<Integer, ResourceGkConfig>>>> gkConfig = new HashMap<Integer, Map<Integer, Map<Integer, Map<Integer, ResourceGkConfig>>>>(); // map,dif{gk{start{config}}}

	private Map<String, ResourceUserInfo> userMap = new ConcurrentHashMap<String, ResourceUserInfo>();

	private ResourceDaoMysqlImpl resourceDaoMysqlImpl;

	public void setResourceDaoMysqlImpl(ResourceDaoMysqlImpl resourceDaoMysqlImpl) {
		this.resourceDaoMysqlImpl = resourceDaoMysqlImpl;
	}

	public void init() {
		List<ResourceStartConfig> sc = resourceDaoMysqlImpl.getStartConfigs();
		List<ResourceGkConfig> gc = resourceDaoMysqlImpl.getGkConfigs();

		for (ResourceStartConfig temp : sc) {
			if (!startConfig.containsKey(temp.getCurrStart())) {
				startConfig.put(temp.getCurrStart(), new ArrayList<ResourceStartConfig>());
			}
			startConfig.get(temp.getCurrStart()).add(temp);
		}

		for (ResourceGkConfig temp : gc) {
			if (!gkConfig.containsKey(temp.getMapId())) {
				gkConfig.put(temp.getMapId(), new HashMap<Integer, Map<Integer, Map<Integer, ResourceGkConfig>>>());
			}
			if (!gkConfig.get(temp.getMapId()).containsKey(temp.getDif())) {
				gkConfig.get(temp.getMapId()).put(temp.getDif(), new HashMap<Integer, Map<Integer, ResourceGkConfig>>());
			}
			if (!gkConfig.get(temp.getMapId()).get(temp.getDif()).containsKey(temp.getGk())) {
				gkConfig.get(temp.getMapId()).get(temp.getDif()).put(temp.getGk(), new HashMap<Integer, ResourceGkConfig>());
			}
			gkConfig.get(temp.getMapId()).get(temp.getDif()).get(temp.getGk()).put(temp.getStartNum(), temp);
		}
	}

	private static String dateStr = DateUtils.getDate(new Date());

	public ResourceUserInfo getInfo(String userId) {
		ResourceUserInfo userInfo = null;
		if (!userMap.containsKey(userId)) {
			List<ResourceGk> gk = resourceDaoMysqlImpl.getGk(userId, dateStr);
			List<ResourceNum> num = resourceDaoMysqlImpl.getNum(userId, dateStr);
			for (ResourceGk temp : gk) {
				temp.setStarts(resourceDaoMysqlImpl.getGkStart(temp.getId()));
				temp.setLogs(resourceDaoMysqlImpl.getGkPkLog(temp.getId()));
			}
			userInfo = new ResourceUserInfo(gk, num, userId);
			userMap.put(userId, userInfo);
		} else {
			userInfo = userMap.get(userId);
		}
		return userInfo;
	}

	public List<ResourceNum> getNum(String userId, String date) {
		return resourceDaoMysqlImpl.getNum(userId, date);
	}

	public void updateNum(ResourceNum num) {
		resourceDaoMysqlImpl.updateNum(num);
	}

	public void insertNum(ResourceNum num) {
		resourceDaoMysqlImpl.insertNum(num);
	}

	public List<ResourceGk> getGk(String userId, String date) {
		return getInfo(userId).getGks();
	}

	public void insertGk(ResourceGk gk) {
		getInfo(gk.getUserId()).getGks().add(gk);
		resourceDaoMysqlImpl.insertGk(gk);
	}

	public List<ResourceGkStart> getGkStart(int ids) {
		return null;
	}

	public void insertGkStart(ResourceGkStart start) {
		resourceDaoMysqlImpl.insertGkStart(start);
		;
	}

	public void updateGkStart(ResourceGkStart start) {
		resourceDaoMysqlImpl.updateGkStart(start);
	}

	public List<ResourceGkPkLog> getGkPkLog(int ids) {
		return null;
	}

	public void insertGkPkLog(ResourceGkPkLog log) {
		resourceDaoMysqlImpl.insertGkPkLog(log);
	}

	public void updateGkPkLog(ResourceGkPkLog log) {
		resourceDaoMysqlImpl.updateGkPkLog(log);
	}

	public void clearOnLoginOut(String userId) throws Exception {
		userMap.remove(userId);
	}

	@Override
	public int getMaxIds() {
		return resourceDaoMysqlImpl.getMaxIds();
	}

	@Override
	public List<ResourceStartConfig> getStartConfigs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ResourceGkConfig> getGkConfigs() {
		return null;
	}

	public ResourceGkConfig getGkConfig(int type, int dif, int g, int start) {
		return gkConfig.get(type).get(dif).get(g).get(start);
	}

	public int getStartByCurr(int curr) {
		List<ResourceStartConfig> config = startConfig.get(curr);
		int num = RandomUtils.nextInt(10000) + 1; // 1~10000
		for (ResourceStartConfig temp : config) {
			if (temp.getMaxNum() == 0)
				continue;
			if (temp.in(num)) {
				return temp.getTargetStart();
			}
		}
		return 1;
	}

	public void replace() {
		dateStr = DateUtils.getDate(new Date());
		userMap.clear();
	}

	public void reset(int id) {
		resourceDaoMysqlImpl.reset(id);
	}

}
