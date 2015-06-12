package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.SystemDrawDetailDao;
import com.lodogame.model.SystemDrawDetail;

public class SystemDrawDetailDaoCacheImpl extends BaseSystemCacheDao<SystemDrawDetail> implements SystemDrawDetailDao {

	@Override
	public List<SystemDrawDetail> getList(int systemDrawId) {
		return super.getList(String.valueOf(systemDrawId));
	}

}
