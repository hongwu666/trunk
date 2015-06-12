package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;
import com.lodogame.model.UserLevelRank;

@Compress
public class UserLevelRankBO implements UserData {

	public UserLevelRankBO(UserLevelRank userLevelRank) {
		this.userId = userLevelRank.getUserId();
		this.username = userLevelRank.getUsername();
		this.level = userLevelRank.getLevel();
		this.vipLevel = userLevelRank.getVipLevel();
	}

	private String userId;

	@Mapper(name = "un")
	private String username;

	@Mapper(name = "vl")
	private int level;

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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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
