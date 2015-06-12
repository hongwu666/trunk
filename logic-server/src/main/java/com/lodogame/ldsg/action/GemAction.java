package com.lodogame.ldsg.action;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.GemBO;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.GemService;

public class GemAction extends LogicRequestAction {

	@Autowired
	private GemService gemService;


	@Autowired
	private PushHandler pushHandler;

	public Response toPo() {
		String userId = getUid();
		gemService.toPo(userId);
		pushHandler.pushUser(userId);
		return render();
	}

	public Response show() {
		String userId = getUid();
		GemBO bo = gemService.show(userId);
		set("bo", bo);
		return render();
	}

	public Response open() {
		String userId = getUid();
		int g = getInt("groups", 1);
		Map<Integer, Long> maps = gemService.open(userId, g);

		pushHandler.pushUser(userId);

		set("stId", maps.get(1));
		set("groups", maps.get(2));
		set("price", maps.get(3));
		set("dates", maps.get(4));
		pushHandler.pushUser(userId);
		return render();
	}

	public Response getAllStone() {
		String userId = getUid();
		gemService.getAllStone(userId);
		return render();
	}

	public Response getStone() {
		String userId = getUid();
		int st = getInt("stoneId", 1);
		long date = getLong("dates", 0L);
		gemService.getStone(userId, st, date);
		return render();
	}

	public Response openAuto() {
		String userId = getUid();
		int level = getInt("levels", 1);
		gemService.openAuto(userId, level);
		return render();
	}

	public Response delAuto() {
		String userId = getUid();
		int level = getInt("levels", 1);
		gemService.delAtuo(userId, level);
		return render();
	}

}
