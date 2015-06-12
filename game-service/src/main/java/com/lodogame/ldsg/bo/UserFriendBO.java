package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;
import com.lodogame.model.User;

@Compress
public class UserFriendBO {

	@Mapper(name = "uid")
	private String userId;

	@Mapper(name = "name")
	private String name;

	@Mapper(name = "level")
	private int level;

	@Mapper(name = "vip")
	private int vip;

	@Mapper(name = "shid")
	/**
	 * 第一个上阵武将的img_id
	 */
	private int systemHeroId;

	/**
	 * 最近一次登陆时间
	 */
	@Mapper(name = "llt")
	private long lastLoginTime;

	/**
	 * 战斗力
	 */
	@Mapper(name = "ccp")
	private int capability;
	
	@Mapper(name = "ipd")
	private int isPraisedToday;
	
	/**
	 * 是否在线
	 */
	@Mapper(name = "iol")
	private int isOnline;
	
	public int getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(int isOnline) {
		this.isOnline = isOnline;
	}

	public UserFriendBO() {

	}

	public UserFriendBO(User user) {
		super();
		this.userId = user.getUserId();
		this.name = user.getUsername();
		this.level = user.getLevel();
		this.vip = user.getVipLevel();
		this.isPraisedToday = 0;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	public int getSystemHeroId() {
		return systemHeroId;
	}

	public void setSystemHeroId(int systemHeroId) {
		this.systemHeroId = systemHeroId;
	}

	public long getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public int getCapability() {
		return capability;
	}

	public void setCapability(int capability) {
		this.capability = capability;
	}

	public int getIsPraisedToday() {
		return isPraisedToday;
	}

	public void setIsPraisedToday(int isPraisedToday) {
		this.isPraisedToday = isPraisedToday;
	}
	
}
