package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserFriendRequest;

public interface UserFriendRequestDao {

	public UserFriendRequest get(String userId, String sendUserId);

	public boolean add(UserFriendRequest request);

	public boolean updateStatus(String userId, String sendUserId, int requestStatus);

	public List<UserFriendRequest> getByStatus(String userId, int requestStatus);

	public List<UserFriendRequest> getList(String userId);

}
