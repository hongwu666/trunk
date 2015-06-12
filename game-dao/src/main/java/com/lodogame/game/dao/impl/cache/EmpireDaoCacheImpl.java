package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lodogame.game.dao.EmpireDao;
import com.lodogame.game.dao.impl.mysql.EmpireDaoMysqlImpl;
import com.lodogame.model.Empire;
import com.lodogame.model.EmpireEnemy;
import com.lodogame.model.EmpireGain;
import com.lodogame.model.EmpireNum;

public class EmpireDaoCacheImpl implements EmpireDao {

	private EmpireDaoMysqlImpl empireDaoMysqlImpl;

	public void setEmpireDaoMysqlImpl(EmpireDaoMysqlImpl empireDaoMysqlImpl) {
		this.empireDaoMysqlImpl = empireDaoMysqlImpl;
	}

	private Map<Integer, Map<Integer, List<Empire>>> map = new HashMap<Integer, Map<Integer, List<Empire>>>();
	private Map<Integer, List<Empire>> posmap = new HashMap<Integer, List<Empire>>();
	private List<Empire> posList = new ArrayList<Empire>();
	
	private Map<Integer ,Set<String>> floorUser=new HashMap<Integer, Set<String>>();
	private Set<String> userIdSet=new HashSet<String>();

	@Override
	public boolean deleteEmpire(String userId, int floor, int pos) {
		if (map.containsKey(floor) && map.get(floor).containsKey(pos)) {
			map.get(floor).remove(pos);
		}
		if(floorUser.containsKey(floor)){
			floorUser.get(floor).remove(userId);
		}
		return this.empireDaoMysqlImpl.deleteEmpire(userId, floor, pos);
	}

	@Override
	public boolean addEmpire(List<Empire> empireList) {
		int floor = 0;
		int pos = 0;
		String userId = "";
		for (Empire empire : empireList) {
			floor = empire.getFloor();
			pos = empire.getPos();
			userId = empire.getUserId();
			break;
		}
		if(!floorUser.containsKey(floor)){
			userIdSet.clear();
			userIdSet.add(userId);
			floorUser.put(floor, userIdSet);
		}else{
			floorUser.get(floor).add(userId);
		}
		if (map.containsKey(floor)) {
			map.get(floor).put(pos, empireList);
		} else {
			Map<Integer, List<Empire>> posmap = new HashMap<Integer, List<Empire>>();
			posmap.put(pos, empireList);
			map.put(floor, posmap);
		}
		return this.empireDaoMysqlImpl.addEmpire(empireList);
	}

	@Override
	public List<Empire> getEmpireHero(int floor, int pos) {
		if (map.containsKey(floor) && map.get(floor).containsKey(pos)) {
			return map.get(floor).get(pos);
		} else if (map.containsKey(floor)) {
			List<Empire> list = this.empireDaoMysqlImpl.getEmpireHero(floor, pos);
			map.get(floor).put(pos, list);
		} else {
			List<Empire> list = this.empireDaoMysqlImpl.getEmpireHero(floor, pos);
			Map<Integer, List<Empire>> posmap = new HashMap<Integer, List<Empire>>();
			posmap.put(pos, list);
			map.put(floor, posmap);
		}
		return map.get(floor).get(pos);
	}

	@Override
	public List<Empire> getEmpire(int floor) {
		if (map.get(floor) == null) {
			posmap.clear();
			List<Empire> floorList = this.empireDaoMysqlImpl.getEmpire(floor);
			for (Empire empire : floorList) {
				if (posmap.get(empire.getPos()) == null) {
					posList.clear();
					posList.add(empire);
					posmap.put(empire.getPos(), posList);
				} else {
					posmap.get(empire.getPos()).add(empire);
				}
			}
			map.put(floor, posmap);
		}
		List<Empire> list = new ArrayList<Empire>();
		for (Integer pos : map.get(floor).keySet()) {
			list.addAll(map.get(floor).get(pos));
		}
		return list;
	}

	@Override
	public List<Integer> getEmpireFloor(String userId) {

		return null;
	}

	@Override
	public List<Empire> getEmpireAll() {

		return this.empireDaoMysqlImpl.getEmpireAll();
	}

	@Override
	public void updateNextCount(int floor, int pos) {

		this.empireDaoMysqlImpl.updateNextCount(floor, pos);
	}

	@Override
	public boolean addEmpireGain(List<EmpireGain> empireGainList) {
		// TODO Auto-generated method stub
		return this.empireDaoMysqlImpl.addEmpireGain(empireGainList);
	}

	@Override
	public boolean deleteEmpireGain(String userId, int floor, int pos) {
		
		return this.empireDaoMysqlImpl.deleteEmpireGain(userId, floor, pos);
	}

	@Override
	public List<EmpireGain> getEmpireGain(String userId, int floor, int pos) {
		return this.empireDaoMysqlImpl.getEmpireGain(userId, floor, pos);
	}

	@Override
	public Empire getEmpireUser(int floor, int pos) {

		return this.empireDaoMysqlImpl.getEmpireUser(floor, pos);
	}

	@Override
	public void addEmpireEnemy(EmpireEnemy empireEnemy) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean deleteEmpireEnemy(String userIdA) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> getEmpireEnemy(String userIdA) {

		return this.empireDaoMysqlImpl.getEmpireEnemy(userIdA);
	}

	@Override
	public EmpireNum getEmpireNum(String userId) {
		EmpireNum empireNum = this.empireDaoMysqlImpl.getEmpireNum(userId);
		if (empireNum == null) {
			int num = 5;
			empireNum = new EmpireNum(userId, num);
			this.empireDaoMysqlImpl.updateEmpireNum(userId, num);
		}
		return empireNum;
	}

	@Override
	public void updateEmpireNum(String userId, int num) {
		this.empireDaoMysqlImpl.updateEmpireNum(userId, num);
	}

	@Override
	public void clearEmpireNum() {
		this.empireDaoMysqlImpl.clearEmpireNum();
	}

	@Override
	public Empire getEmpireUser(int floor, String userId) {
		return this.empireDaoMysqlImpl.getEmpireUser(floor, userId);
	}

	@Override
	public Set<String> getEmpireUser(int floor) {
		if(!floorUser.containsKey(floor)){
			 Set<String> set=this.empireDaoMysqlImpl.getEmpireUser(floor);
			floorUser.put(floor, set);
		}
		return floorUser.get(floor);
	}

	@Override
	public List<Empire> getEmpireHero(int floor, String userId) {
		
		return this.empireDaoMysqlImpl.getEmpireHero(floor, userId);
	}

}
