package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.List;

import com.lodogame.game.dao.SystemGoldSetDao;
import com.lodogame.game.dao.impl.mysql.SystemGoldSetDaoMysqlImpl;
import com.lodogame.game.dao.reload.ReloadAbleBase;
import com.lodogame.model.SystemGoldSet;

public class SystemGoldSetDaoCacheImpl extends ReloadAbleBase implements SystemGoldSetDao {

	private List<SystemGoldSet> cache = new ArrayList<SystemGoldSet>();
	
	private SystemGoldSetDaoMysqlImpl systemGoldSetDaoMysqlImpl;

	@Override
	public List<SystemGoldSet> getAll() {
		return cache;
	}
	
	@Override
	public SystemGoldSet getByPayAmount(int type, double amount) {
		SystemGoldSet result = null;
		for (SystemGoldSet systemGoldSet : cache) {
			if (systemGoldSet.getType() != type) {
				continue;
			}
			
			if (result == null) {
				if (systemGoldSet.getMoney().doubleValue() <= amount) {
					result = systemGoldSet;
				}
			} else {
				if (systemGoldSet.getMoney().doubleValue() <= amount && systemGoldSet.getGold() > result.getGold()) {
					result = systemGoldSet;
				}
			}
		}
		return result;
	}

	public void setSystemGoldSetDaoMysqlImpl(SystemGoldSetDaoMysqlImpl systemGoldSetDaoMysqlImpl) {
		this.systemGoldSetDaoMysqlImpl = systemGoldSetDaoMysqlImpl;
	}

	public void init() {
		cache = systemGoldSetDaoMysqlImpl.getAll();
	}

	@Override
	public void reload() {
		cache.clear();
	}

	@Override
	public SystemGoldSet get(int waresId) {
		for (SystemGoldSet goldSet : cache) {
			if (goldSet.getSystemGoldSetId() == waresId) {
				return goldSet;
			}
		}
		
		return null;
	}

}
