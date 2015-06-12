package com.lodogame.ldsg.web.model.mol;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;

import com.lodogame.ldsg.web.bo.OrderBO;
import com.lodogame.ldsg.web.constants.PaymentChannelConstant;
import com.lodogame.ldsg.web.model.Payment;
import com.lodogame.ldsg.web.model.PurchaseInfo;
import com.lodogame.ldsg.web.sdk.MOLSdk;
import com.lodogame.ldsg.web.util.EncryptUtil;
import com.lodogame.ldsg.web.util.Json;

/**
 * MOL 充值有钱包充值（信用卡、银行卡）和点卡充值两种方式。
 * 
 * 这两种充值方式都会调用创建订单的接口。在登陆服 payment_order 表中记录的金额是游戏中套餐的金额。
 * 使用钱包充值时，最终的充值金额和游戏中套餐的金额是一样的。但是使用点卡充值的金额是由点卡的面值决定，
 * 和游戏中套餐的金额不一致。
 * 
 * 在支付回调时，MOL 传过来的参数中有一个 channelId 的参数，这个参数的值为1时表示是钱包充值，3表示是点卡充值
 */
@Getter
@Setter
public class MOLPayment extends Payment {
	
	public String getApplicationCode() {
		return applicationCode;
	}


	public void setApplicationCode(String applicationCode) {
		this.applicationCode = applicationCode;
	}


	public String getReferenceId() {
		return referenceId;
	}


	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}


	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}


	public BigDecimal getAmount() {
		return amount;
	}


	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	public String getCurrencyCode() {
		return currencyCode;
	}


	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}


	public String getPaymentId() {
		return paymentId;
	}


	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}


	public String getPaymentStatusCode() {
		return paymentStatusCode;
	}


	public void setPaymentStatusCode(String paymentStatusCode) {
		this.paymentStatusCode = paymentStatusCode;
	}


	public String getPaymentStatusDate() {
		return paymentStatusDate;
	}


	public void setPaymentStatusDate(String paymentStatusDate) {
		this.paymentStatusDate = paymentStatusDate;
	}


	public int getChannelId() {
		return channelId;
	}


	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}


	public String getCustomerId() {
		return customerId;
	}


	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}


	public String getSignature() {
		return signature;
	}


	public void setSignature(String signature) {
		this.signature = signature;
	}


	private static final Logger LOGGER = Logger.getLogger(MOLPayment.class);

	public static final String PAY_SUCCEED = "00";
	
	/**
	 * 钱包充值
	 */
	public static final int PAY_WAY_WALLET = 1;
	
	/**
	 * 点卡充值
	 */
	public static final int PAY_WAY_CARD = 3;


	/**
	 * 相当于 appId
	 */
	private String applicationCode;
	
	/**
	 * 游戏中的订单id
	 */
	private String referenceId;
	
	private String version;
	
	/**
	 * 支付的金额
	 */
	private BigDecimal amount;
	
	/**
	 * 货币编码
	 */
	private String currencyCode;
	
	/**
	 * MOL 中的订单id
	 */
	private String paymentId;
	
	/**
	 * 支付状态
	 */
	private String paymentStatusCode;
	
	private String paymentStatusDate;
	
	/**
	 * 支付方式
	 * 
	 * <p>
	 * 为 1 表示通过用户的钱包（例如银行卡，信用卡）支付
	 * 为 3 表示通过点卡支付
	 * </p>
	 */
	private int channelId;
	
	/**
	 * 渠道用户id
	 */
	private String customerId;
	
	private String signature;
	
	private String getStrToComputeSign() {
		StringBuffer buf = new StringBuffer();
		buf.append(amount);
		buf.append(applicationCode);
		buf.append(channelId);
		buf.append(currencyCode);
		buf.append(customerId);
		buf.append(paymentId);
		buf.append(paymentStatusCode);
		buf.append(paymentStatusDate);
		buf.append(referenceId);
		buf.append(version);
		buf.append(MOLSdk.instance().getAppKey());
		
		return buf.toString();
	}
	

	@Override
	public String getGameOrderId() {
		return this.referenceId;
	}

	@Override
	public String getPartnerOrderId() {
		return this.paymentId;
	}

	@Override
	public boolean isPaySucceed() {
		return this.paymentStatusCode.equalsIgnoreCase(PAY_SUCCEED) ? true : false;
	}

	@Override
	public boolean isPaymentDataVerified() {
		
		String str = getStrToComputeSign();
		String computedSign = EncryptUtil.getMD5(str);
		
		if (computedSign.equalsIgnoreCase(this.signature.trim()) == true) {
			return true;
		}
		
		LOGGER.error("MOL 支付签名错误, 计算签名使用的字符串[" + str + "]");
		
		return false;
	}

	@Override
	public String getChannel() {
		if (this.channelId == PAY_WAY_CARD) {
			return PaymentChannelConstant.MOL_CARD;
		} else if (this.channelId == PAY_WAY_WALLET) {
			return PaymentChannelConstant.MOL_WALLET;
		} else {
			return super.getChannel();
		}
	}


	@Override
	public BigDecimal getFinishAmount(BigDecimal gameOrderAmount) throws NotImplementedException {
		throw new NotImplementedException();
	}
	
	public BigDecimal getAmountInYuan() {
		return this.getAmount().divide(new BigDecimal(100));
	}
}
