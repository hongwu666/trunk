package com.lodogame.model;

/**
*
* <br>==========================
* <br> 公司：木屋网络
* <br> 开发：onedear
* <br> 版本：1.0
* <br> 创建时间：Oct 29, 2014 2:49:16 PM
* <br>==========================
*/
public class ActivityDailyTask {

	/**
	 * ✔完成任意副本(普通、精英、资源)N次
	 */
	public static final int TYPE_RENYIFUBN = 1;
	/**
	 * ✔完成任意精英副本N次
	 */
	public static final int TYPE_JINGYINGFUBEN = 2;
	/**
	 * √竞技场N次
	 */
	public static final int TYPE_JINGJICHANG = 3;
	/**
	 * √竞技场胜利N次
	 */
	public static final int TYPE_JINGJISHENGLI = 4;
	/**
	 * 竞技场换N次
	 */
	public static final int TYPE_LUNJIANDUIHUAN = 5;
	/**
	 * 江山令兑换N次
	 */
	public static final int TYPE_JIANGSHANLINGDUIHUAN = 6;
	/**
	 * ✔武将升级N次
	 */
	public static final int TYPE_WUJIANGSHENGJI = 7;
	/**
	 * 装备升级N次
	 */
	public static final int TYPE_ZHUANGBEISHENGJI = 8;
	/**
	 * ✔装备进阶N次
	 */
	public static final int TYPE_ZHUANGBEIJINJIE = 9;
	/**
	 * ✔武将进阶N次
	 */
	public static final int TYPE_WUJIANGJINJIE = 10;
	/**
	 * --武将培养N次
	 */
	public static final int TYPE_WUJIANGPEIYANG = 11;
	/**
	 * 经脉提升N次
	 */
	public static final int TYPE_JINGMAITISHENG = 12;
	/**
	 * 练功房挑战N次
	 */
	public static final int TYPE_LIANGONGFANGTISHENG = 13;
	/**
	 * --远征N次
	 */
	public static final int TYPE_YUANZHENG = 14;
	/**
	 * --领取月卡奖励
	 */
	public static final int TYPE_LINGQUYUEKA = 15;
	/**
	 * 领取体力
	 */
	public static final int TYPE_LINGQUTILI = 16;
	/**
	 * --祷告N次
	 */
	public static final int TYPE_QIDAO = 17;
	/**
	 * 武将升星N次
	 */
	public static final int TYPE_WUJIANGSHENGXING = 18;
	/**
	 * --分解炉分解N次
	 */
	public static final int TYPE_FENJIELUFENJIE = 19;
	
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

}
