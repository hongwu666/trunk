package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.OnlyOneDao;
import com.lodogame.model.OnlyoneUserHero;
import com.lodogame.model.OnlyoneUserReg;
import com.lodogame.model.OnlyoneUserReward;
import com.lodogame.model.OnlyoneWeekRank;

public class OnlyOneDaoCacheImpl implements OnlyOneDao {

	@Autowired
	private OnlyOneDao onlyOneDaoMysqlImpl;

	private Map<String, OnlyoneUserReg> regMap = new ConcurrentHashMap<String, OnlyoneUserReg>();

	@Override
	public void loadData() {
		Collection<OnlyoneUserReg> list = this.onlyOneDaoMysqlImpl.getRegList();
		for (OnlyoneUserReg reg : list) {
			this.regMap.put(reg.getUserId(), reg);
		}
	}

	@Override
	public boolean setStatus(int status) {
		return this.onlyOneDaoMysqlImpl.setStatus(status);
	}

	@Override
	public boolean isWeekCutOff() {
		return this.onlyOneDaoMysqlImpl.isWeekCutOff();
	}

	@Override
	public boolean addWeekCutOffLog() {
		return this.onlyOneDaoMysqlImpl.addWeekCutOffLog();
	}

	@Override
	public int getStatus() {
		return this.onlyOneDaoMysqlImpl.getStatus();
	}

	@Override
	public boolean addReward(OnlyoneUserReward onlyoneUserReward) {
		return onlyOneDaoMysqlImpl.addReward(onlyoneUserReward);
	}

	@Override
	public boolean addWeekPoint(String userId, String username, double point) {
		return onlyOneDaoMysqlImpl.addWeekPoint(userId, username, point);
	}

	@Override
	public boolean add(OnlyoneUserReg onlyOneReg) {
		regMap.put(onlyOneReg.getUserId(), onlyOneReg);
		this.onlyOneDaoMysqlImpl.add(onlyOneReg);
		return true;
	}

	@Override
	public OnlyoneUserReg getByUserId(String userId) {
		if (this.regMap.containsKey(userId)) {
			OnlyoneUserReg reg = this.regMap.get(userId);
			return reg;
		}
		return null;
	}

	@Override
	public List<OnlyoneUserReg> getRankList() {

		List<OnlyoneUserReg> rankList = new ArrayList<OnlyoneUserReg>();
		rankList.addAll(this.regMap.values());

		Collections.sort(rankList, new Comparator<OnlyoneUserReg>() {

			@Override
			public int compare(OnlyoneUserReg o1, OnlyoneUserReg o2) {

				if (o1.getPoint() > o2.getPoint()) {
					return -1;
				} else if (o1.getPoint() < o2.getPoint()) {
					return 1;
				}
				return 0;

			}

		});

		return rankList;
	}

	@Override
	public OnlyoneUserReward getReward(String userId, int id) {
		return this.onlyOneDaoMysqlImpl.getReward(userId, id);
	}

	@Override
	public List<OnlyoneUserReward> getRewardList(String userId) {
		return this.onlyOneDaoMysqlImpl.getRewardList(userId);
	}

	@Override
	public boolean updateReward(String userId, int id, int status) {
		return this.onlyOneDaoMysqlImpl.updateReward(userId, id, status);
	}

	@Override
	public void cleanData() {
		this.regMap.clear();
		this.onlyOneDaoMysqlImpl.cleanData();
	}

	@Override
	public Collection<OnlyoneUserReg> getRegList() {
		return this.regMap.values();
	}

	@Override
	public List<OnlyoneUserReg> getRegList(int status) {
		List<OnlyoneUserReg> onlyoneRegList = new ArrayList<OnlyoneUserReg>();

		for (Entry<String, OnlyoneUserReg> entry : regMap.entrySet()) {
			OnlyoneUserReg onlyOneReg = entry.getValue();

			if (onlyOneReg.getStatus() == status) {
				onlyoneRegList.add(onlyOneReg);
			}

		}

		return onlyoneRegList;
	}

	@Override
	public boolean addOnlyoneUserhero(OnlyoneUserHero onlyoneUserHero) {
		return this.onlyOneDaoMysqlImpl.addOnlyoneUserhero(onlyoneUserHero);
	}

	@Override
	public List<OnlyoneUserReg> getWinRankList() {

		List<OnlyoneUserReg> rankList = new ArrayList<OnlyoneUserReg>();
		rankList.addAll(this.regMap.values());

		Collections.sort(rankList, new Comparator<OnlyoneUserReg>() {

			@Override
			public int compare(OnlyoneUserReg o1, OnlyoneUserReg o2) {

				if (o1.getWinTimes() > o2.getWinTimes()) {
					return -1;
				} else if (o1.getWinTimes() < o2.getWinTimes()) {
					return 1;
				}
				return 0;

			}

		});

		return rankList;
	}

	@Override
	public int getOnlyoneUserHeroLife(String userHeroId) {
		return this.onlyOneDaoMysqlImpl.getOnlyoneUserHeroLife(userHeroId);
	}

	@Override
	public int getOnlyoneUserHeroMorale(String userHeroId) {
		return this.onlyOneDaoMysqlImpl.getOnlyoneUserHeroMorale(userHeroId);
	}

	@Override
	public boolean deleteOnlyoneUserHero(String userId) {
		return this.onlyOneDaoMysqlImpl.deleteOnlyoneUserHero(userId);
	}

	@Override
	public void cleanWeekRank() {
		this.onlyOneDaoMysqlImpl.cleanWeekRank();
	}

	@Override
	public List<OnlyoneWeekRank> getWeekRank() {
		return this.onlyOneDaoMysqlImpl.getWeekRank();
	}

	@Override
	public int getRank(String userId) {
		return this.onlyOneDaoMysqlImpl.getRank(userId);
	}

}
