package com.lodogame.game.dao;

import com.lodogame.model.SystemFragment;

public interface SystemFragmentDao {
	/**
	 * 根据碎片道具id获得碎片
	 * @param fragmentId
	 * @return
	 */
	public SystemFragment getByFragmentId(int fragmentId);
	/**
	 * 根据碎片星级获得万能碎片
	 * @param star
	 * @return
	 */
	public SystemFragment getByStar(int star,int mergedToolType);

}
