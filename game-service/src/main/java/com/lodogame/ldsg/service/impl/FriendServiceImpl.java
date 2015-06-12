package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.CommandDao;
import com.lodogame.game.dao.FriendDao;
import com.lodogame.game.dao.UserDailyGainLogDao;
import com.lodogame.game.dao.UserExtinfoDao;
import com.lodogame.game.dao.UserFriendRequestDao;
import com.lodogame.game.dao.UserOnlineLogDao;
import com.lodogame.game.dao.UserPersonalMailDao;
import com.lodogame.game.dao.UserPraiseDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.ldsg.bo.UserFriendBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.bo.UserMessageBO;
import com.lodogame.ldsg.constants.CommandType;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.constants.UserDailyGainLogType;
import com.lodogame.ldsg.constants.UserFriendConstant;
import com.lodogame.ldsg.event.PraiseEvent;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.HeroHelper;
import com.lodogame.ldsg.service.EventService;
import com.lodogame.ldsg.service.FriendService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.Command;
import com.lodogame.model.Friend;
import com.lodogame.model.SystemHero;
import com.lodogame.model.User;
import com.lodogame.model.UserFriendRequest;
import com.lodogame.model.UserOnlineLog;
import com.lodogame.model.UserPersonalMail;
import com.lodogame.model.UserPraise;

public class FriendServiceImpl implements FriendService {

	@Autowired
	private UserDailyGainLogDao userDailyGainLogDao;

	@Autowired
	private CommandDao commandDao;

	@Autowired
	private UserExtinfoDao userExtinfoDao;

	@Autowired
	private UserPraiseDao userPraiseDao;

	@Autowired
	private HeroService heroService;

	@Autowired
	private UserOnlineLogDao userOnlineLogDao;

	@Autowired
	private UserPersonalMailDao userPersonalMailDao;

	@Autowired
	private UserFriendRequestDao userFriendRequestDao;

	@Autowired
	private UserService userService;

	@Autowired
	private FriendDao friendDao;

	@Autowired
	private EventService eventService;

	/**
	 * 点赞和被被人点赞次数上限
	 */
	private static final int PRAISELIMIT = 30;

	@Override
	public int sendRequest(String uid, String targetUserId) {

		checkUserFriendFull(uid, targetUserId);

		UserFriendRequest request = userFriendRequestDao.get(targetUserId, uid);
		if (request != null && request.getStatus() == UserFriendConstant.REQUEST_STATUS_NEW) {
			String message = "已经发送过好友申请 发送方id[" + uid + ", 接受方id[" + targetUserId + "]";
			throw new ServiceException(FriendService.ALREADY_SEND_REQUEST, message);

		}

		boolean isFriend = isFriend(targetUserId, uid);
		if (isFriend == true) {
			String message = "已经是好友";
			throw new ServiceException(FriendService.ALREADY_FRIEND, message);

		}

		if (request == null) {
			request = new UserFriendRequest(targetUserId, uid, UserFriendConstant.REQUEST_STATUS_NEW);
			userFriendRequestDao.add(request);
			return 0;
		}

		int ignoreCountDownTime = calIgnoreCountDownTime(request);

		if (ignoreCountDownTime <= 0) {
			userFriendRequestDao.updateStatus(targetUserId, uid, UserFriendConstant.REQUEST_STATUS_NEW);
		}

		return ignoreCountDownTime;

	}

	private void checkUserFriendFull(String uid, String targetUserId) {
		if (isUserFriendFull(uid)) {
			String message = "发送好友申请，好友数已满. 发送申请玩家id[" + uid + "], 接受申请玩家id[" + targetUserId + "]";
			throw new ServiceException(FriendService.MY_FRIEND_FULL, message);
		}
		if (isUserFriendFull(targetUserId)) {
			String message = "发送好友申请，对方好友数已满. 发送申请玩家id[" + uid + "], 接受申请玩家id[" + targetUserId + "]";
			throw new ServiceException(FriendService.USER_FRIEND_FULL, message);
		}
	}

	/**
	 * 查看用户的好友数量是否达到上限
	 */
	private boolean isUserFriendFull(String userId) {
		User user = userService.get(userId);
		List<Friend> friendList = friendDao.getFrienddList(userId);
		int friendNum = friendList.size();

		// 玩家的好友上限数量等于玩家的等级
		if (friendNum >= user.getLevel()) {
			return true;
		}

		return false;
	}

