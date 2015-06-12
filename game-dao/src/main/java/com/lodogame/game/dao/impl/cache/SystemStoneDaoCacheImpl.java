package com.lodogame.game.dao.impl.cache;

import java.util.List;

import org.apache.commons.lang.math.RandomUtils;

import com.lodogame.game.dao.SystemStoneDao;
import com.lodogame.model.SystemStone;

public class SystemStoneDaoCacheImpl extends BaseSystemCacheDao<SystemStone> implements SystemStoneDao {

	@Override
	public SystemStone getRandomStone(int level) {
		List<SystemStone> st = super.getList(String.valueOf(level));
		if (st.size() == 0) {
			return null;
		}
		int rand = RandomUtils.nextInt(st.size());
		return st.get(rand);
	}
}
