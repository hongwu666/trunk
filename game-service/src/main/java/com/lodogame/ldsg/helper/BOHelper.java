package com.lodogame.ldsg.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.BeanUtils;

import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.bo.CopyActivityBO;
import com.lodogame.ldsg.bo.DropToolBO;
import com.lodogame.ldsg.bo.ExpeditionShowBO;
import com.lodogame.ldsg.bo.ExpeditionStepBO;
import com.lodogame.ldsg.bo.GemAltarUserLogBO;
import com.lodogame.ldsg.bo.GemBO;
import com.lodogame.ldsg.bo.GemOpenBO;
import com.lodogame.ldsg.bo.GemPackBO;
import com.lodogame.ldsg.bo.MeridianMeridianBO;
import com.lodogame.ldsg.bo.ResourceJdBo;
import com.lodogame.ldsg.bo.ResourceSsBo;
import com.lodogame.ldsg.bo.ResourceViewBO;
import com.lodogame.ldsg.bo.SystemActivityDrawShowBO;
import com.lodogame.ldsg.bo.SystemGoldSetBO;
import com.lodogame.ldsg.bo.SystemMallBO;
import com.lodogame.ldsg.bo.TreasureGiftBo;
import com.lodogame.ldsg.bo.TreasurePriceBo;
import com.lodogame.ldsg.bo.TreasureReturnBO;
import com.lodogame.ldsg.bo.TreasureShowBO;
import com.lodogame.ldsg.bo.UserArenaGiftBO;
import com.lodogame.ldsg.bo.UserBO;
import com.lodogame.ldsg.bo.UserDailyTaskBO;
import com.lodogame.ldsg.bo.UserForcesBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.bo.UserTaskBO;
import com.lodogame.ldsg.bo.UserToolBO;
import com.lodogame.ldsg.constants.InitDefine;
import com.lodogame.ldsg.constants.ToolId;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.model.ExpeditionNum;
import com.lodogame.model.GemAltarUserAutoSell;
import com.lodogame.model.GemAltarUserInfo;
import com.lodogame.model.GemAltarUserOpen;
import com.lodogame.model.GemAltarUserPack;
import com.lodogame.model.MeridianUserInfo;
import com.lodogame.model.ResourceGk;
import com.lodogame.model.ResourceGkPkLog;
import com.lodogame.model.ResourceGkStart;
import com.lodogame.model.SystemActivity;
import com.lodogame.model.SystemActivityDrawShow;
import com.lodogame.model.SystemDailyTask;
import com.lodogame.model.SystemGoldSet;
import com.lodogame.model.SystemMall;
import com.lodogame.model.SystemMonster;
import com.lodogame.model.SystemTask;
import com.lodogame.model.TreasureUserInfo;
import com.lodogame.model.User;
import com.lodogame.model.UserArenaSeriesGift;
import com.lodogame.model.UserDailyTask;
import com.lodogame.model.UserExInfo;
import com.lodogame.model.UserExpeditionVsTable;
import com.lodogame.model.UserExtinfo;
import com.lodogame.model.UserForces;
import com.lodogame.model.UserTask;
import com.lodogame.model.UserTool;
import com.lodogame.model.log.GemAltarUserSellLog;

/**
 * bo帮助类
 * 
 * @author jacky
 * 
 */
public class BOHelper {

	/**
	 * 从怪物模型创建战斗英雄模型
	 * 
	 * @param systemMonster
	 * @return
	 */
	public static BattleHeroBO createBattleHeroBO(SystemMonster systemMonster) {
		BattleHeroBO battleHeroBO = new BattleHeroBO();
		BeanUtils.copyProperties(systemMonster, battleHeroBO);
		battleHeroBO.setSystemHeroId(systemMonster.getHeroModel());
		battleHeroBO.setName(systemMonster.getMonsterName());
		battleHeroBO.setLevel(systemMonster.getMonsterLevel());
		battleHeroBO.addSkill(systemMonster.getSkill1());
		battleHeroBO.addSkill(systemMonster.getSkill2());
		battleHeroBO.addSkill(systemMonster.getSkill3());
		battleHeroBO.addSkill(systemMonster.getSkill4());

		battleHeroBO.setMaxLife(systemMonster.getLife());

		return battleHeroBO;
	}

