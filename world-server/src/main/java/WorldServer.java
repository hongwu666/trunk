import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lodogame.game.remote.factory.BeanFactory;
import com.lodogame.game.utils.UrlRequestUtils;

/**
 * 跨服服务器 world server!
 * 
 */
public class WorldServer {

	private static Logger logger = Logger.getLogger(WorldServer.class);

	private String url = "http://admin.lieguo.lodogame.com/web/battle_status_report";

	public void start() {

		startMonitor();

		BeanFactory.getInstance();

	}

	private void monitor() {

		if (logger.isDebugEnabled()) {
			return;
		}

		try {

			Map<String, String> params = new HashMap<String, String>();
			params.put("method", "battle_status_report");
			String jsonStr = UrlRequestUtils.execute(url, params, UrlRequestUtils.Mode.GET);
			logger.debug("ret:" + jsonStr);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		try {
			Thread.sleep(10 * 1000);
		} catch (InterruptedException ie) {
			logger.error(ie.getMessage());
		}

	}

	private void startMonitor() {

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					monitor();
				}
			}

		}).start();

	}

	public static void main(String[] args) {
		WorldServer server = new WorldServer();
		server.start();
	}

}
