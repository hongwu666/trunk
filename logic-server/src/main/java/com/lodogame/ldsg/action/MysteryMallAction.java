package com.lodogame.ldsg.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.SystemPriceDao;
import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.UserMysteryMallDetailBO;
import com.lodogame.ldsg.bo.UserMysteryMallInfoBO;
import com.lodogame.ldsg.constants.MysteryMallType;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.helper.MysteryMallHelper;
import com.lodogame.ldsg.service.MysteryMallService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.ldsg.service.VipService;
import com.lodogame.model.SystemPrice;
import com.lodogame.model.User;

/**
 * 神密商店action
 * 
 * @author sxw
 * 
 */

public class MysteryMallAction extends LogicRequestAction {

	@Autowired
	private MysteryMallService mysteryMallService;

	@Autowired
	private UserService userService;

	@Autowired
	private VipService vipService;

	@Autowired
	private SystemPriceDao systemPriceDao;

	@Autowired
	private PushHandler pushHandler;

	public Response refresh() {

		String userId = this.getUid();

		int mallType = this.getInt("mt", 1);
		int type = this.getInt("tp", -1);

		if (type != -1) {
			this.mysteryMallService.refresh(userId, mallType, type, true);
			pushHandler.pushUser(userId);
		}

		UserMysteryMallInfoBO userMysteryMallInfoBO = this.mysteryMallService.getUserMysteryMallInfoBO(userId, mallType);

		List<UserMysteryMallDetailBO> list = this.mysteryMallService.getUserMallDetailBO(userId, mallType);

		int advNeedToolType = 1;
		int adVNeedToolNum = 68;

		int maxRefreshTimes = 5;
		User user = this.userService.get(userId);
		if (mallType == MysteryMallType.MYSTERY_MALL) {
			maxRefreshTimes = vipService.getRefreshMysteryMallTimes(user.getVipLevel());
		} else if (mallType == MysteryMallType.PK_MALL) {
			maxRefreshTimes = vipService.getRefreshPkMallTimes(user.getVipLevel());
		}

		int times = userMysteryMallInfoBO.getTimes() + 1;
		if (mallType == MysteryMallType.EXPEDITION_MALL && times > 24) {
			times = 24;
		}

		SystemPrice systemPrice = systemPriceDao.get(MysteryMallHelper.getPriceType(mallType), times);

		set("nntp", systemPrice.getToolType());
		set("nnam", systemPrice.getAmount());
		set("adntp", advNeedToolType);
		set("adnam", adVNeedToolNum);
		set("mts", maxRefreshTimes);
		set("ts", userMysteryMallInfoBO.getTimes());
		set("nts", userMysteryMallInfoBO.getNormalTimes());
		set("rct", MysteryMallHelper.getNextRefreshTime(userMysteryMallInfoBO.getLastRefreshTime()));
		set("rcts", MysteryMallHelper.getNextRefreshTimeDesc(userMysteryMallInfoBO.getLastRefreshTime()));
		set("mls", list);

		return this.render();
	}

	public Response exchange() {

		String userId = this.getUid();
		int mallType = this.getInt("mt", 1);
		int id = this.getInt("sid", 0);
		int type = getInt("type", 2);// 1 yb 2 灵魂
		CommonDropBO commonDropBO = this.mysteryMallService.exchange(userId, mallType, id, type);

		set("dr", commonDropBO);
		if (type == 1 ||type == 2|| commonDropBO.isNeedPushUser()) {
			pushHandler.pushUser(userId);
		}
		return this.render();
	}
}
