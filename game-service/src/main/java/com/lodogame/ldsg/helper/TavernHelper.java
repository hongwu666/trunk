package com.lodogame.ldsg.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.math.RandomUtils;

import com.lodogame.ldsg.constants.InitDefine;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.TavernConstant;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.model.TavernAmendDropTool;
import com.lodogame.model.TavernDropTool;

/**
 * 酒馆帮助类
 * 
 * @author jacky
 * 
 */

public class TavernHelper {

	/**
	 * 抽奖
	 * 
	 * @param list
	 *            正常掉落列表
	 * @param amendList
	 *            修正掉落列表
	 * @param times
	 *            抽奖次数
	 * @param ind
	 *            当前游票
	 * @param amend
	 *            是否需要修正
	 * @param isFirstDraw
	 *            是否第一次大抽将
	 * @return
	 */
	public static List<TavernDropTool> draw(int type, List<TavernDropTool> tavernDropToolList, List<TavernAmendDropTool> amendList, int times, int drawTimes, boolean amend) {

		if (times == 10 || amend) {// 做修正
			TavernDropTool tavernDropTool = getAmendDrop(amendList);
			if (tavernDropTool != null) {
				tavernDropToolList.set(RandomUtils.nextInt(tavernDropToolList.size()), tavernDropTool);
			}
		} else {
			if (type == TavernConstant.DRAW_TYPE_1) {
				if (drawTimes == 0) {
					// 第一次免费抽将替换成固定的武将
					TavernDropTool tavernDropTool = getFirstDrop();
					tavernDropToolList.set(0, tavernDropTool);
				} else if (drawTimes == 1) {
					// 第二次免费抽将替换成固定的武将
					TavernDropTool tavernDropTool = getSecondDrop();
					tavernDropToolList.set(0, tavernDropTool);
				} else if (drawTimes == 2) {
					// 第三次免费抽将替换成固定的武将
					TavernDropTool tavernDropTool = getThirdDrop();
					tavernDropToolList.set(0, tavernDropTool);
				}
			}
		}

		int starCount = 0;
		int sixStar = 0;
		int fiveStar = 0;
		for (int i = 0; i < tavernDropToolList.size(); i++) {
			TavernDropTool tavernDropTool = tavernDropToolList.get(i);
			if (type == TavernConstant.DRAW_TYPE_3) {
				// 组ID即英雄星数只能有一个6星
				if (tavernDropTool.getGroupId() == 6) {
					if (sixStar >= 1) {
						tavernDropToolList.set(i, getFourDrop());
					} else {
						sixStar += 1;
					}
				}
				// 组ID即英雄星数只能有两个5星
				if (tavernDropTool.getGroupId() == 5) {
					if (fiveStar >= 2) {
						tavernDropToolList.set(i, getFourDrop());
					} else {
						fiveStar += 1;
					}
				}
			} else {
				// 组ID即英雄星数
				if (tavernDropTool.getGroupId() >= 5) {
					if (starCount >= 2) {
						tavernDropToolList.set(i, getThirdDrop());
					} else {
						starCount += 1;
					}
				}
			}

		}

		return tavernDropToolList;
	}
	
	private static TavernDropTool getFourDrop() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(331);
		list.add(411);
		list.add(441);
		list.add(491);
		TavernDropTool tavernDropTool = new TavernDropTool();
		tavernDropTool.setSystemHeroId(list.get(RandomUtils.nextInt(list.size())));
		return tavernDropTool;
	}

	/**
	 * 获取首次掉落
	 * 
	 * @return
	 */
	private static TavernDropTool getFirstDrop() {

		TavernDropTool tavernDropTool = new TavernDropTool();
		tavernDropTool.setSystemHeroId(InitDefine.FIRST_DRAW_GIVE_HERO);
		return tavernDropTool;
	}

	private static TavernDropTool getSecondDrop() {

		TavernDropTool tavernDropTool = new TavernDropTool();
		tavernDropTool.setSystemHeroId(InitDefine.SECOND_DRAW_GIVE_HERO);
		return tavernDropTool;
	}

	private static TavernDropTool getThirdDrop() {

		TavernDropTool tavernDropTool = new TavernDropTool();
		tavernDropTool.setSystemHeroId(InitDefine.THIRD_DRAW_GIVE_HERO);
		return tavernDropTool;
	}

	/**
	 * 获取修正掉落
	 * 
	 * @param amendList
	 * @return
	 */
	private static TavernDropTool getAmendDrop(List<TavernAmendDropTool> amendList) {
		TavernDropTool tavernDropTool = null;
		int randVal = 1 + new Random().nextInt(10000);
		for (TavernAmendDropTool t : amendList) {
			if (t.getLowerNum() <= randVal && randVal <= t.getUpperNum()) {
				tavernDropTool = new TavernDropTool();
				tavernDropTool.setSystemHeroId(t.getSystemHeroId());
				// 修正默认5星
				tavernDropTool.setGroupId(5);
				break;
			}
		}

		return tavernDropTool;
	}

	public static int getCostMoney(int type, int times) {

		if (type == TavernConstant.DRAW_TYPE_1) {
			return 50;
		} else if (type == TavernConstant.DRAW_TYPE_2) {
			if (times == 1) {
				return 300;
			} else {
				return 2700;
			}
		} else if (type == TavernConstant.DRAW_TYPE_3) {
			if (times == 1) {
				return 2700;
			} else {
				return 2700;
			}
		}

		throw new ServiceException(ServiceReturnCode.FAILD, "未知的抽奖类型[" + type + "]");
	}

	/**
	 * 获取冷却时间周期
	 * 
	 * @param type
	 * @return
	 */
	public static long getCoolTimeInterval(int type) {
		switch (type) {
		case TavernConstant.DRAW_TYPE_1:
			return TavernConstant.DRAW_TYPE_1_CD_TIME;
		case TavernConstant.DRAW_TYPE_2:
			return TavernConstant.DRAW_TYPE_2_CD_TIME;
		case TavernConstant.DRAW_TYPE_3:
			return TavernConstant.DRAW_TYPE_3_CD_TIME;
		}

		return 0;
	}

	/**
	 * 获取每天次数限制
	 * 
	 * @param type
	 * @return
	 */
	public static int getTimeLimit(int type) {

		switch (type) {
		case 0:
			return 10;
		case 1:
			return 1;
		case 2:
			return 1;
		}

		return 0;
	}
	/**
	 * 根据vip等级筛选列表
	 * 
	 * @param vip
	 * @param tavernDropTool
	 */
	public static List<TavernDropTool> upListByVip(int vip, List<TavernDropTool> tavernDropTool) {
		// 获得组的vip区域
		List<Integer> level = new ArrayList<Integer>();
		for (int j = 0; j < tavernDropTool.size(); j++) {
			int lv = tavernDropTool.get(j).getVipLevel();
			if (!level.contains(lv)) {
				level.add(lv);
			}
		}
		// 根据vip等级来筛选掉落对应vip
		int viplv = -1;
		Collections.sort(level);
		for (int j = 0; j < level.size(); j++) {
			if (vip >= level.get(j)) {
				viplv = level.get(j);
			}
		}
		// 筛选出符合vip等级的掉落列表
		List<TavernDropTool> list = new ArrayList<TavernDropTool>();
		for (int j = 0; j < tavernDropTool.size(); j++) {
			int lv = tavernDropTool.get(j).getVipLevel();
			if (lv == viplv) {
				list.add(tavernDropTool.get(j));
			}
		}
		return list;
	}
}
