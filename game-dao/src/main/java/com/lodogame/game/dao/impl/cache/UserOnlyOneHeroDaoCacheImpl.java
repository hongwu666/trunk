package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.UserOnlyOneHeroDao;
import com.lodogame.game.dao.impl.mysql.UserOnlyOneHeroDaoMysqlImpl;
import com.lodogame.model.UserOnlyOneHero;

public class UserOnlyOneHeroDaoCacheImpl implements UserOnlyOneHeroDao {

	private UserOnlyOneHeroDaoMysqlImpl userOnlyOneHeroDaoMysqlImpl;

	public void setUserOnlyOneHeroDaoMysqlImpl(UserOnlyOneHeroDaoMysqlImpl userOnlyOneHeroDaoMysqlImpl) {
		this.userOnlyOneHeroDaoMysqlImpl = userOnlyOneHeroDaoMysqlImpl;
	}

	@Override
	public List<UserOnlyOneHero> getHeros(String userId) {
		return this.userOnlyOneHeroDaoMysqlImpl.getHeros(userId);
	}

	@Override
	public List<UserOnlyOneHero> getPosHeros(String userId) {
		return this.userOnlyOneHeroDaoMysqlImpl.getPosHeros(userId);
	}

	@Override
	public UserOnlyOneHero getHero(String userId, String userHeroId) {
		return this.userOnlyOneHeroDaoMysqlImpl.getHero(userId, userHeroId);
	}

	@Override
	public void updateHero(String userId, List<UserOnlyOneHero> heos) {
		this.userOnlyOneHeroDaoMysqlImpl.updateHero(userId, heos);
	}

	@Override
	public void updateHero(String userId, UserOnlyOneHero heo) {
		this.userOnlyOneHeroDaoMysqlImpl.updateHero(userId, heo);
	}

	@Override
	public void insertHero(String userId, List<UserOnlyOneHero> heos) {
		if (heos.isEmpty()) {
			return;
		}
		this.userOnlyOneHeroDaoMysqlImpl.insertHero(userId, heos);
	}

	@Override
	public void updateHeroPos(String userId, UserOnlyOneHero heo) {
		this.userOnlyOneHeroDaoMysqlImpl.updateHeroPos(userId, heo);
	}

	@Override
	public void delteHero(String userId) {
		this.userOnlyOneHeroDaoMysqlImpl.delteHero(userId);
	}

	@Override
	public void delteHero(String userId, String userHeroId) {
		this.userOnlyOneHeroDaoMysqlImpl.delteHero(userId, userHeroId);
	}

	@Override
	public void delteHero(String userId, List<String> userHeroIds) {
		this.userOnlyOneHeroDaoMysqlImpl.delteHero(userId, userHeroIds);
	}
}
