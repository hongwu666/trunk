package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class OnlyOneRankBO {

	/**
	 * 用户名
	 */
	@Mapper(name = "un")
	private String username;

	/**
	 * 积分
	 */
	@Mapper(name = "point")
	private int point;

	/**
	 * 连胜次数
	 */
	@Mapper(name = "win")
	private int win;

	/**
	 * 排名
	 */
	@Mapper(name = "rank")
	private int rank;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getWin() {
		return win;
	}

	public void setWin(int win) {
		this.win = win;
	}

}
