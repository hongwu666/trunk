package com.lodogame.ldsg.helper;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.lodogame.game.utils.DateUtils;
import com.lodogame.ldsg.constants.OnlyoneRewardType;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.model.OnlyoneHourRankReward;
import com.lodogame.model.OnlyoneJoinReward;
import com.lodogame.model.OnlyoneUserReg;
import com.lodogame.model.OnlyoneUserReward;

public class OnlyOneHelper {

	public static String getRewardTitle(OnlyoneUserReward reward) {

		if (reward.getType() == OnlyoneRewardType.HOUR_RANK) {
			return "第" + reward.getRound() + "轮排行第" + reward.getRank() + "名";
		} else if (reward.getType() == OnlyoneRewardType.WEEK_RANK) {
			return "周排行第" + reward.getRank() + "名";
		} else {
			return "参加" + reward.getTimes() + "次奖励";
		}
	}

	public static int getCopper(int copperRatio, int level) {
		return copperRatio;
	}

	public static String getText1(int point) {
		return "胜利+" + point + "分，失败+" + (point / 3) + "分";
	}

	public static String getText2(List<Date> list) {
		Date now = new Date();
		for (Date date : list) {
			if (date.after(now)) {
				return DateUtils.getDateStr(date.getTime(), "HH:mm");
			}
		}
		return "";
	}

	/**
	 * 获取倍率
	 * 
	 * @param times
	 * @return
	 */
	public static int getMutil(int times, int winTimes) {

		int multi = 1 + ((times - 1) / 5);

		if (multi > 4) {
			multi = 4;
		}

		if (isDouble()) {
			multi = multi * 2;
		}

		if (winTimes >= 10) {
			multi = multi * 2;
		}

		return multi;

	}

	/**
	 * 获取轮次
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int getRound(Date startTime, Date endTime) {

		Date now = new Date();
		long diff;
		if (now.after(endTime)) {
			diff = endTime.getTime() - startTime.getTime();
		} else {
			diff = now.getTime() - startTime.getTime();
		}
		int round = 1 + (int) (diff / 30 / 60 / 1000);
		return round;
	}

	/**
	 * 下一回合开始时间
	 * 
	 * @param list
	 * @return
	 */
	public static long getNextTime(Date startTime, Date endTime, List<Date> list) {

		Date now = new Date();

		if (now.before(startTime) || now.after(endTime)) {
			return 0;
		}

		for (Date date : list) {
			if (date.after(now)) {
				long diff = date.getTime() - now.getTime();
				return diff;
			}
		}
		return 0;
	}

	public static String getText3(int copper, String toolids) {
		return copper + "金币";
	}

	public static String getText6() {

		Date now = new Date();

		Date date2 = DateUtils.str2Date(DateUtils.getDate() + " 21:00:00");

		if (now.before(date2)) {
			return "21:00";
		}

		return "21:00";

	}

	/**
	 * 获取排行榜
	 * 
	 * @param list
	 * @param userId
	 * @param self
	 * @return
	 */
	public static List<OnlyoneUserReg> getRank(List<OnlyoneUserReg> list, String userId, boolean self) {

		if (!self) {
			if (list.size() > 8) {
				return list.subList(0, 8);
			}
			return list;
		} else {

			int rank = 1;
			for (OnlyoneUserReg reg : list) {
				if (StringUtils.equals(reg.getUserId(), userId)) {
					break;
				}
				rank += 1;
			}

			if (rank <= 8) {
				return getRank(list, userId, false);
			} else {

				if (rank + 4 > list.size()) {
					return list.subList(list.size() - 8, list.size());
				} else {
					return list.subList(rank - 4, rank + 4);
				}

			}

		}

	}

	/**
	 * 获取参与奖
	 * 
	 * @param onlyoneJoinReward
	 * @param level
	 * @return
	 */
	public static String getJoinReward(OnlyoneJoinReward onlyoneJoinReward, int level, boolean desc) {

		if (level > 40) {
			level = 40;
		}

		int honour = onlyoneJoinReward.getHonour();
		int diffTimes = onlyoneJoinReward.getDiffTimes();
		int copperA = onlyoneJoinReward.getCoppera();
		int copperB = onlyoneJoinReward.getCopperb();
		int totalCopper = 0;
		int param = 0;
		if (level < 30) {
			param = copperA;
		} else {
			param = copperB;
		}

		totalCopper = (int) (param * (1 + (level - 1) * 0.04) * diffTimes);

		if (desc) {
			return totalCopper + "金币 " + honour + "荣誉";
		} else {
			return ToolType.COPPER + ",0," + totalCopper + "|" + ToolType.HONOUR + ",0," + honour;
		}
	}

	/**
	 * 获取排名奖厉
	 * 
	 * @param onlyoneHourRankReward
	 * @param level
	 * @return
	 */
	public static String getHourRankReward(OnlyoneHourRankReward onlyoneHourRankReward, int level, boolean desc) {

		if (onlyoneHourRankReward == null) {
			return "";
		}

		if (level > 40) {
			level = 40;
		}

		int honour = onlyoneHourRankReward.getHonour();
		int copperA = onlyoneHourRankReward.getCoppera();
		int copperB = onlyoneHourRankReward.getCopperb();
		double copperC = onlyoneHourRankReward.getCopperc();
		int totalCopper = 0;
		int param = 0;
		if (level < 30) {
			param = copperA;
		} else {
			param = copperB;
		}

		totalCopper = (int) (param * (1 + (level - 1) * 0.04) * copperC / 100 / 4) * 130;

		if (desc) {
			return totalCopper + "金币 " + honour + "荣誉";
		} else {
			return ToolType.COPPER + ",0," + totalCopper + "|" + ToolType.HONOUR + ",0," + honour;
		}
	}

	/**
	 * 是不是结算到周排行榜中
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isSpCut(Date date) {

		String s = DateUtils.getTimeStr(date);

		String date2 = DateUtils.getDate() + " 21:00:00";

		if (StringUtils.equalsIgnoreCase(s, date2)) {
			return true;
		}

		return false;

	}

	/**
	 * 获取连胜奖励
	 * 
	 * @param times
	 * @return
	 */
	public static String getWinReward(int times) {
		if (times == 30 || times == 50) {
			return "5001,91005,2";
		} else if (times == 100) {
			return "5001,91005,4";
		}
		return "";
	}

	public static boolean isDouble() {

		Date time1 = DateUtils.str2Date(DateUtils.getDate() + " 13:00:00");
		Date time2 = DateUtils.str2Date(DateUtils.getDate() + " 13:30:00");
		Date time3 = DateUtils.str2Date(DateUtils.getDate() + " 20:30:00");
		Date time4 = DateUtils.str2Date(DateUtils.getDate() + " 21:00:00");

		Date now = new Date();
		if (now.after(time1) && now.before(time2)) {
			return true;
		}

		if (now.after(time3) && now.before(time4)) {
			return true;
		}

		return false;
	}
}
