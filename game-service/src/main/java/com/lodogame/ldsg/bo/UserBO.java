package com.lodogame.ldsg.bo;

import java.io.Serializable;
import java.util.Map;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserBO implements Serializable {

	public final static int BAG_MAX = 800;

	private static final long serialVersionUID = 1L;

	@Mapper(name = "pid")
	private long playerId;

	@Mapper(name = "un")
	private String username;

	@Mapper(name = "lv")
	private int level;

	@Mapper(name = "gd")
	private long goldNum;

	@Mapper(name = "co")
	private long copper;

	@Mapper(name = "exp")
	private long exp;

	@Mapper(name = "elo")
	private long muhon;
	
	@Mapper(name = "soul")
	private long soul;// 魂力

	@Mapper(name = "mingwen")
	private long mingwen;

	@Mapper(name = "energy")
	private long energy; // 精元

	@Mapper(name = "fc")
	private int power;

	@Mapper(name = "fat")
	private long powerAddTime;

	@Mapper(name = "fai")
	private long powerAddInterval;

	@Mapper(name = "vl")
	private int vipLevel;

	@Mapper(name = "ve")
	private long vipExpired;

	@Mapper(name = "hbl")
	private int heroBagLimit;

	@Mapper(name = "ebl")
	private int equipBagLimit;

	@Mapper(name = "bci")
	private Map<String, Integer> buyCopperInfo;

	@Mapper(name = "pm")
	private int payAmount;

	/**
	 * 用户新手引导步骤
	 */
	@Mapper(name = "gs")
	private int guideStep;

	@Mapper(name = "blt")
	private long bannedLeftTime;

	@Mapper(name = "rep")
	private int reputation;

	@Mapper(name = "img")
	private int imgId;

	@Mapper(name = "coin")
	private int coin;

	@Mapper(name = "honour")
	private int honour;

	@Mapper(name = "skill")
	private int skill;
	/**
	 * 战力
	 */
	@Mapper(name = "ccb")
	private int capability;

	public int getCoin() {
		return coin;
	}

	public int getSkill() {
		return skill;
	}

	public void setSkill(int skill) {
		this.skill = skill;
	}

	public void setCoin(int coin) {
		this.coin = coin;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public int getReputation() {
		return reputation;
	}

	public void setReputation(int reputation) {
		this.reputation = reputation;
	}

	public int getGuideStep() {
		return guideStep;
	}

	public void setGuideStep(int guideStep) {
		this.guideStep = guideStep;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getGoldNum() {
		return goldNum;
	}

	public void setGoldNum(long goldNum) {
		this.goldNum = goldNum;
	}

	public long getCopper() {
		return copper;
	}

	public void setCopper(long copper) {
		this.copper = copper;
	}

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public long getPowerAddTime() {
		return powerAddTime;
	}

	public long getSoul() {
		return soul;
	}

	public void setSoul(long soul) {
		this.soul = soul;
	}

	public long getEnergy() {
		return energy;
	}

	public void setEnergy(long energy) {
		this.energy = energy;
	}

	public void setPowerAddTime(long powerAddTime) {
		this.powerAddTime = powerAddTime;
	}

	public long getPowerAddInterval() {
		return powerAddInterval;
	}

	public void setPowerAddInterval(long powerAddInterval) {
		this.powerAddInterval = powerAddInterval;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public long getVipExpired() {
		return vipExpired;
	}

	public void setVipExpired(long vipExpired) {
		this.vipExpired = vipExpired;
	}

	public int getHeroBagLimit() {
		return heroBagLimit;
	}

	public void setHeroBagLimit(int heroBagLimit) {
		if (heroBagLimit > BAG_MAX) {
			heroBagLimit = BAG_MAX;
		}
		this.heroBagLimit = heroBagLimit;
	}

	public int getEquipBagLimit() {
		return equipBagLimit;
	}

	public void setEquipBagLimit(int equipBagLimit) {
		if (equipBagLimit > BAG_MAX) {
			equipBagLimit = BAG_MAX;
		}
		this.equipBagLimit = equipBagLimit;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public Map<String, Integer> getBuyCopperInfo() {
		return buyCopperInfo;
	}

	public void setBuyCopperInfo(Map<String, Integer> buyCopperInfo) {
		this.buyCopperInfo = buyCopperInfo;
	}

	public int getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(int payAmount) {
		this.payAmount = payAmount;
	}

	public long getBannedLeftTime() {
		return bannedLeftTime;
	}

	public void setBannedLeftTime(long bannedLeftTime) {
		this.bannedLeftTime = bannedLeftTime;
	}

	public long getMuhon() {
		return muhon;
	}

	public void setMuhon(long muhon) {
		this.muhon = muhon;
	}

	public int getCapability() {
		return capability;
	}

	public void setCapability(int capability) {
		this.capability = capability;
	}

	public long getMingwen() {
		return mingwen;
	}

	public void setMingwen(long mingwen) {
		this.mingwen = mingwen;
	}

	public int getHonour() {
		return honour;
	}

	public void setHonour(int honour) {
		this.honour = honour;
	}

}
