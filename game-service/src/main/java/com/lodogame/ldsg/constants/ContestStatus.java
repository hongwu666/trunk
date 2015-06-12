package com.lodogame.ldsg.constants;

/**
 * 跨服战状态
 * 
 * @author shixiangwen
 * 
 */

public enum ContestStatus {

	// 未开始
	NOT_START(1),

	// 布阵
	ARRAY(2),

	// 32强
	MATCH_READY_32_1(3),

	MATCH_32_1(4),

	MATCH_READY_32_2(5),

	MATCH_32_2(6),

	MATCH_READY_32_3(7),

	MATCH_32_3(8),

	// 16强
	MATCH_READY_16_1(9),

	MATCH_16_1(10),

	MATCH_READY_16_2(11),

	MATCH_16_2(12),

	MATCH_READY_16_3(13),

	MATCH_16_3(14),

	// 8强
	MATCH_READY_8_1(15),

	MATCH_8_1(16),

	MATCH_READY_8_2(17),

	MATCH_8_2(18),

	MATCH_READY_8_3(19),

	MATCH_8_3(20),

	// 已经打完
	END(21);

	private int status;

	private ContestStatus(int status) {
		this.status = status;
	}

	public int getValue() {
		return this.status;
	}

	public String getDesc() {

		switch (this) {
		case NOT_START:
			return "跨服战未开始";
		case ARRAY:
			return "布阵阶段";
		case MATCH_READY_32_1:
			return "32强第一场准备阶段";
		case MATCH_32_1:
			return "32强第一场比赛中";
		case MATCH_READY_32_2:
			return "32强第二场准备阶段";
		case MATCH_32_2:
			return "32强第二场比赛中";
		case MATCH_READY_32_3:
			return "32强第三场准备阶段";
		case MATCH_32_3:
			return "16强第三场比赛中";
		case MATCH_READY_16_1:
			return "16强第一场准备阶段";
		case MATCH_16_1:
			return "16强第一场比赛中";
		case MATCH_READY_16_2:
			return "16强第二场准备阶段";
		case MATCH_16_2:
			return "16强第二场比赛中";
		case MATCH_READY_16_3:
			return "16强第三场准备阶段";
		case MATCH_16_3:
			return "16强第三场比赛中";
		case MATCH_READY_8_1:
			return "8强第一场准备阶段";
		case MATCH_8_1:
			return "8强第一场比赛中";
		case MATCH_READY_8_2:
			return "8强第二场准备阶段";
		case MATCH_8_2:
			return "8强第二场比赛中";
		case MATCH_READY_8_3:
			return "8强第三场准备阶段";
		case MATCH_8_3:
			return "8强第三场比赛中";
		case END:
			return "比赛已经完成";
		default:
			return "未知状态";
		}

	}

	/**
	 * 是不是比赛 状态
	 * 
	 * @return
	 */
	public boolean isMatchStatus() {

		if (status == MATCH_32_1.getValue() || status == MATCH_32_2.getValue() || status == MATCH_32_3.getValue()) {
			return true;
		}

		if (status == MATCH_16_1.getValue() || status == MATCH_16_2.getValue() || status == MATCH_16_3.getValue()) {
			return true;
		}

		if (status == MATCH_8_1.getValue() || status == MATCH_8_2.getValue() || status == MATCH_8_3.getValue()) {
			return true;
		}

		return false;
	}

	/**
	 * 是不是比赛准备 状态
	 * 
	 * @return
	 */
	public boolean isMatchReadyStatus() {

		if (status == MATCH_READY_32_1.getValue() || status == MATCH_READY_32_2.getValue() || status == MATCH_READY_32_3.getValue()) {
			return true;
		}

		if (status == MATCH_READY_16_1.getValue() || status == MATCH_READY_16_2.getValue() || status == MATCH_READY_16_3.getValue()) {
			return true;
		}

		if (status == MATCH_READY_8_1.getValue() || status == MATCH_READY_8_2.getValue() || status == MATCH_READY_8_3.getValue()) {
			return true;
		}

		return false;
	}

	public boolean isNeedPushStatus() {
		if (isMatchStatus()) {
			return false;
		}
		return true;
	}

	public static ContestStatus toStatus(int status) {
		for (ContestStatus st : ContestStatus.class.getEnumConstants()) {
			if (st.getValue() == status) {
				return st;
			}
		}
		return null;
	}
}
