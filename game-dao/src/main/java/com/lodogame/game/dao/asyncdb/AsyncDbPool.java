package com.lodogame.game.dao.asyncdb;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class AsyncDbPool {

	BlockingQueue<AsyncDbModel<?>> queue = new LinkedBlockingDeque<AsyncDbModel<?>>();

	private static AsyncDbPool pool;

	public static AsyncDbPool getInstance() {

		if (pool == null) {
			pool = new AsyncDbPool();
		}

		return pool;
	}

	public AsyncDbModel<?> take() throws InterruptedException {
		return this.queue.take();
	}

	public void add(AsyncDbModel<?> model) {
		queue.add(model);
	}
}
