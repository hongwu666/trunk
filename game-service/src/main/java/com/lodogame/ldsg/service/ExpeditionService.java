package com.lodogame.ldsg.service;

import java.util.List;
import java.util.Map;

import com.lodogame.ldsg.bo.ExpeditionShowBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.event.EventHandle;

public interface ExpeditionService {

	int BOX_NO_OPEN = 3000;
	int NO_REPLACE_NUM = 3001;
	int NO_YB = 3002;
	int NO_HERO = 3003;
	int HAS_LIKE = 3004;
	int NO_LIFE = 3005;
	
	ExpeditionShowBO show(String userId);
	void fight(String userId, long exId, EventHandle eventHandle);

	List<UserHeroBO> showHero(String userId, long exId);

	Map<Integer, Object> getGift(String userId, long exId);


	void replace(String userId);
	void changePos(String userId,String hId1, String hId2, int pos1, int pos2) ;
}
