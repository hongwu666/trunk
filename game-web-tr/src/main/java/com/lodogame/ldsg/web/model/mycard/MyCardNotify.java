package com.lodogame.ldsg.web.model.mycard;

import com.lodogame.ldsg.web.sdk.mycard.MyCardSdk;
import com.lodogame.ldsg.web.util.EncryptUtil;

import lombok.Setter;
import lombok.Getter;

/**
 * 在 MyCard 调用支付回调接口后，需要主动调用 MyCard 的接口回传订单信息。
 * 
 * 在MyCard 调用支付回调接口时，会将下一步传回 MyCard 需要的信息保存在数据库中，在接受到 MyCard 的成功状态后，再修改数据库中的状态
 * @author chengevo
 *
 */

@Getter
@Setter
public class MyCardNotify {

	private String auth_code;
	private String mg_txid;
	private String account;
	
	private String getStrToComputeSign() {
		String securityKey = MyCardSdk.instance().getSecurityKey();
		return securityKey + auth_code + mg_txid + account;
	}
	
	public boolean sendConfirm() {
		String strToComputeSign = getStrToComputeSign();
		String sign = EncryptUtil.getSHA1(strToComputeSign);
		
		String sendConfirmUrl = MyCardSdk.instance().getSendConfirmUrl();
		
		return true;
	}
	
}
