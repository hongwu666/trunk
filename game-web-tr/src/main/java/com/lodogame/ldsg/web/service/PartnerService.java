package com.lodogame.ldsg.web.service;

import java.math.BigDecimal;
import java.util.Map;

import com.lodogame.ldsg.web.bo.OrderBO;
import com.lodogame.ldsg.web.bo.PackageInfoBO;
import com.lodogame.ldsg.web.model.Payment;
import com.lodogame.ldsg.web.model.PaymentObj;
import com.lodogame.ldsg.web.model.PurchaseInfo;
import com.lodogame.ldsg.web.model.UserToken;

public interface PartnerService {
	/**
	 * 成功
	 */
	public static final int SUCCESS = 1000;
	/**
	 * 未知错误
	 */
	public static final int UNKNOWN_ERROR = 2000;
	/**
	 * 参数错误
	 */
	public static final int PARAM_ERROR = 2001;
	/**
	 * 校验签名错误
	 */
	public static final int SIGN_CHECK_ERROR = 2002;
	/**
	 * 登录验证失败
	 */
	public static final int LOGIN_VALID_FAILD = 2003;
	/**
	 * 游戏服务器登录失败
	 */
	public static final int LOGIN_GAME_FAILD = 2004;

	/**
	 * 版本失效，无法升级以及登陆
	 */
	public static final int VERSION_EXPIRE = 2005;

	/**
	 * 服务器关闭
	 */
	public static final int SERVER_CLOSE = 2006;

	/**
	 * 激活码无效
	 */
	public static final int ACTIVE_FAILD = 2005;

	/**
	 * 登录接口： 1、去平台商的UserCenter进行授权校验
	 * 2、校验成功后，如果用户注册过，则创建一个UserToken保存在redis中，否则就先创建用户mapper信息，再创建UserToken
	 * 3、返回token信息
	 */
	public UserToken login(String sessionId, String partnerId, String serverId, long timestamp, String sign, Map<String, String> params);

	/**
	 * 创建订单
	 */
	public OrderBO createOrder(PurchaseInfo order);

	/**
	 * 完成订单
	 * 
	 * @param orderId
	 *            订单ID
	 * @param partnerUserId
	 *            合作商订单ID
	 * @param success
	 *            是否成功
	 * @param partnerOrderId
	 *            合作商用户ID
	 * @param finishAmount
	 *            完成的金额
	 * @param reqInfo
	 *            回调时的所有参数
	 * @return
	 */
	public boolean doPayment(String orderId, String partnerUserId, boolean success, String partnerOrderId, BigDecimal finishAmount, String reqInfo);

	/**
	 * 充值回调时执行
	 * 
	 * @param paymentObj
	 * @return
	 * @throws Exception 
	 */
	public boolean doPayment(Payment paymentObj) throws Exception;

	public boolean isActive(String uuid, String partnerId);

	public boolean active(String uuid, String code, String partnerId);

	public PackageInfoBO checkVersion(String version, String fr, String mac, String partnerId, String ip);

	public String getPayBackUrl();
}
