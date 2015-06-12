package com.lodogame.game.dao.impl.cache;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.game.dao.UserDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.model.User;
import com.lodogame.model.UserHeroInfo;
import com.lodogame.model.UserPowerInfo;
import com.lodogame.model.UserShareLog;

public class UserDaoCacheImpl implements UserDao, ReloadAble {

	private UserDao userDaoMysqlImpl;

	private UserDao userDaoRedisImpl;

	private static Map<String, Integer> userMaxPower = new ConcurrentHashMap<String, Integer>();

	private static Map<String, Integer> userMaxInfo = new ConcurrentHashMap<String, Integer>();

	@Override
	public int getUserPower(String userId) {
		if (userMaxInfo.containsKey(userId)) {
			return userMaxInfo.get(userId);
		}
		int power = this.userDaoMysqlImpl.getUserPower(userId);
		userMaxInfo.put(userId, power);
		return power;
	}

	@Override
	public UserPowerInfo getUserIdRand(String userId, int minPower) {
		return userDaoMysqlImpl.getUserIdRand(userId, minPower);
	}

	@Override
	public UserPowerInfo getUserIdByPowerRand(String userId, int type, int min, int max) {
		return userDaoMysqlImpl.getUserIdByPowerRand(userId, type, min, max);
	}

	@Override
	public void updateUserPower(String userId, int cap) {
		userDaoMysqlImpl.updateUserPower(userId, cap);
		userMaxInfo.put(userId, cap);
		updateUserMaxPower(userId, cap);
	}

	public void setUserDaoMysqlImpl(UserDao userDaoMysqlImpl) {
		this.userDaoMysqlImpl = userDaoMysqlImpl;
	}

	public void setUserDaoRedisImpl(UserDao userDaoRedisImpl) {
		this.userDaoRedisImpl = userDaoRedisImpl;
	}

	public boolean add(User user) {
		return this.userDaoMysqlImpl.add(user);
	}

	public User get(String userId) {
		User user = this.userDaoRedisImpl.get(userId);
		if (user != null) {
			return user;
		}
		user = this.userDaoMysqlImpl.get(userId);
		if (user != null) {
			this.userDaoRedisImpl.add(user);
		}
		return user;
	}

	public User getByPlayerId(String playerId) {
		User user = this.userDaoRedisImpl.getByPlayerId(playerId);
		if (user != null) {
			return user;
		}
		user = this.userDaoMysqlImpl.getByPlayerId(playerId);
		if (user != null) {
			this.userDaoRedisImpl.add(user);
		}
		return user;
	}

	public User getByName(String username) {
		return this.userDaoMysqlImpl.getByName(username);
	}

	public boolean addCopper(String userId, int copper) {
		boolean success = this.userDaoMysqlImpl.addCopper(userId, copper);
		if (success) {
			this.userDaoRedisImpl.addCopper(userId, copper);
		}
		return success;
	}

	public boolean reduceCopper(String userId, int copper) {
		boolean success = this.userDaoMysqlImpl.reduceCopper(userId, copper);
		if (success) {
			this.userDaoRedisImpl.reduceCopper(userId, copper);
		}
		return success;
	}

	public boolean reduceGold(String userId, int gold) {
		boolean success = this.userDaoMysqlImpl.reduceGold(userId, gold);
		if (success) {
			this.userDaoRedisImpl.reduceGold(userId, gold);
		}
		return success;
	}

	public boolean updateVIPLevel(String userId, int VIPLevel) {
		boolean success = this.userDaoMysqlImpl.updateVIPLevel(userId, VIPLevel);
		if (success) {
			this.userDaoRedisImpl.updateVIPLevel(userId, VIPLevel);
		}
		return success;
	}

	public boolean addGold(String userId, int gold) {
		boolean success = this.userDaoMysqlImpl.addGold(userId, gold);
		if (success) {
			this.userDaoRedisImpl.addGold(userId, gold);
		}
		return success;
	}

	public boolean addExp(String userId, int exp, int level, int resumePower) {
		boolean success = this.userDaoMysqlImpl.addExp(userId, exp, level, resumePower);
		if (success) {
			this.userDaoRedisImpl.addExp(userId, exp, level, resumePower);
		}
		return success;
	}

	@Override
	public boolean addPower(String userId, int power, int maxPower, Date powerAddTime) {
		boolean success = this.userDaoMysqlImpl.addPower(userId, power, maxPower, powerAddTime);
		if (success) {
			this.userDaoRedisImpl.addPower(userId, power, maxPower, powerAddTime);
		}
		return success;
	}

