package com.lodogame.ldsg.api.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.lodogame.game.utils.DateUtils;
import com.lodogame.game.utils.EncryptUtil;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.Assert;
import com.lodogame.ldsg.service.GameApiService;
import com.lodogame.model.SystemActivity;

/**
 * 后台接口支持
 * 
 * @author CJ
 * 
 */
@Controller
public class AdminController extends BaseController {

	private static final Logger log = Logger.getLogger(AdminController.class);

	@Autowired
	private GameApiService gameApiService;

	private void checkSign(String item, String playerIds, long timestamp, String sign) {
		long now = System.currentTimeMillis();
		if ((now - timestamp) > 1000 * 60) {
			log.info("请求时间超出范围：" + timestamp + "(" + new Date(timestamp) + ")");
			throw new ServiceException(ServiceReturnCode.SIGN_CHECK_ERROR, "请求时间超出范围（10s）");
		}
		String md5 = EncryptUtil.getMD5(item + playerIds + timestamp + Config.ins().getSignKey());
		if (!md5.toLowerCase().equals(sign.toLowerCase())) {
			log.info("请求签名不正确：" + sign + "(" + md5 + ")");
			throw new ServiceException(ServiceReturnCode.SIGN_CHECK_ERROR, "请求签名不正确");
		}
	}

	@RequestMapping("/gameApi/sendSysMsg.do")
	public ModelAndView sendMsg(String content, long timestamp, String sign, String partnerIds) {

		log.info("sensSysMsg.content[" + content + "]");

		checkSign(content, "", timestamp, sign);

		gameApiService.sendSysMsg(content, partnerIds);

		return this.renderJson();
	}

	@RequestMapping(value = "/gameApi/sendMail.do")
	public ModelAndView sendMail(String title, String content, String toolIds, String lodoIds, Integer target, String sourceId, Long date, String partner) {

		Map<String, Object> map = new HashMap<String, Object>();

		String message = "发送成功";
		int rt = 1000;

		try {

			Assert.notEmtpy(title, ServiceReturnCode.FAILD, "邮件标题不能为空.title[" + title + "]");
			Assert.notEmtpy(content, ServiceReturnCode.FAILD, "邮件内容不能为空.content[" + content + "]");
			Assert.notEmtpy(target, ServiceReturnCode.FAILD, "目标类型不能为空.target[" + target + "]");
			Assert.notEmtpy(sourceId, ServiceReturnCode.FAILD, "源ID不能为空.sourceId[" + sourceId + "]");
			Assert.notEmtpy(sourceId, ServiceReturnCode.FAILD, "渠道商 id 不能为空.sourceId[" + partner + "]");

			Date mailDate = new Date(date);

			this.gameApiService.sendMail(title, content, toolIds, target, lodoIds, sourceId, mailDate, partner);

		} catch (ServiceException se) {
			rt = se.getCode();
			message = se.getMessage();
		} catch (Exception se) {
			rt = 3000;
			message = se.getMessage();
		}

		map.put("rt", rt);
		map.put("msg", message);

		return this.renderJson(map);
	}

	@RequestMapping("/gameApi/addActivity.do")
	public ModelAndView addActivity(int activityId, int activityType, String activityName, String activityDesc, String startTime, String endTime, String param, String openWeeks, int display, int sort) {
		System.out.println(activityId);
		SystemActivity systemActivity = new SystemActivity();
		systemActivity.setActivityId(activityId);
		systemActivity.setActivityType(activityType);
		systemActivity.setActivityName(activityName);
		systemActivity.setActivityDesc(activityDesc);
		systemActivity.setStartTime(DateUtils.str2Date(startTime));
		systemActivity.setEndTime(DateUtils.str2Date(endTime));
		systemActivity.setParam(param);
		systemActivity.setOpenWeeks(openWeeks);
		systemActivity.setDisplay(display);
		systemActivity.setSort(sort);
		boolean rec = gameApiService.addActivity(systemActivity);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rc", rec ? 1000 : 3000);
		return this.renderJson(map);
	}

