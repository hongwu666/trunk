package com.lodogame.ldsg.event;

/**
 * 充值事件
 * 
 * @author chengevo
 * 
 */
public class PaymentEvent extends BaseEvent implements Event {

	public PaymentEvent(String uid) {
		this.userId = uid;
	}
}
