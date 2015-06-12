package com.lodogame.ldsg.action;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.EmpireHistoryBO;
import com.lodogame.ldsg.bo.EmpireOccupyBO;
import com.lodogame.ldsg.service.EmpireService;

/**
 * 帝国宝库
 * 
 * @author Administrator
 *
 */
public class EmpireAction extends LogicRequestAction {

	private static final Logger LOG = Logger.getLogger(EmbattleAction.class);
	@Autowired
	private EmpireService empireService;
	
	/**
	 * 进度帝国宝库
	 * @return
	 */
	public Response enter() {
		String userId = getUid();	
		
		int [] data = empireService.enter(userId);
		set("times", data[0]);
		set("time", data[1]);
		
		return this.render();
	}
	
	/**
	 * 购买帝国宝库次数
	 * @return
	 */
	public Response buy() {
		String userId = getUid();	
		
		int [] data = empireService.buy(userId);
		set("times", data[0]);
		set("time", data[1]);
		
		return this.render();
	}
	/**
	 * 切换层数，页数
	 */
	public Response switchpage(){
		String userId = getUid();
		
		int floor=getInt("floor",1); //层数
		int page=getInt("page", 1);//页数
		
	
		List<EmpireOccupyBO> list=empireService.switchpage(userId, floor, page);
		set("list", list);
		return this.render();
	}
	/**
	 * 去展示抢占英雄
	 * @return
	 */
	public Response showHero(){
		
		return this.render();
	}
	/**
	 * 去选择占领英雄
	 * @return
	 */
	public Response defendHero(){
		
		return this.render();
	}
	
	/**
	 * 上阵切换或选择防守武将
	 * @return
	 */
	public Response changePos() {
		String userId = getUid();
		String h1 = getString("hId1", "");
		String h2 = getString("hId2", "");
		int pos1 = getInt("p1", 0);
		int pos2 = getInt("p2", 0);
		
		return render();
	}
	/**
	 * 占领或放弃
	 * @return
	 */
	public Response occupy() {
		String userId = getUid();
		Map<String ,Integer> map=(Map<String, Integer>) this.getMap("m");
		int floor=this.getInt("floor", 1);
		int pos=this.getInt("pos", 1);
		this.empireService.occupy(userId, map, floor, pos);
		
		return render();
	}
	/**
	 * 展示自己的寻宝收获情况
	 * @return
	 */
	public Response showGain() {
		String userId = getUid();
		int floor=getInt("floor",1); //层数
		int page=getInt("page", 1);//页数
		int pos=getInt("pos", 1);//位置
		
		empireService.showGain(userId, floor, page, pos);
		return render();
	}
	/**
	 * 收获
	 * @return
	 */
	public Response Gain() {
		String userId = getUid();
		int floor=getInt("floor",1); //层数
		int page=getInt("page", 1);//页数
		int pos=getInt("pos", 1);//位置
		empireService.gain(userId,floor,false,pos);
		
		return render();
	}
	/**
	 * 去展示抢占历史记录
	 * @return
	 */
	public Response showHistory(){
		String userId = getUid();
		List<EmpireHistoryBO> list=empireService.showHistory(userId);
		set("list", list);
		return this.render();
	}
	/**
	 * 追踪
	 * @return
	 */
	public Response trace(){
		String userId=this.getString("user_id", "");
		List<Integer> list=empireService.trace(userId);
		set("list", list);
		return this.render();
	}
}
