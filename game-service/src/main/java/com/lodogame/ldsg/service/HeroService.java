package com.lodogame.ldsg.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.model.ContestUserHero;
import com.lodogame.model.SystemHero;
import com.lodogame.model.UserExpeditionHero;
import com.lodogame.model.UserHero;

public interface HeroService {

	/**
	 * 布阵，上场武将个数超过限制
	 */
	public static final int CHANGE_POS_HERO_NUM_LIMIT = 2001;

	/**
	 * 布阵，阵上最少有一个武将
	 */
	public static final int CHANGE_POS_HERO_NUM_IS_ZERO = 2002;

	/**
	 * 布阵，同一人物的武将只可以上一个
	 */
	public static final int CHANGE_POS_SAME_HERO_EXIST = 2003;

	/**
	 * 武将进阶 - 节点已经点亮过
	 */
	public static final int UPGRADE_NODE_ALREADY_LIGHTED = 2001;

	/**
	 * 武将进阶 - 道具不足
	 */
	public static final int UPGRADE_TOOL_NOT_ENOUGH = 2002;

	/**
	 * 武将进阶 - 银币不足
	 */
	public static final int UPGRADE_COPPER_NOT_ENOUGH = 2003;

	/**
	 * 武将进阶 - 元宝点亮节点时元宝不足
	 */
	public static final int UPGRADE_GOLD_NOT_ENOUGH = 2004;

	/**
	 * 武将进阶 - 还有节点未点亮
	 */
	public static final int UPGRADE_NOT_ALL_NODES_LIGHTED = 2001;

	/**
	 * 武将进阶 - 武将等级不足
	 */
	public static final int UPGRADE_HERO_LEVEL_NOT_ENOUGH = 2002;
	/**
	 * 删除武将-武将在阵上
	 */
	public static final int DELETE_HERO_IS_IN_EMBATTLE = 2002;

	/**
	 * 删除武将-武将佩戴装备
	 */
	public static final int DELETE_HERO_IS_INSTALL_EQUIP = 2003;

	/**
	 * 吞噬失败，用户银币不足
	 */
	public static final int DEVOUR_HERO_COPPER_NOT_ENOUGH = 2004;

	/**
	 * 吞噬失败，武将等级已经达最大
	 */
	public static final int DEVOUR_HERO_HERO_LEVEL_OVER = 2005;

	/**
	 * 吞噬失败，武将等级高于用户等级
	 */
	public static final int DEVOUR_HERO_HERO_LEVEL_OVER_USER_LEVEL = 2006;

	/**
	 * 武将不存在
	 */
	public static final int HERO_NOT_EXIST = 2001;

	/**
	 * 升级技能-没有技能书
	 */
	public static final int HERO_SKILL_UPGRADE_TOOL_NOT_ENOUGH = 2001;

	/**
	 * 学习技能-没有技能书
	 */
	public static final int HERO_SKILL_STUDY_TOOL_NOT_ENOUGH = 2001;

	/**
	 * 学习技能-高级的不可以直接学习
	 */
	public static final int HERO_SKILL_STUDY_CAN_NOT_STUDY_ = 2002;

	/**
	 * 学习技能-已经学了相同的技能
	 */
	public static final int HERO_SKILL_STUDY_HAD_STUDY_SAME_SKILL = 2003;

	/**
	 * 学习技能-可学技能超过限制
	 */
	public static final int HERO_SKILL_STUDY_COUNT_OVER = 2004;

	/**
	 * 训练技能-银币不足
	 */
	public static final int HERO_SKILL_TRAIN_COPPER_NOT_ENOUGH = 2001;

	/**
	 * 转生-武将等级不足
	 */
	public static final int HERO_LEVEL_NOT_ENOUGH = 2002;

	/**
	 * 转生-武将没有转生数据
	 */
	public static final int HERO_NO_REGENERATE_INFO = 2003;

	/**
	 * 转生-武将转生丹数量不足
	 */
	public static final int HERO_PILL_NOT_ENOUGH = 2004;

	/**
	 * 转生-武将鬼之合约数量不足
	 */
	public static final int HERO_CONTRACT_NOT_ENOUGH = 2005;

	/**
	 * 转生-武将数量不足
	 */
	public static final int HERO_NUM_NOT_ENOUGH = 2006;

