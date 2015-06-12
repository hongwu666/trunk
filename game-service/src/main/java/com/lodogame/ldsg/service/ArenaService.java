package com.lodogame.ldsg.service;

import java.util.List;
import java.util.Map;

import com.lodogame.ldsg.bo.ArenaDefiantBO;
import com.lodogame.ldsg.bo.ArenaScoreRankBo;
import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.UserArenaBo;
import com.lodogame.ldsg.bo.UserArenaRecordBo;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.model.UserArenaSeriesGift;

/**
 * 百人斩service
 * 
 * @author jacky
 * 
 */
public interface ArenaService {

	/**
	 * 没有可以挑战的对手
	 */
	public static final int NO_MAN = 2001;

	/**
	 * 元宝不足
	 */
	public final static int GOLD_NOT_ENOUGH = 2005;
	/**
	 * 已经领过了
	 */
	public final static int IS_GET = 2006;

	public CommonDropBO crateCommonDropBO(String userId, int winCount, boolean isWin, boolean isReward);

	/**
	 * 进入比武场
	 * 
	 * @param userId
	 * @return
	 */
	public Map<String, Object> enter(String userId);

	public UserArenaBo get(String userId);

	/**
	 * 积分排名
	 * 
	 * @return
	 */
	public List<ArenaScoreRankBo> getArenaScoreRankBos();

	/**
	 * 挑战记录
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserArenaRecordBo> getArenaRecordBos(String userId);

	/**
	 * 刷新对手
	 * 
	 * @param userId
	 * @return
	 */
	public List<ArenaDefiantBO> refresh(String userId);

	/**
	 * 挑战
	 * 
	 * @param attackUserId
	 * @param defenseUserId
	 * @return
	 */
	public boolean battle(String attackUserId, long targetPid, EventHandle handle);

	public int getRank(String userId);

	public int buyNum(String userId, int num);

	public List<UserArenaSeriesGift> showGift(String userId);

	public List<UserHeroBO> getArenaUserHeroBOList(String userId);

}
