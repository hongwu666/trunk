package com.lodogame.game.dao.impl.mysql;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.EquipRefineDao;
import com.lodogame.model.EquipRefine;

public class EquipRefineDaoMysqlImpl implements EquipRefineDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public EquipRefine getEquipRefine(String userId, String userEquipId, int type) {
		throw new NotImplementedException();
	}

	@Override
	public boolean addEquipRefine(EquipRefine equipRefine) {
		return this.jdbc.insert(equipRefine) > 0;
	}

	@Override
	public boolean updateEquipRefine(EquipRefine equipRefine) {
		String sql = "update equip_refine set refine_level = ? where user_equip_id = ? and refine_point = ?";
		SqlParameter spm = new SqlParameter();
		spm.setInt(equipRefine.getRefineLevel());
		spm.setString(equipRefine.getUserEquipId());
		spm.setInt(equipRefine.getRefinePoint());
		return this.jdbc.update(sql, spm) > 0;
	}

	@Override
	public List<EquipRefine> getUserEquipRefineList(String userId) {

		String sql = "select * from equip_refine where user_id = ?";
		SqlParameter spm = new SqlParameter();
		spm.setString(userId);
		return this.jdbc.getList(sql, EquipRefine.class, spm);

	}

}
