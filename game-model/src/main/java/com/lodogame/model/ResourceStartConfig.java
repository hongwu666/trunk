package com.lodogame.model;

public class ResourceStartConfig {
/**
 * 	h_id INT,
	s_id INT,
	curr_start INT,
	target_start INT,
	min_num INT,
	max_num INT
 */
	private int hId;
	private int sId;
	private int currStart;
	private int targetStart;
	private int minNum;
	private int maxNum;
	public int gethId() {
		return hId;
	}
	public void sethId(int hId) {
		this.hId = hId;
	}
	public int getsId() {
		return sId;
	}
	public void setsId(int sId) {
		this.sId = sId;
	}
	public int getCurrStart() {
		return currStart;
	}
	public void setCurrStart(int currStart) {
		this.currStart = currStart;
	}
	public int getTargetStart() {
		return targetStart;
	}
	public void setTargetStart(int targetStart) {
		this.targetStart = targetStart;
	}
	public int getMinNum() {
		return minNum;
	}
	public void setMinNum(int minNum) {
		this.minNum = minNum;
	}
	public int getMaxNum() {
		return maxNum;
	}
	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}
	
	public boolean in(int n){
		return this.maxNum>n;
	}
}
