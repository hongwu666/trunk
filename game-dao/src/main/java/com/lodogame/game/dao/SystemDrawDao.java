package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.SystemDraw;

public interface SystemDrawDao {

	public List<SystemDraw> getList();

	public SystemDraw get(int id);

}
