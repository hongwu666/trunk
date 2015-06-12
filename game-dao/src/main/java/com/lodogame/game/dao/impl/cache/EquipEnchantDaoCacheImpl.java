package com.lodogame.game.dao.impl.cache;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.lodogame.game.dao.EquipEnchantDao;
import com.lodogame.game.dao.impl.mysql.EquipEnchantDaoMysqlImpl;
import com.lodogame.game.dao.impl.redis.EquipEnchantDaoRedisImpl;
import com.lodogame.model.EquipEnchant;

public class EquipEnchantDaoCacheImpl implements EquipEnchantDao {

	private EquipEnchantDaoMysqlImpl equipEnchantDaoMysqlImpl;

	private EquipEnchantDaoRedisImpl equipEnchantDaoRedisImpl;

	public void setEquipEnchantDaoMysqlImpl(EquipEnchantDaoMysqlImpl equipEnchantDaoMysqlImpl) {
		this.equipEnchantDaoMysqlImpl = equipEnchantDaoMysqlImpl;
	}

	public void setEquipEnchantDaoRedisImpl(EquipEnchantDaoRedisImpl equipEnchantDaoRedisImpl) {
		this.equipEnchantDaoRedisImpl = equipEnchantDaoRedisImpl;
	}

	@Override
	public EquipEnchant getEquipEnchant(String userId, String userEquipId) {
		List<EquipEnchant> l = this.getEquipEnchantList(userId);
		for (EquipEnchant equipEnchant : l) {
			if (StringUtils.equalsIgnoreCase(userEquipId, equipEnchant.getUserEquipId())) {
				return equipEnchant;
			}
		}
		return null;
	}

	@Override
	public boolean updateEquipEnchant(EquipEnchant equipEnchant) {
		boolean success = this.equipEnchantDaoMysqlImpl.updateEquipEnchant(equipEnchant);
		if (success) {
			equipEnchantDaoRedisImpl.updateEquipEnchant(equipEnchant);
		}
		return success;
	}

	@Override
	public boolean updateEnchantProperty(String userId, String userEquipId, String curProperty) {
		boolean success = this.equipEnchantDaoMysqlImpl.updateEnchantProperty(userId, userEquipId, curProperty);
		if (success) {
			EquipEnchant equipEnchant = this.equipEnchantDaoMysqlImpl.getEquipEnchant(userId, userEquipId);
			equipEnchantDaoRedisImpl.updateEquipEnchant(equipEnchant);
		}
		return success;
	}

	@Override
	public List<EquipEnchant> getEquipEnchantList(String userId) {
		List<EquipEnchant> l = this.equipEnchantDaoRedisImpl.getEquipEnchantList(userId);
		if (l == null) {
			l = this.equipEnchantDaoMysqlImpl.getEquipEnchantList(userId);
			for (EquipEnchant equipEnchant : l) {
				this.equipEnchantDaoRedisImpl.updateEquipEnchant(equipEnchant);
			}
		}
		return l;
	}

}
