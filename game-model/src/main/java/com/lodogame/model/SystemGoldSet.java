package com.lodogame.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 金币套餐
 * 
 * @author jacky
 * 
 */
public class SystemGoldSet implements Serializable {

	private static final long serialVersionUID = 1L;

	private int systemGoldSetId;
	
	private BigDecimal money;

	private int gold;

	private int type;

	private String title;

	private String description;

	private Date startTime;

	private Date endTime;

	/**
	 * 是否是首冲双倍,0 不是，1是
	 */
	private int isDouble;
	
	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getSystemGoldSetId() {
		return systemGoldSetId;
	}

	public void setSystemGoldSetId(int systemGoldSetId) {
		this.systemGoldSetId = systemGoldSetId;
	}

	public int getIsDouble() {
		return isDouble;
	}

	public void setIsDouble(int isDouble) {
		this.isDouble = isDouble;
	}

	public boolean isMonthlyCard() {
		return type == 2 ? true : false;
	}


}
