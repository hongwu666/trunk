package com.lodogame.game.dao;

import java.util.List;

import com.lodogame.model.UserPersonalMail;

public interface UserPersonalMailDao {

	public List<UserPersonalMail> getMailList(String uid, int mid);

	public boolean save(UserPersonalMail mail);

	public boolean updateStatus(String uid, int mailId, int status);
	
	/**
	 * 查询 friendUseId 发送给 uid 的添加好友申请 
	 */
	public UserPersonalMail getInviteMail(String uid, String friendUserId);

	public boolean update(String uid, int id, int status, int isProcessed);

	public List<UserPersonalMail> getNewMails(String uid);
}
