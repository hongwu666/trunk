package com.lodogame.ldsg.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.lodogame.ldsg.bo.DropDescBO;
import com.lodogame.model.GiftbagDropTool;

public class DropDescHelper {

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static String getToolIds(List<GiftbagDropTool> list) {

		String s = "";

		for (GiftbagDropTool giftbagDropTool : list) {

			String ss = giftbagDropTool.getToolType() + "," + giftbagDropTool.getToolId() + "," + giftbagDropTool.getToolNum();

			if (StringUtils.isEmpty(s)) {
				s = ss;
			} else {
				s = s + "|" + ss;
			}

		}

		return s;
	}

	/**
	 * 解析旧落字符串
	 * 
	 * @param toolIds
	 * @return
	 */
	public static List<DropDescBO> parseDropTool(String toolIds) {

		List<DropDescBO> dropToolBOList = new ArrayList<DropDescBO>();

		if (StringUtils.isEmpty(toolIds)) {
			return dropToolBOList;
		}

		String[] strs = toolIds.split("\\|");
		for (String str : strs) {
			String[] infos = str.split(",");
			if (infos.length != 3) {
				continue;
			}

			int toolType = NumberUtils.toInt(infos[0]);
			int toolId = NumberUtils.toInt(infos[1]);
			int toolNum = NumberUtils.toInt(infos[2]);

			DropDescBO dropDescBO = new DropDescBO(toolType, toolId, toolNum);
			dropToolBOList.add(dropDescBO);
		}

		return dropToolBOList;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static List<DropDescBO> toDropDesc(List<GiftbagDropTool> list) {

		List<DropDescBO> dropDescBOList = new ArrayList<DropDescBO>();

		for (GiftbagDropTool giftbagDropTool : list) {
			DropDescBO bo = new DropDescBO(giftbagDropTool.getToolType(), giftbagDropTool.getToolId(), giftbagDropTool.getToolNum());
			dropDescBOList.add(bo);
		}

		return dropDescBOList;
	}

	/**
	 * 是否掉落
	 * 
	 * @param lowerNum
	 * @param upperNum
	 * @return
	 */
	public static boolean isDrop(int lowerNum, int upperNum, int rand) {

		return rand >= lowerNum && rand <= upperNum;

	}

}
