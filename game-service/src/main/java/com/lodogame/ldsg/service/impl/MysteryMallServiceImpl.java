package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.MysteryGroupRateDao;
import com.lodogame.game.dao.MysteryMallDropDao;
import com.lodogame.game.dao.SystemPriceDao;
import com.lodogame.game.dao.UserMysteryMallDao;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.DropDescBO;
import com.lodogame.ldsg.bo.UserMysteryMallDetailBO;
import com.lodogame.ldsg.bo.UserMysteryMallInfoBO;
import com.lodogame.ldsg.constants.MysteryMallType;
import com.lodogame.ldsg.constants.MysteryRefreshType;
import com.lodogame.ldsg.constants.PriceType;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.ToolId;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.MysteryMallHelper;
import com.lodogame.ldsg.helper.RollHalper;
import com.lodogame.ldsg.service.MysteryMallService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.ldsg.service.VipService;
import com.lodogame.model.MysteryGroupRate;
import com.lodogame.model.MysteryMallDrop;
import com.lodogame.model.SystemPrice;
import com.lodogame.model.User;
import com.lodogame.model.UserMysteryMallDetail;
import com.lodogame.model.UserMysteryMallInfo;

public class MysteryMallServiceImpl implements MysteryMallService {

	@Autowired
	private UserService userService;

	@Autowired
	private UserMysteryMallDao userMysteryMallDao;

	@Autowired
	private MysteryGroupRateDao mysteryGroupRateDao;

	@Autowired
	private MysteryMallDropDao mysteryMallDropDao;

	@Autowired
	private SystemPriceDao mysteryMallRefreshPriceDao;

	@Autowired
	private ToolService toolService;

	@Autowired
	private VipService vipService;

