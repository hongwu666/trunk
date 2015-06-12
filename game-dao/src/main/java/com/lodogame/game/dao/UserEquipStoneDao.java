package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserEquipStone;

public interface UserEquipStoneDao {

	public List<UserEquipStone> getUserEquipStone(String userEquipId);

	public UserEquipStone getUserEquipStone(String userEquipId, int pos);

	public boolean insertUserEquipStone(UserEquipStone userEquipStone);

	public boolean delUserEquipStone(String userEquipId, int pos);

	public boolean delUserEquipStone(String userEquipId);

}
