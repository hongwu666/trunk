package com.lodogame.ldsg.bo;

import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ExpeditionShowBO {
	@Mapper(name = "step")
	private ExpeditionStepBO bo;

	@Mapper(name = "num")
	private int num;

	@Mapper(name="hero")
	private List<UserHeroBO> hero;
	
	
	public List<UserHeroBO> getHero() {
		return hero;
	}

	public void setHero(List<UserHeroBO> hero) {
		this.hero = hero;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public ExpeditionStepBO getBo() {
		return bo;
	}

	public void setBo(ExpeditionStepBO bo) {
		this.bo = bo;
	}

}
