package com.lodogame.ldsg.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.EmpireDao;
import com.lodogame.game.dao.EmpireHistoryDao;
import com.lodogame.game.dao.EmpireSystemUpDao;
import com.lodogame.game.utils.DateUtils;
import com.lodogame.ldsg.bo.BattleBO;
import com.lodogame.ldsg.bo.EmpireHistoryBO;
import com.lodogame.ldsg.bo.EmpireOccupyBO;
import com.lodogame.ldsg.bo.UserHeroBO;
import com.lodogame.ldsg.constants.MailTarget;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.EmpireHelper;
import com.lodogame.ldsg.helper.HeroHelper;
import com.lodogame.ldsg.service.EmpireService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.Empire;
import com.lodogame.model.EmpireGain;
import com.lodogame.model.EmpireHistory;
import com.lodogame.model.EmpireNum;
import com.lodogame.model.EmpireSystemUp;
import com.lodogame.model.EmpireSystemUpEnum;
import com.lodogame.model.User;
import com.lodogame.model.UserHero;

public class EmpireServiceImpl implements EmpireService {

	private static final Logger logger = Logger.getLogger(EmpireServiceImpl.class);
	@Autowired
	private EmpireDao empireDao;
	@Autowired
	private EmpireHistoryDao empireHistoryDao;
	@Autowired
	private HeroService heroService;
	@Autowired
	private UserService userService;
	@Autowired
	private EmpireSystemUpDao empireSystemUpDao;
	@Autowired
	private MailServiceImpl mailServiceImpl;

	@Override
	public int[] enter(String userId) {
		int[] data = new int[2];
		EmpireNum empireNum = this.empireDao.getEmpireNum(userId);
		data[0] = empireNum.getNum();
		data[1] = 10;
		return data;
	}

	/**
	 * 根据层数与位置得到收益加成
	 * 
	 * @param floor
	 * @param pos
	 * @return
	 */
	public String getUp(int floor, int pos) {
		List<EmpireSystemUp> list = this.empireSystemUpDao.getEmpireSystemUp(floor);
		int percent = 100;
		for (EmpireSystemUp empireSystemUp : list) {
			if (empireSystemUp.getStart() <= pos && empireSystemUp.getEnd() >= pos || empireSystemUp.getEnd() == -1) {
				percent = empireSystemUp.getPercent();
			}

		}
		return EmpireSystemUpEnum.get(percent).toString();
	}

	@Override
	public List<EmpireOccupyBO> switchpage(String userId, int floor, int page) {
		if (floor == 1) {
			//第一层特殊处理
			return first(userId, floor, page);
		}
		String name = "";
		List<EmpireOccupyBO> bolist = new ArrayList<EmpireOccupyBO>();
		List<String> enemys = this.empireDao.getEmpireEnemy(userId);
		for (int pos = (page - 1) * 5 + 1; pos <= page * 5; pos++) {

			EmpireOccupyBO bo = new EmpireOccupyBO();
			bo.setPos(pos);
			bo.setPercent(getUp(floor, pos));
			
			List<Empire> list = this.empireDao.getEmpireHero(floor, pos);
			if (!list.isEmpty()) {

				bo.setTime(list.get(0).getCreatedTime().getTime());
				bo.setUserId(list.get(0).getUserId());
				name = userService.get(bo.getUserId()).getUsername();
				bo.setName(name);
				bo.setPower(countHeroPower(list));
				if (userId.equals(bo.getUserId())) {
					bo.setEnemy(ME);
				} else if (enemys.contains(bo.getUserId())) {
					bo.setEnemy(ENEMY);
				} else {
					bo.setEnemy(OTHER);
				}
			}

			bolist.add(bo);
		}
		return bolist;
	}

	public List<EmpireOccupyBO> first(String userId, int floor, int page) {
		List<EmpireOccupyBO> bolist = new ArrayList<EmpireOccupyBO>();
		String name = "";
		boolean hasocc = false;
		Set<String> set = this.empireDao.getEmpireUser(floor);
		int pos = set.size() + 1;
		if (set.contains(userId)) {
			hasocc = true;
		}
		EmpireOccupyBO mybo = new EmpireOccupyBO();
		mybo.setPos(pos);
		mybo.setPercent(EmpireSystemUpEnum.A.toString());
		mybo.setUserId(userId);
		if (hasocc) {
			User user=userService.get(userId);
			List<Empire> mes=this.empireDao.getEmpireHero(floor, userId);
			Empire me=mes.get(0);
			pos = me.getPos();
			mybo.setTime(me.getCreatedTime().getTime());
			mybo.setEnemy(ME);
			mybo.setName(user.getUsername());
			mybo.setPower(countHeroPower(mes));
			mybo.setPos(pos);
		}

		for (int i = pos - 4; i <= pos + 4; i++) {
			List<Empire> list = this.empireDao.getEmpireHero(floor, i);
			if (list.isEmpty()||i==pos) {
				continue;
			}
			if(bolist.size()==4){
				break;
			}
			EmpireOccupyBO bo = new EmpireOccupyBO();
			bo.setPos(i);
			bo.setPercent(getUp(floor, i));
			bo.setTime(list.get(0).getCreatedTime().getTime());
			bo.setUserId(list.get(0).getUserId());
			name = userService.get(bo.getUserId()).getUsername();
			bo.setName(name);
			bo.setPower(countHeroPower(list));
			bolist.add(bo);
		}
		int size=bolist.size();
		for (int i = size+1; i <= 4; i++) {	
			bolist.add(EmpireHelper.createRobot());
		}
		bolist.add(2, mybo);
		return bolist;

	}

