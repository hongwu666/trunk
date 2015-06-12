package com.lodogame.ldsg.helper;

public class PowerHelper {

	/**
	 * 获取购买体力需要花费的金币
	 * 
	 * 体力购买公式：40+（次数*20） 200封顶，即第八次200、第九次200、第十次也是200
	 * 
	 * @param times
	 * @return
	 */
	public static int getBuyPowerNeedMoney(int times) {

		if (times <= 0) {
			return 1;
		}

		if (times <= 4) {
			return 40 + (30 * (times - 1));
		} else {
			return 100 + 30 * (int) (Math.ceil((times - 4) / 2));
		}

	}

	public static void main(String[] args) {
		System.out.println(Math.ceil(3.1));
	}
}
