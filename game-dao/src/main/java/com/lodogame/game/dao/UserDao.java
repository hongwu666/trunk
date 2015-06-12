package com.lodogame.game.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.lodogame.model.User;
import com.lodogame.model.UserHeroInfo;
import com.lodogame.model.UserPowerInfo;
import com.lodogame.model.UserShareLog;

public interface UserDao {

	/**
	 * 获取用户最大战力
	 * 
	 * @param userId
	 * @return
	 */
	public int getUserMaxPower(String userId);

	/**
	 * 获取用户战力
	 * 
	 * @param userId
	 * @return
	 */
	public int getUserPower(String userId);

	/**
	 * 
	 * @param userId
	 * @param cap
	 */
	public void updateUserMaxPower(String userId, int cap);

	/**
	 * 
	 * @param userId
	 * @param type
	 * @param min
	 * @param max
	 * @return
	 */
	public UserPowerInfo getUserIdByPowerRand(String userId, int type, int min, int max);

	/**
	 * 
	 * @param userId
	 * @param minPower
	 * @return
	 */
	public UserPowerInfo getUserIdRand(String userId, int minPower);

	/**
	 * 更新角色总战力
	 * 
	 * @param userId
	 * @param cap
	 */
	public void updateUserPower(String userId, int cap);

	/**
	 * 更新用户英雄的战力
	 * 
	 * @param userId
	 * @param cap
	 */
	public void updateHeroAtt(UserHeroInfo userHeroInfo);

	/**
	 * 删除英雄战力榜信息
	 * 
	 * @param userHeroId
	 * @return
	 */
	public boolean deleteHeroAtt(String userHeroId);

	/**
	 * 添加用户对象
	 * 
	 * @param user
	 * @return
	 */
	public boolean add(User user);

	/**
	 * 获取用户
	 * 
	 * @param userId
	 * @return
	 */
	public User get(String userId);

	/**
	 * 根据playerId获取用户
	 * 
	 * @param playerId
	 * @return
	 */
	public User getByPlayerId(String playerId);

	/**
	 * 
	 * @param username
	 * @return
	 */
	public User getByName(String username);

	/**
	 * 增加银币
	 * 
	 * @param userId
	 * @param cooper
	 * @return
	 */
	public boolean addCopper(String userId, int copper);

	/**
	 * 减少银币
	 * 
	 * @param userId
	 * @param copper
	 * @return
	 */
	public boolean reduceCopper(String userId, int copper);

	/**
	 * 增加金币
	 * 
	 * @param userId
	 * @param gold
	 * @return
	 */
	public boolean addGold(String userId, int gold);

	/**
	 * 花费金币
	 * 
	 * @param userId
	 * @param gold
	 * @return
	 */
	public boolean reduceGold(String userId, int gold);

	/**
	 * 增加经验
	 * 
	 * @param userId
	 * @param exp
	 * @param level
	 * @param resumePower
	 *            是否恢复体力
	 * @return
	 */
	public boolean addExp(String userId, int exp, int level, int power);

	/**
	 * 增加体力
	 * 
	 * @param userId
	 * @param power
	 * @param powerAddTime
	 * @return
	 */
	public boolean addPower(String userId, int power, int maxPower, Date powerAddTime);

	/**
	 * 重置增加体力的时间
	 * 
	 * @param userId
	 * @param powerAddTime
	 * @return
	 */
	public boolean resetPowerAddTime(String userId, Date powerAddTime);

	/**
	 * 花费体力
	 * 
	 * @param userId
	 * @param power
	 * @return
	 */
	public boolean reducePowre(String userId, int power);

	/**
	 * 获取在线用户列表
	 * 
	 * @return
	 */
	public Set<String> getOnlineUserIdList();

	/**
	 * 设置在线，不在线
	 * 
	 * @param userId
	 * @param online
	 * @return
	 */
	public boolean setOnline(String userId, boolean online);

	/**
	 * 清除用户所有缓存数据
	 * 
	 * @param userId
	 * @return
	 */
	public boolean cleanCacheData(String userId);

	/**
	 * 更改VIP等级
	 * 
	 * @param userId
	 * @param VIPLevel
	 * @return
	 */
	public boolean updateVIPLevel(String userId, int VIPLevel);

	/**
	 * 根据用户等级倒序获得用户列表
	 * 
	 * @param offset
	 * @param size
	 * @return
	 */
	public List<User> listOrderByLevelDesc(int offset, int size);

	/**
	 * 根据银币数量倒序获得用户列表
	 * 
	 * @param offset
	 * @param size
	 * @return
	 */
	public List<User> listOrderByCopperDesc(int offset, int size);

	/**
	 * 获取所有用户的id 用户后台管理全服发放武平
	 * 
	 * @return
	 */
	public List<String> getAllUserIds();

