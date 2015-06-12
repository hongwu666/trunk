package com.lodogame.model;

public class MeridianConfig implements SystemModel {

	private static final long serialVersionUID = 1L;

	private int meridianType;

	private String meridianName;

	private int attributeType;

	private String attributeName;

	private int nodeId;

	private int needColor;

	private int needNode;

	private int goldNeed;

	private int muhonNeed;

	private int consume;

	private int mingwenNeed;

	private int luckAdd;

	private int minLuck;

	private int chance;

	private int maxLuck;

	private int career;

	public int getCareer() {
		return career;
	}

	public void setCareer(int career) {
		this.career = career;
	}

	public int getGoldNeed() {
		return goldNeed;
	}

	public void setGoldNeed(int goldNeed) {
		this.goldNeed = goldNeed;
	}

	public int getMingwenNeed() {
		return mingwenNeed;
	}

	public void setMingwenNeed(int mingwenNeed) {
		this.mingwenNeed = mingwenNeed;
	}

	public int getChance() {
		return chance;
	}

	public void setChance(int chance) {
		this.chance = chance;
	}

	public int getMeridianType() {
		return meridianType;
	}

	public void setMeridianType(int meridianType) {
		this.meridianType = meridianType;
	}

	public String getMeridianName() {
		return meridianName;
	}

	public void setMeridianName(String meridianName) {
		this.meridianName = meridianName;
	}

	public int getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(int attributeType) {
		this.attributeType = attributeType;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public int getNeedColor() {
		return needColor;
	}

	public void setNeedColor(int needColor) {
		this.needColor = needColor;
	}

	public int getNeedNode() {
		return needNode;
	}

	public void setNeedNode(int needNode) {
		this.needNode = needNode;
	}

	public int getConsume() {
		return consume;
	}

	public void setConsume(int consume) {
		this.consume = consume;
	}

	public int getLuckAdd() {
		return luckAdd;
	}

	public void setLuckAdd(int luckAdd) {
		this.luckAdd = luckAdd;
	}

	public int getMinLuck() {
		return minLuck;
	}

	public void setMinLuck(int minLuck) {
		this.minLuck = minLuck;
	}

	public int getMaxLuck() {
		return maxLuck;
	}

	public void setMaxLuck(int maxLuck) {
		this.maxLuck = maxLuck;
	}

	public String getListeKey() {
		return null;
	}

	public String getObjKey() {
		return this.meridianType + "_" + this.nodeId;
	}

	public int getMuhonNeed() {
		return muhonNeed;
	}

	public void setMuhonNeed(int muhonNeed) {
		this.muhonNeed = muhonNeed;
	}

	@Override
	public String toString() {
		return "MeridianConfig [meridianType=" + meridianType + ", meridianName=" + meridianName + ", attributeType=" + attributeType + ", attributeName=" + attributeName
				+ ", nodeId=" + nodeId + ", needColor=" + needColor + ", needNode=" + needNode + ", consume=" + consume + ", luckAdd=" + luckAdd + ", minLuck=" + minLuck
				+ ", chance=" + chance + ", maxLuck=" + maxLuck + "]";
	}

}
