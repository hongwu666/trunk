package com.lodogame.ldsg.web.model.mol;

import java.math.BigDecimal;

/**
 * MOL 充值套餐配置
 * @author chengevo
 *
 */
public class SystemMOLCardPayment {

	private String waresId;
	private String currencyCode;
	private BigDecimal amount;
	
	/**
	 * 换算成台币后的金额
	 */
	private BigDecimal twdAmount;
	private int goldNum;
	
	/**
	 * 充值返回的金币
	 */
	private int extraGoldNum;
	
	/**
	 * 是否双倍
	 */
	private int isDouble;
	

	public BigDecimal getTwdAmount() {
		return twdAmount;
	}
	public void setTwdAmount(BigDecimal twdAmount) {
		this.twdAmount = twdAmount;
	}
	public int getExtraGoldNum() {
		return extraGoldNum;
	}
	public void setExtraGoldNum(int extraGoldNum) {
		this.extraGoldNum = extraGoldNum;
	}
	public int getIsDouble() {
		return isDouble;
	}
	public void setIsDouble(int isDouble) {
		this.isDouble = isDouble;
	}
	
	public String getWaresId() {
		return waresId;
	}
	public void setWaresId(String waresId) {
		this.waresId = waresId;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public int getGoldNum() {
		return goldNum;
	}
	public void setGoldNum(int goldNum) {
		this.goldNum = goldNum;
	}
}
