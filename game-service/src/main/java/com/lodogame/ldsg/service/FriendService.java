package com.lodogame.ldsg.service;

import java.util.List;
import java.util.Map;

import com.lodogame.ldsg.bo.UserFriendBO;

public interface FriendService {
	/**
	 * 发送好友申请，本身好友数已满
	 */
	public static final int MY_FRIEND_FULL=2000;
	
	/**
	 * 发送好友申请，对方好友数已满
	 */
	public static final int USER_FRIEND_FULL = 2006;
	
	/**
	 * 已经发送过好友申请
	 */
	public static final int ALREADY_SEND_REQUEST = 2002;
	
	/**
	 * 发送好友申请 - 已经是好友
	 */
	public static final int ALREADY_FRIEND = 2003;

	
	public static final int FREIND_NOT_EXIST = 2001;

	/**
	 * 点赞 - 今天已经点赞过
	 */
	public static final int PRAISED_TODAY = 2001;

	/**
	 * 点赞 - 今天点赞次数已满
	 */
	public static final int PRAISED_TODAY_FULL = 2004;
	
	/**
	 * 点赞 - 今天好友被点赞次数已满
	 */
	public static final int FREIND_PRAISED_TODAY_FULL = 2005;

	/**
	 * 
	 * @param uid 发送申请的用户id
	 * @param targetUserId 接受申请的用户id
	 * @return 距离忽略结束的时间，小时为单位
	 */
	public int sendRequest(String uid, String targetUserId);

	/**
	 * 
	 * @param uid
	 * @param sendRequestUserId 发送好友请求的玩家id
	 */
	public void approveAddFriend(String uid, String sendRequestUserId);

	public void ignoreAddFriend(String uid, String sendRequestUserId);

	/**
	 * 进入好友系统
	 */
	public Map<String, Object> enter(String uid);

	boolean isFriend(String uid, String userToTest);

	public List<UserFriendBO> getUserFriendBOList(String uid);

	public long removeFriend(String uid, String friendUserId);

	/**
	 * 点赞
	 */
	public void praise(String uid, String praisedUserId);

}
