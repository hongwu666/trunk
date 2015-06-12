package com.lodogame.game.remote.stub;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.lodogame.game.remote.callback.Callback;
import com.lodogame.game.remote.handle.RemoteCallHandleImpl;

public class StubFactory {

	private static final Logger logger = Logger.getLogger(RemoteCallHandleImpl.class);

	private int count = 0;

	private Map<String, Callback> cm = new HashMap<String, Callback>();

	private Map<String, Long> tm = new HashMap<String, Long>();

	private static StubFactory instance;

	public static StubFactory getInstance() {

		if (instance == null) {
			instance = new StubFactory();
		}

		return instance;
	}

	private void clean() {

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {

				long now = System.currentTimeMillis();

				Set<String> expiredKeySet = new HashSet<String>();

				for (Entry<String, Long> entry : tm.entrySet()) {
					long t = entry.getValue();
					if (now - t > 120 * 1000) {
						expiredKeySet.add(entry.getKey());
					}
				}

				for (String key : expiredKeySet) {
					logger.info("删除过期key[" + key + "]");
					tm.remove(key);
					cm.remove(key);
				}

			}
		});

		t.start();
	}

	public void push(String id, Callback callback) {
		count += 1;
		this.cm.put(id, callback);
		this.tm.put(id, System.currentTimeMillis());
		if (count >= 10000) {
			count = 0;
			clean();
		}
	}

	public Callback get(String id) {
		if (cm.containsKey(id)) {
			Callback callback = cm.get(id);
			cm.remove(id);
			tm.remove(id);
			return callback;
		}
		return null;
	}
}
