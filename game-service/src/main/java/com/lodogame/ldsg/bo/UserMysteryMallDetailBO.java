package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * 用户神秘商店物品BO
 * 
 * @author jackyshi
 * 
 */
@Compress
public class UserMysteryMallDetailBO {

	@Mapper(name = "sid")
	private int id;

	@Mapper(name = "tp")
	private int toolType;

	@Mapper(name = "tid")
	private int toolId;

	@Mapper(name = "tnm")
	private int toolNum;

	@Mapper(name = "tna")
	private String toolName;

	@Mapper(name = "nm")
	private int amount;

	private int dropId;

	@Mapper(name = "iec")
	private int flag;

	@Mapper(name = "ra")
	private int rarity;

	public int getRarity() {
		return rarity;
	}

	public void setRarity(int rarity) {
		this.rarity = rarity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public int getToolNum() {
		return toolNum;
	}

	public void setToolNum(int toolNum) {
		this.toolNum = toolNum;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getDropId() {
		return dropId;
	}

	public void setDropId(int dropId) {
		this.dropId = dropId;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getToolName() {
		return toolName;
	}

	public void setToolName(String toolName) {
		this.toolName = toolName;
	}

}
