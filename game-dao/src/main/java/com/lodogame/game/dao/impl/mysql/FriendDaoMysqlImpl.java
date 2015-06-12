package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.FriendDao;
import com.lodogame.model.Friend;

public class FriendDaoMysqlImpl implements FriendDao{

	private final static String table = "friend";
	
	@Autowired
	private Jdbc jdbc;
	
	@Override
	public List<Friend> getFrienddList(String uid) {
		String sql = "SELECT * FROM " + table + " WHERE user_id_a = ? OR user_id_b = ?";
		SqlParameter param = new SqlParameter();
		param.setString(uid);
		param.setString(uid);
		return this.jdbc.getList(sql, Friend.class, param);
	}

	@Override
	public boolean add(Friend friend) {
		return this.jdbc.insert(friend) > 0;
	}

	@Override
	public Friend getFriend(String uid, String friendUserId) {
		throw new NotImplementedException();
	}

	@Override
	public boolean removeFriend(Friend friend) {
		String sql = "DELETE FROM " + table + " WHERE (user_id_a = ? OR user_id_b = ?) AND (user_id_a = ? OR user_id_b = ?)";
		SqlParameter param = new SqlParameter();
		param.setString(friend.getUserIdA());
		param.setString(friend.getUserIdA());
		param.setString(friend.getUserIdB());
		param.setString(friend.getUserIdB());

		return this.jdbc.update(sql, param) > 0;
		
	}

}
