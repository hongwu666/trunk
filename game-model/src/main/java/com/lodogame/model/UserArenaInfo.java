package com.lodogame.model;

/**
 * 用户比武bean
 * <li>refreshNum，pkNum，winCount，每天00:00要清零</li>
 * @author Candon
 *
 */
public class UserArenaInfo {
	/**
	 * 用户ID
	 */
	private String userId;
	
	/**
	 * 比武积分
	 */
	private int score;
	
	/**
	 * 免费刷新次数(默认0)
	 */
	private int refreshNum;
	
	/**
	 * 挑战次数(默认0)
	 */
	private int pkNum;
	
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 用户等级
	 */
	private int userLevel;
	
	/**
	 * 连胜次数
	 */
	private int winCount;
	
	
	/**
	 * 购买次数
	 */
	private int buyNum;
	
	/**
	 * 最大连胜次数
	 */
	private int maxWinCount;
	
	/**
	 * 胜利次数
	 */
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

	public int getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}

	public int getWinCount() {
		return winCount;
	}

	public void setWinCount(int winCount) {
		this.winCount = winCount;
	}
	
	
}
