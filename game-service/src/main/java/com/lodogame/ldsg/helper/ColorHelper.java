package com.lodogame.ldsg.helper;

/**
 * 武将帮助类
 * 
 * @author jacky
 * 
 */
public class ColorHelper {

	/**
	 * 获取属性值
	 * 
	 * @param initAttr
	 * @param attrGrowth
	 * @return
	 */
	public static String getColorName(int color) {
		switch (color) {
		case 0:
			return "白色";
		case 1:
			return "绿色";
		case 2:
			return "蓝色";
		case 3:
			return "蓝钻";
		case 4:
			return "紫色";
		case 5:
			return "紫钻";
		case 6:
			return "橙色";
		case 7:
			return "橙钻";
		case 8:
			return "红色";
		case 9:
			return "红钻";
		default:

		}

		return "";
	}

}
