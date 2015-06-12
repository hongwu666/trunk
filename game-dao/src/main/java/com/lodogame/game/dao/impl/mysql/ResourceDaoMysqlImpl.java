package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.ResourceDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.model.ResourceGk;
import com.lodogame.model.ResourceGkConfig;
import com.lodogame.model.ResourceGkPkLog;
import com.lodogame.model.ResourceGkStart;
import com.lodogame.model.ResourceNum;
import com.lodogame.model.ResourceStartConfig;
import com.lodogame.model.ResourceUserInfo;

public class ResourceDaoMysqlImpl implements ResourceDao{
	@Autowired
	private Jdbc jdbc;

	public List<ResourceNum>  getNum(String userId,String date) {
		String sql = "SELECT * FROM resource_num WHERE user_id=? AND created_time=?";
		SqlParameter sp = new SqlParameter();
		sp.setString(userId);
		sp.setString(date);
		return jdbc.getList(sql, ResourceNum.class, sp);
	}
	public void updateNum(ResourceNum num) {
		String sql = "UPDATE resource_num SET used_num=?,max_num=? WHERE user_id=? AND type=? AND created_time=?";
		SqlParameter sp = new SqlParameter();
		sp.setInt(num.getUsedNum());
		sp.setInt(num.getMaxNum());
		sp.setString(num.getUserId());
		sp.setInt(num.getType());
		sp.setString(DateUtils.getDate(num.getCreatedTime()));
		jdbc.update(sql, sp);
	}

	public void insertNum(ResourceNum num) {
		String sql = "INSERT INTO resource_num VALUES(?,?,?,?,?)";
		SqlParameter sp = new SqlParameter();
		sp.setString(num.getUserId());
		sp.setInt(num.getUsedNum());
		sp.setInt(num.getMaxNum());
		sp.setString(DateUtils.getDate(num.getCreatedTime()));
		sp.setInt(num.getType());
		jdbc.update(sql, sp);
	}

	public List<ResourceGk> getGk(String userId, String date) {
		String sql = "SELECT * FROM resource_gk WHERE user_id=? AND created_time=?";
		SqlParameter sp = new SqlParameter();
		sp.setString(userId);
		sp.setString(date);
		return jdbc.getList(sql, ResourceGk.class, sp);
	}

	public void insertGk(ResourceGk gk) {
		String sql = "INSERT INTO resource_gk VALUES(?,?,?,?,?)";
		SqlParameter sp = new SqlParameter();
		sp.setString(gk.getUserId());
		sp.setInt(gk.getId());
		sp.setInt(gk.getFbType());
		sp.setInt(gk.getFbDif());
		sp.setObject(gk.getCreatedTime());
		jdbc.update(sql, sp);
	}

	public List<ResourceGkStart> getGkStart(int id) {
		String sql = "SELECT * FROM resource_gk_start WHERE id=?";
		SqlParameter sp = new SqlParameter();
		sp.setInt(id);
		return jdbc.getList(sql, ResourceGkStart.class, sp);
	}

	public void insertGkStart(ResourceGkStart start) {
		String sql = "INSERT INTO resource_gk_start VALUES(?,?,?)";
		SqlParameter sp=new SqlParameter();
		sp.setInt(start.getId());
		sp.setInt(start.getGk());
		sp.setInt(start.getStartLevel());
		jdbc.update(sql, sp);
	}


	public void updateGkStart(ResourceGkStart start) {
		String sql = "UPDATE resource_gk_start SET start_level=? WHERE id=? and gk=?";
		SqlParameter sp = new SqlParameter();
		sp.setInt(start.getStartLevel());
		sp.setInt(start.getId());
		sp.setInt(start.getGk());
		jdbc.update(sql, sp);
	}

	public List<ResourceGkPkLog> getGkPkLog(int ids) {
		String sql = "SELECT * FROM resource_gk_pk_log WHERE id=?";
		SqlParameter sp = new SqlParameter();
		sp.setInt(ids);
		return jdbc.getList(sql, ResourceGkPkLog.class, sp);
	}

	public void insertGkPkLog(ResourceGkPkLog log) {
		String sql = "INSERT INTO resource_gk_pk_log VALUES(?,?,?)";
		SqlParameter sp=new SqlParameter();
		sp.setInt(log.getId());
		sp.setInt(log.getGk());
		sp.setInt(log.getStartLevel());
		jdbc.update(sql, sp);
	}

	public void updateGkPkLog(ResourceGkPkLog log) {
		String sql = "UPDATE resource_gk_pk_log SET start_level=? WHERE id=? and gk=?";
		SqlParameter sp = new SqlParameter();
		sp.setInt(log.getStartLevel());
		sp.setInt(log.getId());
		sp.setInt(log.getGk());
		jdbc.update(sql, sp);
	}

	public ResourceUserInfo getInfo(String userId) {
		return null;
	}

	public int getMaxIds() {
		String sql = "SELECT MAX(id) FROM resource_gk";
		return jdbc.getInt(sql, new SqlParameter());
	}

	public List<ResourceStartConfig> getStartConfigs() {
		String sql = "SELECT * FROM resource_start_config";
		return jdbc.getList(sql, ResourceStartConfig.class);
	}

	public List<ResourceGkConfig> getGkConfigs() {
		String sql = "SELECT * FROM resource_gk_config";
		return jdbc.getList(sql, ResourceGkConfig.class);
	}

	@Override
	public ResourceGkConfig getGkConfig(int type, int dif, int g,int start) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getStartByCurr(int curr) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void replace() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset(int id) {
		String sql = "DELETE FROM resource_gk_pk_log WHERE id=?";
		String sql2 = "DELETE FROM resource_gk_start WHERE id=?";
		SqlParameter sp = new SqlParameter();
		sp.setInt(id);
		jdbc.update(sql, sp);
		jdbc.update(sql2, sp);
	}
}
