package com.lodogame.ldsg.constants;

/**
 * 跨服战状态
 * 
 * @author shixiangwen
 * 
 */

public enum ContestClientStatus {

	// 未开始
	NOT_START(1),

	// 布阵中
	ARRAY(2),

	// 比赛状态中
	MATCH_READY(3),

	// 比赛中
	MATCH(4),

	// 已经打完
	END(5);

	private int status;

	private ContestClientStatus(int status) {
		this.status = status;
	}

	public int getValue() {
		return this.status;
	}

}
