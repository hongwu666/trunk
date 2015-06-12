package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;
import com.lodogame.model.UserHeroInfo;

@Compress
public class HeroPowerRankBO implements UserData {

	public HeroPowerRankBO(UserHeroInfo userHeroInfo) {
		this.userId = userHeroInfo.getUserId();
		this.username = userHeroInfo.getUsername();
		this.systemHeroId = userHeroInfo.getSystemHeroId();
		this.power = userHeroInfo.getPower();
		this.vipLevel = userHeroInfo.getVipLevel();
		this.heroName=userHeroInfo.getHeroname();
	}

	private String userId;

	@Mapper(name = "un")
	private String username;

	@Mapper(name = "shid")
	private int systemHeroId;

	@Mapper(name = "vl")
	private long power;

	@Mapper(name = "rk")
	private int rank;

	@Mapper(name = "nn")
	private String heroName;

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

	public int getSystemHeroId() {
		return systemHeroId;
	}

	public void setSystemHeroId(int systemHeroId) {
		this.systemHeroId = systemHeroId;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getHeroName() {
		return heroName;
	}

	public void setHeroName(String heroName) {
		this.heroName = heroName;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}
	
	

}
