package com.lodogame.game.dao.impl.redis;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;

import com.lodogame.game.dao.UserDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.User;
import com.lodogame.model.UserHeroInfo;
import com.lodogame.model.UserPowerInfo;
import com.lodogame.model.UserShareLog;
import com.mysql.jdbc.StringUtils;

public class UserDaoRedisImpl implements UserDao {

	public boolean add(User user) {

		String key = RedisKey.getUserCacheKey();
		String key2 = RedisKey.getUserByPlayerIdCacheKey();
		String json = Json.toJson(user);
		JedisUtils.setFieldToObject(key, user.getUserId(), json);
		JedisUtils.setFieldToObject(key2, Long.toString(user.getLodoId()), json);
		return true;
	}

	public User get(String userId) {

		String key = RedisKey.getUserCacheKey();
		String json = JedisUtils.getFieldFromObject(key, userId);
		if (!StringUtils.isNullOrEmpty(json)) {
			return Json.toObject(json, User.class);
		}

		return null;
	}

	@Override
	public User getByPlayerId(String playerId) {
		String key = RedisKey.getUserByPlayerIdCacheKey();
		String json = JedisUtils.getFieldFromObject(key, playerId);
		if (!StringUtils.isNullOrEmpty(json)) {
			return Json.toObject(json, User.class);
		}

		return null;
	}

	public User getByName(String username) {
		throw new NotImplementedException();
	}

	public boolean addCopper(String userId, int copper) {
		remove(userId);
		return true;
	}

	@Override
	public boolean resetPowerAddTime(String userId, Date powerAddTime) {
		remove(userId);
		return true;
	}

	public boolean updateVIPLevel(String userId, int VIPLevel) {
		remove(userId);
		return true;
	}

	public boolean reduceCopper(String userId, int copper) {
		remove(userId);
		return true;
	}

	public boolean addGold(String userId, int gold) {
		remove(userId);
		return true;
	}

	public boolean reduceGold(String userId, int gold) {
		remove(userId);
		return true;
	}

	public boolean addExp(String userId, int exp, int level, int resumePower) {
		remove(userId);
		return true;
	}

	@Override
	public boolean addPower(String userId, int power, int maxPower, Date powerAddTime) {
		remove(userId);
		return true;
	}

	@Override
	public boolean reducePowre(String userId, int power) {
		remove(userId);
		return true;
	}

	@Override
	public Set<String> getOnlineUserIdList() {
		String key = RedisKey.getOnlineUserKey();
		return JedisUtils.smembers(key);
	}

	private void remove(String userId) {
		String key = RedisKey.getUserCacheKey();
		JedisUtils.delFieldFromObject(key, userId);
	}

	@Override
	public boolean setOnline(String userId, boolean online) {
		String key = RedisKey.getOnlineUserKey();
		if (online) {
			JedisUtils.sadd(key, userId);
		} else {
			JedisUtils.srem(key, userId);
		}
		return true;
	}

	@Override
	public boolean isOnline(String userId) {
		String key = RedisKey.getOnlineUserKey();
		return JedisUtils.sIsMember(key, userId);
	}

	@Override
	public boolean cleanCacheData(String userId) {

		String equiCacheKey = RedisKey.getUserEquipListCacheKey(userId);
		JedisUtils.delete(equiCacheKey);

		String heroCacheKey = RedisKey.getUserHeroListCacheKey(userId);
		JedisUtils.delete(heroCacheKey);

		String userCacheKey = RedisKey.getUserCacheKey();
		JedisUtils.delFieldFromObject(userCacheKey, userId);

		String pkInfoKey = RedisKey.getPkInfoKey();
		JedisUtils.delFieldFromObject(pkInfoKey, userId);

		String sweepKey = RedisKey.getUserSweepCacheKey();
		JedisUtils.delFieldFromObject(sweepKey, userId);
		return true;
	}

	@Override
	public List<User> listOrderByLevelDesc(int offset, int size) {
		return null;
	}

	@Override
	public List<User> listOrderByCopperDesc(int offset, int size) {
		return null;
	}

