package com.lodogame.ldsg.web.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.lodogame.ldsg.web.util.EncryptUtil;
import com.lodogame.ldsg.web.util.Json;

public class HttpHelper {
	public static String doPost(String url, String body) {
		StringBuffer stringBuffer = new StringBuffer();
		HttpEntity entity = null;
		BufferedReader in = null;
		HttpResponse response = null;
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpParams params = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 20000);
			HttpConnectionParams.setSoTimeout(params, 20000);
			HttpPost httppost = new HttpPost(url);
			httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");

			httppost.setEntity(new ByteArrayEntity(body.getBytes("UTF-8")));
			response = httpclient.execute(httppost);
			entity = response.getEntity();
			in = new BufferedReader(new InputStreamReader(entity.getContent()));
			String ln;
			while ((ln = in.readLine()) != null) {
				stringBuffer.append(ln);
				stringBuffer.append("\r\n");
			}
			httpclient.getConnectionManager().shutdown();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (IllegalStateException e2) {
			e2.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
					in = null;
				} catch (IOException e3) {
					e3.printStackTrace();
				}
			}
		}
		return stringBuffer.toString();
	}
	
	public static void main(String[] args) {
		Map<String, Object> params = new HashMap<String, Object>();
		String appId = "4337565";
		String appKey = "Cokx4aj8S47NyQwgyC48xMiX";
		String sessionid = "4B1BE83A4B6139BB8C61272CB118B2A5";
		String uid = "2303724770";
		params.put("appid", appId);// 当前系统时间
		params.put("appkey", appKey);
		params.put("sessionid", sessionid);
		params.put("uid", uid);

		String sign = EncryptUtil.getMD5(appId + appKey + uid + sessionid + "PhRdxU0lsUPZnuMFIUEPqUhfZpgxQaD2");

		params.put("clientsecret", sign.toLowerCase());
		
		String body = Json.toJson(params);
		
		System.out.println("BaiduDuokuSdk.checkSession body:" + body);
		

		String jsonStr = HttpHelper.doPost("http://sdk.m.duoku.com/openapi/sdk/checksession", body);

		System.out.println("BaiduDuokuSdk.checkSession result:" + jsonStr);

	}
}
