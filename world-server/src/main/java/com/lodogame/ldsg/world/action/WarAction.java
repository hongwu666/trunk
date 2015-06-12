package com.lodogame.ldsg.world.action;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.remote.action.BaseAction;
import com.lodogame.game.remote.callback.Callback;
import com.lodogame.game.remote.handle.RemoteCallHandle;
import com.lodogame.game.remote.request.Request;
import com.lodogame.game.remote.response.Response;
import com.lodogame.game.utils.json.Json;

/**
 * 跨服战action
 * 
 * @author shixiangwen
 * 
 */
public class WarAction extends BaseAction {

	private static Logger logger = Logger.getLogger(WarAction.class);

	@Autowired
	private RemoteCallHandle remoteCallHandle;

	public Response reg(Request req) {

		String uid = req.get("uid", "");

		logger.debug("reg.uid[" + uid + "]");

		Response response = new Response();

		Request req2 = new Request();
		req2.setAction("WarRemote");
		req2.setMethod("userDetail");
		req2.put("uid", uid);

		remoteCallHandle.call("d1", req2, new Callback() {

			@Override
			public void handle(com.lodogame.game.remote.response.Response resp) {
				logger.info(Json.toJson(resp));
			}
		});

		return response;
	}

}
