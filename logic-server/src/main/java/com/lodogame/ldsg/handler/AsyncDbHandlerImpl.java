package com.lodogame.ldsg.handler;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.lodogame.common.jdbc.Jdbc;
import com.lodogame.game.dao.asyncdb.AsyncDbModel;
import com.lodogame.game.dao.asyncdb.AsyncDbPool;


public class AsyncDbHandlerImpl {

	private static final Logger logger = Logger.getLogger(AsyncDbHandlerImpl.class);

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	@Autowired
	private Jdbc jdbc;

	public void handle(final AsyncDbModel<?> model) {

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				_handle(model);
			}
		};

		taskExecutor.execute(runnable);
	}

	public void _handle(AsyncDbModel<?> model) {
		jdbc.insert(model.getTable(), model.getTs());
	}

	public void init() {
		new Thread(new WorkThread()).start();
	}

	class WorkThread implements Runnable {
		@Override
		public void run() {

			boolean go = true;

			while (go) {

				try {

					final AsyncDbModel<?> model = AsyncDbPool.getInstance().take();
					if (model != null) {
						handle(model);
					}

				} catch (InterruptedException ie) {
					go = false;
				} catch (Throwable t) {
					logger.error(t.getMessage(), t);
					try {
						Thread.sleep(1000 * 1);
					} catch (InterruptedException iie) {
						logger.error(iie);
					}

				}

			}

		}
	}
}
