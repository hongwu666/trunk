package com.lodogame.ldsg.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.RandomUtils;

/**
 * 争霸赛
 * 
 * @author CJ
 * 
 */
public class PkHelper {
	private static Map<Integer, List<Integer[]>> ten = new HashMap<Integer, List<Integer[]>>();

	private static List<Integer[]> other = new ArrayList<Integer[]>();

	public static final List<Integer> tenList = new ArrayList<Integer>();
	static {
		Integer[] i1 = new Integer[] { 98, 99 };
		other.add(i1);
		Integer[] i2 = new Integer[] { 50, 90 };
		other.add(i2);
		Integer[] i3 = new Integer[] { 30, 40 };
		other.add(i3);

		String str = "10:1~3,4~6,7~9;9:1~3,4~6,7~8;8:1~2,3~5,6~7;7:1~2,3~4,5~6;6:1,2~3,4~5;5:1,2,3~4;4:1,2,3;3:1,2,4;2:1,3,4;1:2,3,4;";
		String[] info = str.split("[;]");
		for (String temp : info) {
			String[] v = temp.split("[:]");
			Integer key = Integer.parseInt(v[0]);
			String[] val = v[1].split("[,]");
			List<Integer[]> list = new ArrayList<Integer[]>();
			for (String temp2 : val) {
				String[] qu = temp2.split("[~]");
				Integer[] it = new Integer[qu.length];
				for (int i = 0; i < qu.length; i++) {
					it[i] = Integer.parseInt(qu[i]);
				}
				list.add(it);
			}
			ten.put(key, list);
		}

		for (int i = 1; i <= 10; i++) {
			tenList.add(i);
		}
	}

	public static void setAttackAbleList(List<Integer> attackAbleList, int rank) {
		if (rank <= 10) {
			List<Integer[]> val = ten.get(rank);
			for (Integer[] temp : val) {
				if (temp.length > 1) {
					int rand = RandomUtils.nextInt(temp[1] - temp[0]);
					int value = temp[0] + rand;
					attackAbleList.add(value);
				} else {
					attackAbleList.add(temp[0]);
				}
			}
		} else {
			for (Integer[] temp : other) {
				int rand = RandomUtils.nextInt(temp[1] - temp[0]);
				int lv = temp[0] + rand;
				float z = lv / 100f;
				attackAbleList.add((int) (rank * z));
			}
		}

	}

	public static void main(String[] args) {

		List<Integer> list = new ArrayList<Integer>();
		for (int j = 1; j <= 100; j++) {
			list = new ArrayList<Integer>();
			setAttackAbleList(list, j);
			System.out.println(j + "=======" + list);
		}
	}
}
