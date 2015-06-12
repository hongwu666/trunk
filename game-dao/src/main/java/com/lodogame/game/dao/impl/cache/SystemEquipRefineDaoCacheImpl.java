package com.lodogame.game.dao.impl.cache;

import com.lodogame.game.dao.SystemEquipRefineDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.model.RefineCondition;
import com.lodogame.model.SystemEquipRefine;

public class SystemEquipRefineDaoCacheImpl extends BaseSystemCacheDao<SystemEquipRefine> implements SystemEquipRefineDao, ReloadAble {

	@Override
	public SystemEquipRefine getSystemEquipRefine(RefineCondition refineCondition) {
		return super.get(refineCondition.getEquipType() + "_" + refineCondition.getCareer() + "_" + refineCondition.getRefinePoint() + "_" + refineCondition.getRefineLevel());
	}
}