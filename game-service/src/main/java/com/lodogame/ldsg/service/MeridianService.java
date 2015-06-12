package com.lodogame.ldsg.service;

import java.util.List;

import com.lodogame.ldsg.bo.MeridianMeridianBO;
import com.lodogame.model.MeridianUserInfo;
import com.lodogame.model.UserHero;

public interface MeridianService {

	int IS_OPEN = 1001; // 已经打开过

	int COLOR_NOT = 1002; // 武将颜色不满足开启条件

	int HL_NOT_HAVE = 1003; // 魂力不足

	int REQUIRED_NODE_NOT_OPEN = 1004; // 前置接点未开启

	int NOT_OPEN = 1005; // 节点尚未开启，不能升级

	int OPEN_FA = 1006; // 开启失败

	int NO_TOOL = 1007; // 开启道具不足

	int NO_NUM_NODE = 1008;// 节点个数不满足级别

	int NO_MORE = 1009;// 最多4个

	int NO_CA = 1010;// 职业不符合

	int MINGWEN_NOT_HAVE = 1011; // 铭文不足

	int MUHON_NOT_HAVE = 2011; // 奶酪不足

	/**
	 * 获出出售获得的能量
	 * 
	 * @param userId
	 * @param userHeroId
	 * @return
	 */
	public int getSellSoul(String userId, String userHeroId);

	/**
	 * 获取出售获得的铭文
	 * 
	 * @param userId
	 * @param userHeroId
	 * @return
	 */
	public int getSellMingwen(String userId, String userHeroId);

	/**
	 * 获取出售获得的奶酪
	 * 
	 * @param userId
	 * @param userHeroId
	 * @return
	 */
	public int getSellMuhon(String userId, String userHeroId);

	/**
	 * 显示
	 * 
	 * @param userId
	 * @return
	 */
	public List<MeridianMeridianBO> show(String userId); // 显示

	/**
	 * 开启节点
	 * 
	 * @param userId
	 * @param type
	 *            经脉ID
	 * @param nodeId
	 *            节点ID
	 * @param userHeroId
	 * @return
	 */
	public MeridianUserInfo open(String userId, int type, int nodeId, String userHeroId);

	/**
	 * 升级节点
	 * 
	 * @param userId
	 * @param type
	 * @param nodeId
	 * @param userHeroId
	 * @return
	 */
	public MeridianUserInfo upLevel(String userId, int type, int nodeId, String userHeroId);

	/**
	 * 获取增加值
	 * 
	 * @param hero
	 * @return
	 */
	int[] getAddVal(UserHero hero);

}
