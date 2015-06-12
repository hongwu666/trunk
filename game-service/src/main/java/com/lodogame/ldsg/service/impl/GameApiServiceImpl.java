package com.lodogame.ldsg.service.impl;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.PaymentLogDao;
import com.lodogame.game.dao.SystemActivityDao;
import com.lodogame.game.dao.SystemForcesDao;
import com.lodogame.game.dao.SystemGoldSetDao;
import com.lodogame.game.dao.SystemMailDao;
import com.lodogame.game.dao.SystemMallDiscountDao;
import com.lodogame.game.dao.SystemToolDao;
import com.lodogame.game.dao.SystemVipLevelDao;
import com.lodogame.game.dao.UserMapperDao;
import com.lodogame.game.dao.UserMonthlyCardDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.game.utils.IDGenerator;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.constants.ActivityId;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.service.ActivityService;
import com.lodogame.ldsg.service.EquipService;
import com.lodogame.ldsg.service.GameApiService;
import com.lodogame.ldsg.service.GoldSetService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.MailService;
import com.lodogame.ldsg.service.MessageService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UnSynLogService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.Command;
import com.lodogame.model.PaymentLog;
import com.lodogame.model.SystemActivity;
import com.lodogame.model.SystemGoldSet;
import com.lodogame.model.User;
import com.lodogame.model.UserMapper;
import com.lodogame.model.UserMonthlyCard;

public class GameApiServiceImpl implements GameApiService {

	public static Logger LOG = Logger.getLogger(GameApiServiceImpl.class);

	public final static int USER_NOT_EXIST = 2001;

	private static Logger paymentLog = Logger.getLogger("paymentLog");

	@Autowired
	private SystemMallDiscountDao systemMallDiscountDao;

	@Autowired
	private UserService userService;

	@Autowired
	private PaymentLogDao paymentLogDao;

	@Autowired
	private UserMapperDao userMapperDao;

	@Autowired
	private HeroService heroService;

	@Autowired
	private EquipService equipService;

	@Autowired
	private CommandDao commandDao;

	@Autowired
	private GoldSetService goldSetService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private SystemToolDao systemToolDao;

	@Autowired
	private ToolService toolService;

	@Autowired
	private MailService mailService;

	@Autowired
	private SystemGoldSetDao systemGoldSetDao;
	@Autowired
	private UnSynLogService unSynLogService;

	@Autowired
	private SystemActivityDao systemActivityDao;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private SystemMailDao systemMailDao;

	@Autowired
	private SystemForcesDao systemForcesDao;

	@Autowired
	private SystemVipLevelDao systemVipLevelDao;

	@Autowired
	private UserMonthlyCardDao userMonthlyCardDao;

	public UserMapperDao getUserMapperDao() {
		return userMapperDao;
	}

	public void setUserMapperDao(UserMapperDao userMapperDao) {
		this.userMapperDao = userMapperDao;
	}

	@Override
	public UserMapper loadUserMapper(String partnerUserId, String serverId, String partnerId, String qn, String imei, String mac, String idfa, Integer closeReg) {
		if (StringUtils.isBlank(partnerUserId) || StringUtils.isBlank(serverId) || StringUtils.isBlank(partnerId)) {
			return null;
		}

		UserMapper userMapper = null;
		if (Config.ins().isFixServer()) {
			userMapper = userMapperDao.getByPartnerUserId(partnerUserId, partnerId, serverId);
		} else {
			userMapper = userMapperDao.getByPartnerUserId(partnerUserId, serverId);
		}

		if (userMapper == null) {

			boolean close = false;
			if (closeReg == null || closeReg == -1) {
				close = Config.ins().isCloeseReg();
			} else {
				close = closeReg == 1;
			}

			if (close) {// 关闭注册
				throw new ServiceException(ServiceReturnCode.SERVER_CLOSE_REG, "服务器人数已满意");
			}

			userMapper = new UserMapper();
			userMapper.setPartnerId(partnerId);
			userMapper.setPartnerUserId(partnerUserId);
			userMapper.setServerId(serverId);
			userMapper.setUserId(IDGenerator.getID());
			userMapper.setQn(qn);
			userMapper.setImei(imei);
			userMapper.setIdfa(idfa);
			userMapper.setMac(mac);
			userMapper.setCreatedTime(new Date());
			userMapper.setUpdatedTime(new Date());
			userMapperDao.save(userMapper);

		} else {
			userMapperDao.updateLoginDeviceInfo(userMapper.getUserId(), userMapper.getImei(), userMapper.getMac(), userMapper.getIdfa());
		}

		return userMapper;

	}

