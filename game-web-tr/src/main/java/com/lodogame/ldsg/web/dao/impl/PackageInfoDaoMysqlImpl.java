package com.lodogame.ldsg.web.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.common.jdbc.SqlParameter;
import com.lodogame.ldsg.web.dao.PackageInfoDao;
import com.lodogame.ldsg.web.model.PackageInfo;

public class PackageInfoDaoMysqlImpl implements PackageInfoDao {

	@Autowired
	private Jdbc jdbc;

	public final static String table = "package_info";

	public final static String columns = "*";

	@Override
	public boolean add(PackageInfo packageInfo) {
		return this.jdbc.insert(packageInfo) > 0;
	}

	@Override
	public PackageInfo getLast(int pkgType, String partnerId) {
		String sql = "SELECT " + columns + " FROM " + table + " WHERE pkg_type = ? and partner_id = ? ORDER BY ID DESC LIMIT 1";
		SqlParameter paramter = new SqlParameter();
		paramter.setInt(pkgType);
		paramter.setString(partnerId);
		return this.jdbc.get(sql, PackageInfo.class, paramter);
	}

	@Override
	public PackageInfo getLastByTest(int pkgType, int isTest, String partnerId) {
		String sql = "SELECT " + columns + " FROM " + table + " WHERE pkg_type = ? and is_test = ? and partner_id = ? ORDER BY ID DESC LIMIT 1";
		SqlParameter paramter = new SqlParameter();
		paramter.setInt(pkgType);
		paramter.setInt(isTest);
		paramter.setString(partnerId);
		return this.jdbc.get(sql, PackageInfo.class, paramter);
	}

}
