package com.lodogame.model;

/**
 * 
 * <br>=
 * ========================= <br>
 * 公司：木屋网络 <br>
 * 开发：onedear <br>
 * 版本：1.0 <br>
 * 创建时间：Oct 29, 2014 2:49:16 PM <br>=
 * =========================
 */
public class SystemDailyTask implements SystemModel {

	/**
	 * ✔完成任意副本N次
	 */
	public static final int RENYIFUBN = 1;
	/**
	 * ✔完成任意精英副本N次
	 */
	public static final int JINGYINGFUBEN = 2;
	/**
	 * √竞技场N次
	 */
	public static final int PK = 3;
	/**
	 * √竞技场胜利N次
	 */
	public static final int JINGJISHENGLI = 4;
	/**
	 * 论剑 N次
	 */
	public static final int LUNJIANDUIHUAN = 5;
	/**
	 * 江山令兑换N次
	 */
	public static final int JIANGSHANLINGDUIHUAN = 6;
	/**
	 * ✔武将升级N次
	 */
	public static final int WUJIANGSHENGJI = 7;
	/**
	 * 装备强化N次
	 */
	public static final int EQUIP_MERGE = 8;
	/**
	 * ✔装备进阶N次
	 */
	public static final int ZHUANGBEIJINJIE = 9;
	/**
	 * ✔武将进阶N次
	 */
	public static final int WUJIANGJINJIE = 10;
	/**
	 * --武将培养N次
	 */
	public static final int WUJIANGPEIYANG = 11;
	/**
	 * 经脉提升N次
	 */
	public static final int JINGMAITISHENG = 12;
	/**
	 * 练功房挑战N次
	 */
	public static final int LIANGONGFANGTISHENG = 13;
	/**
	 * --远征N次
	 */
	public static final int YUANZHENG = 14;
	/**
	 * --领取月卡奖励
	 */
	public static final int LINGQUYUEKA = 15;

	/**
	 * --祷告N次
	 */
	public static final int QIDAO = 17;
	/**
	 * 武将升星N次
	 */
	public static final int WUJIANGSHENGXING = 18;
	/**
	 * --分解炉分解N次
	 */
	public static final int FENJIELUFENJIE = 19;

	/**
	 * 宝藏峡谷
	 */
	public static final int TREASURE = 20;

	/**
	 * 宝石祭坛
	 */
	public static final int GEM = 21;

	/**
	 * 角斗场
	 */
	public static final int ARENA = 22;

	/**
	 * 资源副本
	 */
	public static final int RESOURCE_FIGHT = 23;

	/**
	 * 12点到14 点领取体力
	 */
	public static final int RECEIVE_POWER_NOON = 24;

	/**
	 * 18点到20点领取体力
	 */
	public static final int RECEIVE_POWER_NIGHT = 25;

	private int id;

	private int type;

	private String name;

	private String desc;

	private int targetValue;

	private int imgId;

	private int minLv;

	private int sort;

	private String rewards;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(int targetValue) {
		this.targetValue = targetValue;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public int getMinLv() {
		return minLv;
	}

	public void setMinLv(int minLv) {
		this.minLv = minLv;
	}

	public String getRewards() {
		return rewards;
	}

	public void setRewards(String rewards) {
		this.rewards = rewards;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getListeKey() {
		return null;
	}

	public String getObjKey() {
		return String.valueOf(this.id);
	}

}
