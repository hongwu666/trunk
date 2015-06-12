package com.lodogame.ldsg.bo;

import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserSceneBO {

	private String userId;

	@Mapper(name = "sid")
	private int sceneId;

	@Mapper(name = "pf")
	private int passFlag;

	@Mapper(name = "fsl")
	private List<UserForcesBO> forcesBOList;

	@Mapper(name = "st1")
	private int oneStarReward;

	@Mapper(name = "st2")
	private int twoStarReward;

	@Mapper(name = "st3")
	private int thirdStarReward;

	public List<UserForcesBO> getForcesBOList() {
		return forcesBOList;
	}

	public void setForcesBOList(List<UserForcesBO> forcesBOList) {
		this.forcesBOList = forcesBOList;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

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

	public int getThirdStarReward() {
		return thirdStarReward;
	}

	public void setThirdStarReward(int thirdStarReward) {
		this.thirdStarReward = thirdStarReward;
	}

}
