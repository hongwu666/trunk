package com.lodogame.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lodogame.model.log.GemAltarUserSellLog;

public class GemAltarUserInfo {
	private List<GemAltarUserPack> pack = new ArrayList<GemAltarUserPack>();
	private Map<Integer, GemAltarUserOpen> open = new ConcurrentHashMap<Integer, GemAltarUserOpen>();
	private List<GemAltarUserAutoSell> sell = new ArrayList<GemAltarUserAutoSell>();
	private List<GemAltarUserSellLog> logs = new LinkedList<GemAltarUserSellLog>();
	
	public GemAltarUserInfo() {
		super();
	}
	
	public List<GemAltarUserSellLog> getLogs() {
		return logs;
	}

	public void addLog(GemAltarUserSellLog log){

		if(logs.size() < 50){
			logs.add(0, log);
		}else{
			logs.add(0, log);
			logs.remove(logs.size()-1);
		}
	}
	
	public void removeAuto(int level){
		GemAltarUserAutoSell l = haveAuto(level);
		sell.remove(l);
	}
	
	public void addAuto(GemAltarUserAutoSell auto){
		sell.add(auto);
	}
	
	public List<GemAltarUserAutoSell> getSell() {
		return sell;
	}

	public void setSell(List<GemAltarUserAutoSell> sell) {
		this.sell = sell;
	}
	
	public GemAltarUserAutoSell haveAuto(int level){
		for(GemAltarUserAutoSell temp : sell){
			if(temp.getLevels()==level){
				return temp;
			}
		}
		return null;
	}



	public GemAltarUserPack getPack(int stoneId,Date date){
		for(GemAltarUserPack temp : pack){
			if(temp.getStoneId() == stoneId && temp.getCreateTime().getTime() ==date.getTime()){
				return temp;
			}
		}
		return null;
	}
	
	public void removeAllPack(){
		pack.clear();
	}
	
	public void addOpen(GemAltarUserOpen open){
		this.open.put(open.getGroups(), open);
	}
	
	public void addPack(GemAltarUserPack p){
		pack.add(p);
	}
	
	public void removeGroup(int groups){
		open.remove(groups);
	}
	
	public boolean removePack(GemAltarUserPack pack){
		return this.pack.remove(pack);
	}
	
	public boolean haveGroup(int groups){
		return open.containsKey(groups);
	}

	public GemAltarUserInfo(List<GemAltarUserPack> pack, List<GemAltarUserOpen> open,List<GemAltarUserAutoSell> auto,List<GemAltarUserSellLog> logs) {
		super();
		this.pack = pack;
		for(GemAltarUserOpen temp : open){
			this.open.put(temp.getGroups(), temp);
		}
		sell = auto;
		this.logs = logs;
	}

	public List<GemAltarUserPack> getPack() {
		return pack;
	}

	public void setPack(List<GemAltarUserPack> pack) {
		this.pack = pack;
	}

	public List<GemAltarUserOpen> getOpen() {
		return new ArrayList<GemAltarUserOpen>(open.values());
	}

}
