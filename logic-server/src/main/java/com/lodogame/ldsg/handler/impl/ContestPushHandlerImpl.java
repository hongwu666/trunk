package com.lodogame.ldsg.handler.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.constants.BattleType;
import com.lodogame.ldsg.handler.BasePushHandler;
import com.lodogame.ldsg.handler.ContestPushHandler;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.ContestBattleReport;

public class ContestPushHandlerImpl extends BasePushHandler implements ContestPushHandler {

	@Autowired
	private UserService userService;

	@Autowired
	private HeroService heroService;

	@Override
	public void pushBattle(String userId, ContestBattleReport report) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", userId);
		params.put("rp", report.getReport());
		params.put("aun", report.getAttackUsername());
		params.put("dun", report.getDefenseUsername());

		int rf = report.getFlag();
		if (rf == 1) {
			if (StringUtils.equals(userId, report.getAttackUserId())) {
				params.put("rf", 1);
			} else {
				params.put("rf", 3);
			}
		} else {
			if (StringUtils.equals(userId, report.getAttackUserId())) {
				params.put("rf", 3);
			} else {
				params.put("rf", 1);
			}
		}

		if (!StringUtils.isEmpty(report.getWinInfo())) {
			params.put("wininfo", Json.toObject(report.getWinInfo(), Map.class));
		} else {
			params.put("wininfo", new HashMap<Integer, Integer>());
		}

		params.put("tp", BattleType.CONTEST);
		this.push("Contest.pushBattle", params);

	}

	@Override
	public void pushStatus(String userId, int status) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", userId);
		params.put("status", status);
		this.push("Contest.pushStatus", params);
	}

}
