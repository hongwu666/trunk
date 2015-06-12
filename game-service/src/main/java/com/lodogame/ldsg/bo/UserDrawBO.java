package com.lodogame.ldsg.bo;

import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserDrawBO {

	@Mapper(name = "id")
	private int id;

	@Mapper(name = "title")
	private String title;

	@Mapper(name = "mt")
	private int moneyType;

	@Mapper(name = "tid")
	private int toolId;

	@Mapper(name = "tn")
	private int toolNum;

	
	@Mapper(name = "ft")
	private int freeTimes;

	@Mapper(name = "tls")
	private List<DropDescBO> dropToolBOList;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(int moneyType) {
		this.moneyType = moneyType;
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

	public List<DropDescBO> getDropToolBOList() {
		return dropToolBOList;
	}

	public void setDropToolBOList(List<DropDescBO> dropToolBOList) {
		this.dropToolBOList = dropToolBOList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFreeTimes() {
		return freeTimes;
	}

	public void setFreeTimes(int freeTimes) {
		this.freeTimes = freeTimes;
	}

}
