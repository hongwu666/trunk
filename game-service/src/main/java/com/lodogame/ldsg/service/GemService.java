package com.lodogame.ldsg.service;

import java.util.Map;

import com.lodogame.ldsg.bo.GemBO;

public interface GemService {
	int NO_GROUPS = 2001;	//对应祭坛未开启
	int NO_MONEY = 2002;	//没钱
	int NO_GIFT = 2003;	//没有奖励
	
	GemBO show(String userId);
	Map<Integer,Long> open(String userId,int groups);
	void getAllStone(String userId);
	void getStone(String userId,int stoneId,long date);
	void openAuto(String userId,int levels);
	void toPo(String userId);
	void delAtuo(String userId,int level);
}
