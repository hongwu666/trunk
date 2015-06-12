package com.lodogame.ldsg.helper;

import java.util.ArrayList;
import java.util.List;

import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.bo.ContestUserHeroBO;
import com.lodogame.ldsg.bo.EnchantProty;
import com.lodogame.ldsg.bo.Property;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.model.RobotUserHero;

/**
 * 武将帮助类
 * 
 * @author jacky
 * 
 */
public class HeroHelper {

	/**
	 * 获取属性值
	 * 
	 * @param initAttr
	 * @param attrGrowth
	 * @return
	 */
	public static int getAttribte(int initAttr, int attrGrowth, int level) {
		return initAttr + (attrGrowth * (level - 1));
	}

	/**
	 * 根据经验获取武将分解可以获得的武魂
	 * 
	 * @param exp
	 * @return
	 */
	public static int getSellMuhon(int exp, int star) {
		return (int) (exp * 0.9);
	}

	/**
	 * 获取用户可以学习的技能数量
	 * 
	 * @param heroLevel
	 * @return
	 */
	public static int getStudyHeroCount(int heroLevel) {
		if (heroLevel >= 30) {
			return 6;
		}
		return 0;
	}

	/**
	 * 武将传承 - 获取元宝传承时，要消耗的元宝数量
	 * 
	 * @param 传承武将的品质
	 * @return
	 */
	public static int getGoldNum(int gColor) {
		int goldenNum = 0;

		switch (gColor) {
		case 0:
			goldenNum = 10;
			break;
		case 1:
			goldenNum = 50;
			break;
		case 2:
			goldenNum = 100;
			break;
		case 3:
			goldenNum = 450;
			break;
		case 4:
			goldenNum = 1000;
			break;
		case 5:
			goldenNum = 2000;
			break;
		default:
			break;
		}

		return goldenNum;
	}

	public static List<String> getAllNodesByHeroColor(int heroColor, int carrer) {
		int index = ((heroColor + 1) * 100) + (carrer * 10);
		List<String> nodes = new ArrayList<String>();
		for (int i = 1; i <= 6; i++) {
			nodes.add(String.valueOf(index + i));
		}
		return nodes;
	}

	/**
	 * 获取用户战力
	 * 
	 * @param userHeroList
	 * @return
	 */
	public static int getCapability(List<UserHeroBO> userHeroList) {

		int capability = 0;
		for (UserHeroBO userHero : userHeroList) {
			capability += userHero.getLife() * 1.5;
			capability += userHero.getPhysicalAttack() * 2;
			capability += userHero.getPhysicalDefense() * 4;
		}

		return capability;

	}

	public static int getCapabilityByBattleHeroBO(List<BattleHeroBO> userHeroList) {

		int capability = 0;
		for (BattleHeroBO userHero : userHeroList) {
			capability += userHero.getLife() * 1.5;
			capability += userHero.getPhysicalAttack() * 2;
			capability += userHero.getPhysicalDefense() * 4;
		}

		return capability;

	}

	public static int getCapabilityByContestHeroBO(List<ContestUserHeroBO> userHeroList) {

		int capability = 0;
		for (ContestUserHeroBO userHero : userHeroList) {
			capability += userHero.getLife() * 1.5;
			capability += userHero.getAttack() * 2;
			capability += userHero.getDefense() * 4;
		}

		return capability;

	}

	/**
	 * 根据星阶获取星级
	 * 
	 * @param starLevel
	 * @return
	 */
	public static int getHeroStar(int starLevel) {
		if (starLevel <= 3) {
			return starLevel;
		} else if (starLevel < 10) {
			return 4;
		} else if (starLevel < 16) {
			return 5;
		} else if (starLevel < 22) {
			return 6;
		} else if (starLevel < 28) {
			return 7;
		} else if (starLevel < 34) {
			return 8;
		} else if (starLevel < 40) {
			return 9;
		} else {
			return 10;
		}

	}

	public static int getCapability(UserHeroBO userHeroBO) {

		int capability = 0;
		capability += userHeroBO.getLife() * 1.5;
		capability += userHeroBO.getPhysicalAttack() * 2;
		capability += userHeroBO.getPhysicalDefense() * 4;

		return capability;

	}

	/**
	 * 加成累加
	 * 
	 * @param values1
	 * @param values2
	 */
	public static void addRatio(float[] values1, float[] values2) {
		values1[0] += values2[0];
		values1[1] += values2[1];
		values1[2] += values2[2];
		values1[3] += values2[3];
		values1[4] += values2[4];
		values1[5] += values2[5];
		values1[6] += values2[6];
		values1[7] += values2[7];
		values1[8] += values2[8];
	}

	/**
	 * 增加点化属性
	 * 
	 * @param list
	 * @param addRatio
	 */
	public static void addEnchantRatio(List<EnchantProty> list, float[] addRatio) {

		for (int i = 0; i <= 8; i++) {

			for (Property property : list) {
				if (property.getType() == i) {
					int newValue = (int) (property.getValue() * (1 + addRatio[i]));
					property.setValue(newValue);
				}

			}

		}
	}

	public static int getCapabilityByRobotUserHeroList(List<RobotUserHero> robotUserHeroList) {

		int capability = 0;
		for (RobotUserHero robotUserHero : robotUserHeroList) {
			capability += robotUserHero.getLife() * 1.5;
			capability += robotUserHero.getAttack() * 2;
			capability += robotUserHero.getDefense() * 4;
		}

		return capability;

	}

	public static void main(String[] args) {
		List<String> nodes = getAllNodesByHeroColor(0, 3);
		System.out.println(nodes);
	}
}
