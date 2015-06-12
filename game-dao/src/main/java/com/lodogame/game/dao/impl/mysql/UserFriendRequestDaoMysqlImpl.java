package com.lodogame.game.dao.impl.mysql;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserFriendRequestDao;
import com.lodogame.model.UserFriendRequest;

public class UserFriendRequestDaoMysqlImpl implements UserFriendRequestDao{
	
	private static final String table = "user_friend_request"; 
	
	@Autowired
	private Jdbc jdbc;
	
	@Override
	public UserFriendRequest get(String userId, String sendUserId) {
		String sql = "SELECT * FROM " + table + " WHERE user_id = ? AND send_user_id = ?";
		SqlParameter param = new SqlParameter();
		param.setString(userId);
		param.setString(sendUserId);
	
		return this.jdbc.get(sql, UserFriendRequest.class, param);
	}

	@Override
	public boolean add(UserFriendRequest request) {
		return this.jdbc.insert(request) > 0;
	}

	@Override
	public boolean updateStatus(String userId, String sendUserId, int requestStatus) {
		String sql = "UPDATE " + table + " SET status = ?, updated_time=? WHERE user_id=? AND send_user_id=?";
		SqlParameter param = new SqlParameter();
		param.setInt(requestStatus);
		param.setObject(new Date());
		param.setString(userId);
		param.setString(sendUserId);
		return this.jdbc.update(sql, param) > 0;
	}

	@Override
	public List<UserFriendRequest> getByStatus(String userId, int requestStatus) {
		String sql = "SELECT * FROM " + table + " WHERE user_id = ? AND status = ?";
		SqlParameter param = new SqlParameter();
		param.setString(userId);
		param.setInt(requestStatus);
		
		return this.jdbc.getList(sql, UserFriendRequest.class, param);
	}
	
	@Override
	public List<UserFriendRequest> getList(String userId) {
		String sql = "SELECT * FROM " + table + " WHERE user_id = ?";
		SqlParameter param = new SqlParameter();
		param.setString(userId);
		return this.jdbc.getList(sql, UserFriendRequest.class, param);
	}

}
