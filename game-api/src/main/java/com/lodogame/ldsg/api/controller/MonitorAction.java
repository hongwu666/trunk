package com.lodogame.ldsg.api.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lodogame.game.utils.DateUtils;
import com.lodogame.ldsg.config.Config;

/**
 * 监控 game-api 状态
 */
@Controller
public class MonitorAction {

	private static final Logger LOG = Logger.getLogger(MonitorAction.class);

	@RequestMapping("/gameApi/checkGameApiStatus.do")
	@ResponseBody
	public String checkGameApiStatus(HttpServletRequest req, HttpServletResponse res) {
		return "1";
	}

	@RequestMapping("/gameApi/setDate.do")
	@ResponseBody
	public String setDate(String date) {
		if (!Config.ins().isDebug()) {
			return "forbidden";
		}

		Runtime run = Runtime.getRuntime();
		String cmd = "date -s \"" + date + "\"";
		try {
			LOG.info("cmd[" + cmd + "]");
			Process p = run.exec(cmd);// 启动另一个进程来执行命令
			BufferedInputStream in = new BufferedInputStream(p.getInputStream());
			BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
			String lineStr;
			while ((lineStr = inBr.readLine()) != null) {
				// 获得命令执行后在控制台的输出信息
				LOG.info(lineStr);// 打印输出信息
			}
			// 检查命令是否执行失败。
			if (p.waitFor() != 0) {
				if (p.exitValue() == 1) {
					LOG.info("extValue[1]");
					return "error";
				}
			}
			inBr.close();
			in.close();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return "error";
		}

		return "success";
	}

	@RequestMapping("/gameApi/getDate.do")
	@ResponseBody
	public String getDate(HttpServletRequest req, HttpServletResponse res) {
		return DateUtils.getDateStr(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
	}
}
