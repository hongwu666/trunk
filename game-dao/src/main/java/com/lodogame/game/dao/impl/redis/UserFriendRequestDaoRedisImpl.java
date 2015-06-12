package com.lodogame.game.dao.impl.redis;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.lodogame.game.dao.UserFriendRequestDao;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.model.UserFriendRequest;

public class UserFriendRequestDaoRedisImpl implements UserFriendRequestDao {

	@Override
	public UserFriendRequest get(String userId, String sendUserId) {
		String key = RedisKey.getUserFriendRequestKey(userId);
		String json = JedisUtils.getFieldFromObject(key, sendUserId);
		if (StringUtils.isNotEmpty(json)) {
			return Json.toObject(json, UserFriendRequest.class);
		}
		return null;
	}

	@Override
	public boolean add(UserFriendRequest request) {
		String key = RedisKey.getUserFriendRequestKey(request.getUserId());
		String json = Json.toJson(request);
		JedisUtils.setFieldToObject(key, request.getUserId(), json);
		return true;
	}

	@Override
	public boolean updateStatus(String userId, String sendUserId, int requestStatus) {
		String key = RedisKey.getUserFriendRequestKey(userId);
		JedisUtils.delFieldFromObject(key, sendUserId);
		return true;
	}

	@Override
	public List<UserFriendRequest> getByStatus(String userId, int requestStatus) {
		List<UserFriendRequest> rt = new ArrayList<UserFriendRequest>();

		String key = RedisKey.getUserFriendRequestKey(userId);
		List<String> jsonList = JedisUtils.getMapValues(key);
		if (jsonList != null && jsonList.size() > 0) {
			List<UserFriendRequest> list = Json.toList(jsonList, UserFriendRequest.class);

			for (UserFriendRequest request : list) {
				if (request.getStatus() == requestStatus) {
					rt.add(request);
				}
			}

			return rt;
		}

		return null;
	}

	@Override
	public List<UserFriendRequest> getList(String userId) {
		List<String> jsonList = JedisUtils.getMapValues(userId);
		if (jsonList != null && jsonList.size() > 0) {
			return Json.toList(jsonList, UserFriendRequest.class);
		}
		return null;
	}
}
