package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * 比武挑战对象
 * 
 * @author Candon
 * 
 */
@Compress
public class ArenaDefiantBO {

	@Mapper(name = "pid")
	private long playId;

	@Mapper(name = "uiid")
	private int userImgId;

	@Mapper(name = "uid")
	private String userId;

	@Mapper(name = "unm")
	private String usrename;

	@Mapper(name = "lv")
	private int level;

	@Mapper(name = "ie")
	private int isEnemy;

	@Mapper(name = "att")
	private int att;

	@Mapper(name = "bn")
	private int buyNum;

	private boolean isRobot;

	public int getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}

	public int getAtt() {
		return att;
	}

	public void setAtt(int att) {
		this.att = att;
	}

	public long getPlayId() {
		return playId;
	}

	public void setPlayId(long playId) {
		this.playId = playId;
	}

	public int getUserImgId() {
		return userImgId;
	}

	public void setUserImgId(int userImgId) {
		this.userImgId = userImgId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsrename() {
		return usrename;
	}

	public void setUsrename(String usrename) {
		this.usrename = usrename;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getIsEnemy() {
		return isEnemy;
	}

	public void setIsEnemy(int isEnemy) {
		this.isEnemy = isEnemy;
	}

	public boolean isRobot() {
		return isRobot;
	}

	public void setRobot(boolean isRobot) {
		this.isRobot = isRobot;
	}

}
