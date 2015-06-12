package com.lodogame.ldsg.bo;

import java.io.Serializable;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * 争霸玩家信息，包含玩家的英雄列表
 * 
 * @author Administrator
 * 
 */
@Compress
public class PkPlayerBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Mapper(name = "nn")
	private String nickname;

	@Mapper(name = "lv")
	private int level;

	@Mapper(name = "pid")
	private long playerId;

	@Mapper(name = "rk")
	private int rank;

	@Mapper(name = "img")
	private int img;

	@Mapper(name = "cor")
	private int color;

	@Mapper(name="att")
	private int att;
	
	public int getAtt() {
		return att;
	}

	public void setAtt(int att) {
		this.att = att;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getImg() {
		return img;
	}

	public void setImg(int img) {
		this.img = img;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

}
