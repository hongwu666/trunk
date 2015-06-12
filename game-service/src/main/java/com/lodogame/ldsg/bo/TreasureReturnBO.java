package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * 打开聚宝盆返回信息
 * 
 * @author
 *
 */
@Compress
public class TreasureReturnBO {

	@Mapper(name = "ts")
	private int times;// 倍数

	@Mapper(name = "gt")
	private int gift;// 奖励数量

	@Mapper(name = "ng")
	private int nextGift;
	
	@Mapper(name="p")
	private int price;

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getNextGift() {
		return nextGift;
	}

	public void setNextGift(int nextGift) {
		this.nextGift = nextGift;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getGift() {
		return gift;
	}

	public void setGift(int gift) {
		this.gift = gift;
	}

}