	/**
	 * 武将传承传承-武将不存在
	 */
	public static final int INHERITPRE_GHERO_NOT_EXIST = 2001;

	/**
	 * 武将传承传承-被传承武将不存在
	 */
	public static final int INHERITPRE_IHERO_NOT_EXIST = 2002;

	/**
	 * 武将传承传承-被传承武将不存在
	 */
	public static final int INHERITPRE_HERO_CAN_NOT_CONTRACT = 2003;

	/**
	 * 武将传承-元宝数量不足
	 */
	public static final int INHERIT_GOLD_NOT_ENOUGH = 2001;

	/**
	 * 武将传承-传承武将不满足条件
	 */
	public static final int INHERIT_GIVING_HERO_NOT_MATCH = 2002;

	/**
	 * 武将传承-被传承武将不满足条件
	 */
	public static final int INHERIT_INHERIT_HERO_NOT_MATCH = 2003;

	/**
	 * 武将传承-武将不存在
	 */
	public static final int INHERIT_HERO_NOT_EXIST = 2004;

	/**
	 * 武将传承-鬼之合约不足
	 */
	public static final int INHERIT_HERO_NOT_ENOUGH_CONTRACT = 2005;

	/**
	 * 武将传承-传承武将不能被鬼化
	 */
	public static final int INHERIT_HERO_CAN_NOT_CONTRACT = 2006;

	/**
	 * 武将血祭-被传承血祭不满足条件
	 */
	public static final int BLOOD_SACRIFICE_HERO_NOT_MATCH = 2001;

	/**
	 * 武将血祭-被传承血祭的英雄达到了最高血祭等级
	 */
	public static final int BLOOD_SACRIFICE_IS_MAX_STAGE = 2002;

	/**
	 * 武将血祭--材料不足
	 */
	public static final int BLOOD_SACRIFICE_TOOL_NOT_ENOUGH = 2001;

	/**
	 * 武将血祭--血祭英雄不存在
	 */
	public static final int BLOOD_SACRIFICE_HERO_NOT_EXIST = 2001;

	/**
	 * 武将血祭--英雄不能血祭自己
	 */
	public static final int BLOOD_SACRIFICE_HERO_REPETTION = 2003;

	/**
	 * 武将血祭--血祭的材料不能是鬼将
	 */
	public static final int BLOOD_SACRIFICE_HAS_GHOST_GENERAL = 2004;

	/**
	 * 武将锁定--武将已被锁定
	 */
	public static final int LOCK_HERO_HAS_LOCKED = 2002;

	/**
	 * 武将锁定--武将星级不够
	 */
	public static final int LOCK_HERO_STAR_NOT_ENOUGH = 2003;

	/**
	 * 武将锁定--武将未被锁定
	 */
	public static final int LOCK_HERO_WAS_UNLOCKED = 2002;

	/**
	 * 武将锁定--武将被锁定,不能用于消耗或卖出
	 */
	public static final int HERO_HAS_LOCKED = 2007;

	/**
	 * 升级 - 功勋不足
	 * 
	 */
	public static final int ADVANCE_EXPLOITS_NOT_ENOUGH = 2001;

	/**
	 * 升级 - 银币不足
	 */
	public static final int ADVANCE_COPPER_NOT_ENOUGH = 2002;

	/**
	 * 升级 - 武将等级不可以超过用户等级
	 */
	public static final int ADVANCE_CANNOT_BEYOND_USER_LEVEL = 2003;

	/**
	 * 碎片和成 - 碎片数量不足
	 */
	public static final int MERGE_FRAGMENT_NOT_ENOUGH = 2001;

	public static final int NO_VIP = 5000;

	public static final int UPSTAR_NO = 3001; // 不能升星
	public static final int NO_UPSTAR = 3002; // 已经到该武将最大级别

	UserHeroBO createUserHeroBO(UserExpeditionHero hero, boolean self);

	/**
	 * 获取用户武将BO列表
	 * 
	 * @param userId
	 * @param type
	 *            0表示获取所有 1表示获取上阵武将
	 * @return
	 */
	public List<UserHeroBO> getUserHeroList(String userId, int type);

	/**
	 * 获取用户武将BO
	 * 
	 * @return
	 */
	public UserHeroBO getUserHeroBO(String userId, String userHeroId);

