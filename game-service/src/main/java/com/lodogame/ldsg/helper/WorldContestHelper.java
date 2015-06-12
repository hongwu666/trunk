package com.lodogame.ldsg.helper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lodogame.game.utils.DateUtils;
import com.lodogame.ldsg.bo.ContestPairBO;
import com.lodogame.ldsg.bo.ContestWorldPairBO;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.constants.ContestClientStatus;
import com.lodogame.ldsg.constants.ContestRound;
import com.lodogame.ldsg.constants.ContestStatus;
import com.lodogame.ldsg.constants.ContestWorldStatus;
import com.lodogame.model.ContestUser;
import com.lodogame.model.ContestWorldUser;

/**
 * 擂台赛帮助类
 * 
 * @author chengevo
 * 
 */
public class WorldContestHelper {

	/**
	 * 获取回合数
	 * 
	 * @param status
	 * @return
	 */
	public static int getRound(ContestWorldStatus statsu) {

		int round = 0;

		switch (statsu) {
		case MATCH_32_1:
		case MATCH_32_2:
		case MATCH_32_3:
			round = ContestRound.DEAD_ROUND_1;
			break;
		case MATCH_16_1:
		case MATCH_16_2:
		case MATCH_16_3:
			round = ContestRound.DEAD_ROUND_2;
			break;
		case MATCH_8_1:
		case MATCH_8_2:
		case MATCH_8_3:
			round = ContestRound.DEAD_ROUND_3;
			break;
		case MATCH_4_1:
		case MATCH_4_2:
		case MATCH_4_3:
			round = ContestRound.DEAD_ROUND_4;
			break;
		case MATCH_2_1:
		case MATCH_2_2:
		case MATCH_2_3:
			round = ContestRound.DEAD_ROUND_5;
			break;
		default:
			round = 0;
			break;
		}

		return round;
	}

	public static int getIndex(ContestWorldStatus statsu) {

		switch (statsu) {
		case MATCH_32_1:
		case MATCH_16_1:
		case MATCH_8_1:
		case MATCH_4_1:
		case MATCH_2_1:
			return 1;
		case MATCH_32_2:
		case MATCH_16_2:
		case MATCH_8_2:
		case MATCH_4_2:
		case MATCH_2_2:
			return 2;
		case MATCH_32_3:
		case MATCH_16_3:
		case MATCH_8_3:
		case MATCH_4_3:
		case MATCH_2_3:
			return 3;
		default:
			break;
		}

		return 0;
	}

	public static ContestClientStatus toClientStatus(ContestWorldStatus status) {

		if (status.isMatchReadyStatus()) {
			return ContestClientStatus.MATCH_READY;
		} else if (status.isMatchStatus()) {
			return ContestClientStatus.MATCH;
		} else if (status == ContestWorldStatus.ARRAY) {
			return ContestClientStatus.ARRAY;
		} else if (status == ContestWorldStatus.END) {
			return ContestClientStatus.END;
		}

		return ContestClientStatus.NOT_START;

	}

	public static int getClientIndex(ContestWorldStatus status) {

		switch (status) {
		case MATCH_READY_32_1:
		case MATCH_READY_16_1:
		case MATCH_READY_8_1:
		case MATCH_READY_4_1:
		case MATCH_READY_2_1:
		case MATCH_32_1:
		case MATCH_16_1:
		case MATCH_8_1:
		case MATCH_4_1:
		case MATCH_2_1:
			return 1;
		case MATCH_READY_32_2:
		case MATCH_READY_16_2:
		case MATCH_READY_8_2:
		case MATCH_READY_4_2:
		case MATCH_READY_2_2:
		case MATCH_32_2:
		case MATCH_16_2:
		case MATCH_8_2:
		case MATCH_4_2:
		case MATCH_2_2:
			return 2;
		case MATCH_READY_32_3:
		case MATCH_READY_16_3:
		case MATCH_READY_8_3:
		case MATCH_32_3:
		case MATCH_16_3:
		case MATCH_8_3:
			return 3;
		default:
			break;
		}

		return 0;
	}

	/**
	 * 获取比赛赛程
	 * 
	 * @param round
	 * @return
	 */
	public static Map<String, ContestWorldPairBO> getSchedule(List<ContestWorldUser> regList, int round) {

		Map<String, ContestWorldPairBO> matchSchedule = new HashMap<String, ContestWorldPairBO>();

		for (ContestWorldUser contestUser : regList) {
			int matchIndex = contestUser.getInd();
			int deadRound = contestUser.getDeadRound();

			if (deadRound != 100) {
				continue;
			}

			Map<String, String> m = getBaseCode(matchIndex, round);
			String tag = m.get("tag");
			String baseCode = m.get("code");

			ContestWorldPairBO contestPairBO;

			if (matchSchedule.containsKey(baseCode)) {
				contestPairBO = matchSchedule.get(baseCode);
				contestPairBO.put(tag, contestUser);
			} else {
				contestPairBO = new ContestWorldPairBO();
				contestPairBO.setBaseCode(baseCode);
				contestPairBO.put(tag, contestUser);
				matchSchedule.put(baseCode, contestPairBO);
			}

		}

		return matchSchedule;
	}

