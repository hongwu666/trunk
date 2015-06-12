package com.lodogame.ldsg.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lodogame.ldsg.bo.ForcesStarRewardBO;
import com.lodogame.ldsg.bo.SweepInfoBO;
import com.lodogame.ldsg.bo.UserSceneBO;
import com.lodogame.ldsg.event.EventHandle;

/**
 * 场景service
 * 
 * @author jacky
 * 
 */
public interface SceneService {

	@SuppressWarnings("serial")
	public final static Map<Integer, String> section = new HashMap<Integer, String>() {
		{
			put(23012, "第三章");
			put(24012, "第四章");
			put(25012, "第五章");
			put(26012, "第六章");
			put(27012, "第七章");
		}
	};
	/**
	 * 体力不足
	 */
	public final static int ATTACK_FORCES_POWER_NOT_ENOUGH = 2003;

	/**
	 * 超过可攻打次数
	 */
	public final static int ATTACK_BEYOND_ATTACK_TIME_LIMIT = 2004;

	/**
	 * 部队没有通过，无法扫荡
	 */
	public final static int ADD_SWEEP_RETURN_FORCES_NOT_PASS = 2001;

	/**
	 * 正在扫荡中
	 */
	public final static int ADD_SWEEP_RETURN_ING = 2002;

	/**
	 * 体力不足
	 */
	public final static int ADD_SWEEP_RETURN_POWER_NOT_ENOUGH = 2003;

	/**
	 * 超出攻打限制
	 */
	public final static int ADD_SWEEP_RETURN_LIMIT_EXCEED = 2004;

	/**
	 * 停止扫荡，没有进行中的扫荡
	 */
	public final static int STOP_SWEEP_NOT_ING = 2001;

	/**
	 * 加速扫荡，没有进行中的扫荡
	 */
	public final static int SPEED_UP_SWEEP_NOT_ING = 2001;

	/**
	 * 金币不足
	 */
	public final static int SPEED_UP_SWEEP_GOLD_NOT_ENOUGH = 2002;

	/**
	 * 消除扫荡金币,没有要消除的稍等cd
	 */
	public final static int GET_CLEAR_CD_GOLD_NO_CD = 2001;

	/**
	 * 武将背包超出限制
	 */
	public final static int USER_HERO_BAG_LIMIT_EXCEED = 2005;

	/**
	 * 装备背包超出限制
	 */
	public final static int USER_EQUIP_BAG_LIMIT_EXCEED = 2006;

	/**
	 * 突击检测部队失败(没有部队可突击)
	 */
	public final static int ASSAULT_SCENE_FORCE_CHECK_FAILED = 2001;

	/**
	 * 突击令不足
	 */
	public final static int ASSAULT_TOKEN_NOT_ENOUGH = 2002;

	/**
	 * 该副本没有通关
	 */
	public final static int DRAW_SCENE_REWARD_HAS_NOT_PASS = 2001;

	/**
	 * 星数不足
	 */
	public final static int DRAW_SCENE_REWARD_HAS_NOT_ENOUGH_STAR = 2002;

	/**
	 * 已领取过该过图奖励
	 */
	public final static int DRAW_SCENE_REWARD_HAS_DRADED = 2003;

	/**
	 * 获取用户场景列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserSceneBO> getUserSceneList(String userId);

	/**
	 * 攻打怪物
	 * 
	 * @param userId
	 * @param forcesId
	 * @param forceStar
	 * @return
	 */
	public boolean attack(String userId, int forcesId, EventHandle handle);

	/**
	 * 开启后面的部队
	 * 
	 * @param userId
	 * @param forcesId
	 */
	public Set<Integer> openAfterForces(String userId, int forcesGroup);

	/**
	 * 突击
	 * 
	 * @param uid
	 * @param sid
	 * @param eventHandle
	 */
	public void assault(String uid, Integer sid, EventHandle eventHandle);

	/**
	 * 领取通关宝箱
	 * 
	 * @param userId
	 * @param sceneId
	 * @param passStar
	 * @return
	 */
	public Map<String, Object> drawForcesStarReward(String userId, int sceneId, int passStar,int type);

	/**
	 * 读取奖励信息
	 */
	public ForcesStarRewardBO getForcesStarReward(int sceneId, String uid);

	/**
	 * 开始扫荡
	 * 
	 * @param userId
	 * @param forcesId
	 * @param times
	 */
	public void sweep(String userId, int forcesId, int times, EventHandle handle);

	/**
	 * 更新用户扫荡信息
	 * 
	 * @param userId
	 * @return
	 */
	public boolean updateSweepComplete(String userId);

	/**
	 * 获得用户当前的扫荡信息
	 * 
	 * @param userId
	 * @return
	 */
	public SweepInfoBO getUserSweepInfo(String userId);

	/**
	 * 获取消除扫荡时间需要消耗的金币
	 * 
	 * @param userId
	 * @return
	 */
	public int getClearCDGold(String userId);

	/**
	 * 加速扫荡
	 * 
	 * @param userId
	 */
	public void speedUpSweep(String userId);

}