	public boolean doPayment(String partnerId, String serverId, String partnerUserId, String channel, String orderId, int waresId, BigDecimal amount, final int gold,
			String userIp, String remark) {

		paymentLog.info("do payment.partnerId[" + partnerId + "], serverId[" + serverId + "], partnerUserId[" + partnerUserId + "], channel[" + channel + "], orderId[" + orderId
				+ "], waresId[" + waresId + "] " + " amount[" + amount + "], gold[" + gold + "], userIp[" + userIp + "], remark[" + remark + "]");

		User user = getUser(partnerId, serverId, partnerUserId);
		String userId = user.getUserId();

		SystemGoldSet systemGoldSet = systemGoldSetDao.get(waresId);
		if (systemGoldSet.isMonthlyCard() == false) {
			// 如果购买的不是月卡，则要给用户加金币
			addGoldToUser(gold, user, userId);
		} else {
			updateUserMonthlyCardRecord(userId);
		}

		// 充值返回金币
		addReturnGoldToUser(amount, gold, user, userId);

		// 首冲双倍奖励
		addFirstPayDoubleGoldToUser(amount, gold, user, userId);

		savePaymentLog(partnerId, serverId, partnerUserId, channel, systemGoldSet.getType(), orderId, amount, gold, userIp, remark, user, userId);

		addCommand(userId, CommandType.COMMAND_PAY_COMPLETED, CommandType.PUSH_USER, amount.doubleValue());

		return true;
	}

	private void updateUserMonthlyCardRecord(String userId) {
		UserMonthlyCard card = userMonthlyCardDao.get(userId);
		if (card == null) {
			Date dueTime = getMonthlyCardDueTime(new Date());
			card = new UserMonthlyCard(userId, dueTime);
			userMonthlyCardDao.add(card);
		} else {
			Date date = new Date();
			if (date.before(card.getDueTime())) {
				date = card.getDueTime();
			}
			Date dueTime = getMonthlyCardDueTime(date);
			userMonthlyCardDao.updateDueTime(userId, dueTime);
		}

	}

	private Date getMonthlyCardDueTime(Date startTime) {
		Date date = DateUtils.addDays(startTime, 30);
		return DateUtils.getDateAtMidnight(date);
	}

	private void savePaymentLog(String partnerId, String serverId, String partnerUserId, String channel, int type, String orderId, BigDecimal amount, final int gold,
			String userIp, String remark, User user, final String userId) {
		PaymentLog paymentLog = new PaymentLog();
		paymentLog.setChannel(channel);
		paymentLog.setCreatedTime(new Date());
		paymentLog.setGold(gold);
		paymentLog.setAmount(amount);
		paymentLog.setType(type);
		paymentLog.setOrderId(orderId);
		paymentLog.setPartnerId(partnerId);
		paymentLog.setPartnerUserId(partnerUserId);
		paymentLog.setRemark(remark);
		paymentLog.setServerId(serverId);
		paymentLog.setUserId(userId);
		paymentLog.setUserIp(userIp);
		paymentLog.setUserName(user.getUsername());

		this.paymentLogDao.add(paymentLog);
	}

	private void addFirstPayDoubleGoldToUser(BigDecimal amount, final int gold, User user, String userId) {
		SystemGoldSet systemGoldSet = this.goldSetService.getByPayAmount(amount.doubleValue());
		if (systemGoldSet != null) {

			// 月卡
			if (systemGoldSet.isMonthlyCard()) {
				return;
			}

			if (this.activityService.isActivityOpen(ActivityId.FIRST_PAY_DOUBLE)) {// 活动的双倍
				SystemActivity systemActivity = this.systemActivityDao.get(ActivityId.FIRST_PAY_DOUBLE);

				boolean isByAmount = this.paymentLogDao.isByAmount(userId, amount.doubleValue(), systemActivity.getStartTime(), systemActivity.getEndTime());// 首冲双倍
				if (isByAmount == false) {
					boolean retSuccess = this.userService.addGold(userId, gold, ToolUseType.ADD_PAYMENT_FIRST_PAY_DOUBLE, user.getLevel());
					if (!retSuccess) {
						String message = "首充双倍赠送失败，更新用户金币失败.userId[" + userId + "]";
						LOG.error(message);
					}
				}

			} else {

				if (systemGoldSet.getIsDouble() == 1) {// 正常的双倍

					boolean isByAmount = this.paymentLogDao.isByAmount(userId, amount.doubleValue());// 首冲双倍
					if (isByAmount == false) {
						boolean retSuccess = this.userService.addGold(userId, gold, ToolUseType.ADD_PAYMENT_FIRST_PAY_DOUBLE, user.getLevel());
						if (!retSuccess) {
							String message = "首充双倍赠送失败，更新用户金币失败.userId[" + userId + "]";
							LOG.error(message);
						}
					}
				}
			}
		}
	}

