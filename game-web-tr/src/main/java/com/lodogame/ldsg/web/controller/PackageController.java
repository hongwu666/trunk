package com.lodogame.ldsg.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.lodogame.ldsg.web.bo.PackageInfoBO;
import com.lodogame.ldsg.web.dao.PackageInfoDao;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;
import com.lodogame.ldsg.web.service.PartnerService;

/**
 * 游戏服务器WEB
 * 
 * @author CJ
 * 
 */
@Controller
public class PackageController {

	@Autowired
	private PartnerServiceFactory serviceFactory;
	@Autowired
	private PackageInfoDao packageInfoDao;

	private static final Logger LOG = Logger.getLogger(PackageController.class);

	@RequestMapping("/webApi/checkVersion.do")
	public ModelAndView checkVersion(HttpServletRequest req, HttpServletResponse res) throws IOException {

		String ip = req.getRemoteAddr();

		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> dt = new HashMap<String, Object>();
		String version = req.getParameter("version");
		String partnerId = req.getParameter("partnerId");
		String qn = req.getParameter("qn");

		String fr = req.getParameter("imei");
		String mac = req.getParameter("mac");
		PartnerService ps = serviceFactory.getBean(partnerId);
		PackageInfoBO bo = ps.checkVersion(version, fr, mac, partnerId, ip);

		if (StringUtils.equalsIgnoreCase("1.1.1.1", version)) {
			bo.setVersion("1.0.1.1");
			bo.setResUrl("http://update.fengshen.lodogame.com:8088/package/20140804/android_fix_bug.zip");
			bo.setDescribe("");
			bo.setApkUrl("");
		}

		dt.put("ver", bo.getVersion());

		dt.put("desc", bo.getDescribe());

		dt.put("resUrl", packUrl(bo.getResUrl()));

		String verUrl = "";

		if (StringUtils.isEmpty(bo.getVersion()) == false) {
			verUrl = "http://api.fengshen.lodogame.com:8088/webApi/getVersion.do?ver=" + bo.getVersion();
		}
		dt.put("verUrl", verUrl);

		if (StringUtils.isEmpty(qn)) {
			qn = "default";
		}

		String apkUrl = "";

		if (StringUtils.isNotBlank(bo.getApkUrl())) {
			apkUrl = bo.getApkUrl().replace("${qn}", qn);
			dt.put("apkUrl", packUrl(apkUrl));
		} else {
			dt.put("apkUrl", "");
		}

		// fix bug
		Map<String, Object> dtt = new HashMap<String, Object>();
		dtt.put("dt", dt);

		map.put("dt", dtt);

		map.put("rc", 1000);
		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);

		return modelView;
	}

	private String packUrl(String urlStr) {

		if (urlStr != null && (urlStr.startsWith("http://") || urlStr.startsWith("https://"))) {
			return urlStr;
		}

		String ips = PackageConfig.instance().getIps();
		String[] ipArr = null;
		if (StringUtils.isBlank(ips) || StringUtils.isBlank(urlStr)) {
			return "";
		}
		ipArr = ips.split(",");
		int idx = RandomUtils.nextInt(ipArr.length);
		String url = "http://" + ipArr[idx] + urlStr;
		return url;
	}

	@RequestMapping("/webApi/updatePackageConfig.do")
	public ModelAndView updatePackageConfig() {
		Map<String, Object> map = new HashMap<String, Object>();

		PackageConfig.instance().reload();

		map.put("rc", 1000);
		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(map);
		modelView.setView(view);
		return modelView;
	}
	
	@RequestMapping("/webApi/getVersion.do")
	@ResponseBody
	public String getVersion(HttpServletRequest req, HttpServletResponse res) {
		String version = req.getParameter("ver");

		return version;
	}

}
