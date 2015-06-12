package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ActivityCopyBO {

	/**
	 * 副本类型
	 */
	@Mapper(name = "tp")
	private int type;

	/**
	 * 副本免费挑战此时
	 */
	@Mapper(name = "ft")
	private int freeTime;

	/**
	 * 玩家已挑战次数
	 */
	@Mapper(name = "ct")
	private int challengeTime;

	/**
	 * 图片颜色
	 */
	@Mapper(name = "cl")
	private int color;

	/**
	 * 剩余可以购买的次数
	 */
	@Mapper(name = "cbt")
	private int challengeBuyTime;

	/**
	 * 可以获得道具的数量
	 */
	@Mapper(name = "cn")
	private int toolNum;

	/**
	 * 部队ID
	 */
	@Mapper(name = "fi")
	private int forceId;

	/**
	 * 怪物名字(图片id)
	 */
	@Mapper(name = "mn")
	private String modelId;

	/**
	 * 下次挑战需要花费多少金币
	 */
	@Mapper(name = "gold")
	private int gold;

	/**
	 * 部队名字
	 */
	@Mapper(name = "fn")
	private String forceName;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getFreeTime() {
		return freeTime;
	}

	public void setFreeTime(int freeTime) {
		this.freeTime = freeTime;
	}

	public int getChallengeTime() {
		return challengeTime;
	}

	public void setChallengeTime(int challengeTime) {
		this.challengeTime = challengeTime;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getChallengeBuyTime() {
		return challengeBuyTime;
	}

	public void setChallengeBuyTime(int challengeBuyTime) {
		this.challengeBuyTime = challengeBuyTime;
	}

	public int getToolNum() {
		return toolNum;
	}

	public void setToolNum(int toolNum) {
		this.toolNum = toolNum;
	}

	public int getForceId() {
		return forceId;
	}

	public void setForceId(int forceId) {
		this.forceId = forceId;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public String getForceName() {
		return forceName;
	}

	public void setForceName(String forceName) {
		this.forceName = forceName;
	}

}
