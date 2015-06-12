package com.lodogame.ldsg.web.sdk.easou;

import com.lodogame.ldsg.web.sdk.easou.para.AuthParametric;
import com.lodogame.ldsg.web.sdk.easou.para.OAuthParametric;
import com.lodogame.ldsg.web.sdk.easou.service.EucAPIException;
import com.lodogame.ldsg.web.sdk.easou.service.EucApiResult;
import com.lodogame.ldsg.web.sdk.easou.service.EucService;
import com.lodogame.ldsg.web.sdk.easou.service.JBean;
import com.lodogame.ldsg.web.sdk.easou.service.JBody;
import com.lodogame.ldsg.web.sdk.easou.service.JUser;
import com.lodogame.ldsg.web.sdk.easou.service.RequestInfo;
import com.lodogame.ldsg.web.sdk.easou.util.CookieUtil;

public class UserAPI {

	private static AuthParametric<RequestInfo> authPara = new OAuthParametric();

	private static EucService eucService = EucService.getInstance();

	/**
	 * 根据用户ID获取信息
	 * 
	 * @param token
	 * @param id
	 * @return
	 * @throws EucAPIException
	 */
	public static EucApiResult<JUser> getUserById(long id,
			RequestInfo info) throws EucAPIException {
		JBody jbody = new JBody();
		jbody.putContent("id", id);
		JBean jbean = eucService.getResult("/api2/getUserById.json", jbody, authPara,
				info);
		return buildResult(jbean);
	}

	/**
	 * 更新用户信息
	 * 
	 * @param token
	 * @param jUser
	 * @return
	 * @throws EucAPIException
	 */
	public static EucApiResult<JUser> updateUser(String token, JUser jUser,
			RequestInfo info) throws EucAPIException {
		JBody jbody = new JBody();
		jbody.put(CookieUtil.COOKIE_TOKEN, token);
		if (jUser != null) {
			if (jUser.getNickName() != null && !"".equals(jUser.getNickName())) {
				jbody.put("nickName", jUser.getNickName());
			}
			if (jUser.getOccuId() != null) {
				jbody.put("occuId", jUser.getOccuId());
			}
			if (jUser.getBirthday() != null) {
				jbody.put("birthday", jUser.getBirthday());
			}
			if (jUser.getSex() != null) {
				jbody.put("sex", jUser.getSex());
			}
			if (jUser.getCity() != null && !"".equals(jUser.getCity())) {
				jbody.put("city", jUser.getCity());
			}
		}
		JBean jbean = eucService
				.getResult("/api2/updateUser.json", jbody, authPara, info);
		return buildResult(jbean);
	}
	
	/**
	 * 更改密码
	 * 
	 * @param token
	 * @param jUser
	 * @return
	 * @throws EucAPIException
	 */
	public static EucApiResult<String> updatePasswd(String token, String oldPass, String newPass,
			RequestInfo info) throws EucAPIException {
		JBody jbody = new JBody();
		jbody.put(CookieUtil.COOKIE_TOKEN, token);
		jbody.put("oldPass", oldPass);
		jbody.put("newPass", newPass);
		JBean jbean = eucService
				.getResult("/api2/updatePasswd.json", jbody, authPara, info);
		EucApiResult<String> result=new EucApiResult<String>(jbean);
		return result;
	}
	
	/**
	 * 获得绑定手机验证码
	 * @param token
	 * @param mobile
	 * @param info
	 * @return
	 * @throws EucAPIException
	 */
	public static EucApiResult<String> requestBindMobile(String token, String mobile,
			RequestInfo info) throws EucAPIException {
		JBody jbody = new JBody();
		jbody.put(CookieUtil.COOKIE_TOKEN, token);
		jbody.put("mobile", mobile);
		JBean jbean = eucService
				.getResult("/api2/requestBindMobile.json", jbody, authPara, info);
		EucApiResult<String> result=new EucApiResult<String>(jbean);
		return result;
	}
	
	/**
	 * 提交手机绑定
	 * @param token
	 * @param mobile
	 * @param veriCode
	 * @param info
	 * @return	用户信息
	 * @throws EucAPIException
	 */
	public static EucApiResult<JUser> applyBindMobile(String token, String mobile, String veriCode,
			RequestInfo info) throws EucAPIException {
		JBody jbody = new JBody();
		jbody.put(CookieUtil.COOKIE_TOKEN, token);
		jbody.put("mobile", mobile);
		jbody.put("veriCode", veriCode);
		JBean jbean = eucService
				.getResult("/api2/applyBindMobile.json", jbody, authPara, info);
		return buildResult(jbean);
	}

	private static EucApiResult<JUser> buildResult(JBean jbean)
			throws EucAPIException {
		EucApiResult<JUser> result = new EucApiResult<JUser>(jbean);
		if (jbean.getBody() != null) {
			JUser juser = jbean.getBody().getObject("user", JUser.class);
			result.setResult(juser);
		}
		return result;
	}
	
	public static void main(String[] args) throws EucAPIException {
		EucApiResult<String> result=requestBindMobile("TGT-166-XY5pg4ZgHyPib9hcccCl1kCPv3kWKwVc0ZjeXcAmgmckqdptVG-sso", "13000000000", null);
		System.out.println(result.getResultCode());
	}
}
