package com.lodogame.ldsg.helper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lodogame.game.utils.DateUtils;
import com.lodogame.ldsg.bo.ContestPairBO;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.constants.ContestClientStatus;
import com.lodogame.ldsg.constants.ContestRound;
import com.lodogame.ldsg.constants.ContestStatus;
import com.lodogame.model.ContestUser;

/**
 * 擂台赛帮助类
 * 
 * @author chengevo
 * 
 */
public class ContestHelper {

	public static Map<String, String> SERVER_MAP = new HashMap<String, String>();

	static {

		SERVER_MAP.put("g1", "夏亚雷鸣");
		SERVER_MAP.put("g2", "汉尼根");
		SERVER_MAP.put("g3", "魔神皇");
		SERVER_MAP.put("g4", "艾德琳");
		SERVER_MAP.put("g5", "梅林");
		SERVER_MAP.put("g6", "黑斯廷");
		SERVER_MAP.put("g7", "黛芬妮");
	}

	public static String getTitle1(int round, boolean win) {

		if (win) {

			if (round == 1) {
				return "进入16强";
			} else if (round == 2) {
				return "进入8强";
			} else if (round == 3) {
				return "进入4强";
			} else if (round == 4) {
				return "进入决赛";
			} else if (round == 5) {
				return "夺得冠军";
			}

		} else {

			if (round == 1) {
				return "止步32强";
			} else if (round == 2) {
				return "止步16强";
			} else if (round == 3) {
				return "止步8强";
			} else if (round == 4) {
				return "止步4强";
			} else if (round == 5) {
				return "痛失冠军";
			}
		}

		return "";

	}

	public static String getTitle2(int winCount) {

		if (winCount == 0) {
			return "三战全败";
		} else if (winCount == 1) {
			return "一胜二负";
		} else if (winCount == 2) {
			return "二胜一负";
		} else if (winCount == 3) {
			return "三战全胜";
		}
		return "";

	}

