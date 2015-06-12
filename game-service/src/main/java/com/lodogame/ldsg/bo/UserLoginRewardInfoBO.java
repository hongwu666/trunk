package com.lodogame.ldsg.bo;

import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserLoginRewardInfoBO {


	@Mapper(name = "day")
	private int day;

	
	@Mapper(name = "rs")
	private int rewardStatus;
	
	@Mapper(name = "dr")
	private List<DropDescBO> dropToolBOList;

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getRewardStatus() {
		return rewardStatus;
	}

	public void setRewardStatus(int rewardStatus) {
		this.rewardStatus = rewardStatus;
	}

	public List<DropDescBO> getDropToolBOList() {
		return dropToolBOList;
	}

	public void setDropToolBOList(List<DropDescBO> dropToolBOList) {
		this.dropToolBOList = dropToolBOList;
	}
	
}
