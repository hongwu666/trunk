package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.List;

import com.lodogame.game.dao.SystemTaskDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.model.SystemTask;

public class SystemTaskDaoCacheImpl extends BaseSystemCacheDao<SystemTask> implements SystemTaskDao, ReloadAble {

	@Override
	public SystemTask get(int systemTaskId) {
		return super.get(String.valueOf(systemTaskId));
	}

	@Override
	public List<SystemTask> getPosTaskList(int systemTaskId) {
		return super.getList(String.valueOf(systemTaskId));
	}

	@Override
	public List<SystemTask> getByTaskTargetType(int targetType) {

		List<SystemTask> list = new ArrayList<SystemTask>();

		List<SystemTask> l = this.getList();
		for (SystemTask systemTask : l) {
			if (systemTask.getTaskTarget() == targetType) {
				list.add(systemTask);
			}
		}
		return list;
	}

}
