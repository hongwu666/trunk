package com.lodogame.ldsg.helper;

import java.util.Calendar;
import java.util.Date;

import com.lodogame.game.utils.DateUtils;
import com.lodogame.ldsg.constants.ActivityId;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.model.SystemRecivePower;

public class ActivityHelper {

	/**
	 * 活动是否开放
	 * 
	 * @param openWeeks
	 * @return
	 */
	public static boolean isActivityOpen(String openWeeks) {

		int dayOfWeek = DateUtils.getDayOfWeek();
		if (dayOfWeek == 1) {
			dayOfWeek = 7;
		} else {
			dayOfWeek = dayOfWeek - 1;
		}

		return openWeeks != null && openWeeks.indexOf(String.valueOf(dayOfWeek)) >= 0;

	}

	/**
	 * 当前是否可以领体力
	 * 
	 * @param systemRecivePower
	 * @return
	 */
	public static boolean isNowCanRecive(SystemRecivePower systemRecivePower) {

		if (systemRecivePower == null) {
			return false;
		}

		Date now = new Date();

		Date start = DateUtils.str2Date(DateUtils.getDate() + " " + systemRecivePower.getStartTime());
		Date end = DateUtils.str2Date(DateUtils.getDate() + " " + systemRecivePower.getEndTime());

		if (now.before(start) || now.after(end)) {
			return false;
		}

		return true;
	}

	public static String getActivityTimeDesc(Date startTime, Date endTime) {
		if (endTime.after(DateUtils.add(new Date(), Calendar.MONTH, 6))) {
			return "永久开放";
		}

		return DateUtils.getDateStr(startTime.getTime(), "yyyy-MM-dd HH:mm") + " 至 " + DateUtils.getDateStr(endTime.getTime(), "yyyy-MM-dd HH:mm");
	}

	public static int getUseTypeByActivityId(int activityId) {

		int useType = 0;

		switch (activityId) {
		case ActivityId.TAVERN_REDUCE_GOLD:
			useType = ToolUseType.REDUCE_TAVERN_DRAW;
			break;
		case ActivityId.GEM_REDUCE_GOLD:
			useType = ToolUseType.REDUCE_GEM;
			break;
		case ActivityId.GOLD_LIGHT_NODE_REDUCE_GOLD:
			useType = ToolUseType.REDUCE_HERO_UPGRADE;
			break;
		case ActivityId.GOLD_UPGRADE_EQUIP:
			useType = ToolUseType.REDUCE_GOLD_MERGE_EQUIP;
			break;
		case ActivityId.MYSTERY_MALL_REDUCE_GOLD:
			useType = ToolUseType.STONE_MALL_EXCHANGE;
			break;
		case ActivityId.RESOURCE_REDUCE_GOLD:
			useType = ToolUseType.REDUCE_TREASURE;
			break;
		default:
			break;
		}

		return useType;

	}

	public static void main(String[] args) {
		System.out.println(isActivityOpen("1, 6, 7"));
		System.out.println(isActivityOpen("1,2,3,4,5,7"));
		System.out.println(isActivityOpen("6"));
	}

}
