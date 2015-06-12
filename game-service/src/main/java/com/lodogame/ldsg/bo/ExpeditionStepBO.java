package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ExpeditionStepBO {
	@Mapper(name = "ei")
	private long exId;
	@Mapper(name = "bs")
	private int boxStat;
	@Mapper(name = "inx")
	private int index;
	@Mapper(name = "mid")
	private String modelId;
	@Mapper(name = "st")
	private int stat;

	public int getStat() {
		return stat;
	}

	public void setStat(int stat) {
		this.stat = stat;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public long getExId() {
		return exId;
	}

	public void setExId(long exId) {
		this.exId = exId;
	}

	public int getBoxStat() {
		return boxStat;
	}

	public void setBoxStat(int boxStat) {
		this.boxStat = boxStat;
	}

}
