package com.lodogame.ldsg.bo;

import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ResourceViewBO {
	@Mapper(name="num")
	private int useNum;
	
	@Mapper(name="numMax")
	private int maxNum;
	
	@Mapper(name="gk")
	private List<Integer> gk;

	public int getUseNum() {
		return useNum;
	}

	public void setUseNum(int useNum) {
		this.useNum = useNum;
	}

	public int getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}

	public List<Integer> getGk() {
		return gk;
	}

	public void setGk(List<Integer> gk) {
		this.gk = gk;
	}
}
