package com.lodogame.game.dao.impl.mysql;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.game.dao.EquipEnchantDao;
import com.lodogame.model.EquipEnchant;

public class EquipEnchantDaoMysqlImpl implements EquipEnchantDao {

	@Autowired
	private Jdbc jdbc;

	@Override
	public EquipEnchant getEquipEnchant(String userId, String userEquipId) {
		String sql = "select * from equip_enchant where user_id = ? and user_equip_id = ? ";

		SqlParameter spm = new SqlParameter();
		spm.setString(userId);
		spm.setString(userEquipId);

		return this.jdbc.get(sql, EquipEnchant.class, spm);
	}

	@Override
	public List<EquipEnchant> getEquipEnchantList(String userId) {

		String sql = "select * from equip_enchant where user_id = ?";

		SqlParameter spm = new SqlParameter();
		spm.setString(userId);

		return this.jdbc.getList(sql, EquipEnchant.class, spm);
	}

	@Override
	public boolean updateEquipEnchant(EquipEnchant equipEnchant) {

		String sql = "insert into equip_enchant (user_equip_id,user_id,equip_id,cur_property,en_property,created_time) values(?,?,?,?,?,?) on duplicate key update en_property=values(en_property)";
		SqlParameter spm = new SqlParameter();
		spm.setString(equipEnchant.getUserEquipId());
		spm.setString(equipEnchant.getUserId());
		spm.setInt(equipEnchant.getEquipId());
		spm.setString(equipEnchant.getCurProperty());
		spm.setString(equipEnchant.getEnProperty());
		spm.setDate(new Date());
		return this.jdbc.update(sql, spm) > 0;

	}

	@Override
	public boolean updateEnchantProperty(String userId, String userEquipId, String curProperty) {

		String sql = "update equip_enchant set en_property = ?, cur_property = ? where user_equip_id = ?";

		SqlParameter spm = new SqlParameter();
		spm.setString("");
		spm.setString(curProperty);
		spm.setString(userEquipId);
		return this.jdbc.update(sql, spm) > 0;

	}

}
