package com.lodogame.ldsg.service;

import java.util.Date;
import java.util.List;

import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.UserMailBO;


/**
 * 邮件相关service
 * 
 * @author jacky
 * 
 */
public interface MailService {
	
	/**
	 * 0表示个人普通邮件，1表示好友申请邮件
	 */
	public static final int PERSONA_REGULAR_MAIL = 0;

	/**
	 *  好友申请邮件
	 */
	public static final int PERSONAL_ADD_FRIEND_REQUEST = 1;
	
	/**
	 * 由系统发送的好友申请，通过好友申请和拒绝好友申请邮件
	 */
	public static final int PERSONAL_MAIL_SEND_BY_SYSTEM = 0;
	
	public static final int PERSONAL_MAIL_SEND_BY_USER = 1; 

	/**
	 * 邮件不存在
	 */
	public final static int RECEIVE_MAIL_ERROR_MAIL_NOT_EXISTS = 2001;

	/**
	 * 邮件已经领取过
	 */
	public final static int RECEIVE_MAIL_ERROR_MAIL_HAD_RECEIVE = 2002;

	/**
	 * 邮件没有奖励
	 */
	public final static int RECEIVE_MAIL_ERROR_MAIL_NOT_REWARD = 2003;

	/**
	 * 发送个人邮件 - 收件用户不存在
	 */
	public static final int PERSONAL_MAIL_USER_NOT_EXIST = 2001;
	
	/**
	 * 发送个人邮件 - 邮件中包含非法文字
	 */
	public static final int PERSONAL_ILLEGAL_WORDS = 2003;

	/**
	 * 发送个人邮件 - 不是好友
	 */
	public static final int PERSONAL_MAIL_NOT_FRIENDS = 2004;


	/**
	 * 领取邮件附件
	 * 
	 * @param userId
	 * @param userMailId
	 * @return
	 */
	public CommonDropBO receive(String userId, int userMailId);

	/**
	 * 读取邮件
	 * 
	 * @param userId
	 * @param userMailId
	 * @param type 
	 */
	public void read(String userId, int userMailId, int type);

	/**
	 * 删除邮件
	 * 
	 * @param userId
	 * @param userMailId
	 * @param type 
	 */
	public void delete(String userId, int userMailId, int type);

	/**
	 * 获取用户邮件列表
	 * 
	 * @param userId
	 * @param personalMailLastId 
	 * @return
	 */
	public List<UserMailBO> getMailList(String userId, int systemMailLastId, int personalMailLastId);

	/**
	 * 发送系统邮件
	 * 
	 * @param title
	 * @param content
	 * @param toolIds
	 * @param target
	 * @param userLodoIds
	 */
	public void send(String title, String content, String toolIds, int target, String userLodoIds, String sourceId, Date date, String partner);

	/**
	 * 是否有新的邮件
	 * 
	 * @param userId
	 * @return
	 */
	public boolean hasNewMail(String userId);

	/**
	 * 
	 * @param mailSender 表示是由系统自动发送的好友申请，通过好友申请，拒绝好友申请邮件还是好友之间发送的邮件。
	 * 		  发送个人邮件时，会先检查这个字段，如果是用户之间发送个人邮件，会先检查这两个用户是不是好友，如果不是返回状态码
	 */
	public void sendPersonalMail(String uid, String targetUserName, String mailTitle, String mailContent, int mailType, int mailSender);

}
