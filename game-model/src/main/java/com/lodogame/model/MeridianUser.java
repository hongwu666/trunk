package com.lodogame.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MeridianUser {
	private Map<String, Map<Integer, List<MeridianUserInfo>>> hero = new ConcurrentHashMap<String, Map<Integer, List<MeridianUserInfo>>>();
	private String userId;
	private MeridianUserInfo lastCheck;

	public List<MeridianUserInfo> getAll() {
		List<MeridianUserInfo> list = new ArrayList<MeridianUserInfo>();
		for (Map<Integer, List<MeridianUserInfo>> temp : hero.values()) {
			for (List<MeridianUserInfo> temps : temp.values()) {
				list.addAll(temps);
			}
		}
		return list;
	}

	public MeridianUser(List<MeridianUserInfo> info, String userId) {
		super();
		this.userId = userId;
		for (MeridianUserInfo temp : info) {
			if (!hero.containsKey(temp.getUserHeroId())) {
				hero.put(temp.getUserHeroId(), new ConcurrentHashMap<Integer, List<MeridianUserInfo>>());
			}
			if (!hero.get(temp.getUserHeroId()).containsKey(temp.getMeridianType())) {
				hero.get(temp.getUserHeroId()).put(temp.getMeridianType(), new ArrayList<MeridianUserInfo>());
			}
			hero.get(temp.getUserHeroId()).get(temp.getMeridianType()).add(temp);
		}
	}

	public void addMeridian(MeridianUserInfo info) {
		Map<Integer, List<MeridianUserInfo>> map = hero.get(info.getUserHeroId());
		if (map == null) {
			map = new ConcurrentHashMap<Integer, List<MeridianUserInfo>>();
			hero.put(info.getUserHeroId(), map);
		}
		List<MeridianUserInfo> list = map.get(info.getMeridianType());
		if (list == null) {
			list = new ArrayList<MeridianUserInfo>();
			map.put(info.getMeridianType(), list);
		}
		list.add(info);
	}

	public boolean isMax(String heroId) {
		Map<Integer, List<MeridianUserInfo>> map = hero.get(heroId);
		if (map == null)
			return false;
		int size = 0;
		for (int temp : map.keySet()) {
			List<MeridianUserInfo> info = map.get(temp);
			for (MeridianUserInfo te : info) {
				if (te.getLevel() > 0) {
					size++;
					break;
				}
			}
		}
		return size >= 4;
	}

	/** 是否打开过 */
	public boolean isOpen(int type, int node, String heroId) {
		Map<Integer, List<MeridianUserInfo>> map = hero.get(heroId);
		if (map == null || map.size() == 0)
			return false;
		List<MeridianUserInfo> info = map.get(type);
		if (info == null || info.size() == 0) {
			return false;
		}
		for (MeridianUserInfo temp : info) {
			if (temp.getMeridianType() == type && temp.getMeridianNode() == node && temp.getLevel() >= 1) {
				lastCheck = temp;
				return true;
			}
		}
		return false;
	}

	public Map<Integer, List<MeridianUserInfo>> getHeroMeridian(String heroId) {
		return hero.get(heroId);
	}

	public int getOpenSize(int type, String heroId) {
		Map<Integer, List<MeridianUserInfo>> map = hero.get(heroId);
		if (map == null || map.size() == 0)
			return 0;
		List<MeridianUserInfo> list = map.get(type);
		if (list == null) {
			return 0;
		}
		int num=0;
		for(MeridianUserInfo meridianUserInfo:list){
			if (meridianUserInfo.getLevel()>0) {
				num++;
			}
		}
		return num;
	}

	public MeridianUserInfo getInfo(int type, int node, String heroId) {
		if (lastCheck != null && lastCheck.getUserHeroId().equals(heroId) && lastCheck.getMeridianNode() == node && lastCheck.getMeridianType() == type) {
			return lastCheck;
		}

		Map<Integer, List<MeridianUserInfo>> map = hero.get(heroId);
		if (map == null || map.size() == 0)
			return null;
		List<MeridianUserInfo> info = map.get(type);
		if (info == null || info.size() == 0) {
			return null;
		}
		for (MeridianUserInfo temp : info) {
			if (temp.getMeridianType() == type && temp.getMeridianNode() == node) {
				return temp;
			}
		}
		return null;
	}

	public boolean getTaiJi(String heroId) {
		return isOpen(-1, -1, heroId);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