	/**
	 * 从用户武将模型创建战斗武将模型
	 * 
	 * @param userHeroBO
	 * @return
	 */
	public static BattleHeroBO createBattleHeroBO(UserHeroBO userHeroBO) {
		BattleHeroBO battleHeroBO = new BattleHeroBO();
		BeanUtils.copyProperties(userHeroBO, battleHeroBO);
		battleHeroBO.addSkill(userHeroBO.getSkill1());
		battleHeroBO.addSkill(userHeroBO.getSkill2());
		battleHeroBO.addSkill(userHeroBO.getSkill3());
		battleHeroBO.addSkill(userHeroBO.getSkill4());
		battleHeroBO.addSkill(userHeroBO.getSkill5());
		battleHeroBO.setMaxLife(battleHeroBO.getLife() > userHeroBO.getMaxLife() ? battleHeroBO.getLife() : userHeroBO.getMaxLife());
		battleHeroBO.setHit(Math.round((battleHeroBO.getHit() / 100f)));
		return battleHeroBO;
	}

	/**
	 * 创建充值套餐BO
	 * 
	 * @param userTask
	 * @return
	 */
	public static SystemGoldSetBO createSystemGoldSetBO(SystemGoldSet systemGoldSet) {
		SystemGoldSetBO systemGoldSetBO = new SystemGoldSetBO();
		BeanUtils.copyProperties(systemGoldSet, systemGoldSetBO);
		return systemGoldSetBO;
	}

	/**
	 * 创建用户BO
	 * 
	 * @param user
	 * @return
	 */
	public static UserBO craeteUserBO(User user, UserExtinfo userExtinfo) {

		UserBO userBO = new UserBO();
		userBO.setCoin(user.getCoin());
		userBO.setCopper(user.getCopper());
		userBO.setExp(user.getExp());
		userBO.setGoldNum(user.getGoldNum());
		userBO.setLevel(user.getLevel());
		userBO.setPlayerId(user.getLodoId());
		userBO.setPower(user.getPower());
		userBO.setUsername(user.getUsername());
		userBO.setReputation(user.getReputation());
		userBO.setPowerAddTime(user.getPowerAddTime().getTime() + InitDefine.POWER_ADD_INTERVAL);
		userBO.setPowerAddInterval(InitDefine.POWER_ADD_INTERVAL);
		userBO.setVipLevel(user.getVipLevel());
		userBO.setBannedLeftTime(user.getBannedChatTime() != null ? user.getBannedChatTime().getTime() : 0);
		userBO.setMuhon(user.getMuhon());
		userBO.setSoul(user.getSoul());
		userBO.setEnergy(user.getEnergy());
		userBO.setImgId(user.getImgId());
		userBO.setHonour(user.getHonour());
		userBO.setMingwen(user.getMingwen());
		userBO.setSkill(user.getSkill());
		if (userExtinfo != null) {
			userBO.setHeroBagLimit(userExtinfo.getHeroMax());
			userBO.setEquipBagLimit(userExtinfo.getEquipMax());
			userBO.setGuideStep(userExtinfo.getGuideStep());
		} else {
			userBO.setHeroBagLimit(InitDefine.HERO_BAG_INIT);
			userBO.setEquipBagLimit(InitDefine.EQUIP_BAG_INIT);
			userBO.setGuideStep(InitDefine.INIT_GUIDE_STEP);
		}

		int buyCoppertimes = 1;

		if (userExtinfo != null && DateUtils.isSameDay(new Date(), userExtinfo.getLastBuyCopperTime())) {
			buyCoppertimes = userExtinfo.getBuyCopperTimes() + 1;
		}

		if (userExtinfo.getRewardVipLevel() > user.getVipLevel()) {
			userBO.setVipLevel(userExtinfo.getRewardVipLevel());
		}

		userBO.setBuyCopperInfo(CopperHelper.getCopperInfo(buyCoppertimes, user.getLevel()));

		return userBO;
	}

