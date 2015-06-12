package com.lodogame.ldsg.web.util.jinli;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;

import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;

import com.lodogame.ldsg.web.model.jinli.Order;

public class PayUtil {

	private static final String GIONEE_PAY_INIT = "https://pay.gionee.com/order/create";

	// 网游类型接入时固定值
	private static final String DELIVER_TYPE = "1";
	// 成功响应状态码
	private static final String CREATE_SUCCESS_RESPONSE_CODE = "200010000";

	public static void createOrder(String orderId, String partnerUserId, String title, String apiKey, String privateKey, BigDecimal price, String time, String notifyURL) {

		Order order = new Order(orderId, partnerUserId, title, apiKey, price, price, time, null, notifyURL);

		String requestBody = null;
		try {
			requestBody = PayUtil.wrapCreateOrder(order, privateKey, DELIVER_TYPE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String response = null;
		try {
			response = HttpUtils.post(GIONEE_PAY_INIT, requestBody);
		} catch (Exception e) {
			// TODO : 处理异常;
			return;
		}

		JSONObject json = (JSONObject) JSONValue.parse(response);

		System.out.println(response);

		if (CREATE_SUCCESS_RESPONSE_CODE.equals(json.get("status"))) {

			String orderNo = (String) json.get("order_no");

			System.out.println("orderNo :" + orderNo);

			if (StringUtils.isBlank(orderNo)) {
				// TODO: 如果返回orderNo为空，处理异常
				return;
			}

			// TODO : 处理商户逻辑
			return;
		}

		return;

	}

	public static String wrapCreateOrder(Order order, String privateKey, String deliverType) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, SignatureException,
			IOException {
		JSONObject jsonReq = new JSONObject();
		String expireTime = order.getExpireTime();
		String notifyURL = order.getNotifyURL();

		StringBuilder signContent = new StringBuilder();
		signContent.append(order.getApiKey());
		jsonReq.put("api_key", order.getApiKey());

		signContent.append(order.getDealPrice());
		jsonReq.put("deal_price", order.getDealPrice().toString());
		signContent.append(deliverType);
		jsonReq.put("deliver_type", deliverType);

		if (!StringUtils.isBlank(expireTime)) {
			signContent.append(expireTime);
			jsonReq.put("expire_time", expireTime);
		}

		if (!StringUtils.isBlank(notifyURL)) {
			signContent.append(notifyURL);
			jsonReq.put("notify_url", notifyURL);
		}

		signContent.append(order.getOutOrderNo());
		jsonReq.put("out_order_no", order.getOutOrderNo());
		signContent.append(order.getSubject());
		jsonReq.put("subject", order.getSubject());
		signContent.append(order.getSubmitTime());
		jsonReq.put("submit_time", order.getSubmitTime());
		signContent.append(order.getTotalFee());
		jsonReq.put("total_fee", order.getTotalFee().toString());

		String sign = RSASignature.sign(signContent.toString(), privateKey, CharEncoding.UTF_8);
		jsonReq.put("sign", sign);

		// player_id不参与签名
		jsonReq.put("player_id", order.getPlayerId());

		return jsonReq.toJSONString(JSONStyle.NO_COMPRESS);
	}
}
