package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.MysteryMallDrop;

public interface MysteryMallDropDao {

	public List<MysteryMallDrop> getList(int mallType, int type, int groupId);

	public MysteryMallDrop get(int id);

}
