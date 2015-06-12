package com.lodogame.ldsg.web.model.mycard;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import lombok.Getter;
import lombok.Setter;

import com.lodogame.ldsg.web.model.Payment;
import com.lodogame.ldsg.web.sdk.mycard.MyCardSdk;
import com.lodogame.ldsg.web.util.EncryptUtil;
import com.lodogame.ldsg.web.util.Json;

@Getter
@Setter
public class MyCardPayment extends Payment {
	
	private static final Logger LOGGER = Logger.getLogger(MyCardPayment.class);
	
	/**
	 * CP 支付成功
	 */
	public static final int CP_PAY_SUCCEED = 1;
	
	/**
	 * CP 支付失败
	 */
	public static final int CP_PAY_FAILED = 1;
	
	/**
	 * 游戏中订单id
	 */
	private String CP_TxID;
	
	/**
	 * MyCard 对传过来的数据计算的签名
	 */
	private String SecurityKey;
	
	/**
	 * MyCard 中的订单id
	 */
	private String MG_TxID;
	
	public String getCP_TxID() {
		return CP_TxID;
	}

	public void setCP_TxID(String cP_TxID) {
		CP_TxID = cP_TxID;
	}

	public String getSecurityKey() {
		return SecurityKey;
	}

	public void setSecurityKey(String securityKey) {
		SecurityKey = securityKey;
	}

	public String getMG_TxID() {
		return MG_TxID;
	}

	public void setMG_TxID(String mG_TxID) {
		MG_TxID = mG_TxID;
	}

	public String getAccount() {
		return Account;
	}

	public void setAccount(String account) {
		Account = account;
	}

	public BigDecimal getAmount() {
		return Amount;
	}

	public void setAmount(BigDecimal amount) {
		Amount = amount;
	}

	public String getRealm_ID() {
		return Realm_ID;
	}

	public void setRealm_ID(String realm_ID) {
		Realm_ID = realm_ID;
	}

	public String getCharacter_ID() {
		return Character_ID;
	}

	public void setCharacter_ID(String character_ID) {
		Character_ID = character_ID;
	}

	public long getTx_Time() {
		return Tx_Time;
	}

	public void setTx_Time(long tx_Time) {
		Tx_Time = tx_Time;
	}

	public String getAUTH_CODE() {
		return AUTH_CODE;
	}

	public void setAUTH_CODE(String aUTH_CODE) {
		AUTH_CODE = aUTH_CODE;
	}

	public String getMyCardProjectNo() {
		return MyCardProjectNo;
	}

	public void setMyCardProjectNo(String myCardProjectNo) {
		MyCardProjectNo = myCardProjectNo;
	}

	public String getMyCardType() {
		return MyCardType;
	}

	public void setMyCardType(String myCardType) {
		MyCardType = myCardType;
	}

	public static Logger getLogger() {
		return LOGGER;
	}

	public static int getCpPaySucceed() {
		return CP_PAY_SUCCEED;
	}

	public static int getCpPayFailed() {
		return CP_PAY_FAILED;
	}

	private String Account;
	
	/**
	 * 订单金额，单位是新台币
	 */
	private BigDecimal Amount;
	
	/**
	 * 服务器id
	 */
	private String Realm_ID;
	
	private String Character_ID;
	
	/**
	 * 交易时间，是 unix 的 timestamp
	 */
	private long Tx_Time;
	
	private String AUTH_CODE;
	
	private String MyCardProjectNo;
	
	private String MyCardType;

	@Override
	public String getGameOrderId() {
		return CP_TxID;
	}

	@Override
	public String getPartnerOrderId() {
		return MG_TxID;
	}

	@Override
	public boolean isPaySucceed() {
		return true;
	}

	@Override
	public boolean isPaymentDataVerified() {
		String strToComputeSign = getStrToComputeSign();
		String computedSign = EncryptUtil.getSHA1(strToComputeSign);
		
		if (computedSign.equalsIgnoreCase(this.SecurityKey) == true) {
			return true;
		}
		
		LOGGER.error("MyCard 支付签名错误, 计算签名使用的字符串[" + strToComputeSign + "] 请求数据 " + Json.toJson(this));
		
		return false;
	}

	private String getStrToComputeSign() {
		StringBuffer buf = new StringBuffer();
		
		buf.append(MyCardSdk.instance().getSecurityKey());
		buf.append(CP_TxID);
		buf.append(MG_TxID);
		buf.append(Account);
		buf.append(Amount);
		buf.append(Realm_ID);
		buf.append(Character_ID);
		buf.append(Tx_Time);
		buf.append(AUTH_CODE);
		buf.append(MyCardProjectNo);
		buf.append(MyCardType);
		
		return buf.toString();
				
	}

	@Override
	public BigDecimal getFinishAmount(BigDecimal gameOrderAmount) throws Exception {
		if (Amount.compareTo(gameOrderAmount) == 0) {
			return Amount;
		}
		LOGGER.error("游戏中订单和支付渠道的订单金额不符, 支付渠道订单信息" + this.printPaymentInfo());

		throw new Exception();
	}

	@Override
	public String getExtInfo() {
		return "";
	}
}
