package com.lodogame.ldsg.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lodogame.game.utils.DateUtils;
import com.lodogame.ldsg.bo.ContestPairBO;
import com.lodogame.ldsg.bo.ContestRegBO;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.constants.ContestConstant;

/**
 * 擂台赛帮助类
 * 
 * @author chengevo
 * 
 */
public class ContestHelper2 {

	/**
	 * 按非赛程
	 * 
	 * @param regInfoList
	 * @param indList
	 */
	public static void scheduling(List<ContestRegBO> regInfoList, List<Integer> indList) {

		if (regInfoList.size() == 0) {
			return;
		}

		int length = regInfoList.size();
		int length2 = indList.size();

		if (length == 1) {

			ContestRegBO regInfo = regInfoList.get(0);
			regInfo.setIndex(indList.get(0));

		} else {

			int mid = (length + 1) / 2;
			List<ContestRegBO> firstRegInfoList = regInfoList.subList(0, mid);
			List<ContestRegBO> secondRegInfoList = regInfoList.subList(mid, length);

			int mid2 = (length2 + 1) / 2;
			List<Integer> firstIndList = indList.subList(0, mid2);
			List<Integer> secondIndList = indList.subList(mid2, length2);

			scheduling(firstRegInfoList, firstIndList);
			scheduling(secondRegInfoList, secondIndList);
		}
	}

	/**
	 * 状态间隔时间
	 * 
	 * @param status
	 * @return
	 */
	public static int getNextStatusTime(int status) {

		int oneMin = 1000 * 60;
		int min = 0;

		switch (status) {
		case ContestConstant.STATUS_REG:
			min = oneMin * 10;
			break;
		case ContestConstant.STATUS_MATCH_32_READY:
		case ContestConstant.STATUS_MATCH_16_READY:
		case ContestConstant.STATUS_MATCH_8_READY:
			min = oneMin * 4;
			break;
		case ContestConstant.STATUS_MATCH_32:
		case ContestConstant.STATUS_MATCH_16:
		case ContestConstant.STATUS_MATCH_8:
		case ContestConstant.STATUS_MATCH_4:
		case ContestConstant.STATUS_MATCH_2:
			min = 0;
			break;
		case ContestConstant.STATUS_BET_READY:
			min = oneMin * 2;
			break;
		case ContestConstant.STATUS_BET:
			min = oneMin * 5;
			break;
		case ContestConstant.STATUS_MATCH_4_READY:
		case ContestConstant.STATUS_MATCH_2_READY:
			min = oneMin * 6;
			break;
		default:
			return 0;
		}

		if (Config.ins().isDebug()) {

			if (status == ContestConstant.STATUS_REG) {
				// return 10 * oneMin;
			}

			if (min > 0) {

				return (int) (oneMin);
			}

		}

		return min;
	}

	/**
	 * 获取下一天开始时装
	 * 
	 * @return
	 */
	public static Date getNextDayStartTime() {
		if (Config.ins().isDebug()) {
			return DateUtils.add(new Date(), Calendar.MINUTE, 1);
		}
		return DateUtils.str2Date(DateUtils.getDate(DateUtils.addDays(new Date(), 1)) + " 12:10:00");
	}

	/**
	 * 是否是比赛准备状态
	 * 
	 * @param status
	 * @return
	 */
	public static boolean isMatchReadyStatus(int status) {

		if (status == ContestConstant.STATUS_MATCH_32_READY || status == ContestConstant.STATUS_MATCH_16_READY || status == ContestConstant.STATUS_MATCH_8_READY
				|| status == ContestConstant.STATUS_MATCH_4_READY || status == ContestConstant.STATUS_MATCH_2_READY) {
			return true;
		}

		return false;
	}

	/**
	 * 是否是比赛状态
	 * 
	 * @param status
	 * @return
	 */
	public static boolean isMatchStatus(int status) {

		if (status == ContestConstant.STATUS_MATCH_32 || status == ContestConstant.STATUS_MATCH_16 || status == ContestConstant.STATUS_MATCH_8 || status == ContestConstant.STATUS_MATCH_4
				|| status == ContestConstant.STATUS_MATCH_2) {
			return true;
		}

		return false;
	}

