package com.lodogame.ldsg.service;

import java.util.List;

import com.lodogame.ldsg.bo.TreasureGiftBo;
import com.lodogame.ldsg.bo.TreasurePriceBo;
import com.lodogame.ldsg.bo.TreasureReturnBO;
import com.lodogame.ldsg.bo.TreasureShowBO;

public interface TreasureService {
	/**
	 * 元宝不足
	 */
	public final static int YB_NOT_ENOUGH = 1000;
	
	/**
	 * 剩余次数不足
	 */
	public final static int NUM_NOT_ENOUGH = 1001;
	
	TreasureReturnBO open(String userId,int type);
	
	List<TreasureShowBO> show(String userId);
	
	TreasureGiftBo getGift(String userId);
	TreasurePriceBo getPriceBo(String userId);
}
