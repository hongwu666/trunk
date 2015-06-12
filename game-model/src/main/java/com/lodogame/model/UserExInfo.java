package com.lodogame.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class UserExInfo {
	private List<UserExpeditionVsTable> tables = new ArrayList<UserExpeditionVsTable>();

	private Map<Long, List<UserExpeditionHero>> heros = new TreeMap<Long, List<UserExpeditionHero>>();	//-1 my
	
	private String userId;
	
	public UserExInfo(String userId,List<UserExpeditionVsTable> tabs, List<UserExpeditionHero> hes) {
		this.userId=userId;
		tables = tabs;
		Collections.sort(tables, new TableSort());
		for (UserExpeditionHero temp : hes) {
			if (!heros.containsKey(temp.getExId())) {
				heros.put(temp.getExId(), new ArrayList<UserExpeditionHero>());
			}
			heros.get(temp.getExId()).add(temp);
		}
	}
	
	public UserExpeditionHero getMyHeroById(String userId){
		List<UserExpeditionHero> heros = getHero(-1L);
		for(UserExpeditionHero temp : heros)
			if(temp.getUserHeroId().equals(userId))
				return temp;
		return null;
	}
	
	public Set<Long> getAllExId(){
		return heros.keySet();
	}
	
	public void addTempHero(long exId,List<UserExpeditionHero> heros){
		this.heros.put(exId,heros);
	}

	public void addNewVs(UserExpeditionVsTable vs,List<UserExpeditionHero> heros){
		tables.add(vs);
		this.heros.put(vs.getExId(),heros);
	}
	public void addMyHero(List<UserExpeditionHero> heros){
		this.heros.put(-1L,heros);
	}
	
	public List<UserExpeditionHero> getHero(long exId) {
		return heros.get(exId);
	}
	
	public void addNewMyHero(List<UserExpeditionHero> list){
		 heros.get(-1L).addAll(list);
	}
	
	public List<UserExpeditionHero> getMyHeroInPk(){
		List<UserExpeditionHero> list = new ArrayList<UserExpeditionHero>();
		List<UserExpeditionHero> hero = heros.get(-1L);
		for(UserExpeditionHero temp : hero)
			if(temp.getPos()>0)
				list.add(temp);
		return list;
	}
	
	public List<UserExpeditionVsTable> getVsTables(){
		return tables;
	}

	public String getUserId() {
		return userId;
	}

	public UserExpeditionVsTable getLastVsTable() {
		if(tables.size()<=0){
			return null;
		}
		return tables.get(tables.size()-1);
	}
	
	public UserExpeditionVsTable getVsTable(long exId){
		for(UserExpeditionVsTable temp: tables){
			if(temp.getExId()==exId)
				return temp;
		}
		return null;
	}
	
	public boolean noHaveVs(){
		return tables.size()==0;
	}
}

class TableSort implements Comparator<UserExpeditionVsTable> {

	public int compare(UserExpeditionVsTable o1, UserExpeditionVsTable o2) {
		if (o1.getExId() < o2.getExId()) {
			return -1;
		} else if (o1.getExId() > o2.getExId()) {
			return 1;
		}
		return 0;
	}

}