package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * 赛程BO
 * 
 * @author shixiangwen
 * 
 */

@Compress
public class ContestScheduleBO {

	@Mapper(name = "uid")
	private String userId;

	@Mapper(name = "un")
	private String username;

	@Mapper(name = "dr")
	private int deadRound;

	@Mapper(name = "ind")
	private int ind;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getDeadRound() {
		return deadRound;
	}

	public void setDeadRound(int deadRound) {
		this.deadRound = deadRound;
	}

	public int getInd() {
		return ind;
	}

	public void setInd(int ind) {
		this.ind = ind;
	}

}