	@Override
	public boolean resetPowerAddTime(String userId, Date powerAddTime) {
		boolean success = this.userDaoMysqlImpl.resetPowerAddTime(userId, powerAddTime);
		if (success) {
			this.userDaoRedisImpl.resetPowerAddTime(userId, powerAddTime);
		}
		return success;
	}

	@Override
	public boolean reducePowre(String userId, int power) {
		boolean success = this.userDaoMysqlImpl.reducePowre(userId, power);
		if (success) {
			this.userDaoRedisImpl.reducePowre(userId, power);
		}
		return success;
	}

	@Override
	public Set<String> getOnlineUserIdList() {
		return this.userDaoRedisImpl.getOnlineUserIdList();
	}

	@Override
	public boolean setOnline(String userId, boolean online) {
		return this.userDaoRedisImpl.setOnline(userId, online);
	}

	@Override
	public boolean cleanOnline() {
		return this.userDaoRedisImpl.cleanOnline();
	}

	@Override
	public boolean isOnline(String userId) {
		return userDaoRedisImpl.isOnline(userId);
	}

	@Override
	public boolean cleanCacheData(String userId) {
		return this.userDaoRedisImpl.cleanCacheData(userId);
	}

	@Override
	public List<User> listOrderByLevelDesc(int offset, int size) {
		return this.userDaoMysqlImpl.listOrderByLevelDesc(offset, size);
	}

	@Override
	public List<User> listOrderByCopperDesc(int offset, int size) {
		return this.userDaoMysqlImpl.listOrderByCopperDesc(offset, size);
	}

	@Override
	public void reload() {

	}

	@Override
	public void init() {
		this.cleanOnline();
	}

	@Override
	public List<String> getAllUserIds() {
		return userDaoMysqlImpl.getAllUserIds();
	}

	@Override
	public boolean reduceExp(String userId, int amount) {
		return userDaoMysqlImpl.reduceExp(userId, amount);
	}

	@Override
	public boolean banUser(String userId, Date dueTime) {
		boolean success = this.userDaoMysqlImpl.banUser(userId, dueTime);
		if (success) {
			this.userDaoRedisImpl.banUser(userId, dueTime);
		}
		return success;
	}

	@Override
	public boolean reduceReputation(String userId, int reputation) {
		boolean success = this.userDaoMysqlImpl.reduceReputation(userId, reputation);
		if (success) {
			this.userDaoRedisImpl.reduceReputation(userId, reputation);
		}
		return success;
	}

	@Override
	public boolean addReputation(String userId, int reputation) {
		boolean success = this.userDaoMysqlImpl.addReputation(userId, reputation);
		if (success) {
			this.userDaoRedisImpl.addReputation(userId, reputation);
		}
		return success;
	}

	@Override
	public boolean updateUserLevel(String userId, int level, int exp) {
		boolean success = this.userDaoMysqlImpl.updateUserLevel(userId, level, exp);
		if (success) {
			this.userDaoRedisImpl.updateUserLevel(userId, level, exp);
		}
		return success;
	}

	@Override
	public UserShareLog getUserLastShareLog(String userId) {

		return this.userDaoMysqlImpl.getUserLastShareLog(userId);
	}

	@Override
	public boolean addUserShareLog(UserShareLog userShareLog) {
		return this.userDaoMysqlImpl.addUserShareLog(userShareLog);
	}

	@Override
	public boolean reduceMind(String userId, int mind) {
		boolean success = this.userDaoMysqlImpl.reduceMind(userId, mind);
		if (success) {
			this.userDaoRedisImpl.reduceMind(userId, mind);
		}
		return success;
	}

	@Override
	public boolean addMind(String userId, int mind) {
		boolean success = this.userDaoMysqlImpl.addMind(userId, mind);
		if (success) {
			this.userDaoRedisImpl.addMind(userId, mind);
		}
		return success;
	}

	@Override
	public boolean bannedToPost(String userId) {
		boolean success = this.userDaoMysqlImpl.bannedToPost(userId);
		if (success) {
			this.userDaoRedisImpl.bannedToPost(userId);
		}
		return success;
	}

	@Override
	public List<User> getRandByLevel(int lowerLevel, int upperLevel, String userId) {
		return this.userDaoMysqlImpl.getRandByLevel(lowerLevel, upperLevel, userId);
	}

	@Override
	public boolean reduceSoul(String userId, int amount) {
		boolean isSuccess = userDaoMysqlImpl.reduceSoul(userId, amount);
		if (isSuccess == true) {
			isSuccess = userDaoRedisImpl.reduceSoul(userId, amount);
		}
		return isSuccess;
	}

	public boolean reduceEnergy(String userId, int amount) {
		boolean isSuccess = userDaoMysqlImpl.reduceEnergy(userId, amount);
		if (isSuccess == true) {
			isSuccess = userDaoRedisImpl.reduceEnergy(userId, amount);
		}
		return isSuccess;
	}