	/**
	 * 判断用户是否在线
	 * 
	 * @param userId
	 * @return
	 */
	public boolean isOnline(String userId);

	/**
	 * 减经验
	 * 
	 * @param userId
	 * @param amount
	 * @return
	 */
	public boolean reduceExp(String userId, int amount);

	/**
	 * 封用户
	 * 
	 * @param userId
	 * @param dueTime
	 * @return
	 */
	public boolean banUser(String userId, Date dueTime);

	/**
	 * 消耗声望
	 * 
	 * @param userId
	 * @param reputation
	 * @return
	 */
	public boolean reduceReputation(String userId, int reputation);

	/**
	 * 添加声望
	 * 
	 * @param userId
	 * @param reputation
	 * @return
	 */
	public boolean addReputation(String userId, int reputation);

	/**
	 * 用户分享的日志
	 * 
	 * @param userId
	 * @return
	 */
	public UserShareLog getUserLastShareLog(String userId);

	/**
	 * 增加用户分享的日志
	 * 
	 * @param userShareLog
	 * @return
	 */
	public boolean addUserShareLog(UserShareLog userShareLog);

	/**
	 * 更改用户等级
	 * 
	 * @param userId
	 * @param level
	 * @param exp
	 * @return
	 */
	public boolean updateUserLevel(String userId, int level, int exp);

	/**
	 * 消耗神魄
	 * 
	 * @param userId
	 * @param mind
	 * @return
	 */
	public boolean reduceMind(String userId, int mind);

	/**
	 * 添加神魄
	 * 
	 * @param userId
	 * @param mind
	 * @return
	 */
	public boolean addMind(String userId, int mind);

	/**
	 * 禁言
	 */
	public boolean bannedToPost(String userId);

	/**
	 * 跟住等级随机获取记录
	 * 
	 * @param lowerScore
	 * @param upperScore
	 * @return
	 */
	public List<User> getRandByLevel(int lowerLevel, int upperLevel, String userId);

	/**
	 * 消耗武魂
	 * 
	 * @param userId
	 * @param amount
	 * @return
	 */
	public boolean reduceMuhon(String userId, int amount);

	/**
	 * 获得武魂
	 * 
	 * @param userId
	 * @param amount
	 * @return
	 */
	public boolean addMuhon(String userId, int amount);

	/**
	 * 清除在线数据
	 * 
	 * @return
	 */
	public boolean cleanOnline();

	/**
	 * 
	 * @return
	 */
	public List<User> getAllUser();

	/**
	 * 
	 * @param userId
	 * @param level
	 * @return
	 */
	public List<User> getOut(String userId, int level);

	/**
	 * 取大于等于用户等级的limit人
	 * 
	 * @param userId
	 * @param level
	 *            等级
	 * @param limit
	 *            人数
	 * @return
	 */
	public List<User> getByGTLevel(String userId, int userLevel, int needLevel, int limit);

	/**
	 * 取小于用户等级的limit人
	 * 
	 * @param userId
	 * @param level
	 *            等级
	 * @param limit
	 *            人数
	 * @return
	 */
	public List<User> getByLTLevel(String userId, int level, int limit);

	/**
	 * 取大于等于用户等级的100人
	 * 
	 * @param userId
	 * @param level
	 *            等级
	 * @param limit
	 *            人数
	 * @return
	 */
	public List<User> getByGTLevel(String userId, int level);

	/**
	 * 
	 * @param userId
	 * @param amount
	 * @return
	 */
	public boolean reduceSoul(String userId, int amount);

	/**
	 * 
	 * @param userId
	 * @param amount
	 * @return
	 */
	public boolean reduceEnergy(String userId, int amount);

	/**
	 * 
	 * @param userId
	 * @param amount
	 * @return
	 */
	public boolean addSoul(String userId, int amount);

	/**
	 * 增加熟练度
	 * @param userId
	 * @param amount
	 * @return
	 */
	public boolean addSkill(String userId, int amount);
	/**
	 * 
	 * @param userId
	 * @param amount
	 * @return
	 */
	public boolean addCoin(String userId, int amount);

	/**
	 * 
	 * @param userId
	 * @param amount
	 * @return
	 */
	public boolean reduceCoin(String userId, int amount);

	/**
	 * 
	 * @param userId
	 * @param amount
	 * @return
	 */
	public boolean reduceMingwen(String userId, int amount);

	/**
	 * 
	 * @param userId
	 * @param amount
	 * @return
	 */
	public boolean addMingwen(String userId, int amount);

	/**
	 * 增加荣誉
	 * 
	 * @param userId
	 * @param amount
	 * @return
	 */
	public boolean addHonour(String userId, int amount);

	/**
	 * 减少荣誉
	 * 
	 * @param userId
	 * @param amount
	 * @return
	 */
	public boolean reduceHonour(String userId, int amount);

}
