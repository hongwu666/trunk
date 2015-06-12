package com.lodogame.ldsg.constants;

public class CommandType {

	/**
	 * 按渠道发送邮件
	 */
	public final static int PUSH_PARTNER = 5;

	/**
	 * 充值用户
	 */
	public final static int PUSH_PAY = 4;

	/**
	 * 登录用户
	 */
	public final static int PUSH_LOGIN = 3;

	/**
	 * 所甩用户
	 */
	public final static int PUSH_ALL = 2;

	/**
	 * 单个用户
	 */
	public final static int PUSH_USER = 1;

	/* 用户相关 10001 - 19999 */
	/**
	 * 推送用户信息
	 */
	public final static int COMMAND_PUSH_USER_INFO = 10001;

	/**
	 * 推送用户任务信息
	 */
	public final static int COMMAND_PUSH_USER_TASK = 10002;

	/**
	 * 推送走马灯
	 */
	public final static int COMMAND_PUSH_MESSAGE = 10003;

	/**
	 * 充值完成
	 */
	public final static int COMMAND_PAY_COMPLETED = 10005;

	/**
	 * 晚上推送
	 */
	public final static int COMMAND_PUSH_MIDNIGHT = 10006;

	/**
	 * 推送有新邮件
	 */
	public final static int COMMAND_PUSH_NEW_MAIL = 10007;

	public static final int COMMAND_PASS_SCENE = 10008;

	/**
	 * 
	 * 推送某个用户 1）是否到吃包子时间，2）是否有可以领取的单笔、累计充值奖励，3）是否可以免费招募
	 */
	public static final int COMMAND_CHECK_NOTIFICATION = 10009;

	/**
	 * 用户被点赞
	 */
	public static final int COMMAND_PRAISED = 10010;

	/** 系统相关 **/

	/**
	 * 异步执行sql
	 */
	public final static int COMMAND_SAVE_LOG = 20001;

	/**
	 * 重新加载配置数据
	 */
	public final static int COMMAND_RELOAD_SYSTEM_DATA = 20002;

	/**
	 * 设置log4j日志级别
	 */
	public final static int COMMAND_SET_LOOGER_LEVEL = 20003;

	/** boss 战相关 30001 - 39999 **/
	/**
	 * 魔物出现
	 */
	public final static int COMMAND_BOSS_PUSH_BOSS_APPEAR = 30001;

	/**
	 * 魔物消失
	 */
	public final static int COMMAND_BOSS_PUSH_BOSS_DISAPPEAR = 30002;

	/**
	 * 自动离队：因为玩家作为某一封魔小队成员，但是登出了游戏
	 */
	public final static int COMMAND_BOSS_PUSH_BOSS_TEAM_USER_LOGOUT = 30003;

	/**
	 * 封魔战报结果
	 */
	public final static int COMMAND_BOSS_PUSH_CHALLENGE_BOSS_RESULT = 30004;

	/**
	 * 玩家封魔信息
	 */
	public final static int COMMAND_BOSS_PUSH_USER_BOSS_INFORMATION = 30005;

	/**
	 * 封魔小队被解散
	 */
	public final static int COMMAND_BOSS_PUSH_TEAM_DISMISSED = 30006;

	/**
	 * 封魔小队更新
	 */
	public final static int COMMAND_BOSS_PUSH_TEAM_UPDATE = 30007;

	/** 千人斩相关 40001 - 49999 **/
	/**
	 * 推送战报
	 */
	public final static int COMMAND_ONLYONE_PUSH_BATTLE = 40001;

	/**
	 * 推送用户信息
	 */
	public final static int COMMAND_ONLYONE_PUSH_USER_INFO = 40002;

	/**
	 * 暂停，继续
	 */
	public final static int COMMAND_ONLYONE_PAUSE = 40003;

	/**
	 * 擂台赛 - 推送战报
	 */
	public final static int COMMAND_CONTEST_PUSH_BATTLE = 50001;

	/**
	 * 擂台赛 - 推送状态
	 */
	public final static int COMMAND_CONTEST_PUSH_STATUS = 50002;

	/**
	 * 推送所有城池
	 */
	public final static int COMMAND_WAR_ALL_CITY = 60001;

	/**
	 * 推送国战结束
	 */
	public final static int COMMAND_WAR_END = 60002;

	/**
	 * 推送防守次数
	 */
	public final static int COMMAND_WAR_DEFENSE_NUM = 60003;

	/**
	 * 推送防守战报
	 */
	public final static int COMMAND_WAR_DEFENSE_REPORT = 60004;

	/**
	 * 推送击杀数排行榜产生
	 */
	public final static int COMMAND_WAR_ATTACK_RANK_CREATE = 60005;

	/**
	 * 推送战斗活动开始
	 */
	public final static int COMMAND_BATTLE_START = 10008;

	public static final int COMMAND_DEIFY_PUSH_TOWER_LIST = 70001;

	public static final int COMMAND_DEIFY_FINISHED = 70002;

	public static final int COMMAND_DEIFY_PUSH_ROOM_LIST = 70003;

	public static final int COMMAND_DEIFY_PUSH_PROTECTED = 70004;

	public static final int COMMAND_DEIFY_PUSH_REPORT = 70005;

	/**
	 * 推送私人聊天信息
	 */
	public final static int COMMAND_CHAT_PLIVATE = 80001;

	/**
	 * 推送世界聊天信息
	 */
	public final static int COMMAND_CHAT_ALL = 80002;

}
