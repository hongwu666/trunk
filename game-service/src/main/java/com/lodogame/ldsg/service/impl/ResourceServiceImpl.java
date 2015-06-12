package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.ResourceDao;
import com.lodogame.game.dao.SystemForcesDao;
import com.lodogame.game.dao.SystemSceneDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.ldsg.bo.BattleBO;
import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.ResourceJdBo;
import com.lodogame.ldsg.bo.ResourceSsBo;
import com.lodogame.ldsg.bo.ResourceViewBO;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.constants.RankType;
import com.lodogame.ldsg.constants.ResourceConstant;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.event.BattleResponseEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.BOHelper;
import com.lodogame.ldsg.helper.DropToolHelper;
import com.lodogame.ldsg.service.BattleService;
import com.lodogame.ldsg.service.DailyTaskService;
import com.lodogame.ldsg.service.EventService;
import com.lodogame.ldsg.service.ForcesService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.ResourceService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.ResourceGk;
import com.lodogame.model.ResourceGkConfig;
import com.lodogame.model.ResourceGkPkLog;
import com.lodogame.model.ResourceGkStart;
import com.lodogame.model.ResourceNum;
import com.lodogame.model.ResourceUserInfo;
import com.lodogame.model.SystemDailyTask;
import com.lodogame.model.SystemForces;
import com.lodogame.model.User;

public class ResourceServiceImpl implements ResourceService {

	private static final Logger logger = Logger.getLogger(ResourceServiceImpl.class);

	private static AtomicInteger ids = new AtomicInteger(1);

	@Autowired
	private DailyTaskService dailyTaskService;

	@Autowired
	private ResourceDao resourceDao;

	@Autowired
	private UserService userService;

	@Autowired
	private ToolService toolService;

	@Autowired
	private SystemForcesDao systemForcesDao;

	@Autowired
	private HeroService heroService;

	@Autowired
	private SystemSceneDao systemSceneDao;

	@Autowired
	private ForcesService forcesService;

	@Autowired
	private EventService eventService;

	@Autowired
	private BattleService battleService;

	public void init() {
		ids = new AtomicInteger(resourceDao.getMaxIds());
	}

	public int reset(String user, int fbType, int fbDif) {
		ResourceUserInfo info = resourceDao.getInfo(user);
		if (info.getNum(fbType).getMaxNum() - info.getNum(fbType).getUsedNum() <= 0) {
			String message = "剩余次数不足";
			throw new ServiceException(ResourceService.NUM_NOT_HAVE, message);
		}
		info.getNum(fbType).setUsedNum(info.getNum(fbType).getUsedNum() + 1);
		resourceDao.updateNum(info.getNum(fbType));
		int id = info.reset(fbType, fbDif);
		if (id > 0)
			resourceDao.reset(id);
		return 0;
	}

	public ResourceViewBO show(String userId, int fbType) {
		ResourceUserInfo info = resourceDao.getInfo(userId);
		checkNum(info, fbType);
		List<ResourceGk> gks = info.getGks(fbType);
		return BOHelper.createResourceViewBO(info.getNum(fbType).getMaxNum(), info.getNum(fbType).getUsedNum(), gks);
	}

	private boolean isOpen(int fbType) {

		if (Config.ins().isDebug()) {
			return true;
		}

		Calendar c = Calendar.getInstance();

		int week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (fbType == ResourceConstant.CUSTOMS_1 || fbType == ResourceConstant.CUSTOMS_3) {
			return week == 1 || week == 3 || week == 5 || week == 0;
		} else {
			return week == 2 || week == 4 || week == 6 || week == 0;
		}

	}

	public List<ResourceSsBo> showSs(String userId, int fbType, int fbDif) {

		ResourceUserInfo info = resourceDao.getInfo(userId);

		ResourceGk gk = info.getGk(fbType, fbDif);

		List<ResourceGkStart> ss = gk.getStarts();
		List<ResourceSsBo> bo = new ArrayList<ResourceSsBo>();
		if (ss != null && ss.size() > 0) {
			for (ResourceGkStart temp : ss) {
				bo.add(BOHelper.createrResourceSsBo(temp));
			}
		}
		return bo;
	}

	public List<ResourceJdBo> showJd(String userId, int fbType, int fbDif) {
		if (!isOpen(fbType)) {
			String message = "不是开放时间";
			throw new ServiceException(ResourceService.NO_OPEN, message);
		}
		ResourceUserInfo info = resourceDao.getInfo(userId);
		ResourceGk gk = info.getGk(fbType, fbDif);
		List<ResourceJdBo> bo = new ArrayList<ResourceJdBo>();
		if (gk != null) {
			for (ResourceGkPkLog temp : gk.getLogs())
				bo.add(BOHelper.createResourceJdBo(temp));
		} else {
			if (info.getNum(fbType).getMaxNum() - info.getNum(fbType).getUsedNum() <= 0) {
				String message = "剩余次数不足";
				throw new ServiceException(ResourceService.NUM_NOT_HAVE, message);
			}
			info.getNum(fbType).setUsedNum(info.getNum(fbType).getUsedNum() + 1);
			gk = new ResourceGk(userId, ids.incrementAndGet(), fbType, fbDif);
			resourceDao.updateNum(info.getNum(fbType));
			resourceDao.insertGk(gk);
		}
		return bo;
	}

