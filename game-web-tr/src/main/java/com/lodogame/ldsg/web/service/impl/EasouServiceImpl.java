package com.lodogame.ldsg.web.service.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.web.dao.PaymentOrderDao;
import com.lodogame.ldsg.web.exception.ServiceException;
import com.lodogame.ldsg.web.model.UserToken;
import com.lodogame.ldsg.web.sdk.GameApiSdk;
import com.lodogame.ldsg.web.sdk.easou.AuthAPI;
import com.lodogame.ldsg.web.sdk.easou.service.AuthBean;
import com.lodogame.ldsg.web.sdk.easou.service.EucAPIException;
import com.lodogame.ldsg.web.sdk.easou.service.EucApiResult;
import com.lodogame.ldsg.web.sdk.easou.service.JUser;
import com.lodogame.ldsg.web.service.BasePartnerService;
import com.lodogame.ldsg.web.service.ServerService;
import com.lodogame.ldsg.web.service.WebApiService;

public class EasouServiceImpl extends BasePartnerService {

	public static Logger LOG = Logger.getLogger(EasouServiceImpl.class);

	@Autowired
	private PaymentOrderDao paymentOrderDao;

	@Autowired
	private ServerService serverService;

	@Override
	public UserToken login(String token, String partnerId, String serverId, long timestamp, String sign, Map<String, String> params) {
		if (StringUtils.isBlank(token) || StringUtils.isBlank(partnerId) || StringUtils.isBlank(serverId) || timestamp == 0 || StringUtils.isBlank(sign)) {
			throw new ServiceException(WebApiService.PARAM_ERROR, "参数不正确");
		}

//		checkSign(token, partnerId, serverId, timestamp, sign);
		try {
		EucApiResult<AuthBean> result = AuthAPI.validate(token, null, null);
			JUser juser = null;
			if (result == null || result.getResult() == null) {
				if (juser == null) {
					throw new ServiceException(WebApiService.LOGIN_VALID_FAILD, "登录验证失败");
				}
			}
			juser = result.getResult().getUser();
			int port = serverService.getServerHttpPort(serverId);
			return GameApiSdk.getInstance().loadUserToken(juser.getId().toString(), partnerId, serverId, port, juser.getQn(), params);

			
		} catch (EucAPIException e) {
			throw new ServiceException(WebApiService.LOGIN_VALID_FAILD, "登录验证失败");
		}
	}

}
