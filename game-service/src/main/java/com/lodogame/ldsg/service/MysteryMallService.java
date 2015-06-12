package com.lodogame.ldsg.service;

import java.util.List;

import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.UserMysteryMallDetailBO;
import com.lodogame.ldsg.bo.UserMysteryMallInfoBO;

public interface MysteryMallService {

	// 银币不足
	public final static int REFRESH_NOT_ENOUGH_COPPER = 2001;

	// 元宝不足
	public final static int REFRESH_NOT_ENOUGH_GOLD = 2002;

	// 次数超了
	public final static int REFRESH_NOT_ENOUGH_TIMES = 2003;

	// 威望不足
	public final static int REFRESH_NOT_ENOUGH_REPUTATION = 2005;

	// 道具不足
	public final static int EXCHANGE_NOT_ENOUGH_TOOL = 2004;

	// 硬币不足
	public final static int EXCHANGE_NOT_ENOUGH_EXPEDITION = 2005;

	// 龙麟不足
	public final static int REFRESH_NOT_ENOUGH_COIN = 2006;

	// 荣誉不足
	public final static int REFRESH_NOT_ENOUGH_HONOUR = 2007;

	/**
	 * 刷新
	 * 
	 * @param userId
	 * @param mallType
	 * @param type
	 * @param useMoney
	 * @return
	 */
	public boolean refresh(String userId, int mallType, int type, boolean useMoney);

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserMysteryMallDetailBO> getUserMallDetailBO(String userId, int mallType);

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public UserMysteryMallInfoBO getUserMysteryMallInfoBO(String userId, int mallType);

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public CommonDropBO exchange(String userId, int mallType, int dropId, int type);
}
