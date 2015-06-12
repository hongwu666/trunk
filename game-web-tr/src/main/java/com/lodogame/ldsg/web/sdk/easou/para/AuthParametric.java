package com.lodogame.ldsg.web.sdk.easou.para;

import com.lodogame.ldsg.web.sdk.easou.service.EucAPIException;
import com.lodogame.ldsg.web.sdk.easou.service.EucService;
import com.lodogame.ldsg.web.sdk.easou.service.JBody;
import com.lodogame.ldsg.web.sdk.easou.service.JHead;

/**
 * 对参数进行加密鉴权接口
 * 
 * @author jay
 * @since 20130121
 * @version 1.0
 */
public interface AuthParametric<T> {
	
	/**
	 * 获取加密验证串
	 * 
	 * @param jHead
	 * @param jBody
	 * @param t 保留参数
	 * @return
	 */
	public JHead getVeriHeader(JBody jBody,EucService service,T t);
	
	/**
	 * 生成验证码
	 * 
	 * @param jBody
	 *     业务数据
	 * @param appId
	 *     appId
	 * @param key
	 *     key
	 * @return
	 */
	public String getSign(JBody jBody,String key)throws EucAPIException;
	
}
