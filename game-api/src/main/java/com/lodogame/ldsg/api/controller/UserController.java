package com.lodogame.ldsg.api.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.lodogame.game.dao.RuntimeDataDao;
import com.lodogame.game.dao.UserMapperDao;
import com.lodogame.game.utils.EncryptUtil;
import com.lodogame.ldsg.bo.UserToken;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.manager.TokenManager;
import com.lodogame.ldsg.service.GameApiService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.UserMapper;

/**
 * 游戏服务器API
 * 
 * @author CJ
 * 
 */
@Controller
public class UserController {

	private static final Logger LOG = Logger.getLogger(UserController.class);

	@Autowired
	private GameApiService gameApiService;

	@Autowired
	private UserMapperDao userMapperDao;

	@Autowired
	private RuntimeDataDao runtimeDataDao;

	@Autowired
	private UserService userService;

	@RequestMapping("/gameApi/loadUserToken.do")
	public ModelAndView loadUserToken(String partnerUserId, String partnerId, String serverId, String qn, String imei, String mac, String idfa, Integer closeReg) {

		LOG.debug("partnerUserId[" + partnerUserId + "], partnerId[" + partnerId + "], serverId[" + serverId + "], imei[" + imei + "]");

		int rc = 1000;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			UserMapper userMapper = gameApiService.loadUserMapper(partnerUserId, serverId, partnerId, qn, imei, mac, idfa, closeReg);
			UserToken userToken = createUserToken(userMapper);
			map.put("userToken", userToken);
		} catch (ServiceException se) {
			rc = se.getCode();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			rc = ServiceReturnCode.FAILD;
		}
		map.put("rc", rc);

		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		return modelView;
	}

	private UserToken createUserToken(UserMapper userMapper) {
		UserToken ut = new UserToken();
		ut.setToken(EncryptUtil.getMD5(userMapper.getUserId() + System.currentTimeMillis() + UUID.randomUUID()));
		ut.setPartnerUserId(userMapper.getPartnerUserId());
		ut.setPartnerId(userMapper.getPartnerId());
		ut.setServerId(userMapper.getServerId());
		ut.setUserId(userMapper.getUserId());
		TokenManager.getInstance().setToken(ut.getToken(), ut);
		return ut;
	}

	public GameApiService getGameApiService() {
		return gameApiService;
	}

	public void setGameApiService(GameApiService gameApiService) {
		this.gameApiService = gameApiService;
	}
}
