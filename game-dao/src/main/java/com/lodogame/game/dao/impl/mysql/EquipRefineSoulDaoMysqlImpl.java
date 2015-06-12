package com.lodogame.game.dao.impl.mysql;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.EquipRefineSoulDao;
import com.lodogame.model.EquipRefineSoul;

public class EquipRefineSoulDaoMysqlImpl implements EquipRefineSoulDao {
	@Autowired
	private Jdbc jdbc;

	@Override
	public EquipRefineSoul getEquipRefineSoul(String userEquipId, int equipId) {
		String sql = "SELECT * FROM equip_refine_soul WHERE user_equip_id= ? AND equip_id= ? ";
		SqlParameter spm = new SqlParameter();
		spm.setString(userEquipId);
		spm.setInt(equipId);
		return this.jdbc.get(sql, EquipRefineSoul.class, spm);
	}

	@Override
	public boolean upEquipRefineSoul(String userEquipId, int equipId, int luck) {
		String sql = "update equip_refine_soul set luck=luck+ ? WHERE user_equip_id= ? AND equip_id= ? ";
		SqlParameter spm = new SqlParameter();
		spm.setInt(luck);
		spm.setString(userEquipId);
		spm.setInt(equipId);
		return this.jdbc.update(sql, spm)>0;
	}

	@Override
	public boolean addEquipRefineSoul(EquipRefineSoul equipRefineSoul) {
		
		return this.jdbc.insert(equipRefineSoul)>0;
	}

	@Override
	public void delEquipRefineSoul(String userEquipId, int equipId) {
		String sql = "delete from equip_refine_soul WHERE user_equip_id= ? AND equip_id= ? ";
		SqlParameter spm = new SqlParameter();
		spm.setString(userEquipId);
		spm.setInt(equipId);
		this.jdbc.update(sql, spm);
		
	}

}
