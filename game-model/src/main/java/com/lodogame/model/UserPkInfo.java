package com.lodogame.model;

import java.io.Serializable;
import java.util.Date;

public class UserPkInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 用户lodo id
	 */
	private long lodoId;

	/**
	 * 排名
	 */
	private int rank;

	/**
	 * 挑战次数
	 */
	private int pkTimes;

	/**
	 * 更新PK次数时间
	 */
	private Date updatePkTime;

	/**
	 * 购买pk次数
	 */
	private int buyPkTimes;

	/**
	 * 最后购买pk次数的时间
	 */
	private Date lastBuyTime;

	/**
	 * 头像ID
	 */
	private int imgId;
	
	/**
	 * 最好名次
	 */
	private int fastRank;

	
	
	
	public int getFastRank() {
		return fastRank;
	}

	public void setFastRank(int fastRank) {
		this.fastRank = fastRank;
	}

	/**
	 * 等级
	 */
	private int level;

	public long getLodoId() {
		return lodoId;
	}

	public void setLodoId(long lodoId) {
		this.lodoId = lodoId;
	}

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

	public int getPkTimes() {
		return pkTimes;
	}

	public void setPkTimes(int pkTimes) {
		this.pkTimes = pkTimes;
	}

	public Date getUpdatePkTime() {
		return updatePkTime;
	}

	public void setUpdatePkTime(Date updatePkTime) {
		this.updatePkTime = updatePkTime;
	}

	public int getBuyPkTimes() {
		return buyPkTimes;
	}

	public void setBuyPkTimes(int buyPkTimes) {
		this.buyPkTimes = buyPkTimes;
	}

	public Date getLastBuyTime() {
		return lastBuyTime;
	}

	public void setLastBuyTime(Date lastBuyTime) {
		this.lastBuyTime = lastBuyTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