	public int countHeroPower(List<Empire> list) {
		List<UserHeroBO> usbolist = new ArrayList<UserHeroBO>();
		for (Empire empire : list) {
			UserHero userHero = heroService.get(empire.getUserId(), empire.getUserHeroId());
			UserHeroBO uhbo = heroService.createUserHeroBO(userHero);
			usbolist.add(uhbo);
		}
		return HeroHelper.getCapability(usbolist);
	}

	@Override
	public void showGain(String userId, int floor, int page, int pos) {
		Empire empire = empireDao.getEmpireUser(floor, pos);
		List<EmpireGain> list = empireDao.getEmpireGain(userId, floor, pos);
		StringBuffer toolIds = new StringBuffer();
		for (EmpireGain empireGain : list) {
			toolIds.append(empireGain.getToolType());
		}
	}

	@Override
	public void gain(String userId, int floor, boolean auto, int pos) {
		// 非服务器自动收获要校验
		User user = userService.get(userId);
		if (!auto) {
			// 校验位置是否该用户
			Empire empire = empireDao.getEmpireUser(floor, pos);
			if (!empire.getUserId().equals(userId)) {
				String message = "操作失败.userId[" + userId + "]";
				throw new ServiceException(POS_IS_NOT_USER, message);
			}
		}
		// 统计发送收益奖励
		List<EmpireGain> gainList = this.empireDao.getEmpireGain(userId, floor, pos);
		String title = "";
		String content = "";
		String toolIds = EmpireHelper.compoundGain(gainList);
		String lodoId = user.getLodoId() + "";
		mailServiceImpl.send(title, content, toolIds, MailTarget.USERS, lodoId, null, new Date(), null);

		// 剔除站位
		this.empireDao.deleteEmpire(userId, floor, pos);
		// 删除位置收益
		this.empireDao.deleteEmpireGain(userId, floor, pos);

	}

	public void init() {
		// 检验结算线程
		new Thread(new Runnable() {

			public void run() {
				while (true) {
					try {
						checkAcount();
					} catch (Throwable t) {
						logger.error(t.getMessage(), t);
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException ie) {
						logger.error(ie.getMessage(), ie);
					}

				}
			}

		}).start();
		// 凌晨重置次数线程
		new Thread(new Runnable() {

			public void run() {
				while (true) {
					try {
						cleanNum();
					} catch (Throwable t) {
						logger.error(t.getMessage(), t);
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException ie) {
						logger.error(ie.getMessage(), ie);
					}

				}
			}

		}).start();
	}

	public void cleanNum() {
		if (DateUtils.isThisTime(0, 2)) {
			this.empireDao.clearEmpireNum();
		}
	}

	public void checkAcount() {
		Date now = new Date();
		List<Empire> list = this.empireDao.getEmpireAll();
		for (Empire empire : list) {
			// 占领已经结束就跳过
			if (empire.getEndCountTime().before(empire.getNextCountTime())) {
				continue;
			}
			if (empire.getNextCountTime().before(now)) {
				acount(empire);
			}
		}
	}

	/**
	 * 统计宝库奖励
	 * 
	 * @param empire
	 * @param isFinish
	 */
	public void acount(Empire empire) {

		// 更新下次结算时间
		this.empireDao.updateNextCount(empire.getFloor(), empire.getPos());
		// 结算
		List<EmpireGain> empireGainList = new ArrayList<EmpireGain>();
		this.empireDao.addEmpireGain(empireGainList);

	}

	public void checkEmpireNum(EmpireNum empireNum) {
		if (empireNum.getNum() <= 0) {
			String message = "次数不足.userId[" + empireNum.getUserId() + "]";
			throw new ServiceException(TIMES_NOT_ENOUGTH, message);
		}
	}

