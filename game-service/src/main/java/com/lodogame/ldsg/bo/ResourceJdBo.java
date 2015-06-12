package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ResourceJdBo {
	@Mapper(name = "g")
	private int g;

	@Mapper(name = "start")
	private int start;

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

}
