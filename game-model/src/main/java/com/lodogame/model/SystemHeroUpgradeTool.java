package com.lodogame.model;

import java.io.Serializable;

/**
 * 武将进阶材料
 * 
 * @author jacky
 * 
 */
public class SystemHeroUpgradeTool implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 节点id
	 */
	private int nodeId;

	/**
	 * 材料类型
	 */
	private int toolType;

	/**
	 * 材料ID
	 */
	private int toolId;

	/**
	 * 需要的数量
	 */
	private int toolNum;
	
	/**
	 * 可以在哪些场景获得这个道具
	 */
	private String sceneId;
	
	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public int getToolType() {
		return toolType;
	}

	public void setToolType(int toolType) {
		this.toolType = toolType;
	}

	public String getSceneId() {
		return sceneId;
	}

	public void setSceneId(String sceneId) {
		this.sceneId = sceneId;
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
