package com.lodogame.ldsg.bo;

import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ToolExchangeBO {

	@Mapper(name = "id")
	private int id;

	@Mapper(name = "prit")
	private List<DropDescBO> preDropToolBOList;

	@Mapper(name = "ptit")
	private List<DropDescBO> postDropToolBOList;

	@Mapper(name = "ts")
	private int times;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<DropDescBO> getPreDropToolBOList() {
		return preDropToolBOList;
	}

	public void setPreDropToolBOList(List<DropDescBO> preDropToolBOList) {
		this.preDropToolBOList = preDropToolBOList;
	}

	public List<DropDescBO> getPostDropToolBOList() {
		return postDropToolBOList;
	}

	public void setPostDropToolBOList(List<DropDescBO> postDropToolBOList) {
		this.postDropToolBOList = postDropToolBOList;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

}
