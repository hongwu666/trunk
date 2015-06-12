package com.lodogame.model;

public class SystemScene implements SystemModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 场景ID
	 */
	public int sceneId;

	/**
	 * 场景名称
	 */
	public String sceneName;

	/**
	 * 开放等级
	 */
	public int openLevel;

	/**
	 * 等级信息
	 */
	public String levelInfo;

	/**
	 * 武将信息
	 */
	public String heroInfo;

	/**
	 * 图片ID
	 */
	public int imgId;

	/**
	 * 1星奖励
	 */
	public int oneStarReward;

	/**
	 * 2星奖励
	 */
	public int twoStarReward;

	/**
	 * 3星奖励
	 */
	public int threeStarReward;

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	public String getSceneName() {
		return sceneName;
	}

	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}

	public int getOpenLevel() {
		return openLevel;
	}

	public void setOpenLevel(int openLevel) {
		this.openLevel = openLevel;
	}

	public String getLevelInfo() {
		return levelInfo;
	}

	public void setLevelInfo(String levelInfo) {
		this.levelInfo = levelInfo;
	}

	public String getHeroInfo() {
		return heroInfo;
	}

	public void setHeroInfo(String heroInfo) {
		this.heroInfo = heroInfo;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public int getOneStarReward() {
		return oneStarReward;
	}

	public void setOneStarReward(int oneStarReward) {
		this.oneStarReward = oneStarReward;
	}

	public int getTwoStarReward() {
		return twoStarReward;
	}

	public void setTwoStarReward(int twoStarReward) {
		this.twoStarReward = twoStarReward;
	}

	public int getThreeStarReward() {
		return threeStarReward;
	}

	public void setThreeStarReward(int threeStarReward) {
		this.threeStarReward = threeStarReward;
	}

	public String getListeKey() {
		return null;
	}

	public String getObjKey() {
		return String.valueOf(this.sceneId);
	}

}