	public static String getNextStatusName(int status) {

		// 小组4强","小组半决赛","小组总决赛","下注阶段","半决赛","总决赛","下一届报名"}

		switch (status) {
		case ContestConstant.STATUS_NOT_STAET:
			return "下一轮报名";
		case ContestConstant.STATUS_REG:
			return "小组4强";
		case ContestConstant.STATUS_MATCH_32_READY:
			return "小组4强";
		case ContestConstant.STATUS_MATCH_32:
			return "小组4强";
		case ContestConstant.STATUS_MATCH_16_READY:
			return "小组半决赛";
		case ContestConstant.STATUS_MATCH_16:
			return "小组半决赛";
		case ContestConstant.STATUS_MATCH_8_READY:
			return "小组总决赛";
		case ContestConstant.STATUS_MATCH_8:
			return "下注阶段";
		case ContestConstant.STATUS_BET_READY:
			return "下注阶段";
		case ContestConstant.STATUS_BET:
			return "半决赛";
		case ContestConstant.STATUS_MATCH_4_READY:
			return "半决赛";
		case ContestConstant.STATUS_MATCH_4:
			return "总决赛";
		case ContestConstant.STATUS_MATCH_2_READY:
			return "总决赛";
		case ContestConstant.STATUS_MATCH_2:
			return "下一轮报名";
		case ContestConstant.STATUS_END:
			return "下一轮报名";

		}

		return "";

	}

	public static String getStatusDesc(int status) {

		switch (status) {
		case ContestConstant.STATUS_NOT_STAET:
			return "比赛未开始";
		case ContestConstant.STATUS_REG:
			return "比赛报名中";
		case ContestConstant.STATUS_MATCH_32_READY:
			return "比赛32强准备中";
		case ContestConstant.STATUS_MATCH_32:
			return "比赛32强比赛中";
		case ContestConstant.STATUS_MATCH_16_READY:
			return "比赛16强准备中";
		case ContestConstant.STATUS_MATCH_16:
			return "比赛16强比赛中";
		case ContestConstant.STATUS_MATCH_8_READY:
			return "比赛8强准备中";
		case ContestConstant.STATUS_MATCH_8:
			return "比赛8强比赛中";
		case ContestConstant.STATUS_BET_READY:
			return "下注准备中";
		case ContestConstant.STATUS_BET:
			return "比赛下注中";
		case ContestConstant.STATUS_MATCH_4_READY:
			return "比赛4强准备中";
		case ContestConstant.STATUS_MATCH_4:
			return "比赛4强比赛中";
		case ContestConstant.STATUS_MATCH_2_READY:
			return "比赛决赛准备中";
		case ContestConstant.STATUS_MATCH_2:
			return "比赛决赛比赛中";
		case ContestConstant.STATUS_END:
			return "比赛已经完成";

		}

		return "";
	}

	/**
	 * 是否需要推送状态
	 * 
	 * @param status
	 * @return
	 */
	public static boolean isNeedPushStatus(int status) {

		if (status == ContestConstant.STATUS_MATCH_32 || status == ContestConstant.STATUS_MATCH_16 || status == ContestConstant.STATUS_MATCH_8 || status == ContestConstant.STATUS_MATCH_4) {
			return false;
		}

		return true;
	}

	public static Map<String, String> getBaseCode(int index, int round) {

		String code = Integer.toBinaryString(index);
		int length = code.length();
		for (int i = 0; i < 6 - length; i++) {
			code = "0" + code;
		}

		String baseCode = code.substring(0, 6 - round);
		String tag = code.substring(6 - round, 6 - round + 1);

		Map<String, String> m = new HashMap<String, String>();
		m.put("code", baseCode);
		m.put("tag", tag);

		// 000001
		return m;
	}

