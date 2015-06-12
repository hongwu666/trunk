package com.lodogame.ldsg.bo;

import java.util.Date;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class GemAltarUserLogBO {

	@Mapper(name="s")
	private int stoneId;
	@Mapper(name="g")
	private int gold;
	@Mapper(name="t")
	private Date createTime;
	public int getStoneId() {
		return stoneId;
	}
	public void setStoneId(int stoneId) {
		this.stoneId = stoneId;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
