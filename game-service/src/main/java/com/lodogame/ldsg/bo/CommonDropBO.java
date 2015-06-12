package com.lodogame.ldsg.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * 通用掉落信息BOO FFF
 * 
 * @author jacky
 * 
 */
@Compress
public class CommonDropBO implements Serializable {

	public void setUserToolBOList(List<UserToolBO> userToolBOList) {
		this.userToolBOList = userToolBOList;
	}

	private static final long serialVersionUID = 1L;

	/**
	 * 掉落英雄列表
	 */
	@Mapper(name = "hls")
	private List<UserHeroBO> userHeroBOList = new ArrayList<UserHeroBO>();

	/**
	 * 掉落装备列表
	 */
	@Mapper(name = "eqs")
	private List<UserEquipBO> userEquipBOList = new ArrayList<UserEquipBO>();

	/**
	 * 掉落材料列表
	 */
	@Mapper(name = "tls")
	private List<UserToolBO> userToolBOList = new ArrayList<UserToolBO>();

	/**
	 * 抽奖道具(抽奖用)
	 */
	@Mapper(name = "dls")
	private List<ActivityDrawToolBO> drawToolBOList = new ArrayList<ActivityDrawToolBO>();

	/**
	 * 经验
	 */
	@Mapper(name = "exp")
	private int exp;

	/**
	 * 金币
	 */
	@Mapper(name = "gd")
	private int gold;

	/**
	 * 银币
	 */
	@Mapper(name = "co")
	private int copper;

	/**
	 * VIP增加的经验
	 */
	@Mapper(name = "aexp")
	private int vipAddExp;

	/**
	 * VIP增加的银币
	 */
	@Mapper(name = "aco")
	private int vipAddCopper;

	/**
	 * 龙鳞
	 */
	@Mapper(name = "coin")
	private int coin;

	/**
	 * 用户等级增长
	 */
	@Mapper(name = "ulv")
	private int levelUp;

	/**
	 * 用户基础攻击增长
	 */
	@Mapper(name = "pha")
	private int attackAdd;

	/**
	 * 用户基础防御增长
	 */
	@Mapper(name = "phd")
	private int defenseAdd;

	/**
	 * 用户基础生命增长
	 */
	@Mapper(name = "hp")
	private int lifeAdd;

	/**
	 * 获得积分，用于争霸赛
	 */
	@Mapper(name = "sc")
	private int score;

	/**
	 * 能量
	 */
	@Mapper(name = "soul")
	private int soul;

	/**
	 * 体力
	 */
	private int power;

	/**
	 * 最新排名，用于争霸赛
	 */
	@Mapper(name = "rk")
	private int rank;

	/**
	 * 总排名上升名次
	 */
	@Mapper(name = "urk")
	private int uprank;

	@Mapper(name = "hb")
	private int heroBag;

	@Mapper(name = "eb")
	private int equipBag;

	/**
	 * 武魂
	 */
	@Mapper(name = "muh")
	private int muhon;

	/**
	 * 铭文
	 */
	@Mapper(name = "mw")
	private int mingwen;

	/**
	 * 荣誉
	 */
	@Mapper(name = "honour")
	private int honour;

	/**
	 * vip等级
	 */
	private int vipLevel;

	/**
	 * 声望
	 */
	@Mapper(name = "re")
	private int reputation;

	@Mapper(name = "lfr")
	private int lastFastRank;

	@Mapper(name = "uss")
	private List<UserStoneBO> userStoneBOList = new ArrayList<UserStoneBO>();

	public int getLastFastRank() {
		return lastFastRank;
	}

	public void setLastFastRank(int lastFastRank) {
		this.lastFastRank = lastFastRank;
	}

	public List<UserStoneBO> getUserStoneBOList() {
		return userStoneBOList;
	}

	public int getReputation() {
		return reputation;
	}

	public void setReputation(int reputation) {
		this.reputation = reputation;
	}

	public int getUprank() {
		return uprank;
	}

	public void setUprank(int uprank) {
		this.uprank = uprank;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public List<ActivityDrawToolBO> getDrawToolBOList() {
		return drawToolBOList;
	}

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
	}

