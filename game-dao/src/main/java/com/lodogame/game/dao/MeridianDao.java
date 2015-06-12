package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.MeridianUser;
import com.lodogame.model.MeridianUserInfo;

public interface MeridianDao {

	public MeridianUser getUserMeridian(String userId);

	public List<MeridianUserInfo> getMeridianUserInfo(String userId);

	public void insertMeridianUserInfo(MeridianUserInfo info);

	public void updateMeridianUserInfo(MeridianUserInfo info);

}
