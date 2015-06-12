package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.EmpireSystemUpDao;
import com.lodogame.game.dao.PreloadAble;
import com.lodogame.model.EmpireSystemUp;

public class EmpireSystemUpDaoCacheImpl extends BaseSystemCacheDao<EmpireSystemUp> implements EmpireSystemUpDao ,PreloadAble{

	@Override
	public List<EmpireSystemUp> getEmpireSystemUp(int floor) {
		return super.getList(floor+"");
	}

}