	/**
	 * 增加一个掉落武将
	 * 
	 * @param userHeroBO
	 */
	public void addHero(UserHeroBO userHeroBO) {
		userHeroBOList.add(userHeroBO);
	}

	/**
	 * 增加一个掉落装备
	 * 
	 * @param userEquipBO
	 */
	public void addEquip(UserEquipBO userEquipBO) {
		userEquipBOList.add(userEquipBO);
	}

	/**
	 * 增加一个闪光道具
	 * 
	 * @param outId
	 */
	public void addFlashTool(ActivityDrawToolBO activityDrawToolBO) {
		drawToolBOList.add(activityDrawToolBO);
	}

	/**
	 * 是否需要推送用户数据
	 * 
	 * @return
	 */
	public boolean isNeedPushUser() {

		if (this.getCopper() > 0 || this.getGold() > 0 || this.getExp() > 0 || this.getPower() > 0) {
			return true;
		}

		if (this.getHeroBag() > 0 || this.getEquipBag() > 0) {
			return true;
		}

		if (this.getVipLevel() > 0) {
			return true;
		}

		if (this.getReputation() > 0) {
			return true;
		}

		if (this.getMuhon() > 0) {
			return true;
		}

		if (this.getMingwen() > 0) {
			return true;
		}

		if (this.getCoin() > 0) {
			return true;
		}

		if (this.getHonour() > 0) {
			return true;
		}

		return false;
	}

	public int getMingwen() {
		return mingwen;
	}

	public void setMingwen(int mingwen) {
		this.mingwen = mingwen;
	}

	public int getMuhon() {
		return muhon;
	}

	public void setMuhon(int muhon) {
		this.muhon = muhon;
	}

	/**
	 * 增加一个掉落装备
	 * 
	 * @param userToolBO
	 */
	public void addTool(UserToolBO userToolBO) {
		userToolBOList.add(userToolBO);
	}

	/**
	 * 增加一个掉落武将
	 * 
	 * @param userHeroBO
	 */
	public void addStone(UserStoneBO userStoneBO) {
		userStoneBOList.add(userStoneBO);
	}

	public int getLevelUp() {
		return levelUp;
	}

	public void setLevelUp(int levelUp) {
		this.levelUp = levelUp;
	}

	public int getAttackAdd() {
		return attackAdd;
	}

	public void setAttackAdd(int attackAdd) {
		this.attackAdd = attackAdd;
	}

	public int getDefenseAdd() {
		return defenseAdd;
	}

	public void setDefenseAdd(int defenseAdd) {
		this.defenseAdd = defenseAdd;
	}

	public int getLifeAdd() {
		return lifeAdd;
	}

	public void setLifeAdd(int lifeAdd) {
		this.lifeAdd = lifeAdd;
	}

	public List<UserHeroBO> getUserHeroBOList() {
		return userHeroBOList;
	}

	public List<UserEquipBO> getUserEquipBOList() {
		return userEquipBOList;
	}

	public List<UserToolBO> getUserToolBOList() {
		return userToolBOList;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getCopper() {
		return copper;
	}

	public void setCopper(int copper) {
		this.copper = copper;
	}

	public int getVipAddExp() {
		return vipAddExp;
	}

	public void setVipAddExp(int vipAddExp) {
		this.vipAddExp = vipAddExp;
	}

	public int getVipAddCopper() {
		return vipAddCopper;
	}

	public void setVipAddCopper(int vipAddCopper) {
		this.vipAddCopper = vipAddCopper;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getHeroBag() {
		return heroBag;
	}

	public void setHeroBag(int heroBag) {
		this.heroBag = heroBag;
	}

	public int getEquipBag() {
		return equipBag;
	}

	public void setEquipBag(int equipBag) {
		this.equipBag = equipBag;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public int getSoul() {
		return soul;
	}

	public void setSoul(int soul) {
		this.soul = soul;
	}

	public int getHonour() {
		return honour;
	}

	public void setHonour(int honour) {
		this.honour = honour;
	}

}
