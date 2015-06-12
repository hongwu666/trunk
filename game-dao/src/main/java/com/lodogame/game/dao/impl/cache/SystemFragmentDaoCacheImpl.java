package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.SystemFragmentDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.model.SystemFragment;

public class SystemFragmentDaoCacheImpl extends BaseSystemCacheDao<SystemFragment> implements SystemFragmentDao, ReloadAble {

	@Override
	public SystemFragment getByFragmentId(int fragmentId) {
		return super.get(String.valueOf(fragmentId));
	}

	@Override
	public SystemFragment getByStar(int star, int mergedToolType) {
		List<SystemFragment> list = super.getList();
		for (SystemFragment systemFragment : list) {
			if (systemFragment.getType() == 1 && systemFragment.getStar() == star && systemFragment.getMergedToolType() == mergedToolType) {
				return systemFragment;
			}
		}
		return null;
	}

}
