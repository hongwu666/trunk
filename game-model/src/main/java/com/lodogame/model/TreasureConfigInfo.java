package com.lodogame.model;


/**
 * 聚宝盆配置
 * 
 * @author
 *
 */
public class TreasureConfigInfo {
	/*
	 * id INT , grade INT , tb INT, wh INT, hl INT, jy INT
	 */
	//铜币 武魂 魂力 精元
	public static final int TYPE_TB = 1;
	public static final int TYPE_WH = 2;
	public static final int TYPE_HL = 3;
	public static final int TYPE_JY = 4;
	
	private int id;
	private int price;
	private int grade;
	private int tb;
	private int wh;
	private int hl;
	private int jy;
	
	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getV(int type){
		switch(type){
		case TYPE_TB:
			return tb;
		case TYPE_WH:
			return wh;
		case TYPE_HL:
			return hl;
		case TYPE_JY:
			return jy;
		default:
			return 0;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getTb() {
		return tb;
	}

	public void setTb(int tb) {
		this.tb = tb;
	}

	public int getWh() {
		return wh;
	}

	public void setWh(int wh) {
		this.wh = wh;
	}

	public int getHl() {
		return hl;
	}

	public void setHl(int hl) {
		this.hl = hl;
	}

	public int getJy() {
		return jy;
	}

	public void setJy(int jy) {
		this.jy = jy;
	}
}
