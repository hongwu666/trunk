package com.lodogame.model;

public class EnchantProperty implements SystemModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 品质
	 */
	private int color;
	/**
	 * 属性类型
	 */
	private int propertyType;
	/**
	 * 坦克
	 */
	private int tank;
	/**
	 * 伤害
	 */
	private int hurt;
	/**
	 * 奶
	 */
	private int milk;
	/**
	 * 奶坦
	 */
	private int milkTemple;
	
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getPropertyType() {
		return propertyType;
	}
	public void setPropertyType(int propertyType) {
		this.propertyType = propertyType;
	}
	public int getTank() {
		return tank;
	}
	public void setTank(int tank) {
		this.tank = tank;
	}
	public int getHurt() {
		return hurt;
	}
	public void setHurt(int hurt) {
		this.hurt = hurt;
	}
	public int getMilk() {
		return milk;
	}
	public void setMilk(int milk) {
		this.milk = milk;
	}
	public int getMilkTemple() {
		return milkTemple;
	}
	public void setMilkTemple(int milkTemple) {
		this.milkTemple = milkTemple;
	}
	public String getListeKey() {
		return null;
	}
	public String getObjKey() {
		return color+"_"+propertyType;
	}
	
}