	public void updateEmpireNum(String userId, int num) {
		this.empireDao.updateEmpireNum(userId, num);
	}

	@Override
	public void fight(String userId, int floor, int pos, EventHandle eventHandle) {

		final EmpireNum empireNum = this.empireDao.getEmpireNum(userId);
		// 检查挑战次数
		checkEmpireNum(empireNum);
		// 扣除一次挑战次数
		updateEmpireNum(userId, empireNum.getNum() - 1);

		final User user = userService.get(userId);
		BattleBO attack = new BattleBO();
		/*
		 * // 英雄列表 List<BattleHeroBO> attackBO =
		 * this.heroService.getUserHeroBO(userId, userHeroId)
		 * attack.setUserLevel(user.getLevel());
		 * attack.setBattleHeroBOList(attackBO);
		 * 
		 * IUser defenseUser = null; boolean robot = false; try { defenseUser =
		 * userService.getByPlayerId(Long.toString(targetPid)); } catch
		 * (Exception e) { robot = true; defenseUser =
		 * robotService.getById(targetPid); } BattleBO defense = new BattleBO();
		 * // 英雄列表 List<BattleHeroBO> defenseBO = null; if (!robot) {
		 * List<UserHeroBO> userHeroBOList =
		 * this.getArenaUserHeroBOList(attackUserId); defenseBO =
		 * this.heroService.getUserArenaBattleHeroBOList(userHeroBOList,
		 * defenseUser.getUserId()); } if (defenseBO == null ||
		 * defenseBO.isEmpty()) { defenseBO =
		 * this.heroService.getUserBattleHeroBOList(defenseUser.getUserId()); }
		 * 
		 * defense.setBattleHeroBOList(defenseBO);
		 * defense.setUserLevel(defenseUser.getLevel());
		 * 
		 * userArenaInfo.setPkNum(userArenaInfo.getPkNum() + 1);
		 * 
		 * final boolean isRevenge =
		 * this.userArenaRecordLogDao.isEnemy(attackUserId,
		 * defenseUser.getUserId());
		 * 
		 * if (isRevenge) {// 复仇
		 * this.userArenaRecordLogDao.deleteRevenge(attackUserId,
		 * defenseUser.getUserId()); }
		 * 
		 * ArenaEvent event = new ArenaEvent(attackUserId);
		 * eventService.dispatchEvent(event);
		 * 
		 * dailyTaskService.sendUpdateDailyTaskEvent(attackUserId,
		 * SystemDailyTask.ARENA, 1);
		 * 
		 * _battle(userArenaInfo, defenseUser, isRevenge, attack, defense,
		 * BattleType.ARENA, handle);
		 * 
		 * return true;
		 */

	}

	@Override
	public List<EmpireHistoryBO> showHistory(String userId) {
		List<EmpireHistory> list = this.empireHistoryDao.getEmpireHistory(userId);
		List<EmpireHistoryBO> bos = new ArrayList<EmpireHistoryBO>();
		for (EmpireHistory empireHistory : list) {
			bos.add(new EmpireHistoryBO(empireHistory));
		}
		return bos;
	}

	@Override
	public List<Integer> trace(String userId) {
		return this.empireDao.getEmpireFloor(userId);
	}

	@Override
	public int[] buy(String userId) {

		EmpireNum empireNum = this.empireDao.getEmpireNum(userId);
		return null;
	}

	@Override
	public void occupy(String userId, Map<String, Integer> map, int floor, int pos) {
		// 该位置是否有人
		Empire empireocc = this.empireDao.getEmpireUser(floor, pos);
		if (empireocc != null) {
			String message = "该位置已被人占领.userId[" + empireocc.getUserId() + "]";
			throw new ServiceException(POS_HSA_USER, message);
		}
		// 该用户在该层的其他位置有占领的话则撤退
		Empire empireUser = this.empireDao.getEmpireUser(floor, userId);
		if (empireUser != null) {
			gain(userId, floor, true, empireUser.getPos());
		}
		
		List<Empire> empireList = new ArrayList<Empire>();
		Date date = new Date();
		for (String userHeroId : map.keySet()) {
			Empire empire = new Empire();
			empire.setUserId(userId);
			empire.setUserHeroId(userHeroId);
			empire.setHeroPos(map.get(userHeroId));
			empire.setCreatedTime(date);
			empire.setFloor(floor);
			empire.setNextCountTime(DateUtils.add(date, Calendar.MINUTE, 30));
			empire.setEndCountTime(DateUtils.add(date, Calendar.HOUR_OF_DAY, 8));
			empire.setPos(pos);
			empireList.add(empire);
		}
		this.empireDao.addEmpire(empireList);

	}

}
