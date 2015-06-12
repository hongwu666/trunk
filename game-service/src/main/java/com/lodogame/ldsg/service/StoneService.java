package com.lodogame.ldsg.service;

import java.util.List;
import java.util.Map;

import com.lodogame.ldsg.bo.UserStoneBO;
import com.lodogame.model.UserEquip;

public interface StoneService {

	/**
	 * 宝石不足
	 */
	public static final int STONE_NOT_ENOUGH = 2001;

	/**
	 * 保护符不足
	 */
	public static final int PROTECT_NOT_ENOUGH = 2002;

	/**
	 * 等级需要到达50级才能合成8级宝石
	 */
	public static final int USER_LEVEL_NOT_ENOUGH = 2003;

	/**
	 * 宝石已是最高等级
	 */
	public static final int STONE_LEVEL_IS_MAX = 2004;

	/**
	 * 宝石不存在
	 */
	public static final int STONE_NOT_EXIST = 2005;

	/**
	 * 宝石穿戴，宝石不足
	 */
	public static final int DRESS_STONE_NOT_ENOUGH = 2001;

	/**
	 * 宝石穿戴，该孔位已有安装宝石
	 */
	public static final int DRESS_STONE_HAS_STONE = 2002;

	/**
	 * 宝石穿戴，孔位不足
	 */
	public static final int DRESS_STONE_HOLE_IS_NOT_ENOUGH = 2004;

	/**
	 * 宝石卸载，该孔位没有安装宝石
	 */
	public static final int UNDRESS_STONE_HAS_STONE = 2001;

	/**
	 * 宝石颜色需求高于装备颜色
	 */
	public static final int NO_EQUIP_COLOR = 3000;

	/**
	 * 不能镶嵌相同类型的宝石
	 */
	public static final int REPEAT_TYPE = 3001;

	public int sellStone(List<String> info, String userId);

	public int enterStone(String userId);

	public Map<String, Object> upgradeStone(String userId, int isAuto, int isProdtect, int stoneId);

	public List<UserStoneBO> getUserStoneBos(String userId);

	/**
	 * 穿戴宝石
	 */
	public void dressStone(String userId, String userEquipId, int stoneId, int pos);

	/**
	 * 卸载宝石
	 * 
	 * @return
	 */
	public Map<String, Object> undressStone(String userId, String userEquipId, int pos);

	/**
	 * 增加宝石
	 * 
	 * @param userId
	 * @param stoneId
	 * @param useType
	 * @return
	 */
	public boolean addStone(String userId, int stoneId, int stoneNum, int useType);

	/**
	 * 把装备上的宝石放入背包
	 * 
	 * @param userEquip
	 */
	public Map<Integer, Integer> putPack(UserEquip userEquip);

	/**
	 * 消费宝石
	 * 
	 * @param userId
	 * @param stoneId
	 * @param useType
	 * @return
	 */
	public boolean reduceStone(String userId, int stoneId, int stoneNum, int useType);

}
