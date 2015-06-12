package com.lodogame.ldsg.bo;

import java.util.Date;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class GemPackBO {
	@Mapper(name = "si")
	private int stoneId;
	@Mapper(name = "d")
	private Date date;

	public int getStoneId() {
		return stoneId;
	}

	public void setStoneId(int stoneId) {
		this.stoneId = stoneId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
