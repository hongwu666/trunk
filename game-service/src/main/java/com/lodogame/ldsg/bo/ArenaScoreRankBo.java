package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * 积分排行对象
 * @author Candon
 *
 */
@Compress
public class ArenaScoreRankBo {
	
	@Mapper(name = "rk")
	private int rank;
	
	@Mapper(name = "un")
	private String username;

	@Mapper(name = "shid")
	private int systemHeroId;
	
	@Mapper(name = "lv")
	private int level;
	
	@Mapper(name="wc")
	private int winCount;
	
	
	
	public int getWinCount() {
		return winCount;
	}

	public void setWinCount(int winCount) {
		this.winCount = winCount;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