	private int calIgnoreCountDownTime(UserFriendRequest request) {
		if (request.getStatus() != UserFriendConstant.REQUEST_STATUS_IGNORE) {
			return 0;
		}

		// 已经“忽略”了多少小时
		int ignoredTime = DateUtils.getHourDiff(request.getUpdatedTime(), new Date());
		return UserFriendConstant.TOTAL_IGNORE_HOUR - ignoredTime;
	}

	@Override
	public void approveAddFriend(String userId, String sendRequestUserId) {
		if(isUserFriendFull(userId)){
			String message = "好友数已满. 发送申请玩家id[" + userId + "], 接受申请玩家id[" + sendRequestUserId + "]";
			throw new ServiceException(FriendService.MY_FRIEND_FULL, message);
		}
		if(isUserFriendFull(sendRequestUserId)){
			String message = "对方好友数已满. 发送申请玩家id[" + userId + "], 接受申请玩家id[" + sendRequestUserId + "]";
			throw new ServiceException(FriendService.USER_FRIEND_FULL, message);
		}
		userFriendRequestDao.updateStatus(userId, sendRequestUserId, UserFriendConstant.REQUEST_STATUS_APPROVED);
		Friend friend = friendDao.getFriend(userId, sendRequestUserId);
		if (friend == null) {
			friend = new Friend(userId, sendRequestUserId);
			friendDao.add(friend);
		}
	}

	@Override
	public void ignoreAddFriend(String userId, String sendRequestUserId) {
		userFriendRequestDao.updateStatus(userId, sendRequestUserId, UserFriendConstant.REQUEST_STATUS_IGNORE);

	}

	public List<String> getFriendUserIdList(String uid) {
		List<Friend> friendList = friendDao.getFrienddList(uid);
		List<String> friendUseridList = new ArrayList<String>();
		for (Friend friend : friendList) {
			String friendUserId = getFriendUserId(uid, friend);
			friendUseridList.add(friendUserId);
		}

		return friendUseridList;
	}

	private String getFriendUserId(String uid, Friend friend) {
		if (uid.equals(friend.getUserIdA())) {
			return friend.getUserIdB();
		} else {
			return friend.getUserIdA();
		}
	}

	@Override
	public Map<String, Object> enter(String uid) {
		List<String> friendUserIdList = getFriendUserIdList(uid);
		List<UserFriendBO> friendBOList = createUserFriendBOList(uid, friendUserIdList);

		List<String> sendRequestUserIdList = getSendRequestUserIdList(uid);
		List<UserFriendBO> sendRequestFriendBOs = createUserFriendBOList(uid, sendRequestUserIdList);

		List<UserPersonalMail> newMails = userPersonalMailDao.getNewMails(uid);
		List<UserMessageBO> messageBOs = new ArrayList<UserMessageBO>();
		for (UserPersonalMail mail : newMails) {
			UserMessageBO bo = new UserMessageBO();
			bo.setType(1);
			bo.setUsername(userService.get(mail.getFriendUserId()).getUsername());
			messageBOs.add(bo);
		}

		int num = userDailyGainLogDao.getUserDailyGain(uid, UserDailyGainLogType.PRAISE);

		Map<String, Object> rt = new HashMap<String, Object>();
		rt.put("ufl", friendBOList);
		rt.put("frl", sendRequestFriendBOs);
		rt.put("fml", messageBOs);
		rt.put("num", num);

		return rt;
	}

	@Override
	public List<UserFriendBO> getUserFriendBOList(String uid) {
		List<String> friendUserIdList = getFriendUserIdList(uid);
		return createUserFriendBOList(uid, friendUserIdList);
	}

	/**
	 * 读取所有还没有处理的好友申请，获取发送申请玩家的id
	 */
	private List<String> getSendRequestUserIdList(String userId) {
		List<String> userIdList = new ArrayList<String>();
		List<UserFriendRequest> requestListt = userFriendRequestDao.getByStatus(userId, UserFriendConstant.REQUEST_STATUS_NEW);
		for (UserFriendRequest request : requestListt) {
			userIdList.add(request.getSendUserId());
		}
		return userIdList;
	}

	public List<UserFriendBO> createUserFriendBOList(String uid, List<String> userFriendIdList) {
		List<UserFriendBO> list = new ArrayList<UserFriendBO>();

		for (String friendUserId : userFriendIdList) {
			UserFriendBO bo = createUserFriendBO(uid, friendUserId);
			list.add(bo);
		}
		return list;
	}

