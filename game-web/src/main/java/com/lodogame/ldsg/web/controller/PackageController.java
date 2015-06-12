package com.lodogame.ldsg.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.lodogame.ldsg.web.Config;
import com.lodogame.ldsg.web.PackageConfig;
import com.lodogame.ldsg.web.bo.PackageInfoBO;
import com.lodogame.ldsg.web.dao.PackageInfoDao;
import com.lodogame.ldsg.web.dao.ServerStatusDao;
import com.lodogame.ldsg.web.exception.ServiceException;
import com.lodogame.ldsg.web.factory.PartnerServiceFactory;
import com.lodogame.ldsg.web.service.PartnerService;
import com.lodogame.ldsg.web.service.WebApiService;
import com.lodogame.ldsg.web.util.DateUtil;

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

	@Autowired
	private ServerStatusDao serverStatusDao;

	@RequestMapping("/webApi/checkVersion.do")
	public ModelAndView checkVersion(HttpServletRequest req, HttpServletResponse res) throws IOException {

		String ip = req.getRemoteAddr();

		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> dt = new HashMap<String, Object>();
		String version = req.getParameter("version");
		String partnerId = req.getParameter("partnerId");
		String qn = req.getParameter("qn");

		// todo 临时代码
		if (!"1001".equals(partnerId) && !"1101".equals(partnerId)) {
			Date now = new Date();
			Date openDate = DateUtil.stringToDate("2015-02-09 11:00:00", DateUtil.STRING_DATE_FORMAT);
			if (now.before(openDate)) {
				if (!this.serverStatusDao.isWhiteIp(partnerId, ip) && !this.serverStatusDao.isWhiteIp(partnerId, "127.0.0.1")) {
					throw new ServiceException(WebApiService.SERVER_CLOSE, "9号11点准时开放");
				}
			}

		}

		String fr = req.getParameter("imei");
		String mac = req.getParameter("mac");
		PartnerService ps = serviceFactory.getBean(partnerId);
		PackageInfoBO bo = ps.checkVersion(version, fr, mac, partnerId, ip);

		dt.put("ver", bo.getVersion());

		dt.put("desc", bo.getDescribe());

		dt.put("resUrl", packUrl(bo.getResUrl()));

		String verUrl = "";

		if (StringUtils.isEmpty(bo.getVersion()) == false) {
			verUrl = "http://wapi.lieguo.lodogame.com:8088/webApi/getVersion.do?ver=" + bo.getVersion();
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
		modelView.s
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

	private String packUrl(String urlStr) {

		if (urlStr != null && (urlStr.startsWith("http://") || urlStr.startsWith("https://") || urlStr.startsWith("itms-services"))) {
			return urlStr;
		}

		String ips = PackageConfig.instance().getIps();
		String[] ipArr = null;
		if (StringUtils.isBlank(ips) || StringUtils.isBlank(urlStr)) {
			return "";
		}
		ipArr = ips.split(",");
		int idx = RandomUtils.nextInt(ipArr.length);
		String url = "http://" + ipArr[idx] + urlStr;// + "?" +
														// UUID.randomUUID().toString();
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

}
