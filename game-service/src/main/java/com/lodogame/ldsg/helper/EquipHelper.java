package com.lodogame.ldsg.helper;

import java.util.List;

import com.lodogame.ldsg.bo.EquipRefineBO;
import com.lodogame.ldsg.bo.UserEquipBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.model.EquipRefine;
import com.lodogame.model.SystemSuitDetail;

public class EquipHelper {

	/**
	 * 获取元宝进阶需要的元宝数
	 * 
	 * @param color
	 * @return
	 */
	public static int getGoldMergeNeedMoney(int color) {

		switch (color) {
		case 0:
			return 400;
		case 1:
			return 550;
		case 2:
			return 750;
		case 3:
			return 1000;
		case 4:
			return 2800;
		case 5:
			return 3000;
		default:
			return 0;

		}
	}
	/**
	 * 获取装备战力
	 * 
	 * @param userHeroList
	 * @return
	 */
	public static int getCapability(EquipRefineBO bo ) {

		int capability = 0;
		switch (bo.getProType()) {
		case 1:
			capability += bo.getProValue() * 1.5;
			break;
		case 2:
			capability += bo.getProValue() * 2;
			break;
		case 3:
			capability += bo.getProValue() * 4;
			break;
		default:
			break;
		}

		return capability;

	}
	public static boolean hasEquip(List<UserEquipBO> userEquipBOList, SystemSuitDetail systemSuitDetail) {

		for (UserEquipBO userEquipBO : userEquipBOList) {

			if (userEquipBO.getEquipId() == systemSuitDetail.getEquipId()) {
				return true;
			}

		}

		return false;

	}
}
