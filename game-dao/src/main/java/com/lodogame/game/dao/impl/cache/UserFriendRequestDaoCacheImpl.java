package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.List;

import com.lodogame.game.dao.UserFriendRequestDao;
import com.lodogame.game.dao.impl.mysql.UserFriendRequestDaoMysqlImpl;
import com.lodogame.game.dao.impl.redis.UserFriendRequestDaoRedisImpl;
import com.lodogame.model.UserFriendRequest;

public class UserFriendRequestDaoCacheImpl implements UserFriendRequestDao {

	private UserFriendRequestDaoMysqlImpl userFriendRequestDaoMysqlImpl;
	private UserFriendRequestDaoRedisImpl userFriendRequestDaoRedisImpl;
	public void setUserFriendRequestDaoMysqlImpl(
			UserFriendRequestDaoMysqlImpl userFriendRequestDaoMysqlImpl) {
		this.userFriendRequestDaoMysqlImpl = userFriendRequestDaoMysqlImpl;
	}


	public void setUserFriendRequestDaoRedisImpl(
			UserFriendRequestDaoRedisImpl userFriendRequestDaoRedisImpl) {
		this.userFriendRequestDaoRedisImpl = userFriendRequestDaoRedisImpl;
	}


	@Override
	public UserFriendRequest get(String userId, String sendUserId) {
		UserFriendRequest request = userFriendRequestDaoRedisImpl.get(userId, sendUserId);
		if (request == null) {
			List<UserFriendRequest> requestList = userFriendRequestDaoMysqlImpl.getList(userId);
			if (requestList == null) {
				return null;
			}
			
			for (UserFriendRequest userFriendRequest : requestList) {
				userFriendRequestDaoRedisImpl.add(userFriendRequest);
				if (userFriendRequest.getSendUserId().equals(sendUserId)) {
					request = userFriendRequest;
				}
			}
		}
		return request;
	}


	@Override
	public boolean add(UserFriendRequest request) {
		boolean success = userFriendRequestDaoMysqlImpl.add(request);
		if (success) {
			userFriendRequestDaoRedisImpl.add(request);
		}
		return success;
	}


	@Override
	public boolean updateStatus(String userId, String sendUserId, int requestStatus) {
		boolean success = userFriendRequestDaoMysqlImpl.updateStatus(userId, sendUserId, requestStatus);
		if (success) {
			success = userFriendRequestDaoRedisImpl.updateStatus(userId, sendUserId, requestStatus);
		}
		return success;
	}


	@Override
	public List<UserFriendRequest> getByStatus(String userId, int requestStatus) {
		List<UserFriendRequest> rt = new ArrayList<UserFriendRequest>();
		
		List<UserFriendRequest> list = getList(userId);
		if (list != null) {
			for  (UserFriendRequest request : list) {
				if (request.getStatus() == requestStatus) {
					rt.add(request);
				}
			}
		}
		
		return rt;
	}


	@Override
	public List<UserFriendRequest> getList(String userId) {
		List<UserFriendRequest> list = userFriendRequestDaoRedisImpl.getList(userId);
		if (list == null) {
			list = userFriendRequestDaoMysqlImpl.getList(userId);
			if (list == null) {
				return null;
			}
			
			for (UserFriendRequest request : list) {
				userFriendRequestDaoRedisImpl.add(request);
			}
		}
		return list;
	}

}
