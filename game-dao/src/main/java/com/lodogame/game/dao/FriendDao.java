package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.Friend;

public interface FriendDao {

	public List<Friend> getFrienddList(String uid);

	public boolean add(Friend friend);

	public Friend getFriend(String uid, String friendUserId);

	public boolean removeFriend(Friend friend);

}
