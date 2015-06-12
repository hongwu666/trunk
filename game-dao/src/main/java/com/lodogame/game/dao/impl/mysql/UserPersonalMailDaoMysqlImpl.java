package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.UserPersonalMailDao;
import com.lodogame.game.utils.TableUtils;
import com.lodogame.model.UserPersonalMail;

public class UserPersonalMailDaoMysqlImpl implements UserPersonalMailDao{

	@Autowired
	private Jdbc jdbc;
	
	@Override
	public List<UserPersonalMail> getMailList(String uid, int mid) {
		String table = TableUtils.getUserPersonalMailTable(uid);
		String sql = "SELECT * FROM " + table + " WHERE user_id = ? AND id > ?";
		SqlParameter param = new SqlParameter();
		param.setString(uid);
		param.setInt(mid);
		return this.jdbc.getList(sql, UserPersonalMail.class, param);
	}

	@Override
	public boolean save(UserPersonalMail mail) {
		String table = TableUtils.getUserPersonalMailTable(mail.getUserId());
		return this.jdbc.insert(table, mail) > 0;
	}

	@Override
	public boolean updateStatus(String uid, int mailId, int status) {
		String table = TableUtils.getUserPersonalMailTable(uid);
		String sql = "UPDATE " + table + " SET status = ? WHERE user_id = ? AND id = ?";
		SqlParameter param = new SqlParameter();
		param.setInt(status);
		param.setString(uid);
		param.setInt(mailId);
		return this.jdbc.update(sql, param) > 0;
	}

	@Override
	public UserPersonalMail getInviteMail(String uid, String friendUserId) {
		String table = TableUtils.getUserPersonalMailTable(uid);
		
		// 如果是好友申请邮件，is_processed 的值：0表示用户没有点击“同意”或者“不同意”，1表示用户点击了“同意”或者“不同意
		String sql = "SELECT * FROM " + table + " WHERE user_id = ? AND friend_user_id = ? AND type = 1 AND is_processed = 0 LIMIT 1";
		SqlParameter param = new SqlParameter();
		param.setString(uid);
		param.setString(friendUserId);
		return this.jdbc.get(sql, UserPersonalMail.class, param);
	}

	@Override
	public boolean update(String uid, int id, int status, int isProcessed) {
		String table = TableUtils.getUserPersonalMailTable(uid);
		String sql = "UPDATE " + table + " SET status = ?, is_processed = ? WHERE user_id = ? AND id = ?";
		SqlParameter param = new SqlParameter();
		param.setInt(status);
		param.setInt(isProcessed);
		param.setString(uid);
		param.setInt(id);
		
		return this.jdbc.update(sql, param) > 0;
		
	}

	@Override
	public List<UserPersonalMail> getNewMails(String uid) {
		String table = TableUtils.getUserPersonalMailTable(uid);
		String sql = "SELECT * FROM " + table + " WHERE user_id = ? and status = 0";
		SqlParameter param = new SqlParameter();
		param.setString(uid);
		return this.jdbc.getList(sql, UserPersonalMail.class, param);
	}

	
	
}
