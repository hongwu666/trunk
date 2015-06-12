package com.lodogame.ldsg.helper;

/**
 * 商城帮助类
 * 
 * @author jacky
 * 
 */
public class MallHelper {

	/**
	 * 获取属性值
	 * 
	 * @param initAttr
	 * @param attrGrowth
	 * @return
	 */
	public static int getRefreshNeedMoney(int times) {
		int money = (int) (Math.ceil((float) times / 3)) * 20;
		if (money == 0) {
			return 20;
		}

		if (money > 100) {
			money = 100;
		}

		return money;
	}

	public static void main(String[] args) {
		System.out.println(getRefreshNeedMoney(1));
		System.out.println(getRefreshNeedMoney(2));
		System.out.println(getRefreshNeedMoney(3));
		System.out.println(getRefreshNeedMoney(4));
		System.out.println(getRefreshNeedMoney(5));

		System.out.println(Math.ceil(1 / 3));
		System.out.println(Math.ceil(2 / 3));
		System.out.println(Math.ceil(3 / 3));
		System.out.println(Math.ceil(4 / 3));
		System.out.println(Math.ceil(5 / 3));

	}
}
