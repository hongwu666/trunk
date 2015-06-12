package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.GemAltarDao;
import com.lodogame.game.utils.TableUtils;
import com.lodogame.model.GemAltarGroupsOpenConfig;
import com.lodogame.model.GemAltarOpenConfig;
import com.lodogame.model.GemAltarUserAutoSell;
import com.lodogame.model.GemAltarUserInfo;
import com.lodogame.model.GemAltarUserOpen;
import com.lodogame.model.GemAltarUserPack;
import com.lodogame.model.log.GemAltarUserSellLog;

public class GemAltarDaoMySqlImpl implements GemAltarDao{
	
	@Autowired
	private Jdbc jdbc;

	public List<GemAltarGroupsOpenConfig> getGroupsOpenConfigs() {
		String sql = "SELECT * FROM gem_altar_groups_open_config";
		return jdbc.getList(sql, GemAltarGroupsOpenConfig.class);
	}

	public List<GemAltarOpenConfig> getOpenConfigs() {
		String sql = "SELECT * FROM gem_altar_open_config";
		return jdbc.getList(sql, GemAltarOpenConfig.class);
	}

	public GemAltarUserInfo getUserInfo(String userId) {
		return null;
	}

	public List<GemAltarUserOpen> getUserOpen(String userId) {
		String sql = "SELECT * FROM gem_altar_user_open where user_id=?";
		SqlParameter sp = new SqlParameter();
		sp.setString(userId);
		return jdbc.getList(sql, GemAltarUserOpen.class,sp);
	}

	public List<GemAltarUserPack> getUserPack(String userId) {
		String sql = "SELECT * FROM gem_altar_user_pack where user_id=?";
		SqlParameter sp = new SqlParameter();
		sp.setString(userId);
		return jdbc.getList(sql, GemAltarUserPack.class,sp);
	}

	public List<GemAltarOpenConfig> getOpenConfig(int groups) {
		return null;
	}

	@Override
	public void removeGroups(String userId, int groups) {
		String sql = "delete from gem_altar_user_open where user_id=? and groups=?";
		SqlParameter sp = new SqlParameter();
		sp.setString(userId);
		sp.setInt(groups);
		jdbc.update(sql, sp);
	}

	@Override
	public void addGroups(String userId, int groups) {
		String sql = "insert into gem_altar_user_open values(?,?)";
		SqlParameter sp = new SqlParameter();
		sp.setString(userId);
		sp.setInt(groups);
		jdbc.update(sql, sp);
	}

	@Override
	public void removePack(GemAltarUserPack pack) {
		String sql = "delete from gem_altar_user_pack where user_id=? and stone_id=? and create_time=?";
		SqlParameter sp = new SqlParameter();
		sp.setString(pack.getUserId());
		sp.setInt(pack.getStoneId());
		sp.setDate(pack.getCreateTime());
		jdbc.update(sql, sp);
	}

	@Override
	public void addPack(GemAltarUserPack pack) {
		String sql = "insert into gem_altar_user_pack values(?,?,?)";
		SqlParameter sp = new SqlParameter();
		sp.setString(pack.getUserId());
		sp.setInt(pack.getStoneId());
		sp.setDate(pack.getCreateTime());
		jdbc.update(sql, sp);
	}

	public GemAltarGroupsOpenConfig getGroupsConfig(int groups) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<GemAltarUserAutoSell> getAuto(String userId) {
		String sql = "select * from gem_altar_user_auto_sell where user_id=?";
		SqlParameter sp = new SqlParameter();
		sp.setString(userId);
		return jdbc.getList(sql, GemAltarUserAutoSell.class,sp);
	}

	public void addAuto(String userId, int level) {
		String sql = "insert into gem_altar_user_auto_sell values(?,?)";
		SqlParameter sp = new SqlParameter();
		sp.setString(userId);
		sp.setInt(level);
		jdbc.update(sql, sp);
	}

	public void removeAuto(String userId, int level) {
		String sql = "delete from gem_altar_user_auto_sell where user_id=? and levels=?";
		SqlParameter sp = new SqlParameter();
		sp.setString(userId);
		sp.setInt(level);
		jdbc.update(sql, sp);
	}

	@Override
	public void addLog(GemAltarUserSellLog log) {
		String sql = "insert into " + TableUtils.getGemAltarLog(log.getUserId()) + " values(?,?,?,?)";
		SqlParameter sp = new SqlParameter();
		sp.setString(log.getUserId());
		sp.setInt(log.getStoneId());
		sp.setInt(log.getGold());
		sp.setDate(log.getCreateTime());
		jdbc.update(sql, sp);
	}

	@Override
	public List<GemAltarUserSellLog> getUserLog(String userId) {
		String sql = "select * from " + TableUtils.getGemAltarLog(userId)+" where user_id=? order by 	create_time DESC limit 50";
		SqlParameter sp = new SqlParameter();
		sp.setString(userId);
		return jdbc.getList(sql, GemAltarUserSellLog.class,sp);
	}

	public void removeAllPack(String userId) {
		String sql = "delete from gem_altar_user_pack where user_id=?";
		SqlParameter sp = new SqlParameter();
		sp.setString(userId);
		jdbc.update(sql, sp);
	}

}
