package com.lodogame.ldsg.bo;

import java.util.ArrayList;
import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;
import com.lodogame.ldsg.helper.HeroHelper;
import com.lodogame.model.SystemEquip;

@Compress
public class UserEquipBO {

	/**
	 * 用户装备ID(唯一ID)
	 */
	@Mapper(name = "ueid")
	private String userEquipId;

	/**
	 * 系统装备ID
	 */
	@Mapper(name = "seid")
	private int equipId;

	/**
	 * 穿戴的武将ID
	 */
	@Mapper(name = "uhid")
	private String userHeroId;

	/**
	 * 装备等级
	 */
	@Mapper(name = "elv")
	private int equipLevel;

	/**
	 * 装备名字
	 */
	private String equipName;

	/**
	 * 装备类型
	 */
	@Mapper(name = "etype")
	private int equipType;

	/*
	 * 0 攻击 1 防御 2 生命 3 命中 4 闪避 5 破击 6 格挡 7 暴击 8 韧性
	 */

	@Mapper(name = "val")
	private int[] addVal = new int[9];
	/*
	 * 宝石加成 0 攻击 1 防御 2 生命 3 命中 4 闪避 5 破击 6 格挡 7 暴击 8 韧性
	 */

	@Mapper(name = "addval")
	private float[] addRatio = new float[9];
	/*
	 * 精炼属性列表
	 */

	@Mapper(name = "reval")
	private List<Property> addRefineVal = new ArrayList<Property>();

	/*
	 * 点化属性列表
	 */

	@Mapper(name = "enval")
	private List<EnchantProty> addEnchantVal = new ArrayList<EnchantProty>();
	
	public List<Property> getAddRefineVal() {
		return addRefineVal;
	}

	public void setAddRefineVal(List<Property> addRefineVal) {
		this.addRefineVal = addRefineVal;
	}

	public List<EnchantProty> getAddEnchantVal() {
		return addEnchantVal;
	}

	public void setAddEnchantVal(List<EnchantProty> addEnchantVal) {
		this.addEnchantVal = addEnchantVal;
	}

	public int[] getAddVal() {
		return addVal;
	}

	public void setAddVal(int[] addVal) {
		this.addVal = addVal;
	}

	public float[] getAddRatio() {
		return addRatio;
	}

	public void setAddRatio(float[] addRatio) {
		this.addRatio = addRatio;
	}

	public void addRefineVal(int type, int val) {
		addVal[type - 1] += val;
	}

	public void addVal(int type, int val, float ration) {
		addVal[type - 1] += val;
		addRatio[type - 1] += ration;
	}

	public void calcTotalValue(SystemEquip systemEquip, float[] addRatio) {

		this.attack = systemEquip.getAttackInit() + (int) (((systemEquip.getAttackGrowth() * equipLevel) + systemEquip.getAttackRatio()) * (1 + addRatio[0]));
		this.defense = systemEquip.getDefenseInit() + (int) (((systemEquip.getDefenseGrowth() * equipLevel) + systemEquip.getDefenseRatio()) * (1 + addRatio[1]));
		this.life = systemEquip.getLifeInit() + (int) (((systemEquip.getLifeGrowth() * equipLevel) + systemEquip.getLifeRatio()) * (1 + addRatio[2]));

		for (int i = 0; i <= 8; i++) {
			addVal[i] = (int) (addVal[i] * (1 + addRatio[i]));
		}
		
		//放大点化属性
		HeroHelper.addEnchantRatio(this.getAddEnchantVal(), addRatio);

	}

	/**
	 * 出售价格
	 */
	@Mapper(name = "pric")
	private int price;

	/**
	 * 强化成功概率
	 */
	@Mapper(name = "ur")
	private int upgradetRate;

	/**
	 * 宝石镶嵌列表
	 */
	@Mapper(name = "dsl")
	private List<UserEquipStoneBO> userEquipStoneBOList;

	/**
	 * 宝石孔的数码
	 */
	@Mapper(name = "shn")
	private int stoneHoleNum;

	/**
	 * 可进阶的等级
	 */
	@Mapper(name = "al")
	private int advanceLevel;

	/**
	 * 初始生命值(前段用于显示)
	 */
	@Mapper(name = "ilife")
	private int initialLife;

	private int life;

	private int attack;

	private int defense;

	/**
	 * 初始物攻值(前段用于显示)
	 */
	@Mapper(name = "ipha")
	private int initialAttack;

	/**
	 * 初始物防(前段用于显示)
	 */
	@Mapper(name = "iphd")
	private int initialPhysicsDefense;

	public int getAdvanceLevel() {
		return advanceLevel;
	}

	public void setAdvanceLevel(int advanceLevel) {
		this.advanceLevel = advanceLevel;
	}

	public String getUserEquipId() {
		return userEquipId;
	}

	public void setUserEquipId(String userEquipId) {
		this.userEquipId = userEquipId;
	}

	public int getEquipId() {
		return equipId;
	}

	public void setEquipId(int equipId) {
		this.equipId = equipId;
	}

	public String getUserHeroId() {
		return userHeroId;
	}

	public void setUserHeroId(String userHeroId) {
		this.userHeroId = userHeroId;
	}

	public int getEquipLevel() {
		return equipLevel;
	}

	public void setEquipLevel(int equipLevel) {
		this.equipLevel = equipLevel;
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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getUpgradetRate() {
		return upgradetRate;
	}

	public void setUpgradetRate(int upgradetRate) {
		this.upgradetRate = upgradetRate;
	}

	public List<UserEquipStoneBO> getUserEquipStoneBOList() {
		return userEquipStoneBOList;
	}

	public void setUserEquipStoneBOList(List<UserEquipStoneBO> userEquipStoneBOList) {
		this.userEquipStoneBOList = userEquipStoneBOList;
	}

	public int getStoneHoleNum() {
		return stoneHoleNum;
	}

	public void setStoneHoleNum(int stoneHoleNum) {
		this.stoneHoleNum = stoneHoleNum;
	}

	public int getInitialLife() {
		return initialLife;
	}

	public void setInitialLife(int initialLife) {
		this.initialLife = initialLife;
	}

	public int getInitialAttack() {
		return initialAttack;
	}

	public void setInitialAttack(int initialAttack) {
		this.initialAttack = initialAttack;
	}

	public int getInitialPhysicsDefense() {
		return initialPhysicsDefense;
	}

	public void setInitialPhysicsDefense(int initialPhysicsDefense) {
		this.initialPhysicsDefense = initialPhysicsDefense;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

}
