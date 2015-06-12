package com.lodogame.ldsg.bo;

import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;
@Compress
public class EmpireOccupyBO {
	/**
	 * 用户ID
	 */
	@Mapper(name="id")
	private String userId;
	/**
	 * 用户名字
	 */
	@Mapper(name="name")
	private String name;
	/**
	 * 用户占领英雄战力
	 */
	@Mapper(name="pow")
	private int power;
	/**
	 * 已占领的时间
	 */
	@Mapper(name="time")
	private long time;
	/**
	 * 站的位置
	 */
	@Mapper(name="pos")
	private int pos;
	/**
	 * 占领英雄列表
	 */
	@Mapper(name="list")
	private List<UserHeroBO> list;
	/**
	 * 收益百分数
	 */
	@Mapper(name="per")
	private String percent;
	/**
	 * 关系：自己1，仇敌-1，其他0
	 */
	@Mapper(name="en")
	private int enemy;
	public String getUserId() {
		return userId;
	}
	public String getName() {
		return name;
	}
	public int getPower() {
		return power;
	}
	public long getTime() {
		return time;
	}
	public List<UserHeroBO> getList() {
		return list;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPower(int power) {
		this.power = power;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public void setList(List<UserHeroBO> list) {
		this.list = list;
	}
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public void setPercent(String percent) {
		this.percent = percent;
	}
	public String getPercent() {
		return percent;
	}
	
	public int getEnemy() {
		return enemy;
	}
	public void setEnemy(int enemy) {
		this.enemy = enemy;
	}
	
}
