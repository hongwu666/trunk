package com.lodogame.game.dao.impl.mysql;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.EmpireDao;
import com.lodogame.model.Empire;
import com.lodogame.model.EmpireEnemy;
import com.lodogame.model.EmpireGain;
import com.lodogame.model.EmpireNum;

public class EmpireDaoMysqlImpl implements EmpireDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public boolean deleteEmpire(String userId, int floor, int pos) {
		String sql = "DELETE FROM empire WHERE floor = ? AND pos = ? AND user_id = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(floor);
		parameter.setInt(pos);
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public boolean addEmpire(List<Empire> empireList) {

		this.jdbc.insert(empireList);

		return true;
	}

	@Override
	public List<Empire> getEmpireHero(int floor, int pos) {
		String sql = "SELECT * FROM empire WHERE floor = ? AND pos = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(floor);
		parameter.setInt(pos);

		return this.jdbc.getList(sql, Empire.class, parameter);
	}
	@Override
	public Empire getEmpireUser(int floor, int pos) {
		String sql = "SELECT * FROM empire WHERE floor = ? AND pos = ? LIMIT 1";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(floor);
		parameter.setInt(pos);

		return this.jdbc.get(sql, Empire.class, parameter);
	}
	@Override
	public List<Empire> getEmpire(int floor) {
		String sql = "SELECT * FROM empire WHERE floor = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(floor);

		return this.jdbc.getList(sql, Empire.class, parameter);
	}

	@Override
	public List<Integer> getEmpireFloor(String userId) {
		String sql = "SELECT floor FROM empire WHERE user_id = ? GROUP BY floor";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		
		return this.jdbc.getList(sql, Integer.class, parameter);
	}

	@Override
	public List<Empire> getEmpireAll() {
		String sql = "SELECT * FROM empire group by floor , pos , user_id";

		SqlParameter parameter = new SqlParameter();

		return this.jdbc.getList(sql, Empire.class, parameter);
	}

	@Override
	public void updateNextCount(int floor,int pos) {
		String sql = "UPDATE empire SET next_count_time = DATE_ADD(next_count_time,INTERVAL 30 MINUTE) WHERE floor = ? AND pos = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(floor);
		parameter.setInt(pos);

		this.jdbc.update(sql, parameter);
	}

	@Override
	public boolean addEmpireGain(List<EmpireGain> empireGainList) {
		this.jdbc.insert(empireGainList);
		return true;
	}

	@Override
	public boolean deleteEmpireGain(String userId, int floor, int pos) {
		String sql = "DELETE FROM empire_gain WHERE floor = ? AND pos = ? AND user_id = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(floor);
		parameter.setInt(pos);
		parameter.setString(userId);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public List<EmpireGain> getEmpireGain(String userId, int floor, int pos) {
		String sql = "SELECT * FROM empire_gain WHERE floor = ? AND pos = ? AND user_id = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(floor);
		parameter.setInt(pos);
		parameter.setString(userId);

		return this.jdbc.getList(sql, EmpireGain.class, parameter);
	}

	@Override
	public void addEmpireEnemy(EmpireEnemy empireEnemy) {
		this.jdbc.insert(empireEnemy);
	}

	@Override
	public boolean deleteEmpireEnemy(String userIdA) {
		String sql = "DELETE FROM empire_enemy WHERE user_id_a = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userIdA);

		return this.jdbc.update(sql, parameter) > 0;
	}

	@Override
	public List<String> getEmpireEnemy(String userIdA) {
		String sql = "SELECT user_id_b FROM empire_enemy WHERE user_id_a = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userIdA);

		return this.jdbc.getList(sql, String.class,parameter);
	}

	@Override
	public EmpireNum getEmpireNum(String userId) {
		String sql = "SELECT * FROM empire_num WHERE user_id = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);

		return this.jdbc.get(sql, EmpireNum.class,parameter);
	}

	@Override
	public void updateEmpireNum(String userId,int num) {
		String sql = "INSERT INTO empire_num (user_id,num) values(?,?) ON DUPLICATE KEY UPDATE num=VALUES(num)";
		
		SqlParameter parameter = new SqlParameter();
		parameter.setString(userId);
		parameter.setInt(num);

		this.jdbc.update(sql, parameter);
		
	}

	@Override
	public void clearEmpireNum() {	
		String sql = "DELETE FROM empire_num";

		SqlParameter parameter = new SqlParameter();

		this.jdbc.update(sql, parameter);
	}

	@Override
	public Empire getEmpireUser(int floor, String userId) {
		String sql = "SELECT * FROM empire WHERE floor = ? AND user_id = ? GROUP BY user_id LIMIT 1";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(floor);
		parameter.setString(userId);

		return this.jdbc.get(sql, Empire.class, parameter);
	}

	@Override
	public Set<String> getEmpireUser(int floor) {
		Set<String> set=new HashSet<String>();
		String sql = "SELECT user_id FROM empire WHERE floor = ? GROUP BY user_id ";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(floor);
		
		List<String> list=this.jdbc.getList(sql, String.class, parameter);
		set.addAll(list);
		
		return set;
	}

	@Override
	public List<Empire> getEmpireHero(int floor, String userId) {
		String sql = "SELECT * FROM empire WHERE floor = ? AND user_id = ?";

		SqlParameter parameter = new SqlParameter();
		parameter.setInt(floor);
		parameter.setString(userId);

		return this.jdbc.getList(sql, Empire.class, parameter);
	}

}
