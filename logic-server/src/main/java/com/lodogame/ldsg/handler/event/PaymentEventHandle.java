package com.lodogame.ldsg.handler.event;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.SystemGoldSetDao;
import com.lodogame.game.dao.SystemVipLevelDao;
import com.lodogame.game.dao.UserMonthlyCardDao;
import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.MailTarget;
import com.lodogame.ldsg.constants.RankType;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.PaymentEvent;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.DailyTaskService;
import com.lodogame.ldsg.service.EventService;
import com.lodogame.ldsg.service.MailService;
import com.lodogame.ldsg.service.TaskService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.SystemDailyTask;
import com.lodogame.model.SystemVipLevel;
import com.lodogame.model.User;
import com.lodogame.model.UserMonthlyCard;

public class PaymentEventHandle extends BaseEventHandle implements EventHandle {

	public static Logger logger = Logger.getLogger(PaymentEventHandle.class);

	@Autowired
	private PushHandler pushHandler;

	@Autowired
	private UserService userService;

	@Autowired
	private MailService mailService;

	@Autowired
	private SystemVipLevelDao systemVipLevelDao;

	@Autowired
	private TaskService taskService;

	@Autowired
	private DailyTaskService dailyTaskService;
	
	@Autowired
	private EventService eventService;

	@Autowired
	private SystemGoldSetDao systemGoldSetDao;

	@Autowired
	private UserMonthlyCardDao userMonthlyCardDao;

	@Override
	public boolean handle(Event event) {

		String userId = event.getUserId();
		User user = this.userService.get(userId);
		if (user == null) {
			logger.error("充值事件处理失败，用户不存在userId[" + userId + "]");
			return false;
		}

		Double amount = event.getDouble("amount");

		int oldVipLevel = user.getVipLevel();

		// 更新VIP等级
		this.userService.updateVipLevel(userId);
		user = this.userService.get(userId);
		int newVipLevel = user.getVipLevel();
		//更新周充排行榜
		eventService.addUpdateRankEvent(userId, RankType.PAY_WEEK.getValue(), amount.intValue());
		// 发送VIP礼包
		sendVipBag(userId, user.getLodoId() + "", amount, newVipLevel, oldVipLevel);

		// 检测是否有新的单笔充值奖励可领
		pushHandler.pushOncePayRewardActivated(userId);

		// 检测是否有新的累计充值奖励可领
		pushHandler.pushTotalPayRewardActivated(userId);
		
		// 推送用户
		pushHandler.pushUser(userId);

		taskService.updateTaskFinish(userId, 1, new EventHandle() {

			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {

			@Override
			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {

				if (taskTarget == TaskTargetType.PAYMENT) {
					return true;
				}
				return false;
			}
		});

		UserMonthlyCard card = userMonthlyCardDao.get(userId);
		if (card != null && !card.isExpires()) {
			dailyTaskService.updateTask(userId, SystemDailyTask.LINGQUYUEKA, 1);
		}

		return true;
	}

	private void sendVipBag(String userId, String lodoId, double amount, int newVipLevel, int oldVipLevel) {

		String content = "恭喜您成功充值了" + amount + "元，当前为VIP" + newVipLevel + "，您会获得相应的VIP特权，并赠送您VIP礼包，请及时查收！";

		for (int i = 0; i <= newVipLevel; i++) {
			if (i <= oldVipLevel) {
				continue;
			}

			SystemVipLevel systemVipLevel = this.systemVipLevelDao.get(i);
			if (systemVipLevel == null) {
				logger.error("礼包发放失败，没有对应的VIP配置，用户不存在userId[" + userId + "], vipLevel[" + i + "]");
				continue;
			}

			String title = "VIP" + systemVipLevel.getVipLevel() + "礼包";
			int toolType = ToolType.GIFT_BOX;
			int toolId = systemVipLevel.getVipGiftbag();
			if (toolId == 0) {
				continue;
			}
			int toolNum = 1;
			String toolIds = toolType + "," + toolId + "," + toolNum + "|";
			mailService.send(title, content, toolIds, MailTarget.USERS, lodoId, null, new Date(), null);

		}
	}

	@Override
	public String getInterestedEvent() {
		return PaymentEvent.class.getSimpleName();
	}

}