	public int buyNum(String userId, int num, int type) {
		int price = getBuyPrice(userId);
		if (!userService.reduceGold(userId, price, ToolUseType.REDUCE_RESOURCE)) {
			String message = "元宝不足";
			throw new ServiceException(ResourceService.YB_NOT_HAVE, message);
		}
		ResourceUserInfo info = resourceDao.getInfo(userId);
		if (info.getNum(type).getMaxNum() > 2) {
			String message = "一天只能购买一次";
			throw new ServiceException(ResourceService.NO_BUY, message);
		}
		info.getNum(type).setMaxNum(info.getNum(type).getMaxNum() + 1);
		resourceDao.updateNum(info.getNum(type));
		return info.getNum(type).getMaxNum();
	}

	public int getBuyPrice(String userId) {
		return 50;
	}

	public int startShow(String userId, int fbType, int fbDif, int g) {
		ResourceUserInfo info = resourceDao.getInfo(userId);
		ResourceGk gk = info.getGk(fbType, fbDif);
		ResourceGkStart st = info.getGkStart(fbType, fbDif, g);
		if (st == null) {
			st = new ResourceGkStart(gk.getId(), g, resourceDao.getStartByCurr(0), userId);
			gk.getStarts().add(st);
			resourceDao.insertGkStart(st);
		}

		eventService.addUpdateRankEvent(userId, RankType.RESOURCE_STAR.getValue(), st.getStartLevel());
		return st.getStartLevel();
	}

	public int startOneKey(String userId, int fbType, int fbDif, int g) {
		int price = g * 10;
		if (!userService.reduceGold(userId, price, ToolUseType.REDUCE_RESOURCE)) {
			String message = "元宝不足";
			throw new ServiceException(ResourceService.YB_NOT_HAVE, message);
		}
		userService.pushUser(userId);
		ResourceUserInfo info = resourceDao.getInfo(userId);
		ResourceGkStart st = info.getGkStart(fbType, fbDif, g);
		st.setStartLevel(ResourceConstant.MAX_START);
		resourceDao.updateGkStart(st);
		eventService.addUpdateRankEvent(userId, RankType.RESOURCE_STAR.getValue(), st.getStartLevel());
		return st.getStartLevel();
	}

	public int startUp(String userId, int fbType, int fbDif, int g) {
		int price = g;
		if (!userService.reduceGold(userId, price, ToolUseType.REDUCE_RESOURCE)) {
			String message = "元宝不足";
			throw new ServiceException(ResourceService.YB_NOT_HAVE, message);
		}
		ResourceUserInfo info = resourceDao.getInfo(userId);
		ResourceGkStart st = info.getGkStart(fbType, fbDif, g);
		if (st.getStartLevel() >= ResourceConstant.MAX_START) {
			String message = "已经满星";
			throw new ServiceException(ResourceService.START_MAX, message);
		}
		st.setStartLevel(resourceDao.getStartByCurr(st.getStartLevel()));
		resourceDao.updateGkStart(st);
		eventService.addUpdateRankEvent(userId, RankType.RESOURCE_STAR.getValue(), st.getStartLevel());
		return st.getStartLevel();
	}

	public void fight(final String userId, final int fbType, final int fbDif, final int g, int start, final EventHandle eventHandle) {
		if (start == 1) {
			startOneKey(userId, fbType, fbDif, g);
		}
		ResourceUserInfo u = resourceDao.getInfo(userId);
		ResourceGkStart star = u.getGkStart(fbType, fbDif, g);
		ResourceGkPkLog logs = u.getGkLog(fbType, fbDif, g);
		if (logs != null)
			return;
		ResourceGkConfig config = resourceDao.getGkConfig(fbType, fbDif, g, star.getStartLevel());
		User user = userService.get(userId);

		BattleBO attack = new BattleBO();
		List<BattleHeroBO> attackBO = this.heroService.getUserBattleHeroBOList(userId);
		attack.setUserLevel(user.getLevel());
		attack.setBattleHeroBOList(attackBO);

		BattleBO defense = new BattleBO();
		int forcesId = config.getNpc();

		SystemForces systemForces = this.systemForcesDao.get(forcesId);

		List<BattleHeroBO> battleHeroBOList = forcesService.getForcesHeroBOList(forcesId);
		if (battleHeroBOList.isEmpty()) {
			logger.error("部队的怪物列表为空.forcesId[" + forcesId + "]");
			throw new ServiceException(NO_GUAI, "这个副本没有怪物，请稍后再打");
		}
		defense.setBattleHeroBOList(battleHeroBOList);
		final String name = systemForces.getForcesName();
		battleService.fight(attack, defense, 12, new EventHandle() {

			public boolean handle(Event event) {
				if (event instanceof BattleResponseEvent) {
					int flag = event.getInt("flag");
					event.setObject("dun", name);
					CommonDropBO bo = null;
					if (flag == 1) {
						bo = pkEnd(userId, fbType, fbDif, g);
					}
					userService.pushUser(userId);
					event.setObject("gift", bo);
					eventHandle.handle(event);
				}
				return true;
			}
		});

	}

