package com.lodogame.ldsg.handler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.ResourceDao;
import com.lodogame.game.dao.TreasureDao;
import com.lodogame.game.dao.UnSynDao;
import com.lodogame.game.dao.UserDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.constants.Priority;
import com.lodogame.ldsg.service.UnSynLogService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.Command;
import com.lodogame.model.User;

public class Worker {

	private static final Logger LOG = Logger.getLogger(Worker.class);

	private boolean started = false;

	private Date lastPushMidnightTime = new Date();

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserService userService;

	@Autowired
	private CommandDao commandDao;

	@Autowired
	private UnSynLogService unSynLogService;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	@Autowired
	private UnSynDao unSynDao;

	@Autowired
	private TreasureDao treasureDao;

	@Autowired
	private ResourceDao resourceDao;

	private void work1() {

		try {
			Set<String> onlineUserIdList = this.userDao.getOnlineUserIdList();
			for (String userId : onlineUserIdList) {

				try {
					User user = this.userService.get(userId);
					if (user != null) {
						LOG.debug("体力恢复判断.userId[" + userId + "]");
						this.userService.checkPowerAdd(user);
					}
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}

		} catch (Throwable t) {
			LOG.error(t.getMessage(), t);
		}

		try {
			Thread.sleep(1000 * 5);
		} catch (InterruptedException ie) {
			LOG.debug(ie);
		}
	}

	private void work2() {

		try {

			while (true) {
				final Command command = this.commandDao.get(Priority.HIGH, Priority.NORMAL, Priority.LOWER, Priority.MESSAGE);
				if (command != null) {
					LOG.info("command[" + Json.toJson(command) + "]");
					final CommandHandler commandHandler = CommandHandlerFactory.getInstance().getHandler(command.getCommand());
					Runnable task = new Runnable() {

						@Override
						public void run() {
							commandHandler.handle(command);
						}
					};

					taskExecutor.execute(task);

				} else {
					break;
				}
			}

		} catch (Throwable t) {
			LOG.error(t.getMessage(), t);
			try {
				Thread.sleep(1000 * 5);
			} catch (InterruptedException ie) {
				LOG.debug(ie);
			}
		}

	}

	private void work3() {
		try {
			Set<String> onlineUserIds = userDao.getOnlineUserIdList();

			for (String userId : onlineUserIds) {
				Command command = new Command();
				command.setCommand(CommandType.COMMAND_CHECK_NOTIFICATION);
				command.setType(CommandType.PUSH_USER);
				Map<String, String> params = new HashMap<String, String>();
				params.put("userId", userId);
				command.setParams(params);

				commandDao.add(command);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		try {
			Thread.sleep(1000 * 60);
		} catch (InterruptedException ie) {
			LOG.debug(ie);
		}
	}

	/**
	 * 晚上12点推送
	 */
	private void work4() {

		try {

			Date now = new Date();
			if (!DateUtils.isSameDay(lastPushMidnightTime, now)) {

				LOG.debug("晚上12点消息推送");

				this.lastPushMidnightTime = now;

				Command command = new Command();
				command.setCommand(CommandType.COMMAND_PUSH_MIDNIGHT);
				command.setType(CommandType.PUSH_ALL);
				commandDao.add(command);
			}

		} catch (Throwable t) {
			LOG.error(t.getMessage(), t);
		}
		try {
			// 睡5分钟
			Thread.sleep(1000 * 10);
		} catch (InterruptedException ie) {
			LOG.debug(ie);
		}

	}

	private void work5() {
		try {
			if (DateUtils.isThisTime(0, 0)) {
				treasureDao.replace();
			}
		} catch (Throwable t) {
			LOG.error(t.getMessage(), t);
		}
		try {
			Thread.sleep(1000 * 60); // 1分钟
		} catch (Exception ie) {
			LOG.debug(ie);
		}
	}

	private void work6() {
		try {
			if (DateUtils.isThisTime(0, 0)) {
				resourceDao.replace();
			}
		} catch (Throwable t) {
			LOG.error(t.getMessage(), t);
		}
		try {
			Thread.sleep(1000 * 60); // 1分钟
		} catch (Exception ie) {
			LOG.debug(ie);
		}
	}

	public void init() {

		// stated = true;

		if (started) {
			return;
		}

		if (!Config.ins().isGameServer()) {
			return;
		}

		started = true;

		LOG.info("开始线程 ");

		new Thread(new Runnable() {

			public void run() {
				while (true) {
					work1();
				}
			}

		}).start();

		new Thread(new Runnable() {

			public void run() {
				while (true) {
					work2();
				}
			}

		}).start();

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					work4();
				}
			}

		}).start();

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					work3();
				}
			}

		}).start();

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					work5();
				}
			}

		}).start();

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					work6();
				}
			}

		}).start();
	}

}
