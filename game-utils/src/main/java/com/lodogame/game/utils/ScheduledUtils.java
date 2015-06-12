package com.lodogame.game.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledUtils {
	private static final ScheduledExecutorService SERVICE = Executors.newScheduledThreadPool(20);

	public static void schelduleAtFixed(Runnable runnable, TimeUnit time, long times) {
		SERVICE.scheduleAtFixedRate(runnable, times, times, time);
	}
}
