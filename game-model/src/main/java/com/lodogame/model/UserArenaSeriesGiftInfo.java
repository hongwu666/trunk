package com.lodogame.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserArenaSeriesGiftInfo {
	private Map<Integer, UserArenaSeriesGift> gifts = new ConcurrentHashMap<Integer, UserArenaSeriesGift>();

	public UserArenaSeriesGiftInfo(List<UserArenaSeriesGift> gift){
		for(UserArenaSeriesGift temp : gift){
			gifts.put(temp.getWinCount(), temp);
		}
	}
	
	
	public UserArenaSeriesGiftInfo() {
		super();
	}

	public List<UserArenaSeriesGift> getList(){
		return new ArrayList<UserArenaSeriesGift>(gifts.values());
	}
	
	public UserArenaSeriesGift get(int v){
		return gifts.get(v);
	}
	
	public boolean have(int v){
		return gifts.containsKey(v);
	}
	
	public void add(UserArenaSeriesGift gift){
		gifts.put(gift.getWinCount(), gift);
	}
}
