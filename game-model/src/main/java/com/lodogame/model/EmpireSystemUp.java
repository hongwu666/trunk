package com.lodogame.model;

public class EmpireSystemUp implements SystemModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 层数
	 */
	private int floor ;
	/**
	 * 库类型
	 */
	private int empireType ;
	/**
	 * 起始位置
	 */
	private int start ;
	/**
	 * 结束位置
	 */
	private int end ;
	/**
	 * 奖励倍率
	 */
	private int percent ;
	
	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public int getEmpireType() {
		return empireType;
	}

	public void setEmpireType(int empireType) {
		this.empireType = empireType;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public String getListeKey() {
		return floor+"";
	}

	public String getObjKey() {
		
		return null;
	}

}