	/**
	 * 创建用户道具BO
	 * 
	 * @param userTool
	 * @return
	 */
	public static UserToolBO createUsetToolBO(UserTool userTool) {
		UserToolBO userToolBO = new UserToolBO();
		BeanUtils.copyProperties(userTool, userToolBO);
		return userToolBO;
	}

	/**
	 * 创建用户部队ID
	 * 
	 * @param userForces
	 * @return
	 */
	public static UserForcesBO createUserForcesBO(UserForces userForces) {
		UserForcesBO userForcesBO = new UserForcesBO();
		BeanUtils.copyProperties(userForces, userForcesBO);
		return userForcesBO;
	}

	/**
	 * 创建道具商城BO
	 * 
	 * @param systemMall
	 * @return
	 */
	public static SystemMallBO createSystemMallBO(SystemMall systemMall) {
		SystemMallBO systemMallBO = new SystemMallBO();
		BeanUtils.copyProperties(systemMall, systemMallBO);
		return systemMallBO;
	}

	/**
	 * 创建抽奖展示道具BO
	 * 
	 * @param systemActivityDrawShow
	 * @return
	 */
	public static SystemActivityDrawShowBO createSystemActivityDrawShowBO(SystemActivityDrawShow systemActivityDrawShow) {
		SystemActivityDrawShowBO systemActivityDrawShowBO = new SystemActivityDrawShowBO();
		BeanUtils.copyProperties(systemActivityDrawShow, systemActivityDrawShowBO);
		return systemActivityDrawShowBO;
	}

	/**
	 * 转成
	 * 
	 * @param systemActivity
	 * @return
	 */
	public static CopyActivityBO crateCopyActivityBO(SystemActivity systemActivity) {
		CopyActivityBO copyActivityBO = new CopyActivityBO();
		copyActivityBO.setActivityDesc(systemActivity.getActivityDesc());
		copyActivityBO.setActivityName(systemActivity.getActivityName());
		copyActivityBO.setActivityId(systemActivity.getActivityId());
		copyActivityBO.setStartTime(systemActivity.getStartTime().getTime());
		copyActivityBO.setEndTime(systemActivity.getEndTime().getTime());
		copyActivityBO.setSceneId(NumberUtils.toInt(systemActivity.getParam()));
		return copyActivityBO;
	}

	public static void addUserToolBOToMap(UserToolBO userToolBO, Map<Integer, UserToolBO> toolBOs) {
		int toolId = userToolBO.getToolId();

		UserToolBO toolBO = toolBOs.get(userToolBO.getToolId());
		if (toolBO != null) {
			toolBO.setToolNum(toolBO.getToolNum() + userToolBO.getToolNum());
		} else {
			toolBO = new UserToolBO(userToolBO.getUserId(), toolId, userToolBO.getToolNum(), userToolBO.getToolType());
			toolBOs.put(toolId, toolBO);
		}
	}

	public static void addUserToolBOListToMap(List<UserToolBO> toolBOList, Map<Integer, UserToolBO> toolBOs) {
		String userId = toolBOList.get(0).getUserId();

		for (UserToolBO bo : toolBOList) {
			UserToolBO userToolBO = toolBOs.get(bo.getToolId());
			if (userToolBO != null) {
				userToolBO.setToolNum(bo.getToolNum() + userToolBO.getToolNum());
			} else {
				userToolBO = new UserToolBO(userId, bo.getToolId(), bo.getToolNum(), bo.getToolType());
				toolBOs.put(bo.getToolId(), userToolBO);
			}
		}
	}

	public static UserDailyTaskBO createUserDailyTaskBO(SystemDailyTask dailyTask, UserDailyTask userTask) {
		UserDailyTaskBO bo = new UserDailyTaskBO();
		bo.setFinishTimes(userTask.getFinishTimes());
		bo.setImgId(dailyTask.getImgId());
		bo.setNeedFinishTimes(dailyTask.getTargetValue());
		bo.setSort(dailyTask.getSort());
		bo.setStatus(userTask.getStatus());
		bo.setTaskDesc(dailyTask.getDesc());
		bo.setTaskId(dailyTask.getId());
		bo.setTaskName(dailyTask.getName());
		bo.setTaskType(dailyTask.getType());
		bo.setUserId(userTask.getUserId());
		List<DropToolBO> dropToolBOList = DropToolHelper.parseDropTool(dailyTask.getRewards());
		bo.setDropToolBO(dropToolBOList);
		return bo;
	}

