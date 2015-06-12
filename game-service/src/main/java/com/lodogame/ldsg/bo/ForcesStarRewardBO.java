package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ForcesStarRewardBO {

	@Mapper(name = "sid")
	private int sceneId;
	
	@Mapper(name = "pf")
	private int passFlag;

	@Mapper(name = "osr")
	private long oneStarReward;
	
	@Mapper(name="tsr")
	private int twoStarReward;

	@Mapper(name="thsr")
	private int thirdStarReward;

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	public int getPassFlag() {
		return passFlag;
	}

	public void setPassFlag(int passFlag) {
		this.passFlag = passFlag;
	}

	public long getOneStarReward() {
		return oneStarReward;
	}

	public void setOneStarReward(long oneStarReward) {
		this.oneStarReward = oneStarReward;
	}

	public int getTwoStarReward() {
		return twoStarReward;
	}

	public void setTwoStarReward(int twoStarReward) {
		this.twoStarReward = twoStarReward;
	}

	public int getThirdStarReward() {
		return thirdStarReward;
	}

	public void setThirdStarReward(int thirdStarReward) {
		this.thirdStarReward = thirdStarReward;
	}
	
	
}
