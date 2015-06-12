package com.lodogame.ldsg.ret;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lodogame.ldsg.bo.ContestUserHeroBO;
import com.lodogame.model.ContestUser;

public class ContestRet {

	public List<ContestUserHeroBO> contestUserHeroBOList;

	public ContestUser contestUser;

	public Map<Integer, Integer> winInfo;

	public Set<Integer> finishTeams;

	/**
	 * 当前状态
	 */
	public int status;

	/**
	 * 当前回合
	 */
	public int round;

	/**
	 * 当前场次
	 */
	public int index;

	/**
	 * 本服，跨服
	 */
	public int runType;

	/**
	 * 下一状态的时间
	 */
	public long nextStatusTime;

	public List<ContestUserHeroBO> getContestUserHeroBOList() {
		return contestUserHeroBOList;
	}

	public void setContestUserHeroBOList(List<ContestUserHeroBO> contestUserHeroBOList) {
		this.contestUserHeroBOList = contestUserHeroBOList;
	}

	public ContestUser getContestUser() {
		return contestUser;
	}

	public void setContestUser(ContestUser contestUser) {
		this.contestUser = contestUser;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Map<Integer, Integer> getWinInfo() {
		return winInfo;
	}

	public void setWinInfo(Map<Integer, Integer> winInfo) {
		this.winInfo = winInfo;
	}

	public Set<Integer> getFinishTeams() {
		return finishTeams;
	}

	public void setFinishTeams(Set<Integer> finishTeams) {
		this.finishTeams = finishTeams;
	}

	public int getRunType() {
		return runType;
	}

	public void setRunType(int runType) {
		this.runType = runType;
	}

	public long getNextStatusTime() {
		return nextStatusTime;
	}

	public void setNextStatusTime(long nextStatusTime) {
		this.nextStatusTime = nextStatusTime;
	}

}
