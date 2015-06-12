package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class MeridianMeridianBO {

	@Mapper(name = "uid")
	private String userId;

	@Mapper(name = "mt")
	private int meridianType;

	@Mapper(name = "mn")
	private int meridianNode;

	@Mapper(name = "uhi")
	private String userHeroId;

	@Mapper(name = "luck")
	private int luck;

	@Mapper(name = "exp")
	private int exp;

	@Mapper(name = "level")
	private int level;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getMeridianType() {
		return meridianType;
	}

	public void setMeridianType(int meridianType) {
		this.meridianType = meridianType;
	}

	public int getMeridianNode() {
		return meridianNode;
	}

	public void setMeridianNode(int meridianNode) {
		this.meridianNode = meridianNode;
	}

	public String getUserHeroId() {
		return userHeroId;
	}

	public void setUserHeroId(String userHeroId) {
		this.userHeroId = userHeroId;
	}

	public int getLuck() {
		return luck;
	}

	public void setLuck(int luck) {
		this.luck = luck;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
