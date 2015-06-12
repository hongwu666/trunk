package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.SkillDataDao;
import com.lodogame.model.SkillData;

public class SkillDataDaoCacheImpl extends BaseSystemCacheDao<SkillData> implements SkillDataDao {

	@Override
	public SkillData getSkillData(int skill) {
		List<SkillData> list = this.getList();
		for (SkillData skillData : list) {
			if (skillData.getSkillLow() <= skill && (skillData.getSkillUp() >= skill||skillData.getSkillUp()<0)) {
				return skillData;
			}
		}
		return null;
	}

}