	@Override
	public List<String> getAllUserIds() {
		throw new NotImplementedException();
	}

	@Override
	public boolean reduceExp(String userId, int amount) {
		throw new NotImplementedException();
	}

	@Override
	public boolean banUser(String userId, Date dueTime) {
		remove(userId);
		return true;
	}

	@Override
	public boolean reduceReputation(String userId, int reputation) {
		remove(userId);
		return true;
	}

	@Override
	public boolean updateUserLevel(String userId, int level, int exp) {
		remove(userId);
		return true;
	}

	@Override
	public boolean addReputation(String userId, int reputation) {
		remove(userId);
		return true;
	}

	@Override
	public UserShareLog getUserLastShareLog(String userId) {
		throw new NotImplementedException();
	}

	@Override
	public boolean addUserShareLog(UserShareLog userShareLog) {
		throw new NotImplementedException();
	}

	@Override
	public boolean reduceMind(String userId, int mind) {
		remove(userId);
		return true;
	}

	@Override
	public int getUserPower(String userId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean addMind(String userId, int mind) {
		remove(userId);
		return true;
	}

	@Override
	public boolean bannedToPost(String userId) {
		remove(userId);
		return true;
	}

	@Override
	public List<User> getRandByLevel(int lowerLevel, int upperLevel, String userId) {
		throw new NotImplementedException();
	}

	@Override
	public boolean reduceMuhon(String userId, int amount) {
		remove(userId);
		return true;
	}

	@Override
	public boolean reduceSoul(String userId, int amount) {
		remove(userId);
		return true;
	}

	@Override
	public boolean reduceEnergy(String userId, int amount) {
		remove(userId);
		return true;
	}

	@Override
	public boolean addMuhon(String userId, int amount) {
		remove(userId);
		return true;
	}

	public boolean addSoul(String userId, int amount) {
		remove(userId);
		return true;
	}

	@Override
	public boolean reduceMingwen(String userId, int amount) {
		remove(userId);
		return true;
	}

	@Override
	public boolean addMingwen(String userId, int amount) {
		remove(userId);
		return true;
	}

	@Override
	public List<User> getAllUser() {
		return null;
	}

	@Override
	public boolean cleanOnline() {
		String key = RedisKey.getOnlineUserKey();
		JedisUtils.delete(key);
		return true;
	}

	@Override
	public List<User> getByGTLevel(String userId, int userLevel, int needLevel, int limit) {
		throw new NotImplementedException();
	}

	@Override
	public List<User> getByLTLevel(String userId, int level, int limit) {
		throw new NotImplementedException();
	}

	@Override
	public List<User> getByGTLevel(String userId, int level) {
		throw new NotImplementedException();
	}

	@Override
	public boolean addCoin(String userId, int amount) {
		remove(userId);
		return true;
	}

	@Override
	public boolean reduceCoin(String userId, int amount) {
		remove(userId);
		return true;
	}

	@Override
	public boolean addHonour(String userId, int amount) {
		remove(userId);
		return true;
	}

	@Override
	public boolean reduceHonour(String userId, int amount) {
		remove(userId);
		return true;
	}

	@Override
	public List<User> getOut(String userId, int level) {
		throw new NotImplementedException();
	}

	@Override
	public UserPowerInfo getUserIdByPowerRand(String userId, int type, int min, int max) {
		throw new NotImplementedException();
	}

	@Override
	public UserPowerInfo getUserIdRand(String userId, int minPower) {
		throw new NotImplementedException();
	}

	@Override
	public void updateUserPower(String userId, int cap) {
		throw new NotImplementedException();
	}

	@Override
	public int getUserMaxPower(String userId) {
		throw new NotImplementedException();
	}

	@Override
	public void updateUserMaxPower(String userId, int cap) {
		throw new NotImplementedException();
	}

	@Override
	public void updateHeroAtt(UserHeroInfo userHeroInfo) {
		throw new NotImplementedException();

	}

	@Override
	public boolean deleteHeroAtt(String userHeroId) {
		throw new NotImplementedException();
	}

	@Override
	public boolean addSkill(String userId, int amount) {
		remove(userId);
		return true;
	}
}
