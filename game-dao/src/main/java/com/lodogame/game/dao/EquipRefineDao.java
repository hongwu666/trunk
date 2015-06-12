package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.EquipRefine;

public interface EquipRefineDao {

	public List<EquipRefine> getUserEquipRefineList(String userId);

	public boolean addEquipRefine(EquipRefine equipRefine);

	public boolean updateEquipRefine(EquipRefine equipRefine);

	public EquipRefine getEquipRefine(String userId, String userEquipId, int type);
}
