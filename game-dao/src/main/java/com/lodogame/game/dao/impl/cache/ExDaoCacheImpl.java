package com.lodogame.game.dao.impl.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import com.lodogame.game.dao.ExDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.game.dao.impl.mysql.ExDaoMysqlImpl;
import com.lodogame.model.ExpeditionConfig;
import com.lodogame.model.ExpeditionMaxExId;
import com.lodogame.model.ExpeditionNum;
import com.lodogame.model.UserExInfo;
import com.lodogame.model.UserExpeditionHero;
import com.lodogame.model.UserExpeditionVsTable;

public class ExDaoCacheImpl implements ExDao, ClearCacheOnLoginOut {

	static Logger logger = Logger.getLogger(ExDaoCacheImpl.class);

	private ExDaoMysqlImpl exDaoMysqlImpl;

	public void setExDaoMysqlImpl(ExDaoMysqlImpl exDaoMysqlImpl) {
		this.exDaoMysqlImpl = exDaoMysqlImpl;
	}

	private static AtomicLong exId;

	public void init() {
		ExpeditionMaxExId id = exDaoMysqlImpl.getMaxExId();
		exId = new AtomicLong(id.getExId() == null ? 1L : id.getExId());
		List<ExpeditionConfig> list = exDaoMysqlImpl.getLevelConfigs();
		for (ExpeditionConfig temp : list) {
			levelConfig.put(temp.getIndx(), temp);
		}
	}

	private static Map<String, UserExInfo> userInfo = new ConcurrentHashMap<String, UserExInfo>();
	private static Map<Integer, ExpeditionConfig> levelConfig = new HashMap<Integer, ExpeditionConfig>();
	private static Map<String, ExpeditionNum> userNum = new ConcurrentHashMap<String, ExpeditionNum>();

	public void clearOnLoginOut(String userId) throws Exception {
		userInfo.remove(userId);
		userNum.remove(userId);
	}

	public UserExInfo getExInfo(String userId) {
		UserExInfo info = userInfo.get(userId);
		if (info == null) {
			List<UserExpeditionVsTable> vs = exDaoMysqlImpl.getVs(userId);
			List<UserExpeditionHero> hero = exDaoMysqlImpl.getHeros(userId);
			for (UserExpeditionVsTable temp : vs) {
				hero.addAll(exDaoMysqlImpl.getHerosByExId(userId, temp.getExId()));
			}
			ExpeditionNum num = exDaoMysqlImpl.getNum(userId);
			if (num == null) {
				num = new ExpeditionNum();
				num.setUserId(userId);
				exDaoMysqlImpl.insertNum(num);
			}
			info = new UserExInfo(userId, vs, hero);
			userNum.put(userId, num);
			userInfo.put(userId, info);
		}
		return info;
	}

	public List<UserExpeditionHero> getHeros(String userId) {
		return null;
	}

	@Override
	public void updateHero(String userId, List<UserExpeditionHero> heos) {
		exDaoMysqlImpl.updateHero(userId, heos);
	}

	public void updateMyHero(String userId, List<UserExpeditionHero> heos) {
		exDaoMysqlImpl.updateMyHero(userId, heos);
	}

	public void insertHero(String userId, List<UserExpeditionHero> heos) {
		if (heos == null || heos.size() <= 0) {
			throw new NullPointerException("null of ExpeditionHeroList[" + userId + "];");
		}
		exDaoMysqlImpl.insertHero(userId, heos);
	}

	public void deleteHeroAll(String userId, Set<Long> exIds) {
		exDaoMysqlImpl.deleteHeroAll(userId, exIds);
	}

	public List<UserExpeditionVsTable> getVs(String userId) {
		return null;
	}

	public void updateVs(UserExpeditionVsTable vs) {
		exDaoMysqlImpl.updateVs(vs);
	}

	public void insertVs(UserExpeditionVsTable vs) {
		exDaoMysqlImpl.insertVs(vs);
	}

	public void deleteVs(UserExpeditionVsTable vs) {
		exDaoMysqlImpl.deleteVs(vs);
	}

	public void deleteVsAll(String userId) {
		exDaoMysqlImpl.deleteVsAll(userId);
	}

	public ExpeditionMaxExId getMaxExId() {
		return null;
	}

	@Override
	public long getExId() {
		return exId.incrementAndGet();
	}

	@Override
	public List<ExpeditionConfig> getLevelConfigs() {
		return null;
	}

	@Override
	public ExpeditionConfig getLevelConfig(int indx) {
		return levelConfig.get(indx);
	}

	public void clearCache(String userId) {
		userInfo.remove(userId);
	}

	@Override
	public ExpeditionNum getNum(String userId) {
		return userNum.get(userId);
	}

	@Override
	public void clearNum() {
		exDaoMysqlImpl.clearNum();
		userNum.clear();
	}

	@Override
	public void insertNum(ExpeditionNum num) {
		exDaoMysqlImpl.insertNum(num);
		userNum.put(num.getUserId(), num);
	}

	@Override
	public void updateNum(ExpeditionNum num) {
		exDaoMysqlImpl.updateNum(num);
	}
}
