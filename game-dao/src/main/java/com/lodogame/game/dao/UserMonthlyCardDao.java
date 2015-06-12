package com.lodogame.game.dao;

import java.util.Date;

import com.lodogame.model.UserMonthlyCard;

public interface UserMonthlyCardDao {

	public UserMonthlyCard get(String userId);

	public boolean add(UserMonthlyCard card);

	public boolean updateDueTime(String userId, Date dueTime);

}