	@RequestMapping("/gameApi/modifyActivity.do")
	public ModelAndView modifyActivity(int activityId, int activityType, String activityName, String activityDesc, String startTime, String endTime, String param, String openWeeks, int display,
			int sort) {
		SystemActivity systemActivity = new SystemActivity();
		systemActivity.setActivityId(activityId);
		systemActivity.setActivityType(activityType);
		systemActivity.setActivityName(activityName);
		systemActivity.setActivityDesc(activityDesc);
		systemActivity.setStartTime(DateUtils.str2Date(startTime));
		systemActivity.setEndTime(DateUtils.str2Date(endTime));
		systemActivity.setParam(param);
		systemActivity.setOpenWeeks(openWeeks);
		systemActivity.setDisplay(display);
		systemActivity.setSort(sort);
		boolean rec = gameApiService.modifyActivity(systemActivity);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rc", rec ? 1000 : 3000);
		return this.renderJson(map);
	}

	@RequestMapping(value = "/gameApi/dataSync.do", method = RequestMethod.POST)
	public ModelAndView dataSync(String table, String sqls) {

		log.debug("table[" + table + "], sqls[" + sqls + "]");

		Map<String, Object> map = new HashMap<String, Object>();

		try {
			this.gameApiService.dataSync(table, sqls);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("rc", 3000);
		}

		map.put("rc", 1000);
		return this.renderJson(map);
	}

	@RequestMapping(value = "/gameApi/updateUserLevel.do", method = RequestMethod.POST)
	public ModelAndView addExp(String userId, Integer level, Integer exp) {

		log.debug("userId[" + userId + "], level[" + level + "], exp[" + exp + "]");

		Map<String, Object> map = new HashMap<String, Object>();

		try {
			this.gameApiService.updateUserLevel(userId, level, exp);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			map.put("rc", 3000);
		}

		map.put("rc", 1000);
		return this.renderJson(map);
	}

	@RequestMapping("/gameApi/modifyForcesTimes.do")
	public ModelAndView modifyForces(int forcesId, int times) {
		int rec = gameApiService.modifyForcesTimes(forcesId, times);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rc", rec > 0 ? 1000 : 3000);
		return this.renderJson(map);
	}

	/**
	 * 用户封号
	 */
	@RequestMapping(value = "/gameApiAdmin/banUser.do")
	public ModelAndView banUser(String userId, String dueTime) {

		Map<String, Object> map = new HashMap<String, Object>();
		int rt = 1000;

		boolean success = gameApiService.banUser(userId, dueTime);
		if (!success) {
			rt = 2001;
		}

		map.put("rt", rt);
		return this.renderJson(map);
	}

	/**
	 * 更新 VIP 等级
	 */
	@RequestMapping(value = "/gameApiAdmin/assignVipLevel.do")
	public ModelAndView updateVipLevel(String userId, Integer vipLevel) {
		Map<String, Object> map = new HashMap<String, Object>();

		int rt = 1000;
		boolean success = gameApiService.assignVipLevel(userId, vipLevel);
		if (!success) {
			rt = 2001;
		}
		map.put("rt", rt);
		return this.renderJson(map);
	}

	/**
	 * 关闭注册
	 */
	@RequestMapping(value = "/gameApiAdmin/setCloseRegStatus.do")
	public ModelAndView setCloseRegStatus(int close) {
		Map<String, Object> map = new HashMap<String, Object>();

		int rt = 1000;
		Config.ins().setCloeseReg(close == 1);
		map.put("rt", rt);
		return this.renderJson(map);
	}

	/**
	 * 关闭注册
	 */
	@RequestMapping(value = "/gameApiAdmin/getCloseRegStatus.do")
	public ModelAndView getCloseRegStatus(String user) {
		Map<String, Object> map = new HashMap<String, Object>();

		int rt = 1000;
		int close = Config.ins().isCloeseReg() ? 1 : 0;
		map.put("rt", rt);
		map.put("close", close);
		return this.renderJson(map);
	}
}
