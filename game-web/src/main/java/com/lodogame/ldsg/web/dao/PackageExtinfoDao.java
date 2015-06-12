package com.lodogame.ldsg.web.dao;

import com.lodogame.ldsg.web.model.PackageExtinfo;

public interface PackageExtinfoDao {
	public PackageExtinfo getByVersion(String version, String partnerId);
}
