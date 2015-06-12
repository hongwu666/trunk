package com.lodogame.ldsg.helper;

import java.util.List;

import org.apache.commons.lang.math.RandomUtils;

import com.lodogame.model.RollAble;

public class RollHalper {

	public static RollAble roll(List<? extends RollAble> list) {

		int total = 0;
		for (RollAble rollAble : list) {
			total += rollAble.getRate();
		}

		int rand = RandomUtils.nextInt(total);
		int start = 0;

		for (RollAble rollAble : list) {
			start += rollAble.getRate();
			if (rand < start) {
				return rollAble;
			}
		}

		return null;
	}
}
