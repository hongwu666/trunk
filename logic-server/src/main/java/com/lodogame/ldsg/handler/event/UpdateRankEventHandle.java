package com.lodogame.ldsg.handler.event;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.RankDao;
import com.lodogame.ldsg.checker.TaskChecker;
import com.lodogame.ldsg.constants.RankType;
import com.lodogame.ldsg.constants.TaskTargetType;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.UpdateRankEvent;
import com.lodogame.ldsg.service.DailyTaskService;
import com.lodogame.ldsg.service.RankService;
import com.lodogame.ldsg.service.TaskService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.PayRankInfo;
import com.lodogame.model.ProcessRankInfo;
import com.lodogame.model.StarRankInfo;
import com.lodogame.model.VipRankInfo;

public class UpdateRankEventHandle extends BaseEventHandle{
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private DailyTaskService dailyTaskService;
	@Autowired
	private RankService rankService;
	@Autowired
	private UserService userService;
	@Autowired
	private RankDao rankDao;
	@Override
	public boolean handle(Event event) {
		int type=event.getInt("type",1);
		switch (RankType.getRankTypeByValue(type)) {
		case STORY:
			updateStoryRank(event);
			break;
		case LEVEL:
			updateUserLevelTask(event);	
			break;
		case ECTYPE_STAR:
			updateEctypeStarRank(event);
			break;
		case RESOURCE_STAR:
			updateResourceStarRank(event);
			break;
		case ELITE:
			updateEliteRank(event);
			break;
		case VIP:
			updateVipRank(event);
			break;
		case PAY_WEEK:
			updatePayRank(event);
			break;
		default:
			break;
		}
		return false;
	}
	/**
	 * 更新剧情副本进度榜
	 * @param event
	 */
	public void updateStoryRank(Event event){
		String userId = event.getUserId();
		ProcessRankInfo processRankInfo=new ProcessRankInfo();
		processRankInfo.setUserId(userId);
		processRankInfo.setUsername(userService.get(userId).getUsername());
		processRankInfo.setVip(userService.get(userId).getVipLevel());
		processRankInfo.setProce(event.getDouble("obj"));
		rankDao.updateStoryRank(processRankInfo);
		
	}
	/**
	 * 更新精英副本进度榜
	 * @param event
	 */
	public void updateEliteRank(Event event){
		String userId = event.getUserId();
		ProcessRankInfo processRankInfo=new ProcessRankInfo();
		processRankInfo.setUserId(userId);
		processRankInfo.setUsername(userService.get(userId).getUsername());
		processRankInfo.setVip(userService.get(userId).getVipLevel());
		processRankInfo.setProce(event.getDouble("obj"));
		rankDao.updateEliteRank(processRankInfo);
		
	}
	/**
	 * 更新副本星数榜
	 * @param event
	 */
	public void updateEctypeStarRank(Event event){
		String userId = event.getUserId();
		StarRankInfo starRankInfo=new StarRankInfo();
		starRankInfo.setUserId(userId);
		starRankInfo.setUsername(userService.get(userId).getUsername());
		starRankInfo.setVip(userService.get(userId).getVipLevel());
		starRankInfo.setStar(event.getInt("obj")+rankDao.getEctypeStarByUserId(userId));
		rankDao.updateEctypeStarRank(starRankInfo);
		
	}
	/**
	 * 更新资源星数榜
	 * @param event
	 */
	public void updateResourceStarRank(Event event){
		String userId = event.getUserId();
		StarRankInfo starRankInfo=new StarRankInfo();
		starRankInfo.setUserId(userId);
		starRankInfo.setUsername(userService.get(userId).getUsername());
		starRankInfo.setVip(userService.get(userId).getVipLevel());
		starRankInfo.setStar(event.getInt("obj")+rankDao.getResourceStarByUserId(userId));
		rankDao.updateResourceStarRank(starRankInfo);
		
	}
	/**
	 * 更新VIP等级榜
	 * @param event
	 */
	public void updateVipRank(Event event){
		String userId = event.getUserId();
		VipRankInfo vipRankInfo=new VipRankInfo();
		vipRankInfo.setUserId(userId);
		vipRankInfo.setUsername(userService.get(userId).getUsername());
		vipRankInfo.setVipLevel(event.getInt("obj"));
		rankDao.updateVipRank(vipRankInfo);
		
	}
	/**
	 * 更新周充值榜
	 * @param event
	 */
	public void updatePayRank(Event event){
		String userId = event.getUserId();
		PayRankInfo payRankInfo=new PayRankInfo();
		payRankInfo.setUserId(userId);
		payRankInfo.setUsername(userService.get(userId).getUsername());
		payRankInfo.setVip(userService.get(userId).getVipLevel());
		payRankInfo.setPay(event.getInt("obj"));
		rankDao.updatePayRank(payRankInfo);
		
	}
	/**
	 * 更新用户等级任务
	 */
	private void updateUserLevelTask(Event event) {
		String userId = event.getUserId();
		final int userLevel = event.getInt("userLevel");

		this.taskService.updateTaskFinish(userId, 1, new EventHandle() {
			@Override
			public boolean handle(Event event) {
				return false;
			}
		}, new TaskChecker() {

			@Override
			public boolean isFinish(int systemTaskId, int taskTarget, Map<String, String> params) {
				if (taskTarget == TaskTargetType.USER_LEVEL_TASK) {

					int needLevel = NumberUtils.toInt(params.get("lv"));
					boolean isFinish = userLevel >= needLevel;
					return isFinish;
				}

				return false;
			}

		});
		dailyTaskService.addTaskByLv(userId, userLevel);
	}
	@Override
	public String getInterestedEvent() {
		return UpdateRankEvent.class.getSimpleName();
	}

}