	public boolean reduceMuhon(String userId, int amount) {
		boolean isSuccess = userDaoMysqlImpl.reduceMuhon(userId, amount);
		if (isSuccess == true) {
			isSuccess = userDaoRedisImpl.reduceMuhon(userId, amount);
		}
		return isSuccess;
	}

	@Override
	public boolean addMuhon(String userId, int amount) {
		boolean isSuccess = userDaoMysqlImpl.addMuhon(userId, amount);
		if (isSuccess == true) {
			isSuccess = userDaoRedisImpl.addMuhon(userId, amount);
		}
		return isSuccess;
	}

	@Override
	public boolean addSoul(String userId, int amount) {
		boolean isSuccess = userDaoMysqlImpl.addSoul(userId, amount);
		if (isSuccess == true) {
			isSuccess = userDaoRedisImpl.addSoul(userId, amount);
		}
		return isSuccess;
	}

	@Override
	public List<User> getAllUser() {
		return userDaoMysqlImpl.getAllUser();
	}

	@Override
	public List<User> getByGTLevel(String userId, int userLevel, int needLevel, int limit) {
		return this.userDaoMysqlImpl.getByGTLevel(userId, userLevel, needLevel, limit);
	}

	@Override
	public List<User> getByLTLevel(String userId, int level, int limit) {
		return this.userDaoMysqlImpl.getByLTLevel(userId, level, limit);
	}

	@Override
	public List<User> getByGTLevel(String userId, int level) {
		return this.userDaoMysqlImpl.getByGTLevel(userId, level);
	}

	@Override
	public boolean addCoin(String userId, int amount) {
		boolean isSuccess = userDaoMysqlImpl.addCoin(userId, amount);
		if (isSuccess == true) {
			isSuccess = userDaoRedisImpl.addCoin(userId, amount);
		}
		return isSuccess;
	}

	@Override
	public boolean reduceCoin(String userId, int amount) {
		boolean isSuccess = userDaoMysqlImpl.reduceCoin(userId, amount);
		if (isSuccess == true) {
			isSuccess = userDaoRedisImpl.reduceCoin(userId, amount);
		}
		return isSuccess;
	}

	@Override
	public List<User> getOut(String userId, int level) {
		return userDaoMysqlImpl.getOut(userId, level);
	}

	@Override
	public boolean reduceMingwen(String userId, int amount) {
		boolean isSuccess = userDaoMysqlImpl.reduceMingwen(userId, amount);
		if (isSuccess == true) {
			isSuccess = userDaoRedisImpl.reduceMingwen(userId, amount);
		}
		return isSuccess;
	}

	@Override
	public boolean addMingwen(String userId, int amount) {
		boolean isSuccess = userDaoMysqlImpl.addMingwen(userId, amount);
		if (isSuccess == true) {
			isSuccess = userDaoRedisImpl.addMingwen(userId, amount);
		}
		return isSuccess;
	}

	@Override
	public boolean addHonour(String userId, int amount) {
		boolean success = userDaoMysqlImpl.addHonour(userId, amount);
		if (success == true) {
			success = userDaoRedisImpl.addHonour(userId, amount);
		}
		return success;
	}

	@Override
	public boolean reduceHonour(String userId, int amount) {
		boolean success = userDaoMysqlImpl.reduceHonour(userId, amount);
		if (success == true) {
			success = userDaoRedisImpl.reduceHonour(userId, amount);
		}
		return success;
	}

	@Override
	public int getUserMaxPower(String userId) {
		Integer max = userMaxPower.get(userId);
		if (max == null || max == 0) {
			max = userDaoMysqlImpl.getUserMaxPower(userId);
			userMaxPower.put(userId, max);
		}
		return max;
	}

	@Override
	public void updateUserMaxPower(String userId, int cap) {
		int maxPower = getUserMaxPower(userId);
		if (cap > maxPower) {
			userMaxPower.put(userId, cap);
			userDaoMysqlImpl.updateUserMaxPower(userId, cap);
		}
	}

	@Override
	public void updateHeroAtt(UserHeroInfo userHeroInfo) {
		userDaoMysqlImpl.updateHeroAtt(userHeroInfo);
	}

	@Override
	public boolean deleteHeroAtt(String userHeroId) {
		return userDaoMysqlImpl.deleteHeroAtt(userHeroId);
	}

	@Override
	public boolean addSkill(String userId, int amount) {
		boolean success = this.userDaoMysqlImpl.addSkill(userId, amount);
		if (success) {
			this.userDaoRedisImpl.addSkill(userId, amount);
		}
		return success;
	}

}
