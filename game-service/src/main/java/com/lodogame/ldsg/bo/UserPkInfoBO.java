package com.lodogame.ldsg.bo;

import java.io.Serializable;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserPkInfoBO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 玩家ID
	 */
	@Mapper(name = "pid")
	private long playerId;

	/**
	 * 排名
	 */
	@Mapper(name = "rk")
	private int rank;

	/**
	 * 挑战剩余次数
	 */
	@Mapper(name = "pkt")
	private int pkTimes;

	/**
	 * 购买挑战次数
	 */
	@Mapper(name = "bpkt")
	private int buyPkTimes;

	/**
	 * 购买需要的元宝
	 */
	@Mapper(name = "bpktm")
	private int buyPkTimesMoney;

	@Mapper(name = "img")
	private int imgId;

	/**
	 * 挑战获取声望数量
	 */
	@Mapper(name = "grep")
	private int gainRepAmount;

	/**
	 * 最好名次
	 */
	@Mapper(name = "fr")
	private int fastRank;

	public int getFastRank() {
		return fastRank;
	}

	public void setFastRank(int fastRank) {
		this.fastRank = fastRank;
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

	public int getPkTimes() {
		return pkTimes;
	}

	public void setPkTimes(int pkTimes) {
		this.pkTimes = pkTimes;
	}

	public int getBuyPkTimes() {
		return buyPkTimes;
	}

	public void setBuyPkTimes(int buyPkTimes) {
		this.buyPkTimes = buyPkTimes;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public int getGainRepAmount() {
		return gainRepAmount;
	}

	public void setGainRepAmount(int gainRepAmount) {
		this.gainRepAmount = gainRepAmount;
	}

	public int getBuyPkTimesMoney() {
		return buyPkTimesMoney;
	}

	public void setBuyPkTimesMoney(int buyPkTimesMoney) {
		this.buyPkTimesMoney = buyPkTimesMoney;
	}

}
