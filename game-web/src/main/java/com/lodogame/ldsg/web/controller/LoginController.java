package com.lodogame.ldsg.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.lodogame.ldsg.web.dao.ActiveCodeDao;
import com.lodogame.ldsg.web.exception.ServiceException;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;
import com.lodogame.ldsg.web.model.GameServer;
import com.lodogame.ldsg.web.model.Notice;
import com.lodogame.ldsg.web.model.UserToken;
import com.lodogame.ldsg.web.service.PartnerService;
import com.lodogame.ldsg.web.service.ServerService;
import com.lodogame.ldsg.web.service.WebApiService;
import com.lodogame.ldsg.web.util.Json;

@Controller
public class LoginController {

	private static Logger LOG = Logger.getLogger(LoginController.class);

	@Autowired
	private ActiveCodeDao activeCodeDao;

	@Autowired
	private WebApiService webApiService;

	@Autowired
	private PartnerServiceFactory serviceFactory;

	@Autowired
	private ServerService serverService;

	@RequestMapping("/webApi/login.do")
	public ModelAndView login(HttpServletRequest req, HttpServletResponse res) {
		String sessionId = req.getParameter("sid");
		sessionId = sessionId.replace(' ', '+');
		// String uid = req.getParameter("uid");
		String partnerId = req.getParameter("partnerId");
		String serverId = req.getParameter("serverId");
		String timestamp = req.getParameter("timestamp");
		String sign = req.getParameter("sign");
		String imei = req.getParameter("imei");
		String mac = req.getParameter("mac");
		String idfa = req.getParameter("idfa");
		String ext = req.getParameter("ext");

		PartnerService ps = serviceFactory.getBean(partnerId);
		Map<String, Object> map = new HashMap<String, Object>();
		try {

			Map<String, String> params = new HashMap<String, String>();
			params.put("imei", imei);
			params.put("mac", mac);
			params.put("idfa", idfa);

			UserToken userToken = ps.login(sessionId, ext, partnerId, serverId, Long.parseLong(timestamp), sign, params);
			if (userToken != null) {
				map.put("rc", WebApiService.SUCCESS);
				Map<String, String> data = new HashMap<String, String>();
				data.put("tk", userToken.getToken());
				data.put("uid", userToken.getUserId());
				data.put("puid", userToken.getPartnerUserId());
				data.put("ptk", userToken.getPartnerToken());
				data.put("exti", userToken.getExtInfo());
				Notice notice = webApiService.getNotice(serverId, partnerId);
				if (notice != null && notice.getIsEnable() == 1) {
					data.put("title", notice.getTitle());
					data.put("notice", notice.getContent());
				} else {
					notice = webApiService.getNotice(serverId, "all");
					if (notice != null && notice.getIsEnable() == 1) {
						data.put("title", notice.getTitle());
						data.put("notice", notice.getContent());
					} else {
						notice = webApiService.getNotice("all", "all");
						if (notice != null && notice.getIsEnable() == 1) {
							data.put("title", notice.getTitle());
							data.put("notice", notice.getContent());
						}
					}
				}
				map.put("dt", data);
			} else {
				map.put("rc", WebApiService.UNKNOWN_ERROR);
			}
		} catch (ServiceException e) {
			LOG.error(e.getMessage(), e);
			map.put("rc", e.getCode());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			map.put("rc", WebApiService.UNKNOWN_ERROR);
		}
		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		LOG.debug("parnterId:" + partnerId + "," + Json.toJson(map));
		return modelView;
	}

	/**
	 * 获取服务器列表
	 * 
	 * @return
	 */
	@RequestMapping("/webApi/getServerList.do")
	public ModelAndView getServerList(HttpServletRequest req) {

		String ip = req.getRemoteAddr();
		String imei = req.getParameter("fr");

		String partnerId = StringUtils.isBlank(req.getParameter("partnerId")) ? "1001" : req.getParameter("partnerId");

		List<GameServer> serverList = serverService.getServerList(partnerId, imei, ip);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sl", serverList);
		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		return modelView;
	}

	@RequestMapping("/webApi/getSysTime.do")
	public ModelAndView getSysTime() {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Long> tMap = new HashMap<String, Long>();
		tMap.put("t", System.currentTimeMillis());
		map.put("rc", 1000);
		map.put("dt", tMap);
		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		return modelView;
	}

	@RequestMapping("/webApi/updateConfigs.do")
	public ModelAndView updateServers() {
		Map<String, Object> map = new HashMap<String, Object>();

		this.serverService.cleanServerMap();

		map.put("rc", 1000);
		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		return modelView;
	}

	@RequestMapping("/webApi/isActive.do")
	public ModelAndView isActive(HttpServletRequest req, HttpServletResponse res) {
		String uuid = req.getParameter("uuid");
		String partnerId = req.getParameter("partnerId");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			boolean isActive = this.activeCodeDao.isActive(uuid, partnerId);
			map.put("rc", isActive ? WebApiService.SUCCESS : WebApiService.ACTIVE_FAILD);
		} catch (ServiceException e) {
			LOG.error(e.getMessage(), e);
			map.put("rc", e.getCode());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			map.put("rc", WebApiService.UNKNOWN_ERROR);
		}
		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		LOG.debug(Json.toJson(map));
		return modelView;
	}

	@RequestMapping("/webApi/active.do")
	public ModelAndView active(HttpServletRequest req, HttpServletResponse res) {
		String uuid = req.getParameter("uuid");
		String partnerId = req.getParameter("partnerId");
		String code = req.getParameter("code");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			boolean success = true;
			if (!StringUtils.equalsIgnoreCase(code, "0898")) {
				this.activeCodeDao.active(uuid, code, partnerId);
			}
			map.put("rc", success ? WebApiService.SUCCESS : WebApiService.ACTIVE_FAILD);
		} catch (ServiceException e) {
			LOG.error(e.getMessage(), e);
			map.put("rc", e.getCode());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			map.put("rc", WebApiService.UNKNOWN_ERROR);
		}
		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		LOG.debug(Json.toJson(map));
		return modelView;
	}

	public WebApiService getWebApiService() {
		return webApiService;
	}

	public void setWebApiService(WebApiService webApiService) {
		this.webApiService = webApiService;
	}

}
