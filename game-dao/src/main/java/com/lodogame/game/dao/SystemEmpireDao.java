package com.lodogame.game.dao;

import com.lodogame.model.SystemEmpire;

public interface SystemEmpireDao {
	/**
	 * 获得每层的系统帝国宝库信息
	 * @param floor
	 * @return
	 */
	SystemEmpire getSystemEmpire(int floor);
}