	private void addReturnGoldToUser(BigDecimal amount, final int gold, User user, final String userId) {
		SystemGoldSet systemGoldSet = this.goldSetService.getByPayAmount(amount.doubleValue());
		if (systemGoldSet != null) {

			int returnGold = systemGoldSet.getGold() - gold;
			if (returnGold > 0) {// 有返还金币
				LOG.info("充值返还金币.amount[" + amount + "], gold[" + gold + "], returnGold[" + returnGold + "]");
				boolean retSuccess = this.userService.addGold(userId, returnGold, ToolUseType.ADD_PAYMENT_RETURN, user.getLevel());
				if (!retSuccess) {
					String message = "充值返还金币失败，更新用户金币失败.userId[" + userId + "]";
					LOG.error(message);
				}
			}

		} else {
			LOG.warn("充值返还失败.找不到套餐[" + amount.doubleValue() + "]");
		}
	}

	private User getUser(String partnerId, String serverId, String partnerUserId) {
		UserMapper userMapper = this.userMapperDao.getByPartnerUserId(partnerUserId, partnerId, serverId);

		if (userMapper == null) {
			userMapper = this.userMapperDao.getByPartnerUserId(partnerUserId, serverId);
			if (userMapper == null) {
				String message = "充值失败,用户不存在.partnerId[" + partnerId + "], serverId[" + serverId + "], partnerUserId[" + partnerUserId + "]";
				LOG.error(message);
				throw new ServiceException(PAYMENT_ERROR_USER_NOT_EXISTS, message);
			}
		}

		User user = this.userService.get(userMapper.getUserId());
		if (user == null) {

			String message = "充值失败,用户不存在.partnerId[" + partnerId + "], serverId[" + serverId + "], userId[" + userMapper.getUserId() + "]";
			LOG.error(message);
			throw new ServiceException(PAYMENT_ERROR_USER_NOT_EXISTS, message);
		}
		return user;
	}

	private boolean addGoldToUser(final int gold, User user, final String userId) {
		boolean success = this.userService.addGold(userId, gold, ToolUseType.ADD_PAYMENT, user.getLevel());
		if (!success) {
			String message = "充值失败，更新用户金币失败.userId[" + userId + "]";
			throw new ServiceException(ServiceReturnCode.FAILD, message);
		}
		return success;
	}

	/**
	 * 添加命令
	 * 
	 * @param userId
	 */
	private void addCommand(final String userId, int cmd, int type, double amount) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("amount", String.valueOf(amount));

		Command command = new Command();
		command.setCommand(cmd);
		command.setType(type);
		command.setParams(params);

		commandDao.add(command);
	}

	@Override
	public void sendSysMsg(String content, String partnerIds) {
		messageService.sendSystemMsg(content, partnerIds);
	}

	public static void main(String[] args) {
		BigDecimal b = new BigDecimal(Double.parseDouble("0.1"));
		BigDecimal a = new BigDecimal(0.1);
		System.out.println(b.compareTo(a));
	}

	@Override
	public List<SystemGoldSet> getPaySettings() {
		return systemGoldSetDao.getAll();
	}

	@Override
	public void delActivity(String activityId) {
		systemMallDiscountDao.delActivity(activityId);
	}

	@Override
	public void delItems(String activityId) {
		systemMallDiscountDao.delItems(activityId);
	}

	@Override
	public void sendMail(String title, String content, String toolIds, int target, String userLodoIds, String sourceId, Date date, String partner) {
		this.mailService.send(title, content, toolIds, target, userLodoIds, sourceId, date, partner);
	}

	@Override
	public boolean banUser(String userId, String dueTime) {
		boolean success = userService.banUser(userId, dueTime);
		return success;
	}

	@Override
	public boolean assignVipLevel(String userId, int vipLevel) {
		return userService.assignVipLevel(userId, vipLevel, true);
	}

	@Override
	public boolean createUser(String username, String userId) {

		boolean success = userService.create(userId, 1, username, new EventHandle() {

			public boolean handle(Event event) {
				return true;
			}

		});

		return success;
	}

	@Override
	public boolean addActivity(SystemActivity systemActivity) {
		boolean success = systemActivityDao.addActivity(systemActivity);
		commandDao.cacheReload("SystemActivityDaoCacheImpl");
		return success;
	}

	@Override
	public boolean modifyActivity(SystemActivity systemActivity) {
		boolean success = systemActivityDao.modifyActivity(systemActivity);
		commandDao.cacheReload("SystemActivityDaoCacheImpl");
		return success;

	}

	@Override
	public boolean updateUserLevel(String userId, int level, int exp) {
		return this.userService.updateUserLevel(userId, level, exp);
	}

	@Override
	public int modifyForcesTimes(int forcesId, int times) {
		return 0;
	}

	@Override
	public boolean dataSync(String table, String sqls) {
		return this.activityService.dataSync(table, sqls);
	}

}
