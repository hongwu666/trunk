package com.lodogame.ldsg.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.lodogame.model.SystemActivity;
import com.lodogame.model.SystemGoldSet;
import com.lodogame.model.UserMapper;

/**
 * 用于游戏服务端提供给外部调用的WEB 接口，如登录、充值等
 * 
 * @author CJ
 * 
 */
public interface GameApiService {

	/**
	 * 用户不存在
	 */
	public static final int PAYMENT_ERROR_USER_NOT_EXISTS = 2005;

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
	 * 校验签名错误sss
	 */
	public static final int SIGN_CHECK_ERROR = 2002;

	/**
	 * 加载平台用户映射，不存在则重新创建
	 * 
	 * @param partnerUserId
	 * @param serverId
	 * @param partnerId
	 * @param qn
	 * @return
	 */
	public UserMapper loadUserMapper(String partnerUserId, String serverId, String partnerId, String qn, String imei, String mac, String idfa, Integer closeReg);

	/**
	 * 游戏支付
	 * 
	 * @param partnerId
	 *            合作商ID
	 * @param serverId
	 *            服务器ID
	 * @param partnerUserId
	 *            合作商用户ID
	 * @param channel
	 *            充值渠道
	 * @param orderId
	 *            订单ID
	 * @param waresId 
	 *			  商品id，对应于 system_gold_set 中的 system_gold_set_id 字段 		
	 * @param amount
	 *            金额
	 * @param gold
	 *            金币
	 * @param userIp
	 *            用户IP
	 * @param remark
	 *            备注
	 * @return
	 */
	public boolean doPayment(String partnerId, String serverId, String partnerUserId, String channel, String orderId, int waresId, BigDecimal amount, int gold, String userIp, String remark);

	/**
	 * 发送消息
	 * 
	 * @param content
	 * @param partnerIds
	 */
	public void sendSysMsg(String content, String partnerIds);

	/**
	 * 获取支付设置
	 * 
	 * @return
	 */
	public List<SystemGoldSet> getPaySettings();

	/**
	 * 删除商城打折活动信息
	 */
	public void delActivity(String activityId);

	/**
	 * 删除商城打折活动商品
	 */
	public void delItems(String activityId);

	/**
	 * 发送系统邮件
	 * 
	 * @param title
	 * @param content
	 * @param toolIds
	 * @param target
	 * @param userLodoIds
	 */
	public void sendMail(String title, String content, String toolIds, int target, String userLodoIds, String sourceId, Date date, String partner);

	/**
	 * 封号
	 * 
	 * @param dueTime
	 * @return
	 */
	public boolean banUser(String userId, String dueTime);

	/**
	 * 设置 VIP 等级
	 * 
	 * @param userId
	 * @param vipLevel
	 * @return
	 */
	public boolean assignVipLevel(String userId, int vipLevel);

	/**
	 * 创建用户
	 * 
	 * @param username
	 */
	public boolean createUser(String username, String userId);

	/**
	 * 添加活动
	 * 
	 * @param systemActivity
	 */
	public boolean addActivity(SystemActivity systemActivity);

	/**
	 * 修改活动信息
	 * 
	 * @param systemActivity
	 */
	public boolean modifyActivity(SystemActivity systemActivity);

	/**
	 * 更新FB次数
	 * 
	 * @param forcesId
	 * @param times
	 * @return
	 */
	public int modifyForcesTimes(int forcesId, int times);

	/**
	 * 数据同步
	 * 
	 * @param table
	 * @param sqls
	 * @return
	 */
	public boolean dataSync(String table, String sqls);

	/**
	 * 修改用户等级
	 * 
	 * @param userId
	 * @param level
	 * @param exp
	 * @return
	 */
	public boolean updateUserLevel(String userId, int level, int exp);
}
