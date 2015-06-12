package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;
import com.lodogame.model.UserPowerRank;

@Compress
public class UserPowerRankBO implements UserData {

	public UserPowerRankBO(UserPowerRank userPowerRank) {
		this.userId = userPowerRank.getUserId();
		this.username = userPowerRank.getUsername();
		this.power = userPowerRank.getPower();
		this.vipLevel = userPowerRank.getVipLevel();
	}


	private String userId;

	@Mapper(name = "un")
	private String username;

	@Mapper(name = "vl")
	private long power;

	@Mapper(name = "rk")
	private int rank;

	@Mapper(name = "vp")
	private int vipLevel;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getPower() {
		return power;
	}

	public void setPower(long power) {
		this.power = power;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

}