	/**
	 * 获取回合数
	 * 
	 * @param status
	 * @return
	 */
	public static int getClientRound(ContestStatus status) {

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
		case END:
			round = ContestRound.DEAD_ROUND_4;
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
	public static int getRound(ContestStatus status) {

		int round = 0;

		switch (status) {
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
		default:
			round = 0;
			break;
		}

		return round;
	}

	public static int getIndex(ContestStatus status) {

		switch (status) {
		case MATCH_32_1:
		case MATCH_16_1:
		case MATCH_8_1:
			return 1;
		case MATCH_32_2:
		case MATCH_16_2:
		case MATCH_8_2:
			return 2;
		case MATCH_32_3:
		case MATCH_16_3:
		case MATCH_8_3:
			return 3;
		default:
			break;
		}

		return 0;
	}

	public static int getClientIndex(ContestStatus status) {

		switch (status) {
		case MATCH_READY_32_1:
		case MATCH_READY_16_1:
		case MATCH_READY_8_1:
		case MATCH_32_1:
		case MATCH_16_1:
		case MATCH_8_1:
			return 1;
		case MATCH_READY_32_2:
		case MATCH_READY_16_2:
		case MATCH_READY_8_2:
		case MATCH_32_2:
		case MATCH_16_2:
		case MATCH_8_2:
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
	public static Map<String, ContestPairBO> getSchedule(List<ContestUser> regList, int round) {

		Map<String, ContestPairBO> matchSchedule = new HashMap<String, ContestPairBO>();

		for (ContestUser contestUser : regList) {
			int matchIndex = contestUser.getInd();
			int deadRound = contestUser.getDeadRound();

			if (deadRound != 100) {
				continue;
			}

			Map<String, String> m = getBaseCode(matchIndex, round);
			String tag = m.get("tag");
			String baseCode = m.get("code");

			ContestPairBO contestPairBO;

			if (matchSchedule.containsKey(baseCode)) {
				contestPairBO = matchSchedule.get(baseCode);
				contestPairBO.put(tag, contestUser);
			} else {
				contestPairBO = new ContestPairBO();
				contestPairBO.setBaseCode(baseCode);
				contestPairBO.put(tag, contestUser);
				matchSchedule.put(baseCode, contestPairBO);
			}

		}

		return matchSchedule;
	}

	public static int getNextStatusTime(ContestStatus status) {

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
			min = oneMin * 1;
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
	 * 转换成客户端 状态
	 * 
	 * @param status
	 * @return
	 */
	public static ContestClientStatus toClientStatus(ContestStatus status) {

		if (status.isMatchReadyStatus()) {
			return ContestClientStatus.MATCH_READY;
		} else if (status.isMatchStatus()) {
			return ContestClientStatus.MATCH;
		} else if (status == ContestStatus.ARRAY) {
			return ContestClientStatus.ARRAY;
		} else if (status == ContestStatus.END) {
			return ContestClientStatus.END;
		}

		return ContestClientStatus.NOT_START;

	}

	/**
	 * 淘汰回合数转成排名
	 * 
	 * @param deadRound
	 * @return
	 */
	public static int deadRound2Rank(int deadRound) {

		switch (deadRound) {
		case 100:
			return 1;
		case 1:
			return 6;
		case 2:
			return 5;
		case 3:
			return 4;
		case 4:
			return 3;
		case 5:
			return 2;
		default:
			return 0;
		}

	}

	/**
	 * 按非赛程
	 * 
	 * @param regInfoList
	 * @param indList
	 */
	public static void scheduling(List<ContestUser> regInfoList, List<Integer> indList) {

		if (regInfoList.size() == 0) {
			return;
		}

		int length = regInfoList.size();
		int length2 = indList.size();

		if (length == 1) {

			ContestUser contestUser = regInfoList.get(0);
			contestUser.setInd(indList.get(0));

		} else {

			int mid = (length + 1) / 2;
			List<ContestUser> firstRegInfoList = regInfoList.subList(0, mid);
			List<ContestUser> secondRegInfoList = regInfoList.subList(mid, length);

			int mid2 = (length2 + 1) / 2;
			List<Integer> firstIndList = indList.subList(0, mid2);
			List<Integer> secondIndList = indList.subList(mid2, length2);

			scheduling(firstRegInfoList, firstIndList);
			scheduling(secondRegInfoList, secondIndList);
		}
	}

	/**
	 * 获取消息
	 * 
	 * @param runningType
	 * @param round
	 * @return
	 */
	public static Map<String, String> getMsg(int runningType, int round) {

		Map<String, String> m = new HashMap<String, String>();
		String title1 = "";
		String title2 = "";

		if (runningType == 1) {
			if (round == 1) {
				title1 = "恭喜";
				title2 = "成功晋级16强，巅峰之战火即将燎原！";
			} else if (round == 2) {
				title1 = "恭喜";
				title2 = "成功晋级8强，巅峰战场愈加白热化，为他们欢呼吧！";
			} else if (round == 3) {
				title1 = "恭喜";
				title2 = "成功晋级最强四人阵容，并获得参加全服巅峰战的资格，为我们服的荣誉而战！";
			}
		} else {
			if (round == 1) {
				title1 = "恭喜";
				title2 = "成功晋级16强，巅峰之战火即将燎原！";
			} else if (round == 2) {
				title1 = "恭喜";
				title2 = "成功晋级8强，巅峰战场愈加白热化，为他们欢呼吧！";
			} else if (round == 3) {
				title1 = "恭喜";
				title2 = "成功晋级半决赛，巅峰王者的宝座已经在近在眼前！";
			} else if (round == 4) {
				title1 = "巅峰双雄已经出现！恭喜";
				title2 = "成功晋级决赛！一场绝世大战即将展开，谁是全服之王，拭目以待吧！";
			} else if (round == 5) {
				title1 = "恭喜";
				title2 = "获得全服巅峰战决赛胜利，他是真正的全服之王，颤抖吧！";
			}
		}

		m.put("title1", title1);
		m.put("title2", title2);

		return m;
	}

	/**
	 * 获取服务器名称
	 * 
	 * @param sid
	 * @return
	 */
	public static String getServerName(String sid) {

		if (SERVER_MAP.containsKey(sid)) {
			return SERVER_MAP.get(sid);
		}

		return sid;
	}

	public static String getMailContent(int type, int rank) {

		String local = "本服";
		if (type == 2) {
			local = "跨服";
		}

		if (rank == 1) {
			return "恭喜您在" + local + "巅峰战场获得冠军！";
		} else if (rank == 2) {
			return "恭喜您在" + local + "巅峰战场获得亚军！";
		} else if (rank == 3) {
			return "恭喜您在" + local + "巅峰战场获得4强的名次！";
		} else if (rank == 4) {
			String s = "恭喜您在" + local + "巅峰战场获得8强的名次！";
			if (type == 1) {
				s = s + "，同时获得代表本服参与明晚8:00开启的全服巅峰战场参赛名额，请准时参加哦！";
			}
			return s;
		} else if (rank == 5) {
			return "恭喜您在" + local + "巅峰战场获得16强的名次！";
		} else if (rank == 6) {
			return "恭喜您在" + local + "巅峰战场获得32强的名次！";
		}

		return "";

	}

	public static void main(String[] args) {
		System.out.println(ContestHelper.getBaseCode(1, 2));
		System.out.println(ContestHelper.getBaseCode(3, 2));
	}
}
