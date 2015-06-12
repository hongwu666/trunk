package com.lodogame.ldsg.bo;

import java.util.ArrayList;
import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ActivityCostRewardBO {
	/**
	 * 奖励ID
	 */
	@Mapper(name = "rewardId")
	private int rewardId;
	
	/**
	 * 描述
	 */
	@Mapper(name = "desc")
	private String desc;
	
	/**
	 * 需要消耗
	 */
	@Mapper(name = "va")
	private int value;
	
	/**
	 * 掉落道具列表
	 */
	@Mapper(name = "rl")
	private List<DropDescBO> rl = new ArrayList<DropDescBO>();

	/**
	 * 标记-1未达到，0可以领取，1已领取
	 */
	@Mapper(name = "flag")
	private int flag;

	public List<DropDescBO> getRl() {
		return rl;
	}

	public void setRl(List<DropDescBO> rl) {
		this.rl = rl;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getRewardId() {
		return rewardId;
	}

	public void setRewardId(int rewardId) {
		this.rewardId = rewardId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