	public static int getNextStatusTime(ContestWorldStatus status) {

		int oneMin = 1000 * 60;
		int min = 0;

		switch (status) {
		case ARRAY:
			min = oneMin * 15;
			break;
		case MATCH_READY_32_1:
		case MATCH_READY_32_2:
		case MATCH_READY_32_3:
		case MATCH_READY_16_1:
		case MATCH_READY_16_2:
		case MATCH_READY_16_3:
		case MATCH_READY_8_1:
		case MATCH_READY_8_2:
		case MATCH_READY_8_3:
		case MATCH_READY_4_1:
		case MATCH_READY_4_2:
		case MATCH_READY_4_3:
		case MATCH_READY_2_1:
		case MATCH_READY_2_2:
		case MATCH_READY_2_3:
			min = (int) (oneMin * 1);
			break;
		case MATCH_32_1:
		case MATCH_32_2:
		case MATCH_32_3:
		case MATCH_16_1:
		case MATCH_16_2:
		case MATCH_16_3:
		case MATCH_8_1:
		case MATCH_8_2:
		case MATCH_8_3:
		case MATCH_4_1:
		case MATCH_4_2:
		case MATCH_4_3:
		case MATCH_2_1:
		case MATCH_2_2:
		case MATCH_2_3:

			min = 0;
			break;
		default:
			return 0;
		}

		if (Config.ins().isDebug()) {

			if (min > 0) {
				return (int) (oneMin / 10);
			}

		}

		return min;
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

	public static Date getNextDayStartTime() {
		return DateUtils.str2Date(DateUtils.getDate(DateUtils.addDays(new Date(), 7)) + " 20:00:00");
	}

	/**
	 * 获取回合数
	 * 
	 * @param status
	 * @return
	 */
	public static int getClientRound(ContestWorldStatus status) {

		int round = 0;

		switch (status) {
		case ARRAY:
		case MATCH_READY_32_1:
		case MATCH_READY_32_2:
		case MATCH_READY_32_3:
		case MATCH_32_1:
		case MATCH_32_2:
		case MATCH_32_3:
			round = ContestRound.DEAD_ROUND_1;
			break;
		case MATCH_READY_16_1:
		case MATCH_READY_16_2:
		case MATCH_READY_16_3:
		case MATCH_16_1:
		case MATCH_16_2:
		case MATCH_16_3:
			round = ContestRound.DEAD_ROUND_2;
			break;
		case MATCH_READY_8_1:
		case MATCH_READY_8_2:
		case MATCH_READY_8_3:
		case MATCH_8_1:
		case MATCH_8_2:
		case MATCH_8_3:
			round = ContestRound.DEAD_ROUND_3;
			break;
		case MATCH_READY_4_1:
		case MATCH_READY_4_2:
		case MATCH_READY_4_3:
		case MATCH_4_1:
		case MATCH_4_2:
		case MATCH_4_3:
			round = ContestRound.DEAD_ROUND_4;
			break;
		case MATCH_READY_2_1:
		case MATCH_READY_2_2:
		case MATCH_READY_2_3:
		case MATCH_2_1:
		case MATCH_2_2:
		case MATCH_2_3:
			round = ContestRound.DEAD_ROUND_5;
			break;
		case END:
			round = ContestRound.DEAD_ROUND_6;
			break;
		default:
			round = 0;
			break;
		}

		return round;
	}

	/**
	 * 按非赛程
	 * 
	 * @param regInfoList
	 * @param indList
	 */
	public static void scheduling(List<ContestWorldUser> regInfoList, List<Integer> indList) {

		if (regInfoList.size() == 0) {
			return;
		}

		int length = regInfoList.size();
		int length2 = indList.size();

		if (length == 1) {

			ContestWorldUser contestUser = regInfoList.get(0);
			contestUser.setInd(indList.get(0));

		} else {

			int mid = (length + 1) / 2;
			List<ContestWorldUser> firstRegInfoList = regInfoList.subList(0, mid);
			List<ContestWorldUser> secondRegInfoList = regInfoList.subList(mid, length);

			int mid2 = (length2 + 1) / 2;
			List<Integer> firstIndList = indList.subList(0, mid2);
			List<Integer> secondIndList = indList.subList(mid2, length2);

			scheduling(firstRegInfoList, firstIndList);
			scheduling(secondRegInfoList, secondIndList);
		}
	}

}