	private void checkNum(ResourceUserInfo info, int fbType) {
		if (info.getNum(fbType) == null) {
			List<ResourceNum> nums = resourceDao.getNum(info.getUserId(), DateUtils.getDate(new Date()));
			if (!isexist(nums, fbType)) {
				ResourceNum num = new ResourceNum();
				num.setCreatedTime(new Date());
				num.setMaxNum(1);
				num.setUsedNum(0);
				num.setType(fbType);
				num.setUserId(info.getUserId());
				info.setNum(num);
				resourceDao.insertNum(num);
			}

		}
	}

	public boolean isexist(List<ResourceNum> nums, int type) {
		if (nums == null) {
			return false;
		}
		for (ResourceNum num : nums) {
			if (num.getType() == type)
				return true;
		}
		return false;
	}

	@Override
	public CommonDropBO pkEnd(String userId, int fbType, int fbDif, int g) {
		ResourceUserInfo info = resourceDao.getInfo(userId);
		ResourceGk gk = info.getGk(fbType, fbDif);
		ResourceGkPkLog log = info.getGkLog(fbType, fbDif, g);
		ResourceGkStart st = info.getGkStart(fbType, fbDif, g);
		CommonDropBO bo = new CommonDropBO();
		gift(userId, gk, st, bo); // 星级发奖
		if (log == null) { // 是否创建新的纪录
			log = new ResourceGkPkLog(gk.getId(), g, st.getStartLevel(), userId);
			gk.getLogs().add(log);
			resourceDao.insertGkPkLog(log);
		} else {
			log.setStartLevel(st.getStartLevel());
			resourceDao.updateGkPkLog(log);
		}
		checkBox(userId, gk, bo);

		dailyTaskService.sendUpdateDailyTaskEvent(userId, SystemDailyTask.RESOURCE_FIGHT, 1);

		return bo;

	}

	private void gift(String userId, ResourceGk gk, ResourceGkStart star, CommonDropBO bo) {
		if (gk == null || star == null) {
			return;
		}
		ResourceGkConfig config = resourceDao.getGkConfig(gk.getFbType(), gk.getFbDif(), star.getGk(), star.getStartLevel());
		List<DropToolBO> dropToolBOList = DropToolHelper.parseDropTool(config.getGift());
		for (DropToolBO dropToolBO : dropToolBOList) {
			int toolType = dropToolBO.getToolType();
			int toolId = dropToolBO.getToolId();
			int toolNum = dropToolBO.getToolNum();

			List<DropToolBO> dropBOList = toolService.giveTools(userId, toolType, toolId, toolNum, ToolUseType.ADD_RESOURCE);

			for (DropToolBO dropBO : dropBOList) {
				this.toolService.appendToDropBO(userId, bo, dropBO);
			}
		}
	}

	private void checkBox(String userId, ResourceGk gk, CommonDropBO bo) {
		if (gk == null || gk.getLogs().size() < 5) {
			return;
		}
		boolean g = true;
		for (ResourceGkPkLog temp : gk.getLogs()) {
			if (temp.getStartLevel() != ResourceConstant.MAX_START) {
				g = false;
				break;
			}
		}
		if (g) {
			ResourceGkConfig config = resourceDao.getGkConfig(gk.getFbType(), gk.getFbDif(), ResourceConstant.FINAL_G, ResourceConstant.MAX_START);// 最后一关5星有宝箱
			List<DropToolBO> dropToolBOList = DropToolHelper.parseDropTool(config.getBox());
			for (DropToolBO dropToolBO : dropToolBOList) {
				int toolType = dropToolBO.getToolType();
				int toolId = dropToolBO.getToolId();
				int toolNum = dropToolBO.getToolNum();

				List<DropToolBO> dropBOList = toolService.giveTools(userId, toolType, toolId, toolNum, ToolUseType.ADD_RESOURCE);

				for (DropToolBO dropBO : dropBOList) {
					this.toolService.appendToDropBO(userId, bo, dropBO);
				}
			}

		}
	}
}
