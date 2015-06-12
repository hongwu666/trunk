package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ContestRegBO {

	private String userId;

	@Mapper(name = "un")
	private String username;

	@Mapper(name = "drn")
	private int deadRound;

	@Mapper(name = "ind")
	private int index;

	@Mapper(name = "gid")
	private int groupId;

	@Mapper(name = "shid")
	private int systemHeroId;

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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getSystemHeroId() {
		return systemHeroId;
	}

	public void setSystemHeroId(int systemHeroId) {
		this.systemHeroId = systemHeroId;
	}

}
