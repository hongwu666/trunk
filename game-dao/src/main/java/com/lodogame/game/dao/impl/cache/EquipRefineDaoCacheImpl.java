package com.lodogame.game.dao.impl.cache;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.lodogame.game.dao.EquipRefineDao;
import com.lodogame.game.dao.impl.mysql.EquipRefineDaoMysqlImpl;
import com.lodogame.game.dao.impl.redis.EquipRefineDaoRedisImpl;
import com.lodogame.model.EquipRefine;

public class EquipRefineDaoCacheImpl implements EquipRefineDao {

	private EquipRefineDaoMysqlImpl equipRefineDaoMysqlImpl;

	private EquipRefineDaoRedisImpl equipRefineDaoRedisImpl;

	public void setEquipRefineDaoMysqlImpl(EquipRefineDaoMysqlImpl equipRefineDaoMysqlImpl) {
		this.equipRefineDaoMysqlImpl = equipRefineDaoMysqlImpl;
	}

	public void setEquipRefineDaoRedisImpl(EquipRefineDaoRedisImpl equipRefineDaoRedisImpl) {
		this.equipRefineDaoRedisImpl = equipRefineDaoRedisImpl;
	}

	@Override
	public boolean addEquipRefine(EquipRefine equipRefine) {
		boolean success = equipRefineDaoMysqlImpl.addEquipRefine(equipRefine);
		if (success) {
			this.equipRefineDaoRedisImpl.addEquipRefine(equipRefine);
		}
		return success;
	}

	@Override
	public boolean updateEquipRefine(EquipRefine equipRefine) {
		boolean success = equipRefineDaoMysqlImpl.updateEquipRefine(equipRefine);
		if (success) {
			this.equipRefineDaoRedisImpl.updateEquipRefine(equipRefine);
		}
		return success;
	}

	@Override
	public EquipRefine getEquipRefine(String userId, String userEquipId, int type) {

		List<EquipRefine> list = getUserEquipRefineList(userId);
		for (EquipRefine equipRefine : list) {
			if (StringUtils.equalsIgnoreCase(equipRefine.getUserEquipId(), userEquipId) && equipRefine.getRefinePoint() == type) {
				return equipRefine;
			}
		}
		return null;
	}

	@Override
	public List<EquipRefine> getUserEquipRefineList(String userId) {
		List<EquipRefine> list = this.equipRefineDaoRedisImpl.getUserEquipRefineList(userId);
		if (list == null) {
			list = this.equipRefineDaoMysqlImpl.getUserEquipRefineList(userId);
			for (EquipRefine equipRefine : list) {
				this.equipRefineDaoRedisImpl.addEquipRefine(equipRefine);
			}
		}
		return list;
	}

}
