package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;
@Compress
public class RankBO implements UserData{
	
	private String userId;
	
	/**
	 * 玩家名称
	 */
	@Mapper(name = "un")
	private String username;
	/**
	 * 玩家排名
	 */
	@Mapper(name = "rk")
	private int rank;
	

	@Mapper(name = "vp")
	private int vip;
	
	@Mapper(name = "vl")
	private String value;

	@Override
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
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getVip() {
		return vip;
	}
	public void setVip(int vip) {
		this.vip = vip;
	}
	public RankBO(String userId, String username, int rank, int vip, String value) {
		super();
		this.userId = userId;
		this.username = username;
		this.rank = rank;
		this.vip = vip;
		this.value = value;
	}
	
}
