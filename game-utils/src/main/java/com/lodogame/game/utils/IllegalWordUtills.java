package com.lodogame.game.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import alex.zhrenjie04.wordfilter.WordFilterUtil;
import alex.zhrenjie04.wordfilter.result.FilteredResult;

/**
 * 非法字符工具类
 * 
 * @author CJ
 * 
 */
public class IllegalWordUtills {

	public static boolean hasIllegalWords(String str) {

		FilteredResult result = WordFilterUtil.filterText(str, '*');
		String badWords = result.getBadWords();

		if (badWords != null && !badWords.trim().equals("")) {
			return true;
		}

		return false;
	}

	public static String replaceIllegalWords(String str) {

		if (str == null) {
			return null;
		}

		FilteredResult result = WordFilterUtil.filterText(str, '*');
		return result.getFilteredContent();
	}

	/**
	 * 如果字符串中只包含 中文、英文和数字在，返回 true， 否则，返回 false
	 */
	public static boolean hasIllegalCharacters(String str) {
		String reg = "^[\u4e00-\u9fa5a-zA-Z0-9]+$";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return false;
		}

		return true;
	}

	public static void main(String[] args) {
		System.out.println(hasIllegalWords("法轮"));
		System.out.println(hasIllegalWords("毛泽东"));
		System.out.println(hasIllegalWords("妈逼"));
		System.out.println(hasIllegalWords("分裂中国"));
		System.out.println(hasIllegalWords("六四真相"));
		System.out.println(hasIllegalWords("李克强"));
	}
}
