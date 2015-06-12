package com.lodogame.ldsg.service;

import java.util.List;
import java.util.Map;

import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.EquipEnchantBO;
import com.lodogame.ldsg.bo.EquipRefineBO;
import com.lodogame.ldsg.bo.EquipRefineSoulBO;
import com.lodogame.ldsg.bo.UserEquipBO;
import com.lodogame.ldsg.bo.UserToolBO;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.model.SystemEquip;
import com.lodogame.model.UserEquip;

/**
 * 装备service
 * 
 * @author jacky
 * 
 */
public interface EquipService {

	/**
	 * 装备出售错误，装备已经在武将身上
	 */
	public final static int SELL_EQUIP_IS_INSTALLED = 2002;

	/**
	 * 装备出售错误，装备上有宝石
	 */
	public final static int SELL_EQUIP_HAS_STONE = 2003;
	/**
	 * 强化失败，银币不足
	 */
	public final static int UPGRADE_EQUIP_COPPER_NOT_ENOUGH = 2001;

	/**
	 * 强化失败，武将等级不足
	 */
	public final static int UPGRADE_EQUIP_HERO_LEVEL_NOT_ENOUGH = 2004;

	/**
	 * 强化失败，装备等级高于用户等级
	 */
	public final static int UPGRADE_EQUIP_LEVEL_OVER_USER_LEVEL = 2003;

	/**
	 * 装备强化失败(正常的概率上的失败)
	 */
	public final static int UPGRADE_EQUIP_NORMAL_PROBABILITY_FAILED = 2002;

	/**
	 * 装备合成，等级不足
	 */
	public final static int MERGE_EQUIP_NOT_ENOUGH_LEVEL = 2001;

	/**
	 * 装备合成，材料不足
	 */
	public final static int MERGE_EQUIP_NOT_ENOUGH_TOOL = 2002;

	/**
	 * 装备合成(金币)，金币不足
	 */
	public final static int MERGE_EQUIP_NOT_ENOUGH_GOLD = 2004;

	/**
	 * 装备合成(金币)，VIP不够
	 */
	public final static int MERGE_EQUIP_NOT_ENOUGH_VIP = 2003;

	/**
	 * 穿戴装备，职业不符
	 */
	public final static int INSTALL_EQUIP_CAREER_ERROR = 2002;
	/**
	 * 装备精炼石不足
	 */
	public final static int REFINE_NOT_ENOUGH_TOOL = 2001;
	/**
	 * 装备精炼等级不足
	 */
	public final static int REFINE_NOT_ENOUGH_STAR_LEVEL = 2002;

	/**
	 * 装备精炼等级已满
	 */
	public final static int REFINE_MAX_LEVEL = 2003;

	/**
	 * 装备精炼银币不足
	 */
	public final static int REFINE_NOT_ENOUGH_COPPER = 2004;

	/**
	 * 装备精炼装备职业和武将职业不符
	 */
	public final static int REFINE_CAREER_ERROR = 2005;

	/**
	 * 魂石不足
	 */
	public final static int REFINE_SOUL_NOT_ENOUGH_TOOL = 2001;

	/**
	 * 金币不足
	 */
	public final static int REFINE_SOUL_NOT_ENOUGH_COPPER = 2002;

	/**
	 * 神兵不能炼魂
	 */
	public final static int REFINE_SOUL_MAX_LEVEL = 2003;

	/**
	 * 星级不足
	 */
	public final static int REFINE_SOUL_NOT_ENOUGH_STAR_LEVEL = 2004;
	

	/**
	 * 炼魂失败，没有该装备
	 */
	public final static int REFINE_SOUL_NOT_EQUIP= 2005;
	/**
	 * 装备没有点化属性，请点化
	 */
	public final static int ENCHANT_NOT_EQUIP = 2006;
	/**
	 * 装备点化金币不足
	 */
	public final static int ENCHANT_EQUIP_NOT_ENOUGH_COPPER = 2007;
	/**
	 * 装备点化水晶不足
	 */
	public final static int ENCHANT_EQUIP_NOT_ENOUGH_CRYSTAL = 2008;
	/**
	 * 装备星级不足3星
	 */
	public final static int ENCHANT_EQUIP_NOT_ENOUGH_STAR = 2009;
	/**
	 * 用户等级不足45级
	 */
	public final static int ENCHANT_EQUIP_NOT_ENOUGH_LEVEL = 2010;
	/**
	 * 炼魂失败，该装备没有缘分装备
	 */
	public final static int REFINE_SOUL_NOT_PRE_EQUIP= 2011;
	/**
	 * 获取用户装备列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserEquipBO> getUserEquipList(String userId);

	/**
	 * 获取用户装备列表
	 * 
	 * @param userId
	 * @param all
	 * @return
	 */
	public List<UserEquipBO> getUserEquipList(String userId, boolean all);

