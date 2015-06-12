package com.lodogame.ldsg.bo;


import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserMessageBO {

	@Mapper(name = "name")
	private String username;
	
	@Mapper(name = "tp")
	private int type;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
