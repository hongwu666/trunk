package com.lodogame.ldsg.bo;

import java.util.ArrayList;
import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class SystemReduceRebateBo {

	@Mapper(name = "id")
	private int id;

	@Mapper(name = "gl")
	private int gold;

	@Mapper(name = "st")
	private int status;

	@Mapper(name = "tls")
	private List<DropToolBO> dropToolList = new ArrayList<DropToolBO>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<DropToolBO> getDropToolList() {
		return dropToolList;
	}

	public void setDropToolList(List<DropToolBO> dropToolList) {
		this.dropToolList = dropToolList;
	}

}
