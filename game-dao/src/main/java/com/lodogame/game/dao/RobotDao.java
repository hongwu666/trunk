package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.RobotUser;
import com.lodogame.model.RobotUserHero;

public interface RobotDao {

	public boolean insert(List<RobotUserHero> robotUserHeroList);

	public boolean insertUser(List<RobotUser> userList);

	public RobotUser getUser(String userId);

	public RobotUser getUser(long lodoId);

	public RobotUser getByName(String username);

	public List<RobotUser> getByCapability(int minCapability, int maxCapability);

	public int count();

	public RobotUser getRobotUserByLevel(int level);

	public List<RobotUserHero> getRobotUserHero(String userId);

	public int getMaxLodoId();
}
