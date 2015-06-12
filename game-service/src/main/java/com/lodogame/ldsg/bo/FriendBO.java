package com.lodogame.ldsg.bo;

import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class FriendBO {

	@Mapper(name = "uid")
	private String userId;
	
	@Mapper(name = "loid")
	private long lodoId;
	
	/**
	 * 玩家等级
	 */
	@Mapper(name = "level")
	private int level;
	
	@Mapper(name = "name")
	private String userName;
	
	/**
	 * 0表示在线，1表示不在线
	 */
	@Mapper(name = "st")
	private int staus;
	
	/**
	 * 玩家的一个上阵武将的系统武将id
	 */
	@Mapper(name = "shidl")
	private List<UserHeroBO> heroList;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 0表示在线，1表示不在线
	 */
	public int getStaus() {
		return staus;
	}

	/**
	 * 0表示在线，1表示不在线
	 */
	public void setStaus(int staus) {
		this.staus = staus;
	}


	public long getLodoId() {
		return lodoId;
	}

	public void setLodoId(long lodoId) {
		this.lodoId = lodoId;
	}

	public List<UserHeroBO> getHeroList() {
		return heroList;
	}

	public void setHeroList(List<UserHeroBO> heroList) {
		this.heroList = heroList;
	}

	
}
