package com.lodogame.ldsg.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.UserMailBO;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.MailService;

public class MailAction extends LogicRequestAction {

	@Autowired
	private MailService mailService;

	@Autowired
	private PushHandler pushHandler;

	/**
	 * 读取邮件列表
	 * 
	 * @return
	 */
	public Response getList() {

		String userId = this.getUid();
		int systemMailLastId = this.getInt("smid", 0);
		int personalMailLastId = this.getInt("pmid", 0);

		List<UserMailBO> mailBOList = this.mailService.getMailList(userId, systemMailLastId, personalMailLastId);

		set("ems", mailBOList);

		return this.render();
	}

	/**
	 * 读取邮件
	 * 
	 * @return
	 */
	public Response read() {

		String userId = this.getUid();
		int userMailId = this.getInt("emid", 0);
		int type = this.getInt("type", 0);

		this.mailService.read(userId, userMailId, type);

		set("emid", userMailId);
		set("type", type);

		return this.render();
	}

	/*
	 * 领取邮件附件
	 */
	public Response receive() {

		String userId = this.getUid();
		int userMailId = this.getInt("emid", 0);

		CommonDropBO commonDropBO = this.mailService.receive(userId, userMailId);

		set("emid", userMailId);
		set("dr", commonDropBO);

		if (commonDropBO.isNeedPushUser()) {
			pushHandler.pushUser(userId);
		}

		return this.render();
	}

	public Response delete() {

		String userId = this.getUid();
		int userMailId = this.getInt("emid", 0);
		int type = this.getInt("type", 0);

		this.mailService.delete(userId, userMailId, type);

		set("emid", userMailId);
		set("type", type);

		return this.render();
	}

	
	public Response sendPersonalMail() {
		String name = getString("name", "");
		String content = getString("content", "");
		String mailTitle = null;
		mailService.sendPersonalMail(getUid(), name, mailTitle, content, MailService.PERSONA_REGULAR_MAIL, MailService.PERSONAL_MAIL_SEND_BY_USER);
		
		return this.render();
	}
	
}
