package com.lodogame.ldsg.bo;

import java.util.ArrayList;
import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;
import com.lodogame.model.EquipEnchant;

/**
 * 装备点化对象
 * 
 * @author Administrator
 *
 */
@Compress
public class EquipEnchantBO {

	/**
	 * 当前属性值
	 */
	@Mapper(name = "cpv")
	private List<EnchantProty> curProVal = new ArrayList<EnchantProty>();
	/**
	 * 点化属性值
	 */
	@Mapper(name = "epv")
	private List<EnchantProty> encProVal = new ArrayList<EnchantProty>();

	/**
	 * 消耗点化水晶
	 */
	@Mapper(name = "co")
	private int cost;
	/**
	 * 消耗点化水晶
	 */
	@Mapper(name = "ci")
	private int coin;

	public List<EnchantProty> getCurProVal() {
		return curProVal;
	}

	public void setCurProVal(List<EnchantProty> curProVal) {
		this.curProVal = curProVal;
	}

	public List<EnchantProty> getEncProVal() {
		return encProVal;
	}

	public void setEncProVal(List<EnchantProty> encProVal) {
		this.encProVal = encProVal;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
	}

	public EquipEnchantBO(EquipEnchant equipEnchant) {
		if (equipEnchant == null) {
			return;
		}
		String enPro = equipEnchant.getEnProperty();
		String curPro = equipEnchant.getCurProperty();
		String[] eStr = enPro.split(",");
		String[] cStr = curPro.split(",");

		for (String str : eStr) {
			String[] enStr = str.split(":");
			if (enStr.length > 1) {
				EnchantProty enchantProty = new EnchantProty(Integer.parseInt(enStr[0]), Integer.parseInt(enStr[2]));
				enchantProty.setColor(Integer.parseInt(enStr[1]));
				encProVal.add(enchantProty);
			}
		}

		for (String str : cStr) {
			String[] curStr = str.split(":");
			if (curStr.length > 1) {
				EnchantProty enchantProty = new EnchantProty(Integer.parseInt(curStr[0]), Integer.parseInt(curStr[2]));
				enchantProty.setColor(Integer.parseInt(curStr[1]));
				curProVal.add(enchantProty);
			}
		}

	}

	public EquipEnchantBO() {
		super();
	}

}