	/**
	 * 创建用户任务BO
	 * 
	 * @param userTask
	 * @return
	 */
	public static UserTaskBO crateUserTaskBO(SystemTask systemTask, UserTask userTask) {
		UserTaskBO userTaskBO = new UserTaskBO();
		BeanUtils.copyProperties(userTask, userTaskBO);
		if (systemTask != null) {
			userTaskBO.setNeedFinishTimes(systemTask.getNeedFinishTimes());
			userTaskBO.setTaskName(systemTask.getTaskName());
			userTaskBO.setTaskDesc(systemTask.getTaskDesc());
			userTaskBO.setSort(systemTask.getSort());
			userTaskBO.setImgId(systemTask.getImgId());
			userTaskBO.setTaskType(systemTask.getTaskGroup());
			List<DropToolBO> dropToolBOList = DropToolHelper.parseDropTool(systemTask.getToolIds());
			// 特殊处理下金币
			int goldNum = systemTask.getGoldNum();
			if (goldNum > 0) {
				DropToolBO dropToolBo = new DropToolBO(ToolType.GOLD, ToolId.TOOL_GLOD_ID, goldNum);
				dropToolBOList.add(dropToolBo);
			}

			userTaskBO.setDropToolBO(dropToolBOList);
		}
		return userTaskBO;
	}

	public static TreasureGiftBo createTreasureGiftBo(int tb, int wh, int hl, int jy) {
		TreasureGiftBo bo = new TreasureGiftBo();
		bo.setHl(hl);
		bo.setTb(tb);
		bo.setJy(jy);
		bo.setWh(wh);
		return bo;
	}

	public static TreasurePriceBo createTreasurePriceBo(int tb, int wh, int hl, int jy) {
		TreasurePriceBo bo = new TreasurePriceBo();
		bo.setHl(hl);
		bo.setTb(tb);
		bo.setJy(jy);
		bo.setWh(wh);
		return bo;
	}

	public static TreasureShowBO createTreasureShowBO(TreasureUserInfo info) {

		TreasureShowBO bo = new TreasureShowBO();
		bo.setOpenNum(info.getTreasureNum());
		bo.setType(info.getTreasureType());
		return bo;
	}

	public static TreasureReturnBO createTreasureReturnBO(int gift, int t, int g, int p) {
		TreasureReturnBO bo = new TreasureReturnBO();
		bo.setTimes(t);
		bo.setGift(gift);
		bo.setNextGift(g);
		bo.setPrice(p);
		return bo;
	}

	public static ResourceViewBO createResourceViewBO(int max, int curr, List<ResourceGk> gk) {
		ResourceViewBO bo = new ResourceViewBO();
		bo.setMaxNum(max);
		bo.setUseNum(curr);
		List<Integer> its = new ArrayList<Integer>();
		for (ResourceGk temp : gk)
			its.add(temp.getFbDif());
		bo.setGk(its);
		return bo;
	}

	public static ResourceJdBo createResourceJdBo(ResourceGkPkLog log) {
		ResourceJdBo bo = new ResourceJdBo();
		bo.setG(log.getGk());
		bo.setStart(log.getStartLevel());
		return bo;
	}

	public static ResourceSsBo createrResourceSsBo(ResourceGkStart st) {
		ResourceSsBo bo = new ResourceSsBo();
		bo.setG(st.getGk());
		bo.setSs(st.getStartLevel());
		return bo;
	}

	public static List<MeridianMeridianBO> getMeridianBO(List<MeridianUserInfo> info) {
		List<MeridianMeridianBO> list = new ArrayList<MeridianMeridianBO>();
		for (MeridianUserInfo temp : info) {
			list.add(getMeridianBO(temp));
		}
		return list;
	}

	public static MeridianMeridianBO getMeridianBO(MeridianUserInfo info) {
		MeridianMeridianBO bo = new MeridianMeridianBO();
		BeanUtils.copyProperties(info, bo);
		return bo;
	}

