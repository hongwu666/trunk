package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class HeroStoneMallBo {
	@Mapper(name = "sid")
	private int systemId;

	@Mapper(name = "tp")
	private int toolType;

	@Mapper(name = "tid")
	private int toolId;

	@Mapper(name = "tna")
	private String toolName;

	@Mapper(name = "tnm")
	private int toolNum;

	@Mapper(name = "nm")
	private int needMoney;

	@Mapper(name = "iec")
	private int isExChange;

	public int getSystemId() {
		return systemId;
	}

	public void setSystemId(int systemId) {
		this.systemId = systemId;
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

	public String getToolName() {
		return toolName;
	}

	public void setToolName(String toolName) {
		this.toolName = toolName;
	}

	public int getToolNum() {
		return toolNum;
	}

	public void setToolNum(int toolNum) {
		this.toolNum = toolNum;
	}

	public int getNeedMoney() {
		return needMoney;
	}

	public void setNeedMoney(int needMoney) {
		this.needMoney = needMoney;
	}

	public int getIsExChange() {
		return isExChange;
	}

	public void setIsExChange(int isExChange) {
		this.isExChange = isExChange;
	}

}
