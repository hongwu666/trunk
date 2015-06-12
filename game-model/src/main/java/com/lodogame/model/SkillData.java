package com.lodogame.model;
/**
 * 点化熟练度和消耗
 * @author Administrator
 *
 */
public class SkillData implements SystemModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 熟练度下限
	 */
	private int skillLow;
	/**
	 * 熟练度上限
	 */
	private int skillUp;
	/**
	 * 属性数下限
	 */
	private int propertyNumLow;
	/**
	 * 属性数上限
	 */
	private int propertyNumUp;
	/**
	 * 属性品质下限
	 */
	private int propertyColorLow;
	/**
	 * 属性品质上限
	 */
	private int propertyColorUp;
	/**
	 * 消耗点化水晶
	 */
	private int crystal;
	/**
	 * 消耗金币
	 */
	private int coin;
	public int getSkillLow() {
		return skillLow;
	}
	public void setSkillLow(int skillLow) {
		this.skillLow = skillLow;
	}
	public int getSkillUp() {
		return skillUp;
	}
	public void setSkillUp(int skillUp) {
		this.skillUp = skillUp;
	}
	public int getPropertyNumLow() {
		return propertyNumLow;
	}
	public void setPropertyNumLow(int propertyNumLow) {
		this.propertyNumLow = propertyNumLow;
	}
	public int getPropertyNumUp() {
		return propertyNumUp;
	}
	public void setPropertyNumUp(int propertyNumUp) {
		this.propertyNumUp = propertyNumUp;
	}
	public int getPropertyColorLow() {
		return propertyColorLow;
	}
	public void setPropertyColorLow(int propertyColorLow) {
		this.propertyColorLow = propertyColorLow;
	}
	public int getPropertyColorUp() {
		return propertyColorUp;
	}
	public void setPropertyColorUp(int propertyColorUp) {
		this.propertyColorUp = propertyColorUp;
	}
	public int getCrystal() {
		return crystal;
	}
	public void setCrystal(int crystal) {
		this.crystal = crystal;
	}
	public int getCoin() {
		return coin;
	}
	public void setCoin(int coin) {
		this.coin = coin;
	}
	public String getListeKey() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getObjKey() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
