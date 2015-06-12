package com.lodogame.ldsg.web.model.google;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

import org.apache.log4j.Logger;

import com.lodogame.ldsg.web.model.Payment;

@Getter
@Setter
public class GooglePayment extends Payment {
	
	private static final Logger LOGGER = Logger.getLogger(GooglePayment.class);
	
	private static final int PAY_SUCCESS = 0;
	
	
	/**
	 * 支付渠道订单id
	 */
	private String orderId;
	private String packageName;
	private String productId;
	private long purchaseTime;
	private int purchaseState;
	
	/**
	 * CP 方订单id
	 */
	private String developerPayload;
	private String purchaseToken;
	

	@Override
	public String getGameOrderId() {
		return developerPayload;
	}

	@Override
	public String getPartnerOrderId() {
		return orderId;
	}

	@Override
	public boolean isPaySucceed() {
		return purchaseState == PAY_SUCCESS ? true : false;
	}

	/**
	 * 在 GooglePlayController 中拿到数据时会验证数据的完整性，所以在这里直接返回  true
	 */
	@Override
	public boolean isPaymentDataVerified() {
		return true;
	}


	@Override
	public BigDecimal getFinishAmount(BigDecimal gameOrderAmount) {
		return gameOrderAmount;
	}
}
