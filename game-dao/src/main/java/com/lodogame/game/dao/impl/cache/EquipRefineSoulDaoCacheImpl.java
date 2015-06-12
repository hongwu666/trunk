package com.lodogame.game.dao.impl.cache;

import com.lodogame.game.dao.EquipRefineSoulDao;
import com.lodogame.game.dao.impl.mysql.EquipRefineSoulDaoMysqlImpl;
import com.lodogame.model.EquipRefineSoul;

public class EquipRefineSoulDaoCacheImpl implements EquipRefineSoulDao {

	
	private EquipRefineSoulDaoMysqlImpl equipRefineSoulDaoMysqlImpl;

	public void setEquipRefineSoulDaoMysqlImpl(EquipRefineSoulDaoMysqlImpl equipRefineSoulDaoMysqlImpl) {
		this.equipRefineSoulDaoMysqlImpl = equipRefineSoulDaoMysqlImpl;
	}

	@Override
	public EquipRefineSoul getEquipRefineSoul(String userEquipId, int equipId) {

		return equipRefineSoulDaoMysqlImpl.getEquipRefineSoul(userEquipId, equipId);
	}

	@Override
	public boolean upEquipRefineSoul(String userEquipId, int equipId, int luck) {
		return equipRefineSoulDaoMysqlImpl.upEquipRefineSoul(userEquipId, equipId, luck);
	}

	@Override
	public boolean addEquipRefineSoul(EquipRefineSoul equipRefineSoul) {
		return equipRefineSoulDaoMysqlImpl.addEquipRefineSoul(equipRefineSoul);
	}

	@Override
	public void delEquipRefineSoul(String userEquipId, int equipId) {
		equipRefineSoulDaoMysqlImpl.delEquipRefineSoul(userEquipId, equipId);
	}

}
