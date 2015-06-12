package com.lodogame.ldsg.bo;

import java.io.Serializable;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

/**
 * 掉落描述BO
 * 
 * @author jacky
 * 
 */
@Compress
public class DropDescBO implements Serializable {

	private static final long serialVersionUID = 1L;

	public DropDescBO(int toolType, int toolId, int toolNum) {
		this.toolType = toolType;
		this.toolId = toolId;
		this.toolNum = toolNum;
	}

	/**
	 * 道具类型
	 */
	@Mapper(name = "tt")
	protected int toolType;

	/**
	 * 道具ID
	 */
	@Mapper(name = "tid")
	private int toolId;

	/**
	 * 道具数量
	 */
	@Mapper(name = "cnt")
	protected int toolNum;

	public int getToolType() {
		return toolType;
	}

	public void setToolType(int toolType) {
		this.toolType = toolType;
	}

	public int getToolId() {
		return toolId;
	}

	public void setToolId(int toolId) {
		this.toolId = toolId;
	}

	public int getToolNum() {
		return toolNum;
	}

	public void setToolNum(int toolNum) {
		this.toolNum = toolNum;
	}

}
