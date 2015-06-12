package com.lodogame.ldsg.ret;

import java.util.List;

import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.UserTavernBO;

public class TavernDrawRet {

	private UserTavernBO userTavernBO;

	private CommonDropBO commonDropBO;

	private List<String> userHeroIds;

	public TavernDrawRet(List<String> userHeroIds, UserTavernBO userTavernBO, CommonDropBO commonDropBO) {
		this.userHeroIds = userHeroIds;
		this.userTavernBO = userTavernBO;
		this.commonDropBO = commonDropBO;
	}

	public TavernDrawRet(List<String> userHeroIds, UserTavernBO userTavernBO) {
		this.userHeroIds = userHeroIds;
		this.userTavernBO = userTavernBO;
		this.commonDropBO = new CommonDropBO();
	}

	public UserTavernBO getUserTavernBO() {
		return userTavernBO;
	}

	public List<String> getUserHeroIds() {
		return userHeroIds;
	}

	public CommonDropBO getCommonDropBO() {
		return commonDropBO;
	}

}