	/**
	 * 获取比赛赛程
	 * 
	 * @param round
	 * @return
	 */
	public static Map<String, ContestPairBO> getSchedule(Collection<ContestRegBO> regList, int round) {

		Map<String, ContestPairBO> matchSchedule = new HashMap<String, ContestPairBO>();

//		for (ContestRegBO contestRegBO : regList) {
//			int matchIndex = contestRegBO.getIndex();
//			int deadRound = contestRegBO.getDeadRound();
//
//			if (deadRound != 100) {
//				continue;
//			}
//
//			Map<String, String> m = getBaseCode(matchIndex, round);
//			String tag = m.get("tag");
//			String baseCode = m.get("code");
//
//			ContestPairBO contestPairBO;
//
//			if (matchSchedule.containsKey(baseCode)) {
//				contestPairBO = matchSchedule.get(baseCode);
//				contestPairBO.put(tag, contestRegBO.getUserId());
//			} else {
//				contestPairBO = new ContestPairBO();
//				contestPairBO.setBaseCode(baseCode);
//				contestPairBO.put(tag, contestRegBO.getUserId());
//				matchSchedule.put(baseCode, contestPairBO);
//
//			}
//
//		}

		return matchSchedule;
	}

	public static int getClientStatus(int status) {

		if (status == ContestConstant.STATUS_NOT_STAET) {
			return 0;
		} else if (status == ContestConstant.STATUS_REG) {
			return 1;
		} else if (status == ContestConstant.STATUS_MATCH_32_READY || status == ContestConstant.STATUS_MATCH_32 || status == ContestConstant.STATUS_MATCH_16_READY
				|| status == ContestConstant.STATUS_MATCH_16 || status == ContestConstant.STATUS_MATCH_8_READY || status == ContestConstant.STATUS_MATCH_8
				|| status == ContestConstant.STATUS_BET_READY) {
			return 2;
		} else {
			return 3;
		}
	}

	public static int getClientRound(int status) {

		int round = 0;

		switch (status) {
		case ContestConstant.STATUS_REG:
			round = 0;
			break;
		case ContestConstant.STATUS_MATCH_32_READY:
		case ContestConstant.STATUS_MATCH_32:
			round = 0;
			break;
		case ContestConstant.STATUS_MATCH_16_READY:
		case ContestConstant.STATUS_MATCH_16:
			round = 1;
			break;
		case ContestConstant.STATUS_MATCH_8_READY:
		case ContestConstant.STATUS_MATCH_8:
			round = 2;
			break;
		case ContestConstant.STATUS_BET_READY:
		case ContestConstant.STATUS_BET:
		case ContestConstant.STATUS_MATCH_4_READY:
		case ContestConstant.STATUS_MATCH_4:
			round = 3;
			break;
		case ContestConstant.STATUS_MATCH_2_READY:
		case ContestConstant.STATUS_MATCH_2:
			round = 4;
			break;
		case ContestConstant.STATUS_END:
			round = 5;
			break;
		default:
			round = 0;
			break;
		}

		return round;
	}

	/**
	 * 获取回合数
	 * 
	 * @param status
	 * @return
	 */
	public static int getRound(int status) {

		int round = 0;

		switch (status) {
		case ContestConstant.STATUS_MATCH_32:
			round = ContestConstant.DEAD_ROUND_1;
			break;
		case ContestConstant.STATUS_MATCH_16:
			round = ContestConstant.DEAD_ROUND_2;
			break;
		case ContestConstant.STATUS_MATCH_8:
			round = ContestConstant.DEAD_ROUND_3;
			break;
		case ContestConstant.STATUS_MATCH_4:
			round = ContestConstant.DEAD_ROUND_4;
			break;
		case ContestConstant.STATUS_MATCH_2:
			round = ContestConstant.DEAD_ROUND_5;
			break;
		default:
			round = 0;
			break;
		}

		return round;
	}

	/**
	 * 获取最后一场比赛的tips
	 * 
	 * @param lastBattleType
	 * @param lastBattleStatus
	 * @param targetUsername
	 * @return
	 */
	public static String getLastTips(int lastBattleType, int lastBattleStatus, String targetUsername) {

		if (lastBattleStatus == 0) {
			return "获得决赛阶段资格";
		} else if (lastBattleStatus == 1) {
			if (lastBattleType == 1) {
				return "你刚刚打败了" + targetUsername + ",获得决赛阶段资格";
			} else {
				return "你刚刚打败了" + targetUsername + ",守住了决赛阶段资格";
			}
		} else if (lastBattleStatus == 2) {
			if (lastBattleType == 1) {
				return "你刚刚被" + targetUsername + "虐了,无法获得决赛阶段资格";
			} else {
				return "你刚刚被" + targetUsername + "虐了,失去了决赛阶段资格";
			}
		}

		return "";
	}

