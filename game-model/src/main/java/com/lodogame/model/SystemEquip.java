package com.lodogame.model;

import java.io.Serializable;

public class SystemEquip implements SystemModel, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 装备ID
	 */
	private int equipId;

	/**
	 * yuanfen
	 */
	private int predestinedId;
	
	private int predestinedHeroId;

	/**
	 * 装备名称
	 */
	private String equipName;

	/**
	 * 装备类型
	 */
	private int equipType;

	/**
	 * 套装id
	 */
	private int suitId;

	/**
	 * 物攻
	 */
	private int attackInit;

	/**
	 * 物防
	 */
	private int defenseInit;

	/**
	 * 生命
	 */
	private int lifeInit;

	/**
	 * 物攻
	 */
	private double attackGrowth;

	/**
	 * 物防
	 */
	private double defenseGrowth;

	/**
	 * 生命
	 */
	private double lifeGrowth;

	/**
	 * 进攻进阶参数
	 */
	private int attackRatio;

	/**
	 * 防守进阶参数
	 */
	private int defenseRatio;

	/**
	 * 生命进阶参数
	 */
	private int lifeRatio;

	/**
	 * 可进阶等级
	 */
	private int advanceLevel;

	/**
	 * 品质颜色
	 */
	private int color;

	/**
	 * 星级
	 */
	private int equipStar;

	/**
	 * 职业
	 */
	private int career;

	/**
	 * 宝石的孔位
	 */
	private int stoneHoleNum;

	/**
	 * 格挡
	 */
	private int parry;

	/**
	 * 暴击
	 */
	private int crit;

	/**
	 * 闪避
	 */
	private int dodge;

	/**
	 * 命中
	 */
	private int hit;

	/**
	 * 韧性
	 */
	private int toughness;

	/**
	 * 破击
	 */
	private int bogey;

	public int getPredestinedId() {
		return predestinedId;
	}

	public int getPredestinedHeroId() {
		return predestinedHeroId;
	}

	public void setPredestinedHeroId(int predestinedHeroId) {
		this.predestinedHeroId = predestinedHeroId;
	}

	public void setPredestinedId(int predestinedId) {
		this.predestinedId = predestinedId;
	}

	public int getEquipStar() {
		return equipStar;
	}

	public void setEquipStar(int equipStar) {
		this.equipStar = equipStar;
	}

	public int getEquipId() {
		return equipId;
	}

	public void setEquipId(int equipId) {
		this.equipId = equipId;
	}

	public String getEquipName() {
		return equipName;
	}

	public void setEquipName(String equipName) {
		this.equipName = equipName;
	}

	public int getEquipType() {
		return equipType;
	}

	public void setEquipType(int equipType) {
		this.equipType = equipType;
	}

	public int getAdvanceLevel() {
		return advanceLevel;
	}

	public void setAdvanceLevel(int advanceLevel) {
		this.advanceLevel = advanceLevel;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getCareer() {
		return career;
	}

	public void setCareer(int career) {
		this.career = career;
	}

	public int getStoneHoleNum() {
		return stoneHoleNum;
	}

	public void setStoneHoleNum(int stoneHoleNum) {
		this.stoneHoleNum = stoneHoleNum;
	}

	public int getSuitId() {
		return suitId;
	}

	public void setSuitId(int suitId) {
		this.suitId = suitId;
	}

	public String getListeKey() {
		return equipType+"_"+career+"_"+equipStar;
	}

	public String getObjKey() {
		return String.valueOf(equipId);
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

	public int getLifeInit() {
		return lifeInit;
	}

	public void setLifeInit(int lifeInit) {
		this.lifeInit = lifeInit;
	}

	public double getAttackGrowth() {
		return attackGrowth;
	}

	public void setAttackGrowth(double attackGrowth) {
		this.attackGrowth = attackGrowth;
	}

	public double getDefenseGrowth() {
		return defenseGrowth;
	}

	public void setDefenseGrowth(double defenseGrowth) {
		this.defenseGrowth = defenseGrowth;
	}

	public double getLifeGrowth() {
		return lifeGrowth;
	}

	public void setLifeGrowth(double lifeGrowth) {
		this.lifeGrowth = lifeGrowth;
	}

	public int getParry() {
		return parry;
	}

	public void setParry(int parry) {
		this.parry = parry;
	}

	public int getCrit() {
		return crit;
	}

	public void setCrit(int crit) {
		this.crit = crit;
	}

	public int getDodge() {
		return dodge;
	}

	public void setDodge(int dodge) {
		this.dodge = dodge;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public int getToughness() {
		return toughness;
	}

	public void setToughness(int toughness) {
		this.toughness = toughness;
	}

	public int getBogey() {
		return bogey;
	}

	public void setBogey(int bogey) {
		this.bogey = bogey;
	}

	public int getAttackRatio() {
		return attackRatio;
	}

	public void setAttackRatio(int attackRatio) {
		this.attackRatio = attackRatio;
	}

	public int getDefenseRatio() {
		return defenseRatio;
	}

	public void setDefenseRatio(int defenseRatio) {
		this.defenseRatio = defenseRatio;
	}

	public int getLifeRatio() {
		return lifeRatio;
	}

	public void setLifeRatio(int lifeRatio) {
		this.lifeRatio = lifeRatio;
	}

}
