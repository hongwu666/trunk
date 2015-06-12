package com.lodogame.ldsg.web.model.mycard;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import lombok.Getter;
import lombok.Setter;

import com.lodogame.ldsg.web.constants.PaymentChannelConstant;
import com.lodogame.ldsg.web.model.Payment;
import com.lodogame.ldsg.web.model.PaymentOrder;

/**
 * 
 * @author chengevo
 *
 */
@Getter
@Setter
public class MyCardServerToServerPayment extends Payment {
	
	private static final Logger LOGGER = Logger.getLogger(MyCardServerToServerPayment.class);
	
	private static final int PAY_SUCCESS = 1;
	
	private int CardKind;
	
	private String cardId;
	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}


	/**
	 * 卡片面额
	 */
	private int CardPoint;
	
	/**
	 * MyCard 订单号
	 */
	private String SaveSeq;
	
	/**
	 * CP 方订单号
	 */
	private String facTradeSeq;
	private String oProjNo;
	
	/**
	 * 返回状态码
	 */
	private int ReturnMsgNo;
	
	/**
	 * 返回消息
	 */
	private String ReturnMsg;
	
	private int goldNum;
	
	private BigDecimal finishAmount;

	@Override
	public String getGameOrderId() {
		return facTradeSeq;
	}

	@Override
	public String getPartnerOrderId() {
		return SaveSeq;
	}

	@Override
	public boolean isPaySucceed() {
		if (this.ReturnMsgNo == PAY_SUCCESS) {
			return true;
		}
		
		return false;
	}

	
	/**
	 * MyCard 返回的参数中没有计算签名，因此都返回 true
	 */
	@Override
	public boolean isPaymentDataVerified() {
		return true;
	}

	@Override
	public BigDecimal getFinishAmount(BigDecimal gameOrderAmount) throws Exception {
		return finishAmount;
	}
	
	@Override
	public int getGoldNum(PaymentOrder order) {
		return goldNum;
	}

	public int getGameWaresId(PaymentOrder order) {
		return this.CardPoint;
	}
	
	public String getChannel() {
		return PaymentChannelConstant.MYCARD_CARD;
	}

	public int getCardKind() {
		return CardKind;
	}

	public void setCardKind(int cardKind) {
		CardKind = cardKind;
	}

	public int getCardPoint() {
		return CardPoint;
	}

	public void setCardPoint(int cardPoint) {
		CardPoint = cardPoint;
	}

	public String getSaveSeq() {
		return SaveSeq;
	}

	public void setSaveSeq(String saveSeq) {
		SaveSeq = saveSeq;
	}

	public String getFacTradeSeq() {
		return facTradeSeq;
	}

	public void setFacTradeSeq(String facTradeSeq) {
		this.facTradeSeq = facTradeSeq;
	}

	public String getOProjNo() {
		return oProjNo;
	}

	public void setOProjNo(String oProjNo) {
		this.oProjNo = oProjNo;
	}

	public int getReturnMsgNo() {
		return ReturnMsgNo;
	}

	public void setReturnMsgNo(int returnMsgNo) {
		ReturnMsgNo = returnMsgNo;
	}

	public String getReturnMsg() {
		return ReturnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		ReturnMsg = returnMsg;
	}

	public int getGoldNum() {
		return goldNum;
	}

	public void setGoldNum(int goldNum) {
		this.goldNum = goldNum;
	}

	public BigDecimal getFinishAmount() {
		return finishAmount;
	}

	public void setFinishAmount(BigDecimal finishAmount) {
		this.finishAmount = finishAmount;
	}
	
	
	@Override
	public String getExtInfo(){
		return cardId;
	}
}
