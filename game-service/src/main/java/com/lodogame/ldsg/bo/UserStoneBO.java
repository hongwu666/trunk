package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserStoneBO {
	@Mapper(name = "sid")
	private int stoneId;
	
	@Mapper(name = "nm")
	private int stoneNum;

	public int getStoneId() {
		return stoneId;
	}

	public void setStoneId(int stoneId) {
		this.stoneId = stoneId;
	}

	public int getStoneNum() {
		return stoneNum;
	}

	public void setStoneNum(int stoneNum) {
		this.stoneNum = stoneNum;
	}
	
}
