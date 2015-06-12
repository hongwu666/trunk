package com.lodogame.game.dao.impl.mysql;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.ExDao;
import com.lodogame.game.utils.TableUtils;
import com.lodogame.model.ExpeditionConfig;
import com.lodogame.model.ExpeditionMaxExId;
import com.lodogame.model.ExpeditionNum;
import com.lodogame.model.UserExInfo;
import com.lodogame.model.UserExpeditionHero;
import com.lodogame.model.UserExpeditionVsTable;

public class ExDaoMysqlImpl implements ExDao {
	@Autowired
	private Jdbc jdbc;

	public List<UserExpeditionHero> getHeros(String userId) {
		String sql = "select * from " + TableUtils.getExHeroTable(userId) + " where user_id=?";
		SqlParameter sp = new SqlParameter();
		sp.setString(userId);
		return jdbc.getList(sql, UserExpeditionHero.class, sp);
	}

	public List<UserExpeditionHero> getHerosByExId(String userId, long exId) {
		String sql = "select * from " + TableUtils.getExHeroTable(userId) + " where ex_id=?";
		SqlParameter sp = new SqlParameter();
		sp.setLong(exId);
		return jdbc.getList(sql, UserExpeditionHero.class, sp);
	}

	public List<UserExpeditionVsTable> getVs(String userId) {
		String sql = "select * from user_expedition_vs_table where user_id=?";
		SqlParameter sp = new SqlParameter();
		sp.setString(userId);
		return jdbc.getList(sql, UserExpeditionVsTable.class, sp);
	}

	public void updateHero(String userId, List<UserExpeditionHero> heos) {
		// TODO 为什么不批量插入
		for (UserExpeditionHero temp : heos) {
			String sql = "update " + TableUtils.getExHeroTable(userId) + " set pos=?,life=?,rage=? where ex_id=? and user_hero_id=?";
			SqlParameter sp = new SqlParameter();
			sp.setInt(temp.getPos());
			sp.setInt(temp.getLife());
			sp.setInt(temp.getRage());
			sp.setLong(temp.getExId());
			sp.setString(temp.getUserHeroId());
			jdbc.update(sql, sp);
		}
	}

	public void insertHero(String userId, List<UserExpeditionHero> heos) {
		String tab = TableUtils.getExHeroTable(userId);
		int[] it = jdbc.insert(tab, heos);
		int size = 0;
		for (int i : it)
			size += i;
		if (size != heos.size()) {
			throw new IllegalArgumentException("insert ExpeditionHero fal [" + userId + " " + heos.size() + ":" + size + "];");
		}
	}

	public void deleteHeroAll(String userId, Set<Long> exIds) {
		// TODO 为什么不批量删除
		String tab = TableUtils.getExHeroTable(userId);
		for (long temp : exIds) {
			if (temp == -1L) {
				continue;
			}
			String sql = "delete from " + tab + " where ex_id=?";
			SqlParameter sp = new SqlParameter();
			sp.setLong(temp);
			jdbc.update(sql, sp);
		}
		String sql = "delete from " + tab + " where ex_id=? and user_id=?";
		SqlParameter sp = new SqlParameter();
		sp.setLong(-1L);
		sp.setString(userId);
		jdbc.update(sql, sp);
	}

	public void updateVs(UserExpeditionVsTable vs) {
		String sql = "update user_expedition_vs_table set stat=? ,box_stat=? where ex_id=?";
		SqlParameter sp = new SqlParameter();
		sp.setInt(vs.getStat());
		sp.setInt(vs.getBoxStat());
		sp.setLong(vs.getExId());
		jdbc.update(sql, sp);
	}

	public void insertVs(UserExpeditionVsTable vs) {
		String sql = "INSERT INTO `user_expedition_vs_table`(`user_id`,`index`,`ex_id`,`stat`,`box_stat`) VALUES (?,?,?,?,?)";
		SqlParameter sp = new SqlParameter();
		sp.setString(vs.getUserId());
		sp.setInt(vs.getIndex());
		sp.setLong(vs.getExId());
		sp.setInt(vs.getStat());
		sp.setInt(vs.getBoxStat());
		jdbc.update(sql, sp);
		// jdbc.insert("user_expedition_vs_table", vs);
	}

	public void deleteVs(UserExpeditionVsTable vs) {
		String sql = "delete from user_expedition_vs_table where ex_id=?";
		SqlParameter sp = new SqlParameter();
		sp.setLong(vs.getExId());
		jdbc.update(sql, sp);
	}

	public void deleteVsAll(String userId) {
		String sql = "delete from user_expedition_vs_table where user_id=?";
		SqlParameter sp = new SqlParameter();
		sp.setString(userId);
		jdbc.update(sql, sp);
	}

	public UserExInfo getExInfo(String userId) {
		return null;
	}

	@Override
	public ExpeditionMaxExId getMaxExId() {
		String sql = "select max(ex_id) as ex_id from user_expedition_vs_table";
		ExpeditionMaxExId v = jdbc.get(sql, ExpeditionMaxExId.class, new SqlParameter());
		return v;
	}

	public long getExId() {
		return 0;
	}

	@Override
	public List<ExpeditionConfig> getLevelConfigs() {
		String sql = "select * from expedition_config";
		return jdbc.getList(sql, ExpeditionConfig.class);
	}

	@Override
	public ExpeditionConfig getLevelConfig(int indx) {
		return null;
	}

	@Override
	public ExpeditionNum getNum(String userId) {
		String sql = "select * from expedition_num where user_id=?";
		SqlParameter sp = new SqlParameter();
		sp.setString(userId);
		return jdbc.get(sql, ExpeditionNum.class, sp);
	}

	@Override
	public void clearNum() {
		String sql = "delete from expedition_num";
		jdbc.update(sql, new SqlParameter());
	}

	@Override
	public void insertNum(ExpeditionNum num) {
		String sql = "insert into expedition_num values(?,?)";
		SqlParameter sp = new SqlParameter();
		sp.setString(num.getUserId());
		sp.setInt(num.getNum());
		jdbc.update(sql, sp);
	}

	@Override
	public void updateNum(ExpeditionNum num) {
		String sql = "update expedition_num set num=? where user_id=?";
		SqlParameter sp = new SqlParameter();
		sp.setInt(num.getNum());
		sp.setString(num.getUserId());
		jdbc.update(sql, sp);
	}

	public void updateMyHero(String userId, List<UserExpeditionHero> heos) {
		for (UserExpeditionHero temp : heos) {
			String sql = "update " + TableUtils.getExHeroTable(userId) + " set pos=?,life=?,rage=? where ex_id=? and user_id=?";
			SqlParameter sp = new SqlParameter();
			sp.setInt(temp.getPos());
			sp.setInt(temp.getLife());
			sp.setInt(temp.getRage());
			sp.setLong(temp.getExId());
			sp.setString(temp.getUserId());
			jdbc.update(sql, sp);
		}
	}

	@Override
	public void clearCache(String userId) {

	}
}
