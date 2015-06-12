package com.lodogame.model;

import java.io.Serializable;

/**
 * 系统武将
 * 
 * @author jacky
 * 
 */
public class SystemHero implements Serializable, SystemModel {

	private static final long serialVersionUID = -5030957883739690497L;

	/**
	 * 系统武将ID
	 */
	private int systemHeroId;

	/**
	 * 武将星数
	 */
	private int heroStar;

	/**
	 * 武将人物唯一ID
	 */
	private int heroId;

	/**
	 * 武将名称
	 */
	private String heroName;

	/**
	 * 武将品质
	 */
	private int heroColor;

	/**
	 * 武将职业
	 */
	private int career;

	/**
	 * 普通攻击
	 */
	private int normalPlan;

	/**
	 * 主动技能
	 */
	private int plan;

	/**
	 * 天赋技能1
	 */
	private int skill1;

	/**
	 * 天赋技能2
	 */
	private int skill2;

	/**
	 * 天赋技能3
	 */
	private int skill3;

	/**
	 * 天赋技能4
	 */
	private int skill4;

	/**
	 * 头像id
	 */
	private int imgId;

	/**
	 * 卡牌ID
	 */
	private int cardId;

	/**
	 * 分解获得的数
	 */
	private int jiangshanOrder;

	/**
	 * 是否可以进阶，0不可以，1可以
	 */
	private int canUpgrade;

	/**
	 * 生命基本值
	 */
	private int lifeInit;

	/**
	 * 攻击基本值
	 */
	private int attackInit;

	/**
	 * 防疫基本值
	 */
	private int defenseInit;

	/**
	 * 生命成长值
	 */
	private int lifeGrowth;

	/**
	 * 攻击成长值
	 */
	private int attackGrowth;

	/**
	 * 防御成长值
	 */
	private int defenseGrowth;

	/**
	 * 最大等级
	 */
	private int upgradeLevel;

	/**
	 * modelId
	 */
	private String modelId;

	/**
	 * 升星经验
	 */
	private int starPoint;

	public int getSystemHeroId() {
		return systemHeroId;
	}

	public void setSystemHeroId(int systemHeroId) {
		this.systemHeroId = systemHeroId;
	}

	public int getHeroStar() {
		return heroStar;
	}

	public void setHeroStar(int heroStar) {
		this.heroStar = heroStar;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public String getHeroName() {
		return heroName;
	}

	public void setHeroName(String heroName) {
		this.heroName = heroName;
	}

	public int getHeroColor() {
		return heroColor;
	}

	public void setHeroColor(int heroColor) {
		this.heroColor = heroColor;
	}

	public int getCareer() {
		return career;
	}

	public void setCareer(int career) {
		this.career = career;
	}

	public int getPlan() {
		return plan;
	}

	public void setPlan(int plan) {
		this.plan = plan;
	}

	public int getSkill1() {
		return skill1;
	}

	public void setSkill1(int skill1) {
		this.skill1 = skill1;
	}

	public int getSkill2() {
		return skill2;
	}

	public void setSkill2(int skill2) {
		this.skill2 = skill2;
	}

	public int getSkill3() {
		return skill3;
	}

	public void setSkill3(int skill3) {
		this.skill3 = skill3;
	}

	public int getSkill4() {
		return skill4;
	}

	public void setSkill4(int skill4) {
		this.skill4 = skill4;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public int getCardId() {
		return cardId;
	}

	public void setCardId(int cardId) {
		this.cardId = cardId;
	}

	public int getCanUpgrade() {
		return canUpgrade;
	}

	public void setCanUpgrade(int canUpgrade) {
		this.canUpgrade = canUpgrade;
	}

	public int getUpgradeLevel() {
		return upgradeLevel;
	}

	public void setUpgradeLevel(int upgradeLevel) {
		this.upgradeLevel = upgradeLevel;
	}

	public int getJiangshanOrder() {
		return jiangshanOrder;
	}

	public void setJiangshanOrder(int jiangshanOrder) {
		this.jiangshanOrder = jiangshanOrder;
	}

	public int getNormalPlan() {
		return normalPlan;
	}

	public void setNormalPlan(int normalPlan) {
		this.normalPlan = normalPlan;
	}

	public int getLifeInit() {
		return lifeInit;
	}

	public void setLifeInit(int lifeInit) {
		this.lifeInit = lifeInit;
	}

	public int getAttackInit() {
		return attackInit;
	}

	public void setAttackInit(int attackInit) {
		this.attackInit = attackInit;
	}

	public int getDefenseInit() {
		return defenseInit;
	}

	public void setDefenseInit(int defenseInit) {
		this.defenseInit = defenseInit;
	}

	public int getLifeGrowth() {
		return lifeGrowth;
	}

	public void setLifeGrowth(int lifeGrowth) {
		this.lifeGrowth = lifeGrowth;
	}

	public int getAttackGrowth() {
		return attackGrowth;
	}

	public void setAttackGrowth(int attackGrowth) {
		this.attackGrowth = attackGrowth;
	}

	public int getDefenseGrowth() {
		return defenseGrowth;
	}

	public void setDefenseGrowth(int defenseGrowth) {
		this.defenseGrowth = defenseGrowth;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public int getStarPoint() {
		return starPoint;
	}

	public void setStarPoint(int starPoint) {
		this.starPoint = starPoint;
	}

	public String getListeKey() {
		return String.valueOf(this.heroId);
	}

	public String getObjKey() {
		return String.valueOf(this.systemHeroId);
	}

}
