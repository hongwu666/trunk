package com.lodogame.ldsg.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.HeroPowerRankBO;
import com.lodogame.ldsg.bo.RankBO;
import com.lodogame.ldsg.bo.UserLevelRankBO;
import com.lodogame.ldsg.bo.UserPowerRankBO;
import com.lodogame.ldsg.constants.RankType;
import com.lodogame.ldsg.helper.RankHelper;
import com.lodogame.ldsg.service.PkService;
import com.lodogame.ldsg.service.RankService;

/**
 * 排行榜action
 * 
 * @author shixiangwen
 * 
 */

public class RankAction extends LogicRequestAction {

	@Autowired
	private RankService rankService;
	@Autowired
	private PkService pkService;
	public Response getRanks() {

		String userId = this.getUid();

		int type = this.getInt("tp", 1);
		int rank = 0;
		if(RankType.constantValue(type)){
			if (type == RankType.HERO_POWER.getValue()) {
				List<HeroPowerRankBO> list = this.rankService.getHeroPowerRankList();
				if (list.size() > 50) {
					list = list.subList(0, 50);
				}
				set("ul", list);
				rank = RankHelper.getRank(list, userId, 200);
			}else if (type == RankType.POWER.getValue()) {
				List<UserPowerRankBO> list = this.rankService.getUserPowerRankList();
				if (list.size() > 50) {
					list = list.subList(0, 50);
				}
				set("ul", list);
				rank = RankHelper.getRank(list, userId, 200);
			}else if (type == RankType.LEVEL.getValue()) {
				List<UserLevelRankBO> list = this.rankService.getUserLevelRankList();
				if (list.size() > 50) {
					list = list.subList(0, 50);
				}
				set("ul", list);
				rank = RankHelper.getRank(list, userId, 200);
			} else{
				List<RankBO> list = this.rankService.getRankList(type);
				if (list.size() > 50) {
					list = list.subList(0, 50);
				}
				set("ul", list);
				rank = RankHelper.getRank(list, userId, 200);
			}
		
		}
		set("tp", type);
		set("ur", rank);

		return this.render();
	}

}
