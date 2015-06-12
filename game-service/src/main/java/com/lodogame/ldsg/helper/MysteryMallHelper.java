package com.lodogame.ldsg.helper;

import java.util.Date;

import com.lodogame.game.utils.DateUtils;
import com.lodogame.ldsg.constants.MysteryMallType;
import com.lodogame.ldsg.constants.PriceType;

public class MysteryMallHelper {

	public static boolean isNeedRefresh(int type, Date lastRefreshTime) {

		Date now = new Date();
		Date date1 = DateUtils.str2Date(DateUtils.getDate() + " 07:00:00");
		Date date2 = DateUtils.str2Date(DateUtils.getDate() + " 21:00:00");

		if (now.after(date1) && lastRefreshTime.before(date1)) {
			return true;
		}

		if (now.after(date2) && lastRefreshTime.before(date2)) {
			return true;
		}

		return false;
	}

	public static int getPriceType(int mallType) {

		if (mallType <= 3) {
			return mallType;
		} else if (mallType == MysteryMallType.ONLY_ONY) {
			return PriceType.ONLY_ONE_MALL;
		}
		return 0;
	}

	public static long getNextRefreshTime(Date lastRefreshTime) {

		Date date1 = DateUtils.str2Date(DateUtils.getDate() + " 07:00:00");
		Date date2 = DateUtils.str2Date(DateUtils.getDate() + " 21:00:00");
		if (lastRefreshTime.before(date1)) {
			return date1.getTime();
		}

		if (lastRefreshTime.before(date2)) {
			return date2.getTime();
		}

		return DateUtils.addDays(date1, 1).getTime();

	}

	public static String getNextRefreshTimeDesc(Date lastRefreshTime) {

		Date date1 = DateUtils.str2Date(DateUtils.getDate() + " 07:00:00");
		Date date2 = DateUtils.str2Date(DateUtils.getDate() + " 21:00:00");
		if (lastRefreshTime.before(date1)) {
			return "7点";
		}

		if (lastRefreshTime.before(date2)) {
			return "21点";
		}

		return "7点";

	}

	/**
	 * 获取刷新需要的银币
	 * 
	 * @param mallType
	 * @param times
	 * @return
	 */
	public static int getNeedCopper(int mallType, int times) {
		return 20;
	}

	/**
	 * 获取刷新需要的金币
	 * 
	 * @param mallType
	 * @param times
	 * @return
	 */
	public static int getNeedGold(int mallType, int times) {
		return 20;
	}

}
