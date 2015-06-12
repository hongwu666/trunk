package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class SystemActivityBO {

	/**
	 * 活动类型
	 */
	@Mapper(name = "tp")
	private int type;

	/**
	 * 活动名称
	 */
	@Mapper(name = "tt")
	private String title;

	/**
	 * 活动描述
	 */
	@Mapper(name = "desc")
	private String desc;

	/**
	 * 活动时间
	 */
	@Mapper(name = "date")
	private String date;

	/**
	 * 活动结束时间
	 */
	@Mapper(name = "et")
	private long endTime;

	/**
	 * 活动tips
	 */
	@Mapper(name = "tips")
	private String tips;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

}
