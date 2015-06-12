package com.lodogame.ldsg.web.sdk.easou;

import java.util.HashMap;
import java.util.Map;

import com.lodogame.ldsg.web.sdk.easou.httpclient.EucHttpClient;
import com.lodogame.ldsg.web.sdk.easou.service.ClientConfig;
import com.lodogame.ldsg.web.sdk.easou.service.Md5SignUtil;
import com.lodogame.ldsg.web.sdk.easou.service.PayBean;
import com.lodogame.ldsg.web.sdk.easou.util.GsonUtil;

public class PayAPI {

	/**
	 * 获得用户e币余额
	 * @param userId
	 * @return
	 */
	public static PayBean getUserCurrency(String userId) {
		StringBuffer urlBuffer = new StringBuffer(
				ClientConfig.getProperty("payment.url"));
		urlBuffer.append("/json/userEb.e");
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("userId", userId);
		paraMap.put("partnerId", ClientConfig.getProperty("partnerId"));
		String sign = Md5SignUtil.sign(paraMap, ClientConfig.getProperty("secertKey"));
		paraMap.put("sign", sign);
		String json = EucHttpClient.httpGet(urlBuffer.toString(), paraMap);
		return GsonUtil.fromJson(json, PayBean.class);
	}
}
