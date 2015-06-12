package com.lodogame.game.test;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.lodogame.game.utils.IDGenerator;
import com.lodogame.game.utils.json.Json;


public class Test {

	public static void main(String[] args) {
		String str = "1,1,1,1,";
		int index = str.lastIndexOf("1");
		System.out.println(str.subSequence(0, index).toString() + "2,");
	}
	

}

class Time {
	String value;
	String date;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	
}
