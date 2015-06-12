package com.lodogame.model;

public class SystemRobotUser implements SystemModel {

	private int level;

	private int hero1;

	private int hero2;

	private int hero3;

	private int hero4;

	private int hero5;

	private int hero6;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getHero1() {
		return hero1;
	}

	public void setHero1(int hero1) {
		this.hero1 = hero1;
	}

	public int getHero2() {
		return hero2;
	}

	public void setHero2(int hero2) {
		this.hero2 = hero2;
	}

	public int getHero3() {
		return hero3;
	}

	public void setHero3(int hero3) {
		this.hero3 = hero3;
	}

	public int getHero4() {
		return hero4;
	}

	public void setHero4(int hero4) {
		this.hero4 = hero4;
	}

	public int getHero5() {
		return hero5;
	}

	public void setHero5(int hero5) {
		this.hero5 = hero5;
	}

	public int getHero6() {
		return hero6;
	}

	public void setHero6(int hero6) {
		this.hero6 = hero6;
	}

	public String getListeKey() {
		return null;
	}

	public String getObjKey() {
		return String.valueOf(level);
	}

}
