package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class EquipRefineSoulBO {
	/**
	 * 装备id
	 */
	@Mapper(name = "eqid")
	private int equipId;
	/**
	 * 煉魂后的值
	 */
	@Mapper(name = "def")
	private int value;
	/**
	 * 消耗魂石
	 */
	@Mapper(name = "co")
	private int cost;
	/**
	 * 消耗金币
	 */
	@Mapper(name = "cn")
	private int coin;
	/**
	 * 幸运值
	 */
	@Mapper(name = "lc")
	private int luck;
	
	
	public int getEquipId() {
		return equipId;
	}

	public void setEquipId(int equipId) {
		this.equipId = equipId;
	}

	public int getValue() {
		return value;
	}

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getLuck() {
		return luck;
	}

	public void setLuck(int luck) {
		this.luck = luck;
	}

}
