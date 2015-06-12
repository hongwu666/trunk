package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ResourceSsBo {
	
	@Mapper(name="g")
	private int g;
	
	@Mapper(name="ss")
	private int ss;
	
	public int getG() {
		return g;
	}
	public void setG(int g) {
		this.g = g;
	}
	public int getSs() {
		return ss;
	}
	public void setSs(int ss) {
		this.ss = ss;
	}
	
}
