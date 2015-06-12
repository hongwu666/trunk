package com.lodogame.ldsg.web.controller.payback;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lodogame.ldsg.web.factory.PartnerServiceFactory;
import com.lodogame.ldsg.web.model.huawei.HuaweiPaymentObj;
import com.lodogame.ldsg.web.sdk.HuaweiSdk;
import com.lodogame.ldsg.web.service.PartnerService;

@Controller
public class HuaweiController {
	private static Logger LOG = Logger.getLogger(HuaweiController.class);

	@Autowired
	private PartnerServiceFactory serviceFactory;

	@RequestMapping(value = "/webApi/huaweiPayment.do")
	public ModelAndView payCallback(HttpServletRequest req, HttpServletResponse res) {
		PartnerService ps = serviceFactory.getBean(HuaweiSdk.instance().getPartnerId());
		try {
			HuaweiPaymentObj data = new HuaweiPaymentObj();
			data.setParams(getValue(req));
			if (ps.doPayment(data)) {
				res.getWriter().print("success");
			} else {
				res.getWriter().print("fail");
			}
		} catch (Exception e) {
			LOG.error("huawei payment error!", e);
		}
		return null;
	}

	public Map<String, Object> getValue(HttpServletRequest request) {

		String line = null;
		StringBuffer sb = new StringBuffer();
		try {
			request.setCharacterEncoding("UTF-8");

			InputStream stream = request.getInputStream();
			InputStreamReader isr = new InputStreamReader(stream);
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\r\n");
			}
			System.out.println("The original data is : " + sb.toString());
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}

		String str = sb.toString();
		Map<String, Object> valueMap = new HashMap<String, Object>();
		if (null == str || "".equals(str)) {
			return valueMap;
		}

		String[] valueKey = str.split("&");
		for (String temp : valueKey) {
			String[] single = temp.split("=");
			valueMap.put(single[0], single[1]);
		}

		// 接口中，如下参数sign和extReserved、sysReserved是URLEncode的，所以需要decode，其他参数直接是原始信息发送，不需要decode
		try {
			String sign = (String) valueMap.get("sign");
			String extReserved = (String) valueMap.get("extReserved");
			String sysReserved = (String) valueMap.get("sysReserved");

			if (null != sign) {
				sign = URLDecoder.decode(sign, "utf-8");
				valueMap.put("sign", sign);
			}
			if (null != extReserved) {
				extReserved = URLDecoder.decode(extReserved, "utf-8");
				valueMap.put("extReserved", extReserved);
			}

			if (null != sysReserved) {
				sysReserved = URLDecoder.decode(sysReserved, "utf-8");
				valueMap.put("sysReserved", sysReserved);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return valueMap;

	}
}
