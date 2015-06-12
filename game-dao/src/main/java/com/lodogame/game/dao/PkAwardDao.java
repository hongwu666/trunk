package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.PkAward;
import com.lodogame.model.PkGiftDay;
import com.lodogame.model.PkRankUpGift;

public interface PkAwardDao {
	
	
	public List<PkRankUpGift>  getUpGifts();
	
	public int getUpGift(int old,int news);
	
	public List<PkGiftDay> getDayGift();

	public List<PkAward> getAll();

	public PkAward getById(int awardId);

	/**
	 * 添天奖励是否已经发放
	 * 
	 * @param date
	 * @return
	 */
	public boolean isAwardSended(String date);

	/**
	 * 添天奖励是否已经发放
	 * 
	 * @param date
	 * @return
	 */
	public boolean addAwardSendLog(String date);
}
