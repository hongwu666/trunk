package com.lodogame.ldsg.ret;

public class SellHeroRet {

	private int copper;
	private int muhon;
	private int jiangshanOrder;
	private int mingwen;
	private int soul;

	public SellHeroRet(int copper, int muhon, int jiangshanOrder,int mingwen,int soul) {
		this.copper = copper;
		this.muhon = muhon;
		this.jiangshanOrder = jiangshanOrder;
		this.soul=soul;
		this.mingwen=mingwen;
	}

	public int getCopper() {
		return copper;
	}

	public int getMuhon() {
		return muhon;
	}

	public int getJiangshanOrder() {
		return jiangshanOrder;
	}

	public int getMingwen() {
		return mingwen;
	}

	public void setMingwen(int mingwen) {
		this.mingwen = mingwen;
	}

	public int getSoul() {
		return soul;
	}

	public void setCopper(int copper) {
		this.copper = copper;
	}

	public void setMuhon(int muhon) {
		this.muhon = muhon;
	}

	public void setJiangshanOrder(int jiangshanOrder) {
		this.jiangshanOrder = jiangshanOrder;
	}

	public void setSoul(int soul) {
		this.soul = soul;
	}

}
