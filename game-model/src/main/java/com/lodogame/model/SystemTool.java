package com.lodogame.model;

import java.io.Serializable;

/**
 * 系统道具表
 * 
 * @author jacky
 * 
 */
public class SystemTool implements SystemModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 道具ID
	 */
	private int toolId;

	/**
	 * 类型
	 */
	private int type;

	/**
	 * 道具名称
	 */
	private String name;

	/**
	 * 道具描述
	 */
	private String description;

	/**
	 * 道具价格
	 */
	private int price;

	/**
	 * 元宝进阶需要的元宝数量
	 */
	private int goldMerge;

	/**
	 * 道具图标
	 */
	private String imgId;

	/**
	 * 需要消耗的钥匙
	 */
	private int looseToolId;

	/**
	 * 道具使用类型
	 * 
	 * 2 打开 3 出售 7 使用
	 * 
	 * 这个值是所有使用类型的乘积。例如，一个道具既可以被打开又可以出售，则 toolUseType 的值是 2*3=6
	 * 
	 */
	private int toolUseType;

	public int getToolUseType() {
		return toolUseType;
	}

	public void setToolUseType(int toolUseType) {
		this.toolUseType = toolUseType;
	}

	public int getToolId() {
		return toolId;
	}

	public void setToolId(int toolId) {
		this.toolId = toolId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getImgId() {
		return imgId;
	}

	public void setImgId(String imgId) {
		this.imgId = imgId;
	}

	public int getLooseToolId() {
		return looseToolId;
	}

	public void setLooseToolId(int looseToolId) {
		this.looseToolId = looseToolId;
	}

	public int getGoldMerge() {
		return goldMerge;
	}

	public void setGoldMerge(int goldMerge) {
		this.goldMerge = goldMerge;
	}

	public String getListeKey() {
		return null;
	}

	public String getObjKey() {
		return String.valueOf(this.toolId);
	}

}
