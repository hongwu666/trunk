package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemArenaGift;

public interface SystemArenaGiftDao {
	public SystemArenaGift get(int groupId,int rank);
	public List<SystemArenaGift> getList(int groupId);
	public int getGroupItemCount();
}
