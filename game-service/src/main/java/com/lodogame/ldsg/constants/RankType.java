package com.lodogame.ldsg.constants;


public enum RankType {

	ECTYPE_STAR(1),//副本星数榜
	ELITE(2),//精英副本进度
	STORY(3),//剧情副本进度
	ONLYONE(4),//千人斬
	ATHLETIC(5),//竞技场排行榜
	HERO_POWER(6),//英雄战力榜
	POWER(7),//总战力榜	
	LEVEL(8),//等级榜
	VIP(9),//VIP等级榜
	DRAW(10),//抽奖榜
	RESOURCE_STAR(11),//资源星数榜
	PAY_WEEK(12);//周充值榜
	private int value;

	RankType(int value) {
		this.value = value;
	}
	public int getValue(){
		return value;
	}
	public static boolean constantValue(int value){
		for(RankType rankType:RankType.values()){
			if(rankType.getValue()==value)
			return true;
		}
		return false;
	}
	public static RankType getRankTypeByValue(int value){
		for(RankType rankType:RankType.values()){
			if(rankType.getValue()==value)
			return rankType;
		}
		return null;
	}
}
