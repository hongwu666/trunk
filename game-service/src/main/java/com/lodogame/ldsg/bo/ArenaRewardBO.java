package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ArenaRewardBO {

	/**
	 * 最高连胜
	 */
	@Mapper(name = "mwc")
	private int maxWinCount;

	/**
	 * 胜利场数
	 */
	@Mapper(name = "wt")
	private int winTimes;

	/**
	 * 失败场数
	 */
	@Mapper(name = "ft")
	private int loseTiems;

	/**
	 * 总的银币奖励
	 */
	@Mapper(name = "co")
	private int copperNum;

	/**
	 * 排箱奖励箱子ID
	 */
	@Mapper(name = "tid")
	private int toolId;

	public int getMaxWinCount() {
		return maxWinCount;
	}

	public void setMaxWinCount(int maxWinCount) {
		this.maxWinCount = maxWinCount;
	}

	public int getWinTimes() {
		return winTimes;
	}

	public void setWinTimes(int winTimes) {
		this.winTimes = winTimes;
	}

	public int getLoseTiems() {
		return loseTiems;
	}

	public void setLoseTiems(int loseTiems) {
		this.loseTiems = loseTiems;
	}

	public int getCopperNum() {
		return copperNum;
	}

	public void setCopperNum(int copperNum) {
		this.copperNum = copperNum;
	}

	public int getToolId() {
		return toolId;
	}

	public void setToolId(int toolId) {
		this.toolId = toolId;
	}

}