	@Override
	public boolean refresh(String userId, int mallType, int type, boolean useMoney) {

		Date now = new Date();

		UserMysteryMallInfo userMysteryMallInfo = userMysteryMallDao.getUserMysteryMallInfo(userId, mallType);
		if (userMysteryMallInfo == null) {
			userMysteryMallInfo = new UserMysteryMallInfo();
			userMysteryMallInfo.setUserId(userId);
			userMysteryMallInfo.setMallType(mallType);
			userMysteryMallInfo.setTimes(0);
			userMysteryMallInfo.setNormalTimes(0);
			userMysteryMallInfo.setUpdatedTime(now);
			userMysteryMallInfo.setLastRefreshTime(now);
		}

		if (!DateUtils.isSameDay(userMysteryMallInfo.getUpdatedTime(), now)) {
			userMysteryMallInfo.setTimes(0);
			userMysteryMallInfo.setNormalTimes(0);
		}

		int maxRefreshTimes = 5;
		User user = this.userService.get(userId);
		if (mallType == MysteryMallType.MYSTERY_MALL) {
			maxRefreshTimes = vipService.getRefreshMysteryMallTimes(user.getVipLevel());
		} else if (mallType == MysteryMallType.PK_MALL) {
			maxRefreshTimes = vipService.getRefreshPkMallTimes(user.getVipLevel());
		} else if (mallType == MysteryMallType.EXPEDITION_MALL) {
			maxRefreshTimes = Integer.MAX_VALUE;
		} else if (mallType == MysteryMallType.ONLY_ONY) {
			maxRefreshTimes = 20;
		}
		if (useMoney && userMysteryMallInfo.getTimes() >= maxRefreshTimes) {
			throw new ServiceException(REFRESH_NOT_ENOUGH_TIMES, "每日刷新次数超过限制");
		}

		// 玩家手动刷新
		if (useMoney) {
			// 扣钱
			this.costMoney(userId, mallType, type, userMysteryMallInfo.getNormalTimes() + 1, userMysteryMallInfo.getTimes() + 1);
		} else {
			userMysteryMallInfo.setLastRefreshTime(now);// 记录上次自动刷新的时间
		}

		userMysteryMallInfo.setUpdatedTime(now);

		// 计算掉落
		List<UserMysteryMallDetail> userMysteryMallDetailList = new ArrayList<UserMysteryMallDetail>();
		List<MysteryGroupRate> mysteryGroupRateList = mysteryGroupRateDao.getList(mallType, type);

		if (mallType == MysteryMallType.EXPEDITION_MALL) {

			MysteryGroupRate mysteryGroupRate = (MysteryGroupRate) RollHalper.roll(mysteryGroupRateList);
			List<MysteryMallDrop> mysteryMallDropList = mysteryMallDropDao.getList(mallType, type, mysteryGroupRate.getGroupId());

			for (MysteryMallDrop mysteryMallDrop : mysteryMallDropList) {
				UserMysteryMallDetail userMysteryMallDetail = new UserMysteryMallDetail();
				userMysteryMallDetail.setUserId(userId);
				userMysteryMallDetail.setDropId(mysteryMallDrop.getId());
				userMysteryMallDetail.setFlag(0);
				userMysteryMallDetail.setMallType(mallType);
				int amount = mysteryMallDrop.getAmount();
				userMysteryMallDetail.setAmount(amount);
				userMysteryMallDetail.setCreatedTime(now);
				userMysteryMallDetailList.add(userMysteryMallDetail);
			}

		} else {

			for (int i = 0; i < 5; i++) {
				// 获取掉落哪组
				MysteryGroupRate mysteryGroupRate = (MysteryGroupRate) RollHalper.roll(mysteryGroupRateList);
				List<MysteryMallDrop> mysteryMallDropList = mysteryMallDropDao.getList(mallType, type, mysteryGroupRate.getGroupId());
				MysteryMallDrop mysteryMallDrop = (MysteryMallDrop) RollHalper.roll(mysteryMallDropList);

				UserMysteryMallDetail userMysteryMallDetail = new UserMysteryMallDetail();
				userMysteryMallDetail.setUserId(userId);
				userMysteryMallDetail.setDropId(mysteryMallDrop.getId());
				userMysteryMallDetail.setFlag(0);
				userMysteryMallDetail.setMallType(mallType);
				int amount = (int) (mysteryMallDrop.getAmount() * ((95 + RandomUtils.nextInt(10)) / 100.0));
				userMysteryMallDetail.setAmount(amount);
				userMysteryMallDetail.setCreatedTime(now);
				userMysteryMallDetailList.add(userMysteryMallDetail);
			}
		}

		if (useMoney) {
			userMysteryMallInfo.setTimes(userMysteryMallInfo.getTimes() + 1);
			if (type == MysteryRefreshType.NORMAL) {
				userMysteryMallInfo.setNormalTimes(userMysteryMallInfo.getNormalTimes() + 1);
			}
		}

		userMysteryMallDao.addUserMysterMallInfo(userId, userMysteryMallInfo);
		userMysteryMallDao.addUserMysterMallDetail(userId, mallType, userMysteryMallDetailList);

		return true;
	}

