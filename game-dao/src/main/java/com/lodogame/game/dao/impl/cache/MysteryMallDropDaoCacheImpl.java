package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.MysteryMallDropDao;
import com.lodogame.model.MysteryMallDrop;

public class MysteryMallDropDaoCacheImpl extends BaseSystemCacheDao<MysteryMallDrop> implements MysteryMallDropDao {

	@Override
	public List<MysteryMallDrop> getList(int mallType, int type, int groupId) {
		String listKey = mallType + "_" + type + "_" + groupId;
		return this.getList(listKey);
	}

	@Override
	public MysteryMallDrop get(int id) {
		return this.get(String.valueOf(id));
	}

}
