package com.lodogame.ldsg.helper;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.lodogame.ldsg.bo.UserData;

public class RankHelper {

	/**
	 * 获取用户排名
	 * 
	 * @param list
	 * @param userId
	 * @param defaultRank
	 * @return
	 */
	public static int getRank(List<?> list, String userId, int defaultRank) {

		int rank = 1;

		for (Object obj : list) {
			UserData userData = (UserData) obj;
			if (StringUtils.equalsIgnoreCase(userData.getUserId(), userId)) {
				return rank;
			}

			rank++;
		}

		return defaultRank;
	}
}
