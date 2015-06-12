package com.lodogame.model;

public class MeridianUserInfo {
	private String userId;
	private int meridianType;
	private int meridianNode;
	private String userHeroId;
	private int luck;
	private int exp;
	private int level;
	

	public static MeridianUserInfo create(String userId,String heroId,int meridianType,int meridianNode){
		MeridianUserInfo m = new MeridianUserInfo();
		m.setUserId(userId);
		m.setUserHeroId(heroId);
		m.setMeridianNode(meridianNode);
		m.setMeridianType(meridianType);
		return m;
	}
	
	public void addExp(int v){
		exp+=v;
	}
	
	public void addLuck(int v){
		luck+=v;
	}

	public String getUserHeroId() {
		return userHeroId;
	}

	public void setUserHeroId(String userHeroId) {
		this.userHeroId = userHeroId;
	}

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
