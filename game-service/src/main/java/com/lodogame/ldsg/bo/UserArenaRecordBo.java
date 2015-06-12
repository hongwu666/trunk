package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * 挑战记录对象
 * @author Candon
 *
 */
@Compress
public class UserArenaRecordBo {
	@Mapper(name = "tm")
	private String time;
	
	@Mapper(name = "unm")
	private String username;
	
	@Mapper(name = "df")
	private int defiant;
	
	@Mapper(name = "rst")
	private int result;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getDefiant() {
		return defiant;
	}
	public void setDefiant(int defiant) {
		this.defiant = defiant;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	
	
}
