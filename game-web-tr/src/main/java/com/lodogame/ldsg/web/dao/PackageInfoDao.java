package com.lodogame.ldsg.web.dao;

import com.lodogame.ldsg.web.model.PackageInfo;

/**
 * 
 * @author jacky
 * 
 */
public interface PackageInfoDao {

	/**
	 * 添加一个包信息
	 * 
	 * @param packageInfo
	 * @return
	 */
	public boolean add(PackageInfo packageInfo);

	/**
	 * 根据是否测试 获取最后一个
	 * 
	 * @param pkgType
	 * @param isTest
	 * @return
	 */
	PackageInfo getLastByTest(int pkgType, int isTest, String partnerId);

	/**
	 * 根据类型获取最后一个
	 * 
	 * @param pkgType
	 * @return
	 */
	PackageInfo getLast(int pkgType, String partnerId);

}
