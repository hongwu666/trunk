package com.lodogame.game.dao.impl.cache;

import java.util.List;

import com.lodogame.game.dao.FriendDao;
import com.lodogame.game.dao.impl.mysql.FriendDaoMysqlImpl;
import com.lodogame.game.dao.impl.redis.FriendDaoRedisImpl;
import com.lodogame.model.Friend;

public class FriendDaoCacheImpl implements FriendDao {

	private FriendDaoMysqlImpl friendDaoMysqlImpl;

	private FriendDaoRedisImpl friendDaoRedisImpl;

	public void setFriendDaoRedisImpl(FriendDaoRedisImpl friendDaoRedisImpl) {
		this.friendDaoRedisImpl = friendDaoRedisImpl;
	}

	public void setFriendDaoMysqlImpl(FriendDaoMysqlImpl friendDaoMysqlImpl) {
		this.friendDaoMysqlImpl = friendDaoMysqlImpl;
	}

	@Override
	public List<Friend> getFrienddList(String uid) {
		List<Friend> list = friendDaoRedisImpl.getFrienddList(uid);
		if (list != null) {
			return list;
		}

		list = friendDaoMysqlImpl.getFrienddList(uid);
		if (list != null) {
			for (Friend friend : list) {
				friendDaoRedisImpl.add(friend);
			}
		}

		return list;
	}

	@Override
	public boolean add(Friend friend) {
		friendDaoRedisImpl.add(friend);
		friendDaoMysqlImpl.add(friend);
		return true;
	}

	@Override
	public Friend getFriend(String uid, String friendUserId) {
		List<Friend> friends = getFrienddList(uid);
		for (Friend friend : friends) {
			if (friendUserId.equals(friend.getUserIdA()) || friendUserId.equals(friend.getUserIdB())) {
				return friend;
			}
		}

		return null;
	}

	@Override
	public boolean removeFriend(Friend friend) {
		friendDaoRedisImpl.removeFriend(friend);
		friendDaoMysqlImpl.removeFriend(friend);
		return true;
	}

}