	private boolean costMoney(String userId, int mallType, int type, int normalTimes, int times) {

		if (mallType == MysteryMallType.MYSTERY_MALL || mallType == MysteryMallType.PK_MALL || mallType == MysteryMallType.EXPEDITION_MALL || mallType == MysteryMallType.ONLY_ONY) {

			if (type == MysteryRefreshType.ADVANCED) {
				if (!this.userService.reduceGold(userId, 68, ToolUseType.STONE_MALL_EXCHANGE)) {
					throw new ServiceException(REFRESH_NOT_ENOUGH_GOLD, "刷新金币不足");
				}
			} else {
				if (mallType == MysteryMallType.EXPEDITION_MALL && times > 24) {
					times = 24;
				}
				SystemPrice systemPrice = mysteryMallRefreshPriceDao.get(MysteryMallHelper.getPriceType(mallType), times);
				int toolType = systemPrice.getToolType();
				int amount = systemPrice.getAmount();

				if (toolType == ToolType.GOLD) {
					if (!this.userService.reduceGold(userId, amount, ToolUseType.STONE_MALL_EXCHANGE)) {
						throw new ServiceException(REFRESH_NOT_ENOUGH_GOLD, "刷新金币不足");
					}
				} else if (toolType == ToolType.COPPER) {
					if (!this.userService.reduceCopper(userId, amount, ToolUseType.STONE_MALL_EXCHANGE)) {
						throw new ServiceException(REFRESH_NOT_ENOUGH_COPPER, "刷新银币不足");
					}
				} else if (toolType == ToolType.REPUTATION) {
					if (!this.userService.reduceReputation(userId, amount, ToolUseType.STONE_MALL_EXCHANGE)) {
						throw new ServiceException(REFRESH_NOT_ENOUGH_REPUTATION, "刷新威望不足");
					}
				} else if (toolType == ToolType.COIN) {
					if (!this.userService.reduceCoin(userId, amount, ToolUseType.STONE_MALL_EXCHANGE)) {
						throw new ServiceException(REFRESH_NOT_ENOUGH_COIN, "刷新硬币不足");
					}
				}

			}

		} else {
			throw new ServiceException(ServiceReturnCode.FAILD, "类型错误");
		}
		return false;

	}

	private List<UserMysteryMallDetail> getUserMallDetail(String userId, int mallType) {

		List<UserMysteryMallDetail> list = this.userMysteryMallDao.getUserMysteryMallDetail(userId, mallType);
		if (list.isEmpty()) {
			this.refresh(userId, mallType, MysteryRefreshType.NORMAL, false);
		}
		return this.userMysteryMallDao.getUserMysteryMallDetail(userId, mallType);
	}

	private UserMysteryMallInfo getUserMallInfo(String userId, int mallType) {

		UserMysteryMallInfo userMysteryMallInfo = this.userMysteryMallDao.getUserMysteryMallInfo(userId, mallType);

		if (userMysteryMallInfo == null || MysteryMallHelper.isNeedRefresh(mallType, userMysteryMallInfo.getLastRefreshTime())) {
			this.refresh(userId, mallType, MysteryRefreshType.NORMAL, false);
		}

		return this.userMysteryMallDao.getUserMysteryMallInfo(userId, mallType);
	}

	@Override
	public List<UserMysteryMallDetailBO> getUserMallDetailBO(String userId, int mallType) {

		List<UserMysteryMallDetailBO> userMysteryMallDetailBOList = new ArrayList<UserMysteryMallDetailBO>();

		List<UserMysteryMallDetail> userMysteryMallDetailList = getUserMallDetail(userId, mallType);
		for (UserMysteryMallDetail userMysteryMallDetail : userMysteryMallDetailList) {

			MysteryMallDrop mysteryMallDrop = mysteryMallDropDao.get(userMysteryMallDetail.getDropId());

			UserMysteryMallDetailBO userMysteryMallDetailBO = new UserMysteryMallDetailBO();
			userMysteryMallDetailBO.setAmount(userMysteryMallDetail.getAmount());
			userMysteryMallDetailBO.setToolId(mysteryMallDrop.getToolId());
			userMysteryMallDetailBO.setToolNum(mysteryMallDrop.getToolNum());
			userMysteryMallDetailBO.setToolType(mysteryMallDrop.getToolType());
			userMysteryMallDetailBO.setDropId(userMysteryMallDetail.getDropId());
			userMysteryMallDetailBO.setId(userMysteryMallDetail.getId());
			userMysteryMallDetailBO.setFlag(userMysteryMallDetail.getFlag());
			userMysteryMallDetailBO.setToolName(mysteryMallDrop.getToolName());
			userMysteryMallDetailBO.setRarity(mysteryMallDrop.getRarity());

			userMysteryMallDetailBOList.add(userMysteryMallDetailBO);
		}

		return userMysteryMallDetailBOList;
	}

