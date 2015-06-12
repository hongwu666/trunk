package com.lodogame.ldsg.bo;

import com.lodogame.model.ContestWorldUser;

public class ContestWorldPairBO {

	private int indA;

	private int indB;

	private ContestWorldUser userA;

	private ContestWorldUser userB;

	private String baseCode;

	public ContestWorldUser getUserA() {
		return userA;
	}

	public void setUserA(ContestWorldUser userA) {
		this.userA = userA;
	}

	public ContestWorldUser getUserB() {
		return userB;
	}

	public void setUserB(ContestWorldUser userB) {
		this.userB = userB;
	}

	/**
	 * 是否需要比赛(没有轮空)
	 * 
	 * @return
	 */
	public boolean isFullPair() {

		if (userA.getIsEmpty() == 1) {
			return false;
		}

		if (userB.getIsEmpty() == 1) {
			return false;
		}

		return true;
	}

	public String getBaseCode() {
		return baseCode;
	}

	public void setBaseCode(String baseCode) {
		this.baseCode = baseCode;
	}

	/**
	 * 添加球队
	 * 
	 * @param tag
	 * @param userId
	 * @param ind
	 */
	public void put(String tag, ContestWorldUser user) {

		if ("0".equals(tag)) {
			this.userA = user;
			this.indA = user.getInd();
		} else {
			this.userB = user;
			this.indB = user.getInd();
		}

	}

	public int getIndA() {
		return indA;
	}

	public int getIndB() {
		return indB;
	}

}