	/**
	 * 获取用户某种类型的装备
	 * 
	 * @param userId
	 * @param equipId
	 * @return
	 */
	public List<UserEquip> getUserEquipList(String userId, int equipId);

	/**
	 * 获取用户某个武将的装备列表
	 * 
	 * @param userId
	 * @param userHeroId
	 * @return
	 */
	public List<UserEquipBO> getUserHeroEquipList(String userId, String userHeroId);

	/**
	 * 穿戴装备
	 * 
	 * @param userId
	 * @param userEquipId
	 * @param userHeroId
	 * @param equipType
	 * @param handle
	 * @return
	 */
	public boolean updateEquipHero(String userId, String userEquipId, String userHeroId, int equipType, EventHandle handle);

	/**
	 * 装备强化
	 * 
	 * @param userId
	 * @param userEquipId
	 * @return
	 */
	public boolean upgrade(String userId, String userEquipId, EventHandle handle);

	/**
	 * 装备强化预览
	 * 
	 * @param userId
	 * @param userEquipId
	 * @return
	 */
	public Map<String, Object> upgradePre(String userId, String userEquipId);

	/**
	 * 装备出售
	 * 
	 * @param userId
	 * @param userEquipIdList
	 * @return
	 */
	public CommonDropBO sell(String userId, List<String> userEquipIdList, EventHandle handle);

	/**
	 * 获取用户装备
	 * 
	 * @param userEquipId
	 * @return
	 */
	public UserEquip getUserEquip(String userId, String userEquipId);

	/**
	 * 获取用户装备
	 * 
	 * @param equipId
	 * @return
	 */
	public SystemEquip getSysEquip(int equipId);

	/**
	 * 获取用户装备BO对象
	 * 
	 * @param userEquipId
	 * @return
	 */
	public UserEquipBO getUserEquipBO(String userId, String userEquipId);

	/**
	 * 添加用户装备
	 * 
	 * @param userId
	 * @param equipId
	 * @param useType
	 *            日志类型
	 * @return
	 */
	public boolean addUserEquip(String userId, String userEquipId, int equipId, int useType);

	/**
	 * 装备进阶
	 * 
	 * @param userId
	 * @param userEquipId
	 * @param useMoney
	 * @param handle
	 * @return
	 */
	public Map<String, Object> mergeEquip(String userId, String userEquipId, boolean useMoney, EventHandle handle);

	/**
	 * 装备进阶预览
	 * 
	 * @param userId
	 * @param userEquipId
	 * @return
	 */
	public UserEquipBO mergeEquipPre(String userId, String userEquipId);

	/**
	 * 获取用户装备长度
	 * 
	 * @param userId
	 * @return
	 */
	public int getUserEquipCount(String userId);

	/**
	 * 批量增加装备
	 * 
	 * @param userId
	 * @param equipIdMap
	 * @param useType
	 * @return
	 */
	public boolean addUserEquips(String userId, Map<String, Integer> equipIdMap, int useType);

	/**
	 * 在用户的一组装备中获取等级最低的装备
	 */
	public UserEquip getLowestLevel(List<UserEquip> userEquipList);

	/**
	 * 根据DropToolBO 列表 创建 UserEquipBO 列表
	 */
	public List<UserEquipBO> createUserEquipBOList(String userId, List<DropToolBO> boList);

	/**
	 * 一键强化
	 * 
	 * @param userId
	 * @param userEquipId
	 * @return
	 */
	public int autoUpgrade(String userId, String userEquipId, List<Integer> addLevelList);

	public int calEquipUpgradeNeedGold(String userId, int systemEquipId);

	/**
	 * 装备精炼
	 * 
	 * @param userEquipId
	 */
	public List<UserToolBO> refine(String userId, String userEquipId, int type);

	/**
	 * 装备精炼预览
	 * 
	 * @param userEquipId
	 */
	public List<EquipRefineBO> refinePre(String userId, String userEquipId);

	/**
	 * 装备点化预览
	 * 
	 * @param userEquipId
	 */
	public EquipEnchantBO enchantPre(String userId, String userEquipId);
	
	/**
	 * 装备点化
	 * 
	 * @param userEquipId
	 */
	public EquipEnchantBO enchant(String userId, String userEquipId);
	
	/**
	 * 保持装备点化值
	 * 
	 * @param userEquipId
	 */
	public EquipEnchantBO save(String userId, String userEquipId);
	/**
	 * 装备炼魂
	 * 
	 * @param userEquipId
	 */
	public EquipRefineSoulBO refineSoul(String userId, String userEquipId, int refineEquipId);

	/**
	 * 装备炼魂预览
	 * 
	 * @param userEquipId
	 */
	public List<EquipRefineSoulBO> refineSoulPre(String userId, String userEquipId);
}
