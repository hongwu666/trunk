package com.lodogame.ldsg.web.sdk.easou;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lodogame.ldsg.web.sdk.easou.para.AuthParametric;
import com.lodogame.ldsg.web.sdk.easou.para.OAuthParametric;
import com.lodogame.ldsg.web.sdk.easou.service.CodeConstant;
import com.lodogame.ldsg.web.sdk.easou.service.EucAPIException;
import com.lodogame.ldsg.web.sdk.easou.service.EucApiResult;
import com.lodogame.ldsg.web.sdk.easou.service.EucService;
import com.lodogame.ldsg.web.sdk.easou.service.JBean;
import com.lodogame.ldsg.web.sdk.easou.service.JBody;
import com.lodogame.ldsg.web.sdk.easou.service.OUser;
import com.lodogame.ldsg.web.sdk.easou.service.RequestInfo;
import com.lodogame.ldsg.web.sdk.easou.util.CookieUtil;
import com.lodogame.ldsg.web.sdk.easou.util.GsonUtil;

public class TrdUserAPI {

	private static AuthParametric<RequestInfo> authPara = new OAuthParametric();

	private static EucService eucService = EucService.getInstance();

	/**
	 * 获得登录用户的绑定信息
	 * 
	 * @param token
	 * @param id
	 * @return
	 * @throws EucAPIException
	 */
	public static EucApiResult<List<OUser>> getTrdBindList(String token,
			RequestInfo info) throws EucAPIException {
		JBody jbody = new JBody();
		jbody.putContent(CookieUtil.COOKIE_TOKEN, token);
		JBean jbean = eucService.getResult("/api2/getTrdBindList.json", jbody, authPara,
				info);
		return buildResult(jbean);
	}
	
	/**
	 * 解除第三方绑定用户
	 * @param token		登录令牌
	 * @param netType	绑定类型 1: 新浪 2: 腾讯
	 * @param info
	 * @return
	 * @throws EucAPIException
	 */
	public static EucApiResult<List<OUser>> jbindTrdUser(String token, String netType,
			RequestInfo info) throws EucAPIException {
		JBody jbody = new JBody();
		jbody.putContent(CookieUtil.COOKIE_TOKEN, token);
		jbody.putContent("netType", netType);
		JBean jbean = eucService.getResult("/api2/jbindTrdUser.json", jbody, authPara,
				info);
		return buildResult(jbean);
	}
	
	/**
	 * 返回绑定列表结果
	 * @param jbean
	 * @return
	 * @throws EucAPIException
	 */
	private static EucApiResult<List<OUser>> buildResult(JBean jbean)
			throws EucAPIException {
		EucApiResult<List<OUser>> result = new EucApiResult<List<OUser>>(jbean);
		List<OUser> olist=new ArrayList<OUser>();
		if (jbean.getBody() != null) {
			JsonElement list= jbean.getBody().getObject("list", JsonElement.class);
			if(null!=list) {
				JsonArray array = list.getAsJsonArray();
				for (int i = 0; i < array.size(); i++) {
					JsonElement element=array.get(i);
					olist.add(GsonUtil.fromJson(element, OUser.class));
				}
			}
		}
		result.setResult(olist);
		return result;
	}
	
	public static void main(String[] args) throws EucAPIException {
		// 查询绑定
		EucApiResult<List<OUser>> result=getTrdBindList("TGT-113-feXSr9soSqGUyIhbYPPzwk7rXcU4qtC1aYEig5ZQscldOVfcmD-sso",null);
		if(CodeConstant.OK.equals(result.getResultCode())) {
			System.out.println(result.getResultCode());
			List<OUser> olist=result.getResult();
			for (OUser oUser : olist) {
				System.out.println(oUser.getNet_id() + "  " + oUser.getNick_name());
			}
		} else {
			System.out.println(result.getResultCode());
		}
		// 解除绑定
//		EucApiResult<List<OUser>> result1=jbindTrdUser("TGT-76-GrP7PDeDyUGYL1jw0gNoiJ1aj3jMu0HbPcDbVbLcyDfPbiXtog-sso", "1", null);
//		if(CodeConstant.OK.equals(result1.getResultCode())) {
//			List<OUser> olist=result1.getResult();
//			for (OUser oUser : olist) {
//				System.out.println(oUser.getNet_id() + "  " + oUser.getNick_name());
//			}
//		}
	}
}