package com.lodogame.model;

public class SystemFragment implements SystemModel {

	/**
	 * 碎片的道具类型
	 */
	private int toolType;

	/**
	 * 碎片的道具id
	 */
	private int toolId;

	/**
	 * 品质
	 */
	private int color;

	/**
	 * 碎片的名称
	 */
	private String toolName;

	/**
	 * 描述
	 */
	private String desc;

	/**
	 * 合成需要的碎片数量
	 */
	private int needFragmentNum;

	/**
	 * 合成得到的道具类型
	 */
	private int mergedToolType;

	/**
	 * 合成得到的道具id
	 */
	private int mergedToolId;
	/**
	 * 星级
	 */
	private int star;
	/**
	 * 类型
	 */
	private int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	private int eatExp;

	public int getEatExp() {
		return eatExp;
	}

	public void setEatExp(int eatExp) {
		this.eatExp = eatExp;
	}

	/**
	 * 合成得到的道具数量
	 */
	private int mergedToolNum;

	public int getToolType() {
		return toolType;
	}

	public void setToolType(int toolType) {
		this.toolType = toolType;
	}

	public int getToolId() {
		return toolId;
	}

	public void setToolId(int toolId) {
		this.toolId = toolId;
	}

	public String getToolName() {
		return toolName;
	}

	public void setToolName(String toolName) {
		this.toolName = toolName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getNeedFragmentNum() {
		return needFragmentNum;
	}

	public void setNeedFragmentNum(int needFragmentNum) {
		this.needFragmentNum = needFragmentNum;
	}

	public int getMergedToolType() {
		return mergedToolType;
	}

	public void setMergedToolType(int mergedToolType) {
		this.mergedToolType = mergedToolType;
	}

	public int getMergedToolId() {
		return mergedToolId;
	}

	public void setMergedToolId(int mergedToolId) {
		this.mergedToolId = mergedToolId;
	}

	public int getMergedToolNum() {
		return mergedToolNum;
	}

	public void setMergedToolNum(int mergedToolNum) {
		this.mergedToolNum = mergedToolNum;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public String getListeKey() {
		return null;
	}

	public String getObjKey() {
		return String.valueOf(toolId);
	}

}
