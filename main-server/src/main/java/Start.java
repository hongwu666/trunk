import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lodogame.game.connector.ServerConnectorMgr;
import com.lodogame.game.server.GameServer;
import com.lodogame.game.server.session.DefaultSessionManager;
import com.lodogame.game.server.session.Session;
import com.lodogame.game.utils.ProcessUtils;
import com.lodogame.game.utils.UrlRequestUtils;
import com.lodogame.ldsg.Config;
import com.lodogame.ldsg.handler.MainSessionCloseHandler;
import com.lodogame.model.DiskInfo;

public class Start {

	private static Logger logger = Logger.getLogger(Start.class);

	private static String url = "http://admin.lieguo.lodogame.com/web/main_server_status_report";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GameServer mainServer = new GameServer(args.length > 1 ? args[0] : "");
		if (args.length == 2) {
			ProcessUtils.outputPid("/data/run-" + args[1] + "/main-server.pid");
		}

		if (args.length == 3) {
			if ("true".equalsIgnoreCase(args[2])) {
				Config.isTest = true;
			}
		}
		mainServer.start();
		DefaultSessionManager.getInstance().setSessionCloseHandler(new MainSessionCloseHandler());

		final int port = mainServer.getPort();

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					monitor(port);
				}
			}

		}).start();

	}

	static private void monitor(int port) {

		try {

			int logicServerStatus = getLogicServerStatus();
			int gameApiStatus = getGameApiStatus();

			DiskInfo diskInfo = HardWardMonitor.getDiskInfo();

			Map<String, String> params = new HashMap<String, String>();
			params.put("port", String.valueOf(port));
			params.put("logicServerStatus", String.valueOf(logicServerStatus));
			params.put("gameApiStatus", String.valueOf(gameApiStatus));
			params.put("totalSpace", String.valueOf(diskInfo.getTotalSpace()));
			params.put("usableSpace", String.valueOf(diskInfo.getUsableSpace()));

			String jsonStr = UrlRequestUtils.execute(url, params, UrlRequestUtils.Mode.GET);
			logger.debug("ret:" + jsonStr);

		} catch (Exception e) {
			logger.error("ERROR OCCUR " + e.getMessage());
		}

		try {
			Thread.sleep(10 * 1000);
		} catch (InterruptedException ie) {
			logger.error(ie.getMessage());
		}
	}

	/**
	 * 读取 logic-server 状态，0不正常，1正常
	 * 
	 * @return
	 */
	private static int getLogicServerStatus() {
		Session session = ServerConnectorMgr.getInstance().getServerSession("logic");

		int status = session != null ? 1 : 0;
		return status;
	}

	/**
	 * 获取 game-api 状态，0不正常，1正常
	 * 
	 * @return
	 */
	static private int getGameApiStatus() {
		/*
		 * try{ String result =
		 * UrlRequestUtils.execute(CHECK_GAME_API_STATUS_URL, null, Mode.GET);
		 * if (StringUtils.isEmpty(result)) { return 0; } return
		 * Integer.valueOf(result);
		 */
		return 0;
	}

}
