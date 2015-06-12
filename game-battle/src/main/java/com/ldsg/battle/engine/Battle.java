package com.ldsg.battle.engine;

import java.util.Map;

import com.ldsg.battle.bo.BattleInfo;

public interface Battle {

	/**
	 * 执行战斗
	 * 
	 * @param attackInfo
	 * @param defenseInfo
	 * @return
	 */
	public Map<String, Object> execute(int type, BattleInfo attackInfo, BattleInfo defenseInfo);
}
