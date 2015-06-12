package com.lodogame.ldsg.web.service;

import com.lodogame.ldsg.web.model.Notice;

public interface WebApiService {

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
	 * 人数满
	 */
	public static final int SERVER_FULL = 2009;

	/**
	 * 根据服务器和渠道获取公告信息
	 * 
	 * @param serverId
	 * @parm partnerId
	 * @return
	 */
	public Notice getNotice(String serverId, String partnerId);

	public Notice updateNotice(String serverId, int isEnable, String title, String content, String partnerId);

}
