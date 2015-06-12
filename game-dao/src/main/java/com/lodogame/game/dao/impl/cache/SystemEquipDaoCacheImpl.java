package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.SystemEquipDao;
import com.lodogame.model.RefineSoulCondition;
import com.lodogame.model.SystemEquip;

public class SystemEquipDaoCacheImpl extends BaseSystemCacheDao<SystemEquip> implements SystemEquipDao {

	@Override
	public SystemEquip get(int equipId) {
		return super.get(String.valueOf(equipId));
	}
	@Override
	public SystemEquip get(int predestinedId,int color){
		List<SystemEquip> list=getList();
		for(SystemEquip systemEquip:list){
			if (systemEquip.getPredestinedId()==predestinedId&&color==systemEquip.getColor()) {
				return systemEquip;
			}
		}
		return null;
	}
	@Override
	public List<SystemEquip> getRefineSoul(RefineSoulCondition refineSoulCondition) {
		return super.getList(refineSoulCondition.getEquipType() + "_" + refineSoulCondition.getCareer() + "_" + refineSoulCondition.getEquipStar());
	}
}