	/**
	 * 获取最高名次tips
	 * 
	 * @param deadRound
	 * @return
	 */
	public static String getMaxRankTips(int deadRound) {
		switch (deadRound) {
		case 1:
			return "32强";
		case 2:
			return "16强";
		case 3:
			return "8强";
		case 4:
			return "4强";
		case 5:
			return "亚军";
		case 6:
			return "冠军";
		default:
			return "暂无名次";
		}
	}

	/**
	 * 获取分组ID
	 * 
	 * @param ind
	 * @return
	 */
	public static int getGroupId(int ind) {
		if (ind <= 7) {
			return 1;
		} else if (ind <= 15) {
			return 2;
		} else if (ind <= 23) {
			return 3;
		} else {
			return 4;
		}
	}

	/**
	 * 获取回合数对应的奖励
	 * 
	 * @param status
	 * @return
	 */
	public static int getRoundReward(int round) {

		int rewardId = 0;

		switch (round) {
		case ContestConstant.DEAD_ROUND_1:
			rewardId = 6;
			break;
		case ContestConstant.DEAD_ROUND_2:
			rewardId = 5;
			break;
		case ContestConstant.DEAD_ROUND_3:
			rewardId = 4;
			break;
		case ContestConstant.DEAD_ROUND_4:
			rewardId = 3;
			break;
		case ContestConstant.DEAD_ROUND_5:
			rewardId = 2;
			break;
		case ContestConstant.DEAD_ROUND_0:
			rewardId = 1;
			break;
		default:
			rewardId = 0;
			break;
		}

		return rewardId;
	}

	/**
	 * 根据挂的回合数，得到排名
	 * 
	 * @param status
	 * @return
	 */
	public static String getRank(int round) {

		String rank = "";

		switch (round) {
		case ContestConstant.DEAD_ROUND_1:
			rank = "三十二强";
			break;
		case ContestConstant.DEAD_ROUND_2:
			rank = "十六强";
			break;
		case ContestConstant.DEAD_ROUND_3:
			rank = "八强";
			break;
		case ContestConstant.DEAD_ROUND_4:
			rank = "四强";
			break;
		case ContestConstant.DEAD_ROUND_5:
			rank = "亚军";
			break;
		case ContestConstant.DEAD_ROUND_0:
			rank = "冠军";
			break;
		default:
			rank = "";
			break;
		}

		return rank;
	}

	public static int getContestRank(int round) {

		int rank = 100;
		switch (round) {
		case ContestConstant.DEAD_ROUND_1:
			rank = 32;
			break;
		case ContestConstant.DEAD_ROUND_2:
			rank = 16;
			break;
		case ContestConstant.DEAD_ROUND_3:
			rank = 8;
			break;
		case ContestConstant.DEAD_ROUND_4:
			rank = 4;
			break;
		case ContestConstant.DEAD_ROUND_5:
			rank = 2;
			break;
		case ContestConstant.DEAD_ROUND_0:
			rank = 2;
			break;
		default:
			rank = 100;
			break;
		}

		return rank;
	}

	public static String getStep(int type, String code) {

		if (type == 1) {
			return "报名赛";
		}

		int size = code.length();

		switch (size) {
		case 1:
			return "决赛";
		case 2:
			return "4强比赛";
		case 3:
			return "8强比赛";
		case 4:
			return "16强比赛";
		case 5:
			return "32赛比赛";
		default:
			return "";

		}

	}

	public static void main(String[] args) {

		//
		List<ContestRegBO> list = new ArrayList<ContestRegBO>();
		List<Integer> indList = new ArrayList<Integer>();
		for (int i = 0; i < 23; i++) {
			ContestRegBO bo = new ContestRegBO();
			bo.setIndex(i);
			list.add(bo);
		}

		for (int i = 0; i < 32; i++) {
			indList.add(i);
		}

		scheduling(list, indList);

		for (ContestRegBO bo : list) {
			System.out.println(bo.getIndex());
		}

	}

}
