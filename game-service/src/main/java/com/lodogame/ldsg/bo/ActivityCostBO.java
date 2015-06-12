package com.lodogame.ldsg.bo;

import java.util.ArrayList;
import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ActivityCostBO {
	/**
	 * 消耗貨幣的值
	 */
	@Mapper(name = "va")
	private int value;

	/**
	 * 奖励列表
	 */
	@Mapper(name = "list")
	private List<ActivityCostRewardBO> list = new ArrayList<ActivityCostRewardBO>();

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public List<ActivityCostRewardBO> getList() {
		return list;
	}

	public void setList(List<ActivityCostRewardBO> list) {
		this.list = list;
	}

}
