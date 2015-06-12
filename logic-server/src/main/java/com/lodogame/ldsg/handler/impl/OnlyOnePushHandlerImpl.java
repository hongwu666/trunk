package com.lodogame.ldsg.handler.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.OnlyOneRegBO;
import com.lodogame.ldsg.constants.BattleType;
import com.lodogame.ldsg.handler.BasePushHandler;
import com.lodogame.ldsg.handler.OnlyOnePusHandler;
import com.lodogame.ldsg.service.OnlyOneService;

public class OnlyOnePushHandlerImpl extends BasePushHandler implements OnlyOnePusHandler {

	private static final Logger logger = Logger.getLogger(OnlyOnePushHandlerImpl.class);

	@Autowired
	private OnlyOneService onlyOneService;

	@Override
	public void pushUserInfo(String userId) {

		logger.debug("推送千人斩用户信息.userId[" + userId + "]");

		OnlyOneRegBO onlyOneRegBO = this.onlyOneService.getRegBO(userId);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", userId);
		params.put("uinfo", onlyOneRegBO);

		this.push("OnlyOne.pushUserInfo", params);

	}

	@Override
	public void pushBattle(String userId, Map<String, String> params) {

		logger.debug("推送千人斩战斗.userId[" + userId + "], params[" + params + "]");

		CommonDropBO commonDropBO = new CommonDropBO();
		// commonDropBO.setCopper(Integer.parseInt(params.get("copper")));

		boolean isAttack = StringUtils.equalsIgnoreCase(userId, params.get("attackUserId"));
		boolean isAttackWin = Integer.parseInt(params.get("flag")) == 1;

		int rf = 0;
		if (isAttack && isAttackWin) {
			rf = 1;
		} else if (!isAttack && !isAttackWin) {
			rf = 1;
		}

		Map<String, Object> pushParam = new HashMap<String, Object>();
		pushParam.put("uid", userId);
		pushParam.put("aun", params.get("attackUsername"));
		pushParam.put("dun", params.get("defenseUsername"));
		pushParam.put("dr", commonDropBO);
		pushParam.put("point", params.get("point"));
		pushParam.put("rf", rf);
		pushParam.put("rp", params.get("report"));
		pushParam.put("at", isAttack ? 1 : 0);
		pushParam.put("tp", BattleType.ONLY_ONE);

		this.push("OnlyOne.pushBattle", pushParam);
	}

}
