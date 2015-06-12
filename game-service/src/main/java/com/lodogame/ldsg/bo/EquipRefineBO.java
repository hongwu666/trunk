package com.lodogame.ldsg.bo;

import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;
import com.lodogame.ldsg.helper.DropDescHelper;
import com.lodogame.model.SystemEquipRefine;

@Compress
public class EquipRefineBO {

	/**
	 * 消耗货币值
	 */
	@Mapper(name = "cova")
	private int costValue;
	/**
	 * 消耗金币值
	 */
	@Mapper(name = "co")
	private int coin;
	/**
	 * 消耗货币类型
	 */
	@Mapper(name = "cotp")
	private int costType;
	/**
	 * 当前等级
	 */
	@Mapper(name = "lv")
	private int level;
	/**
	 * 下一等级
	 */
	@Mapper(name = "lvnex")
	private int levelNex;
	/**
	 * 当前属性值
	 */
	@Mapper(name = "prova")
	private int proValue;
	/**
	 * 下一等级属性值
	 */
	@Mapper(name = "nexprova")
	private int proNexValue;
	/**
	 * 属性类型
	 */
	@Mapper(name = "protp")
	private int proType;
	/**
	 * 当前战力
	 */
	@Mapper(name = "pow")
	private int power;
	/**
	 * 下一战力
	 */
	@Mapper(name = "pownex")
	private int powerNex;
	/**
	 * 精炼节点
	 */
	@Mapper(name = "tp")
	private int refinePoint;

	public int getCostValue() {
		return costValue;
	}

	public void setCostValue(int costValue) {
		this.costValue = costValue;
	}

	public int getCostType() {
		return costType;
	}

	public void setCostType(int costType) {
		this.costType = costType;
	}

	public int getLevel() {
		return level;
	}

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getProValue() {
		return proValue;
	}

	public void setProValue(int proValue) {
		this.proValue = proValue;
	}

	public int getRefinePoint() {
		return refinePoint;
	}

	public void setRefinePoint(int refinePoint) {
		this.refinePoint = refinePoint;
	}

	public int getProType() {
		return proType;
	}

	public void setProType(int proType) {
		this.proType = proType;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public int getLevelNex() {
		return levelNex;
	}

	public void setLevelNex(int levelNex) {
		this.levelNex = levelNex;
	}

	public int getProNexValue() {
		return proNexValue;
	}

	public void setProNexValue(int proNexValue) {
		this.proNexValue = proNexValue;
	}

	public int getPowerNex() {
		return powerNex;
	}

	public void setPowerNex(int powerNex) {
		this.powerNex = powerNex;
	}

	public EquipRefineBO(SystemEquipRefine systemEquipRefine) {
		if (systemEquipRefine == null) {
			return;
		}
		this.refinePoint = systemEquipRefine.getRefinePoint();
		this.proType = systemEquipRefine.getProType();
		this.proValue = systemEquipRefine.getProValue();
		this.level = systemEquipRefine.getRefineLevel();
		List<DropDescBO> list = DropDescHelper.parseDropTool(systemEquipRefine.getCost());
		if (list == null || list.size() < 1) {
			return;
		}
		for (DropDescBO bo : list) {
			if (bo.getToolType() == 2) {
				this.coin = bo.getToolNum();
			} else {
				this.costType = bo.getToolType();
				this.costValue = bo.getToolNum();
			}

		}
		this.levelNex = level + 1;
	}

}
