package com.lodogame.ldsg.bo;

import java.io.Serializable;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserEquipStoneBO  implements Serializable  {

	private static final long serialVersionUID = 671020292262984172L;

	@Mapper(name = "pos")
	private int pos;

	@Mapper(name = "sid")
	private int stoneId;

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getStoneId() {
		return stoneId;
	}

	public void setStoneId(int stoneId) {
		this.stoneId = stoneId;
	}

	
	
}
