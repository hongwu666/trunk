package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * 用户比武对象
 * @author Candon
 *
 */
@Compress
public class UserArenaBo {
	@Mapper(name = "sc")
	private int score;
	
	@Mapper(name = "rfn")
	private int refreshNum;
	
	@Mapper(name = "pn")
	private int pkNum;
	
	@Mapper(name = "trs")
	private int threeRewardStatus;
	
	@Mapper(name = "frs")
	private int fiveRewardStatus;
	
	@Mapper(name = "ers")
	private int eightRewardStatus;
	
	@Mapper(name = "rk")
	private int rank;
	
	@Mapper(name = "wc")
	private int winCount;
	
	@Mapper(name = "ng")
	private int needGold;
	
	@Mapper(name="mwc")
	private int maxWinCount;
	

	@Mapper(name="wn")
	private int winNum;
	
	public int getWinNum() {
		return winNum;
	}
	public void setWinNum(int winNum) {
		this.winNum = winNum;
	}
	
	public int getMaxWinCount() {
		return maxWinCount;
	}

	public void setMaxWinCount(int maxWinCount) {
		this.maxWinCount = maxWinCount;
	}

	public int getNeedGold() {
		return needGold;
	}

	public void setNeedGold(int needGold) {
		this.needGold = needGold;
	}

	public int getWinCount() {
		return winCount;
	}

	public void setWinCount(int winCount) {
		this.winCount = winCount;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getRefreshNum() {
		return refreshNum;
	}

	public void setRefreshNum(int refreshNum) {
		this.refreshNum = refreshNum;
	}

	public int getPkNum() {
		return pkNum;
	}

	public void setPkNum(int pkNum) {
		this.pkNum = pkNum;
	}

	public int getThreeRewardStatus() {
		return threeRewardStatus;
	}

	public void setThreeRewardStatus(int threeRewardStatus) {
		this.threeRewardStatus = threeRewardStatus;
	}

	public int getFiveRewardStatus() {
		return fiveRewardStatus;
	}

	public void setFiveRewardStatus(int fiveRewardStatus) {
		this.fiveRewardStatus = fiveRewardStatus;
	}

	public int getEightRewardStatus() {
		return eightRewardStatus;
	}

	public void setEightRewardStatus(int eightRewardStatus) {
		this.eightRewardStatus = eightRewardStatus;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
	
	
	
}
