package com.lodogame.model.log;

public class GoldLog implements ILog {

	private String userId;

	private int useType;

	private int amount;

	private int flag;

	private long beforeAmount;

	private long afterAmount;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getUseType() {
		return useType;
	}

	public void setUseType(int useType) {
		this.useType = useType;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public long getBeforeAmount() {
		return beforeAmount;
	}

	public void setBeforeAmount(long beforeAmount) {
		this.beforeAmount = beforeAmount;
	}

	public long getAfterAmount() {
		return afterAmount;
	}

	public void setAfterAmount(long afterAmount) {
		this.afterAmount = afterAmount;
	}

}
