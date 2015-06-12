package com.lodogame.game.dao.impl.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.lodogame.game.dao.FriendDao;
import com.lodogame.game.dao.clearcache.ClearCacheOnLoginOut;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.model.Friend;

public class FriendDaoRedisImpl implements FriendDao, ClearCacheOnLoginOut {

	@Override
	public List<Friend> getFrienddList(String uid) {
		String key = RedisKey.getUserFriendCacheKey(uid);

		Set<String> friendUids = JedisUtils.smembers(key);

		if (friendUids.size() == 0) {
			return null;
		}

		List<Friend> friends = new ArrayList<Friend>();
		for (String friendUid : friendUids) {
			Friend friend = new Friend();
			friend.setUserIdA(uid);
			friend.setUserIdB(friendUid);
			friends.add(friend);
		}

		return friends;
	}

	@Override
	public boolean add(Friend friend) {

		String key1 = RedisKey.getUserFriendCacheKey(friend.getUserIdA());
		if (JedisUtils.exists(key1)) {
			JedisUtils.sadd(key1, friend.getUserIdB());
		}

		String key2 = RedisKey.getUserFriendCacheKey(friend.getUserIdB());
		if (JedisUtils.exists(key2)) {
			JedisUtils.sadd(key2, friend.getUserIdA());
		}

		return true;
	}

	@Override
	public Friend getFriend(String uid, String friendUserId) {
		String key = RedisKey.getUserFriendCacheKey(uid);
		if (JedisUtils.sIsMember(key, friendUserId) == true) {
			Friend friend = new Friend();
			friend.setUserIdA(uid);
			friend.setUserIdB(friendUserId);

			return friend;
		}

		return null;
	}

	@Override
	public boolean removeFriend(Friend friend) {
		String key1 = RedisKey.getUserFriendCacheKey(friend.getUserIdA());
		JedisUtils.srem(key1, friend.getUserIdB());

		String key2 = RedisKey.getUserFriendCacheKey(friend.getUserIdB());
		JedisUtils.srem(key2, friend.getUserIdA());

		return true;
	}

	@Override
	public void clearOnLoginOut(String userId) throws Exception {
		String key = RedisKey.getUserFriendCacheKey(userId);
		JedisUtils.delete(key);
	}

}
