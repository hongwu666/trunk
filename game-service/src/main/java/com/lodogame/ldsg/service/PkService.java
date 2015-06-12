package com.lodogame.ldsg.service;

import java.util.List;

import com.lodogame.ldsg.bo.AwardDescBO;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.PkPlayerBO;
import com.lodogame.ldsg.bo.UserPkInfoBO;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.model.UserPkInfo;

/**
 * 争霸赛业务接口
 * 
 * @author shixiangwen
 * 
 */
public interface PkService {

	/**
	 * 兑换，声望不足
	 */
	public final static int REPUTATION_NOT_ENOUGH = 2001;

	/**
	 * 挑战次数不足
	 */
	public final static int FIGHT_TIMES_LIMIT = 2004;
	
	/**
	 * 购买次数不足
	 */
	public final static int BUY_TIMES_LIMIT = 2005;
	
	/**
	 * 金钱不足
	 */
	public final static int NO_MONEY = 2006;

	
	/**
	 * 结算昨日奖励中
	 */
	public final static int GIFTING = 2007;
	
	public void buyNum(String userId);
	public void replace(String userId);
	
	/**
	 * 进入
	 */
	public void enter(String uid, EventHandle handle);

	/**
	 * 兑换
	 * 
	 * @param userId
	 * @param exchangeId
	 * @param handle
	 * @return
	 */
	public CommonDropBO exchange(String userId, int exchangeId, int num);

	/**
	 * 挑战
	 * 
	 * @param userId
	 * @param targetId
	 */
	public void fight(String userId, long targetId, boolean useMoney, EventHandle handle);

	/**
	 * 获取用户pk info
	 * 
	 * @param userPkInfo
	 * @return
	 */
	public UserPkInfoBO createUserPkInfoBo(UserPkInfo userPkInfo);

	/**
	 * 获取可挑战列表
	 * 
	 * @param rank
	 * @return
	 */
	public List<PkPlayerBO> getChallengeList(int rank,String uid);
	
	public List<PkPlayerBO> getTenList();

	/**
	 * 获取用户pk信息
	 * 
	 * @param userId
	 * @return
	 */
	public UserPkInfoBO getUserPkInfo(String userId);

	/**
	 * 获取奖厉列表
	 * 
	 * @return
	 */
	public List<AwardDescBO> getAwardList();


}