	public static List<UserArenaGiftBO> getArenaGiftBOs(List<UserArenaSeriesGift> gift) {
		List<UserArenaGiftBO> bos = new ArrayList<UserArenaGiftBO>();
		for (UserArenaSeriesGift temp : gift) {
			bos.add(getArenaGiftBO(temp));
		}
		return bos;
	}

	public static UserArenaGiftBO getArenaGiftBO(UserArenaSeriesGift gift) {
		UserArenaGiftBO bo = new UserArenaGiftBO();
		bo.setCount(gift.getWinCount());
		bo.setNum(gift.getNum());
		return bo;
	}

	public static GemBO getGemBO(GemAltarUserInfo info) {
		GemBO bo = new GemBO();
		bo.setOpen(getGemOpenBOs(info.getOpen()));
		bo.setPacks(getgemPackBOs(info.getPack()));
		bo.setAuto(getGemAtuo(info.getSell()));
		bo.setLog(getGemLogs(info.getLogs()));
		return bo;
	}

	public static List<GemAltarUserLogBO> getGemLogs(List<GemAltarUserSellLog> log) {
		List<GemAltarUserLogBO> list = new ArrayList<GemAltarUserLogBO>();
		for (GemAltarUserSellLog temp : log) {
			list.add(getGemLog(temp));
		}
		return list;
	}

	public static GemAltarUserLogBO getGemLog(GemAltarUserSellLog log) {
		GemAltarUserLogBO bo = new GemAltarUserLogBO();
		BeanUtils.copyProperties(log, bo);
		return bo;
	}

	public static List<Integer> getGemAtuo(List<GemAltarUserAutoSell> a) {
		List<Integer> list = new ArrayList<Integer>();
		for (GemAltarUserAutoSell temp : a) {
			list.add(temp.getLevels());
		}
		return list;
	}

	public static List<GemPackBO> getgemPackBOs(List<GemAltarUserPack> packs) {
		List<GemPackBO> bos = new ArrayList<GemPackBO>();
		for (GemAltarUserPack temp : packs) {
			bos.add(getGemPackBO(temp));
		}
		return bos;
	}

	public static List<GemOpenBO> getGemOpenBOs(List<GemAltarUserOpen> open) {
		List<GemOpenBO> bos = new ArrayList<GemOpenBO>();
		for (GemAltarUserOpen temp : open) {
			bos.add(getgemOpenBO(temp));
		}
		return bos;
	}

	public static GemPackBO getGemPackBO(GemAltarUserPack pack) {
		GemPackBO bo = new GemPackBO();
		bo.setDate(pack.getCreateTime());
		bo.setStoneId(pack.getStoneId());
		return bo;
	}

	public static GemOpenBO getgemOpenBO(GemAltarUserOpen open) {
		GemOpenBO bo = new GemOpenBO();
		bo.setGroups(open.getGroups());
		return bo;
	}

	public static ExpeditionShowBO getExShowBo(UserExInfo info, ExpeditionNum num, List<UserHeroBO> hero, String modelId) {
		int n = num == null ? 0 : num.getNum();
		ExpeditionShowBO bo = new ExpeditionShowBO();

		bo.setBo(getExStepBo(info.getLastVsTable()));
		bo.getBo().setModelId(modelId);
		bo.setNum(n);
		bo.setHero(hero);
		return bo;
	}

	/*
	 * public static List<ExpeditionStepBO>
	 * getExStepBos(List<UserExpeditionVsTable> tab){ List<ExpeditionStepBO> bo
	 * = new ArrayList<ExpeditionStepBO>(); for(UserExpeditionVsTable temp :
	 * tab) bo.add(getExStepBo(temp)); return bo; }
	 */

	public static ExpeditionStepBO getExStepBo(UserExpeditionVsTable tab) {
		ExpeditionStepBO bo = new ExpeditionStepBO();
		bo.setBoxStat(tab.getBoxStat());
		bo.setExId(tab.getExId());
		bo.setIndex(tab.getIndex());
		bo.setStat(tab.getStat());
		return bo;
	}

}
