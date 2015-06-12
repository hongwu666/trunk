package com.lodogame.ldsg.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.UserEquipBO;
import com.lodogame.ldsg.bo.UserFriendBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.event.FriendAddEvent;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.HeroHelper;
import com.lodogame.ldsg.service.EquipService;
import com.lodogame.ldsg.service.EventService;
import com.lodogame.ldsg.service.FriendService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.User;

public class FriendAction extends LogicRequestAction {

	@Autowired
	private FriendService friendService;
	
	@Autowired
	private HeroService heroService;
	
	@Autowired
	private EquipService equipService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EventService eventService;
	
	public Response sendRequest() {
		String targetUserId = getString("tuid", "");
		int cd = friendService.sendRequest(getUid(), targetUserId);
		set("cd", cd);
		return this.render();
	}
	
	public Response approveAddFriend() {
		String sendRequestUserId = getString("suid", "");
		friendService.approveAddFriend(getUid(), sendRequestUserId);
		List<UserFriendBO> userFriendBOList = friendService.getUserFriendBOList(getUid());
		try {
			FriendAddEvent e = new FriendAddEvent(sendRequestUserId,friendService.getUserFriendBOList(sendRequestUserId).size());
			eventService.dispatchEvent(e);
		} catch (Exception e) {
		}
		set("ufl", userFriendBOList);
		return this.render();
	}
	
	public Response ignoreAddFriend() {
		String sendRequestUserId = getString("suid", "");
		friendService.ignoreAddFriend(getUid(), sendRequestUserId);
		try {
			FriendAddEvent e = new FriendAddEvent(sendRequestUserId,friendService.getUserFriendBOList(sendRequestUserId).size());
			eventService.dispatchEvent(e);
		} catch (Exception e) {
		}
		return this.render();
	}
	
	public Response enter() {
		Map<String, Object> rt = friendService.enter(getUid());
		set("ufl", rt.get("ufl"));
		set("frl", rt.get("frl"));
		set("fml", rt.get("fml"));
 		set("num", rt.get("num"));
 		
		return this.render();
	}
	
	public Response removeFriend() {
		String friendUserId = getString("fuid", "");
		friendService.removeFriend(getUid(), friendUserId);
		
		set("loid", friendUserId);
		
		return this.render();
	}
	
	public Response praise() {
		String praisedUserId = getString("puid", "");
		friendService.praise(getUid(), praisedUserId);
		
		return this.render();
	}

	public Response getUserInfo() {
		String userId = getUid();

		int playid = 0;
		String username = "";
		playid = this.getInt("pid", 0);
		username = this.getString("un", "");

		String uid = "";
		User user = null;

		if (playid != 0) {
			user = this.userService.getByPlayerId(String.valueOf(playid));
			if (user != null) {
				uid = user.getUserId();
			}
		} else if (StringUtils.isNotEmpty(username)) {
			user = userService.getUserByUserName(username);
			if (user != null) {
				uid = user.getUserId();
			}
		}
		
		if  (StringUtils.isEmpty(uid)) {
			// 查找的用户不存在
			return this.render(2001);
		}

		if (uid.equals(userId)) {
			String message = "不可以查找自己";
			throw new ServiceException(2002, message);
		}

		if (uid != null) {
			List<UserHeroBO> userHeroList = this.heroService.getUserHeroList(uid, 1);
			List<UserEquipBO> userEquipList = this.equipService.getUserEquipList(uid, false);

			set("hls", userHeroList);
			set("eqs", userEquipList);
			set("ccp", HeroHelper.getCapability(userHeroList));
			set("userId", user.getUserId());
			set("username", user.getUsername());
			set("vip", user.getVipLevel());
			set("level", user.getLevel());
		} 

		return this.render();
	}
}
