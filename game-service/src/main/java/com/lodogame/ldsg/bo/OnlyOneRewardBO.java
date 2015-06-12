package com.lodogame.ldsg.bo;

import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class OnlyOneRewardBO {

	@Mapper(name = "id")
	private int id;

	/**
	 * 1.周排行 2。30分钟排行 3.次数奖励
	 */
	@Mapper(name = "tp")
	private int type;

	@Mapper(name = "tls")
	private List<DropToolBO> dropToolBOList;

	/**
	 * 标题
	 */
	@Mapper(name = "title")
	private String title;

	/**
	 * 发放时间
	 */
	@Mapper(name = "date")
	private String date;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<DropToolBO> getDropToolBOList() {
		return dropToolBOList;
	}

	public void setDropToolBOList(List<DropToolBO> dropToolBOList) {
		this.dropToolBOList = dropToolBOList;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