	/**
	 * 获取武将
	 * 
	 * @param userHeroId
	 * @return
	 */
	public UserHero get(String userId, String userHeroId);

	/**
	 * 改变武将站位
	 * 
	 * @param userId
	 * @param userHeroId
	 * @param pos
	 * @param handle
	 * @return
	 */
	@Transactional
	public boolean changePos(String userId, String userHeroId, int pos, EventHandle handle);

	/**
	 * 进阶预览
	 * 
	 * @param userHeroId
	 * @return
	 */
	public UserHeroBO upgradePre(String userId, String userHeroId);

	/**
	 * 武将进阶
	 */
	public UserHeroBO upgrade(String userId, String userHeroId);

	/**
	 * 武将出售
	 * 
	 * @param userId
	 * @param userHeroIdList
	 * @param handle
	 * @return
	 */
	public CommonDropBO sell(String userId, List<String> userHeroIdList, EventHandle handle);

	/**
	 * 获取用户的战斗武将模型列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<BattleHeroBO> getUserBattleHeroBOList(String userId);

	/**
	 * 获取用户的战斗武将模型列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<BattleHeroBO> getUserOnlyOneBattleHeroBOList(String userId);

	/**
	 * 添加用户武将
	 * 
	 * @param userId
	 * @param systemHeroId
	 * @return
	 */
	public boolean addUserHero(String userId, String userHeroId, int systemHeroId, int pos, int useType);

	/**
	 * 添加用户武将
	 * 
	 * @param userId
	 * @param systemHeroId
	 * @return
	 */
	@Transactional
	public boolean addUserHero(String userId, Map<String, Integer> heroIdMap, int useType);

	/**
	 * 修正阵法,如果阵法和当前阵法不一致的话
	 * 
	 * @param userId
	 * @param posMap
	 */
	@Transactional
	public void amendEmbattle(String userId, Map<Integer, String> posMap);

	/**
	 * 获取用户武将个数
	 * 
	 * @param userId
	 */
	public int getUserHeroCount(String userId);

	/**
	 * 获取系统武将
	 * 
	 * @param systemHeroId
	 * @return
	 */
	public SystemHero getSysHero(int systemHeroId);

	/**
	 * 根据 UserHero 获取对应的 hero_id
	 * 
	 * @param userHero
	 * @return
	 */
	public int getHeroId(int userHeroId);

	/**
	 * 根据 DropToolBO 列表创建 UserHeroBO
	 * 
	 * @param boList
	 * @return
	 */
	public List<UserHeroBO> createUserHeroBOList(String userId, List<DropToolBO> boList);

	/**
	 * 武将复活
	 */
	public void retrieve(String uid, String userHeroId);

	/**
	 * 进阶 - 点亮节点
	 * 
	 * @param type
	 */
	public Map<String, Object> lightNode(String userId, String userHeroId, int nodeId, int type);

	/**
	 * 升级
	 */
	public Map<String, Object> advance(String uid, int type, String userHeroId);

	/**
	 * 跟住位置获取用户武将
	 */
	public UserHeroBO getUserHeroByPos(String userId, int pos);

	public UserHeroBO createUserHeroBO(UserHero userHero);

	/**
	 * 一键升级
	 */
	public Map<String, Object> autoAdvance(String uid, String userHeroId);

	/**
	 * 碎片合成
	 */
	public CommonDropBO merge(String uid, int fragmentId);

	CommonDropBO eatHero(String userId, String userHeroId, List<String> eatHeroIds);

	void eatFragment(String userId, String userHeroId, List<Integer> id);

	void heroBreak(String userId, String userHeroId, List<String> eatHeroId);

	/**
	 * 根据远征英雄获取战斗英雄BO
	 * 
	 * @param hero
	 * @return
	 */
	List<BattleHeroBO> getUserBattleHeroBOList(List<UserExpeditionHero> hero, boolean self);

	List<UserHeroBO> getUserHeroList(String userId, int type, int grade);

	public List<BattleHeroBO> getUserArenaBattleHeroBOList(List<UserHeroBO> userHeroBOList, String userId);

	/**
	 * 根据跨服战获取战斗英雄BO
	 * 
	 * @param list
	 * @return
	 */
	public List<BattleHeroBO> getUserBattleHeroBOList(List<ContestUserHero> list);
}
