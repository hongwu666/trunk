package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.SystemActivityDao;
import com.lodogame.game.dao.SystemDrawDao;
import com.lodogame.game.dao.SystemDrawDetailDao;
import com.lodogame.game.dao.UserDrawDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DropDescBO;
import com.lodogame.ldsg.bo.UserDrawBO;
import com.lodogame.ldsg.constants.ActivityId;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.event.DropHeroEvent;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.RollHalper;
import com.lodogame.ldsg.service.ActivityService;
import com.lodogame.ldsg.service.DrawService;
import com.lodogame.ldsg.service.EventService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.SystemActivity;
import com.lodogame.model.SystemDraw;
import com.lodogame.model.SystemDrawDetail;
import com.lodogame.model.SystemHero;
import com.lodogame.model.User;
import com.lodogame.model.UserDrawLog;

public class DrawServiceImpl implements DrawService {

	@Autowired
	private SystemDrawDao systemDrawDao;

	@Autowired
	private UserDrawDao userDrawDao;

	@Autowired
	private SystemDrawDetailDao systemDrawDetailDao;

	@Autowired
	private ToolService toolService;

	@Autowired
	private UserService userService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private EventService eventService;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private SystemActivityDao systemActivityDao;

	@Override
	public List<UserDrawBO> getList(String userId) {

		List<UserDrawBO> ulist = new ArrayList<UserDrawBO>();
		List<SystemDraw> list = systemDrawDao.getList();

		for (SystemDraw systemDraw : list) {
			UserDrawBO bo = new UserDrawBO();
			List<DropDescBO> l = this.getDrawDescList(this.systemDrawDetailDao.getList(systemDraw.getId()));
			bo.setDropToolBOList(l);
			bo.setId(systemDraw.getId());
			bo.setMoneyType(systemDraw.getMoneyType());
			bo.setToolNum(systemDraw.getToolNum());
			bo.setToolId(systemDraw.getToolId());
			bo.setTitle(systemDraw.getTitle());
			int freeTimes = systemDraw.getFreeTimes();
			if (freeTimes > 0) {
				int userDrawTimes = userDrawDao.getDateDrawCount(userId);
				freeTimes = freeTimes - userDrawTimes;
				if (freeTimes <= 0) {
					freeTimes = 0;
				}
			}
			bo.setFreeTimes(freeTimes);
			ulist.add(bo);
		}

		return ulist;
	}

	private List<DropDescBO> getDrawDescList(List<SystemDrawDetail> list) {

		List<DropDescBO> dropDescBOList = new ArrayList<DropDescBO>();

		for (SystemDrawDetail systemDrawDetail : list) {
			if (systemDrawDetail.getDisplay() == 0) {
				continue;
			}
			DropDescBO bo = new DropDescBO(systemDrawDetail.getToolType(), systemDrawDetail.getToolId(), systemDrawDetail.getToolNum());
			dropDescBOList.add(bo);
		}

		return dropDescBOList;
	}

	@Override
	public CommonDropBO draw(String userId, int id) {

		if (!activityService.isActivityOpen(ActivityId.DRAW)) {
			String message = "抽奖失败，活动时间已过.userId[" + userId + "]";
			throw new ServiceException(DRAW_ACTIVITY_CLOSE, message);
		}

		SystemDraw systemDraw = this.systemDrawDao.get(id);
		int freeTimes = systemDraw.getFreeTimes();
		if (freeTimes > 0) {
			int userDrawTimes = userDrawDao.getDateDrawCount(userId);
			freeTimes = freeTimes - userDrawTimes;
		}

		int isFree = 1;

		if (freeTimes <= 0) {
			isFree = 0;
			this.costMoney(userId, systemDraw);
		}

		List<SystemDrawDetail> list = this.systemDrawDetailDao.getList(systemDraw.getId());
		SystemDrawDetail systemDrawDetail = (SystemDrawDetail) RollHalper.roll(list);

		this.saveUserDrawLog(userId, systemDraw, systemDrawDetail, isFree);

		int toolType = systemDrawDetail.getToolType();
		int toolId = systemDrawDetail.getToolId();
		int toolNum = systemDrawDetail.getToolNum();

		if (toolType == ToolType.HERO) {
			SystemHero systemHero = this.heroService.getSysHero(toolId);
			User user = this.userService.get(userId);
			SystemActivity systemActivity = systemActivityDao.get(ActivityId.DRAW);
			if (systemActivity != null) {
				DropHeroEvent toolDropEvent = new DropHeroEvent(userId, user.getUsername(), systemHero.getHeroName(), systemHero.getHeroStar(), systemActivity.getActivityName());
				eventService.dispatchEvent(toolDropEvent);
			}
		}

		CommonDropBO commonDropBO = toolService.give(userId, toolType, toolId, toolNum, ToolUseType.ADD_DRAW_DROP);

		return commonDropBO;
	}

	private void saveUserDrawLog(String userId, SystemDraw systemDraw, SystemDrawDetail systemDrawDetail, int isFree) {
		UserDrawLog userDrawLog = new UserDrawLog();
		userDrawLog.setCreatedTime(new Date());
		userDrawLog.setDate(DateUtils.getDate());
		userDrawLog.setIsFree(isFree);
		userDrawLog.setPoint(systemDraw.getPoint());
		userDrawLog.setSystemDrawId(systemDraw.getId());
		userDrawLog.setToolId(systemDrawDetail.getToolId());
		userDrawLog.setToolNum(systemDrawDetail.getToolNum());
		userDrawLog.setToolType(systemDrawDetail.getToolType());
		userDrawLog.setUserId(userId);
		this.userDrawDao.add(userDrawLog);
	}

	/**
	 * 抽奖扣钱
	 * 
	 * @param userId
	 * @param systemDraw
	 */
	private void costMoney(String userId, SystemDraw systemDraw) {

		if (systemDraw.getToolNum() <= 0) {
			throw new ServiceException(ServiceReturnCode.FAILD, "数据异常,抽奖需要的产具数量为[" + systemDraw.getToolNum() + "]");
		}

		if (systemDraw.getMoneyType() == 2) {

			if (!this.userService.reduceGold(userId, systemDraw.getToolNum(), ToolUseType.REDUCE_DRAW_COST)) {
				String message = "抽奖失败，钻石不足.userId[" + userId + "]";
				throw new ServiceException(DRAW_GOLD_NOT_ENOUGH, message);
			}

		} else if (systemDraw.getToolId() > 0) {
			if (!this.toolService.reduceTool(userId, ToolType.MATERIAL, systemDraw.getToolId(), systemDraw.getToolNum(), ToolUseType.REDUCE_DRAW_COST)) {
				String message = "抽奖失败，道具不足.userId[" + userId + "]";
				throw new ServiceException(DRAW_TOOL_NOT_ENOUGH, message);
			}
		} else {
			throw new ServiceException(ServiceReturnCode.FAILD, "数据异常");
		}

	}
}
