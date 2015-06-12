package com.lodogame.game.dao.impl.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lodogame.game.dao.UserEquipStoneDao;
import com.lodogame.model.UserEquipStone;

public class UserEquipStoneDaoCacheImpl implements UserEquipStoneDao {

	private UserEquipStoneDao userEquipStoneDaoMysqlImpl;

	public void setUserEquipStoneDaoMysqlImpl(UserEquipStoneDao userEquipStoneDaoMysqlImpl) {
		this.userEquipStoneDaoMysqlImpl = userEquipStoneDaoMysqlImpl;
	}

	private Map<String, List<UserEquipStone>> userEquipStoneMap = new HashMap<String, List<UserEquipStone>>();

	@Override
	public List<UserEquipStone> getUserEquipStone(String userEquipId) {
		if (!userEquipStoneMap.containsKey(userEquipId)) {
			List<UserEquipStone> userEquipStoneList = this.userEquipStoneDaoMysqlImpl.getUserEquipStone(userEquipId);
			userEquipStoneMap.put(userEquipId, userEquipStoneList);
		}

		return userEquipStoneMap.get(userEquipId);
	}

	@Override
	public UserEquipStone getUserEquipStone(String userEquipId, int pos) {
		List<UserEquipStone> userEquipStoneList = this.getUserEquipStone(userEquipId);
		for (UserEquipStone userEquipStone : userEquipStoneList) {
			if (userEquipStone.getPos() == pos) {
				return userEquipStone;
			}
		}

		return null;
	}

	@Override
	public boolean insertUserEquipStone(UserEquipStone userEquipStone) {
		boolean succ = this.userEquipStoneDaoMysqlImpl.insertUserEquipStone(userEquipStone);
		if (succ) {
			if (userEquipStoneMap.containsKey(userEquipStone.getUserEquipId())) {
				userEquipStoneMap.remove(userEquipStone.getUserEquipId());
			}
		}

		return succ;
	}

	@Override
	public boolean delUserEquipStone(String userEquipId, int pos) {
		boolean succ = this.userEquipStoneDaoMysqlImpl.delUserEquipStone(userEquipId, pos);
		if (succ) {
			if (userEquipStoneMap.containsKey(userEquipId)) {
				userEquipStoneMap.remove(userEquipId);
			}
		}

		return succ;
	}

	@Override
	public boolean delUserEquipStone(String userEquipId) {
		boolean succ = this.userEquipStoneDaoMysqlImpl.delUserEquipStone(userEquipId);
		if (succ) {
			if (userEquipStoneMap.containsKey(userEquipId)) {
				userEquipStoneMap.remove(userEquipId);
			}
		}

		return succ;
	}
}
