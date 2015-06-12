package com.lodogame.game.utils;

/**
 * 文件夹工具类
 */

public class DirectoryUtils {

	/**
	 * 获取不带后缀的文件名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getFileNameNoEx(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length()))) {
				return filename.substring(0, dot);
			}
		}
		return filename;
	}

}