	@Override
	public UserMysteryMallInfoBO getUserMysteryMallInfoBO(String userId, int mallType) {

		UserMysteryMallInfo userMysteryMallInfo = this.getUserMallInfo(userId, mallType);

		UserMysteryMallInfoBO userMysteryMallInfoBO = new UserMysteryMallInfoBO();
		int times = userMysteryMallInfo.getTimes();
		int normalTimes = userMysteryMallInfo.getNormalTimes();
		if (!DateUtils.isSameDay(userMysteryMallInfo.getUpdatedTime(), new Date())) {
			times = 0;
			normalTimes = 0;
		}
		userMysteryMallInfoBO.setTimes(times);
		userMysteryMallInfoBO.setNormalTimes(normalTimes);
		userMysteryMallInfoBO.setLastRefreshTime(userMysteryMallInfo.getLastRefreshTime());

		return userMysteryMallInfoBO;
	}

	@Override
	public CommonDropBO exchange(String userId, int mallType, int id, int type) {

		UserMysteryMallDetail userMysteryMallDetail = userMysteryMallDao.getUserMysteryMallDetail(userId, mallType, id);
		if (userMysteryMallDetail == null || userMysteryMallDetail.getFlag() != 0) {
			// 不能兑换
			throw new ServiceException(ServiceReturnCode.FAILD, "兑换失败，当前不可兑换");
		}

		MysteryMallDrop mysteryMallDrop = this.mysteryMallDropDao.get(userMysteryMallDetail.getDropId());
		int amout = userMysteryMallDetail.getAmount();
		if (mallType == MysteryMallType.MYSTERY_MALL) {
			if (type == 1) { // 新增灵魂不足用元宝替换
				int price = (int) Math.ceil(amout * 3.2f);
				if (!this.userService.reduceGold(userId, price, ToolUseType.STONE_MALL_EXCHANGE)) {
					throw new ServiceException(EXCHANGE_NOT_ENOUGH_TOOL, "兑换失败，道具不足");
				}
			} else {
				if (!this.toolService.reduceTool(userId, ToolType.MATERIAL, ToolId.JIANGSHAN_ORDER, amout, ToolUseType.STONE_MALL_EXCHANGE)) {
					// 江山领不足
					throw new ServiceException(EXCHANGE_NOT_ENOUGH_TOOL, "兑换失败，道具不足");
				}
			}
		} else if (mallType == MysteryMallType.PK_MALL) {
			if (!this.userService.reduceReputation(userId, amout, ToolUseType.REDUCE_PK_EXCHANGE)) {
				// 论剑声望不够
				throw new ServiceException(EXCHANGE_NOT_ENOUGH_TOOL, "兑换失败，声望不足");
			}
		} else if (mallType == MysteryMallType.EXPEDITION_MALL) {
			if (!this.userService.reduceCoin(userId, amout, ToolUseType.REDUCE_EXP_EXCHANGE)) {
				throw new ServiceException(EXCHANGE_NOT_ENOUGH_EXPEDITION, "兑换失败，硬币不足");
			}
		} else if (mallType == MysteryMallType.ONLY_ONY) {
			if (!this.userService.reduceHonour(userId, amout, ToolUseType.REDUCE_ONLYONE_EXCHANGE)) {
				throw new ServiceException(REFRESH_NOT_ENOUGH_HONOUR, "兑换失败，荣誉不足");
			}
		} else {
			throw new ServiceException(ServiceReturnCode.FAILD, "兑换失败，当前不可兑换");
		}

		// 给道具
		DropDescBO dropDescBO = new DropDescBO(mysteryMallDrop.getToolType(), mysteryMallDrop.getToolId(), mysteryMallDrop.getToolNum());
		CommonDropBO commonDropBO = this.toolService.give(userId, dropDescBO, ToolUseType.ADD_HERO_SOUL_MALL_EXCHANGE);

		this.userMysteryMallDao.updateUserMysteryMallFlag(userId, mallType, id);

		return commonDropBO;
	}
}