	public UserFriendBO createUserFriendBO(String uid, String userId) {
		User user = userService.get(userId);

		UserFriendBO userFriendBO = new UserFriendBO(user);
		SystemHero systemHero = userService.getSystemHero(userId);
		userFriendBO.setSystemHeroId(systemHero.getImgId());

		UserOnlineLog lastOnlineLog = userOnlineLogDao.getLastOnlineLog(userId);
		userFriendBO.setLastLoginTime(lastOnlineLog.getLoginTime().getTime());

		List<UserHeroBO> userHeroList = heroService.getUserHeroList(userId, 1);
		int capability = HeroHelper.getCapability(userHeroList);
		userFriendBO.setCapability(capability);

		if (userService.isOnline(userId) == true) {
			userFriendBO.setIsOnline(1);
		}

		if (isPraisedToday(uid, userId)) {
			userFriendBO.setIsPraisedToday(1);
		}
		return userFriendBO;
	}

	private boolean isPraisedToday(String uid, String friendUserId) {
		UserPraise userPraise = userPraiseDao.get(uid, friendUserId);
		if (userPraise == null || isPraisedToday(userPraise) == false) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isFriend(String uid, String userToTest) {
		List<String> friendUserIdList = getFriendUserIdList(uid);
		if (friendUserIdList.contains(userToTest)) {
			return true;
		}

		return false;
	}

	@Override
	public long removeFriend(String uid, String friendUserId) {
		Friend friend = friendDao.getFriend(uid, friendUserId);
		if (friend == null) {
			String message = "删除好友 - 要删除的玩家不是好友 userId[" + uid + "] friendUserId[" + friendUserId + "]";
			throw new ServiceException(FriendService.FREIND_NOT_EXIST, message);
		}

		friendDao.removeFriend(friend);

		return userService.get(friendUserId).getLodoId();
	}

	@Override
	public void praise(String uid, String praisedUserId) {

		tryUpdatePraiseStatus(uid, praisedUserId);
		addPower(uid, praisedUserId);
		pushPraisedMessage(uid, praisedUserId);

		PraiseEvent praiseEvent = new PraiseEvent(uid, praisedUserId);
		eventService.dispatchEvent(praiseEvent);

	}

	private void pushPraisedMessage(String uid, String praisedUserId) {
		String sender = userService.get(uid).getUsername();

		Command command = new Command();
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", praisedUserId);
		params.put("sender", sender);
		command.setCommand(CommandType.COMMAND_PRAISED);
		command.setType(CommandType.PUSH_USER);
		command.setParams(params);
		commandDao.add(command);
	}

	private void addPower(String uid, String praisedUserId) {
		/**
		 * 增加自己的体力
		 */
		int praiseNum = userDailyGainLogDao.getUserDailyGain(uid, UserDailyGainLogType.PRAISE);
		if (praiseNum < PRAISELIMIT) {
			userService.addPower(uid, 1, ToolUseType.ADD_PRAISE_ADD_POWER, null);
			userDailyGainLogDao.addUserDailyGain(uid, UserDailyGainLogType.PRAISE, 1);

		}

		/**
		 * 增加对方的体力
		 */
		int bePraisedNum = userDailyGainLogDao.getUserDailyGain(uid, UserDailyGainLogType.BE_PRAISE);
		if (bePraisedNum < PRAISELIMIT) {
			userService.addPower(praisedUserId, 2, ToolUseType.ADD_BE_PRAISE_ADD_POWER, null);
			userDailyGainLogDao.addUserDailyGain(praisedUserId, UserDailyGainLogType.BE_PRAISE, 1);
		}

		userService.pushUser(uid);
		userService.pushUser(praisedUserId);
	}

	private void tryUpdatePraiseStatus(String uid, String praisedUserId) {
		UserPraise userPraise = userPraiseDao.get(uid, praisedUserId);
		if(userPraiseDao.getTodayPraiseNum(uid)>=30){
			String message = "今天点赞次数已满  ";
			throw new ServiceException(FriendService.PRAISED_TODAY_FULL, message);
		}
		if(userPraiseDao.getTodayPraisedNum(praisedUserId)>=30){
			String message = "今天好友被点赞次数已满  ";
			throw new ServiceException(FriendService.FREIND_PRAISED_TODAY_FULL, message);
		}
		if (userPraise != null && isPraisedToday(userPraise)) {
			String message = "今天已经点赞过";
			throw new ServiceException(FriendService.PRAISED_TODAY, message);
		} else if (userPraise == null) {
			userPraise = new UserPraise(uid, praisedUserId);
			userPraiseDao.add(userPraise);
		}

		userPraiseDao.update(uid, praisedUserId, new Date());
	}

	private boolean isPraisedToday(UserPraise userPraise) {
		return DateUtils.isSameDay(new Date(), userPraise.getUpdatedTime());
	}

}
