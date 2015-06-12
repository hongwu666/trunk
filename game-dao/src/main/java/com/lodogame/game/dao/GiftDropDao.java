package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.GiftDrop;

public interface GiftDropDao {
	public GiftDrop get(int giftType, int giftBagType);
	
	/**
	 * 重载用
	 * @return
	 */
	public List<GiftDrop> getAllDrops();
	
}
