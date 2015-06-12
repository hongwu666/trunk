package com.lodogame.ldsg.action;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import com.lodogame.game.server.constant.CodeConstants;
import com.lodogame.game.server.request.Message;
import com.lodogame.game.server.response.Response;
import com.lodogame.game.server.session.DefaultSessionManager;
import com.lodogame.game.server.session.Session;
import com.lodogame.ldsg.Config;
import com.lodogame.ldsg.bo.OfflineUserToken;
import com.lodogame.ldsg.bo.UserToken;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.manager.OfflineTokenManager;
import com.lodogame.ldsg.manager.TokenManager;
import com.lodogame.ldsg.manager.UserSessionManager;

/**
 * 用户模块对外接口
 * 
 * @author CJ
 * 
 */
public class UserAction extends ProxyBaseAction {
	private final static Logger LOG = Logger.getLogger(UserAction.class);

	public final static int SUCCESS = 1000;
	/**
	 * 用户未注册角色
	 */
	public final static int LOGIN_NO_REG = 2001;
	/**
	 * 用户未登陆
	 */
	public final static int LOGIN_NOT_LOGIN = 2002;

	/**
	 * 登陆请求
	 * 
	 * @return
	 * @throws IOException
	 */
	public Response login() throws IOException {

		String tk = (String) this.request.getParameter("tk");
		String pid = String.valueOf(this.request.getParameter("pid"));
		String sid = (String) this.request.getParameter("sid");

		UserToken userToken = checkUserLogin(tk, getUserId(), pid, sid);
		if (userToken != null) {
			// 暂时屏蔽
			kickoutOtherSession(userToken);
			UserSessionManager.getInstance().setUserSession(userToken.getUserId(), request.getSession().getSid());

			request.getSession().setAttribute("userToken", userToken);

			InetSocketAddress address = (InetSocketAddress) request.getSession().getChannel().getRemoteAddress();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("ip", address.getHostName());
			return this.dispatchRequest("User.login", param);
		}
		return null;
	}

	public Response reLogin() throws IOException {

		String tk = (String) this.request.getParameter("tk");
		String pid = String.valueOf(this.request.getParameter("pid"));

		UserToken userToken = TokenManager.getInstance().getToken(tk);
		if (userToken == null && tk != null) {
			OfflineUserToken offlineUserToken = OfflineTokenManager.getInstance().getToken(tk);
			// 离线不超过一个小时可以再次登录
			if (offlineUserToken != null && System.currentTimeMillis() - offlineUserToken.getLogoutTime() < 12 * 60 * 60 * 1000) {
				userToken = new UserToken();
				userToken.setUserId(offlineUserToken.getUserId());
				userToken.setPartnerUserId(offlineUserToken.getPartnerUserId());
				userToken.setServerId(offlineUserToken.getServerId());
				userToken.setPartnerId(offlineUserToken.getPartnerId());
				userToken.setToken(offlineUserToken.getToken());
				TokenManager.getInstance().setToken(userToken.getToken(), userToken);

				// 从离线那清掉
				OfflineTokenManager.getInstance().removeToken(tk);
			}
		}

		if (userToken == null) {
			throw new ServiceException(LOGIN_NOT_LOGIN, "userToken为空[" + tk + ":" + getUserId() + ":" + pid + "]");
		}

		// UserToken userToken = checkUserLogin(tk, getUserId(), pid, sid);
		if (userToken != null) {
			// 暂时屏蔽
			kickoutOtherSession(userToken);
			UserSessionManager.getInstance().setUserSession(userToken.getUserId(), request.getSession().getSid());

			InetSocketAddress address = (InetSocketAddress) request.getSession().getChannel().getRemoteAddress();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("ip", address.getHostName());

			request.getSession().setAttribute("userToken", userToken);

			this.dispatchRequest("User.reLogin", param);
		}
		return null;
	}

	/**
	 * 踢掉同一个用户的其它登陆链接
	 * 
	 * @param userToken
	 */
	private void kickoutOtherSession(UserToken userToken) {
		String sessionId = UserSessionManager.getInstance().getUserSessionId(userToken.getUserId());
		Session session = DefaultSessionManager.getInstance().getSession(sessionId);
		if (session != null) {
			Message msg = new Message();
			msg.setAct("error");
			msg.setRc(CodeConstants.KICKOUT);
			try {
				ChannelFuture future = session.send(msg);
				future.addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture future) throws Exception {
						future.getChannel().close();
					}
				});
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 根据token判断用户是否有登陆
	 * 
	 * @param token
	 *            用户登陆令牌
	 * @param userId
	 *            用户ID
	 * @param partnerId
	 *            合作厂商ID
	 * @param serverId
	 *            服务器ID
	 * @return
	 */
	private UserToken checkUserLogin(String token, String userId, String partnerId, String serverId) {
		if (Config.isTest) {
			UserToken userToken = new UserToken();
			userToken.setPartnerId(partnerId);
			userToken.setUserId(userId);
			userToken.setToken(token);
			userToken.setServerId(serverId);
			userToken.setPartnerId(partnerId);
			return userToken;
		}
		if (StringUtils.isBlank(token) || StringUtils.isBlank(userId) || StringUtils.isBlank(partnerId) || StringUtils.isBlank(serverId)) {
			throw new ServiceException(ServiceReturnCode.PARAM_ERROR, "参数不正确[" + token + ":" + userId + ":" + partnerId + "]");
		}

		UserToken userToken = TokenManager.getInstance().getToken(token);
		if (userToken == null) {
			throw new ServiceException(LOGIN_NOT_LOGIN, "userToken为空[" + token + ":" + userId + ":" + partnerId + "]");
		}

		if (!userId.equals(userToken.getUserId()) || !partnerId.equals(userToken.getPartnerId()) || !serverId.equals(userToken.getServerId())) {
			throw new ServiceException(LOGIN_NOT_LOGIN, "userToken与缓存不匹配[" + token + ":" + userId + ":" + partnerId + "]");
		}
		return userToken;
	}

	/**
	 * 用户退出，清除userToken、Session并断开连接
	 * 
	 * @return
	 */
	public Response logout() {
		Session session = request.getSession();
		UserToken userToken = (UserToken) session.getAttribute("userToken");
		// 清楚userToken和session
		TokenManager.getInstance().removeToken(userToken.getToken());
		DefaultSessionManager.getInstance().closeSession(session);
		return null;
	}

	/**
	 * 创建游戏角色
	 * 
	 * @return
	 * @throws IOException
	 */
	public Response create() throws IOException {
		InetSocketAddress address = (InetSocketAddress) request.getSession().getChannel().getRemoteAddress();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ip", address.getHostName());
		return this.dispatchRequest("User.create", param);
	}

	/**
	 * 检测掉线状况，给逻辑服调用，如果socket已经断开，则调用logout接口
	 * 
	 * @return
	 * @throws IOException
	 */
	public Response checkOnline() throws IOException {

		String uid = (String) this.request.getParameter("uid");

		String userSessionId = UserSessionManager.getInstance().getUserSessionId(uid);
		Session userSession = DefaultSessionManager.getInstance().getSession(userSessionId);
		if (userSession == null || userSession.getChannel() == null || !userSession.getChannel().isConnected()) {
			Message msg = response.getMessage();
			msg.setAct("User.logout");
			msg.setAttribate("uid", uid);
			response.setMessage(msg);
			return response;
		} else {
			return null;
		}

	}

}
