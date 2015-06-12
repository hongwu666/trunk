package com.lodogame.ldsg.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.connector.ServerConnectorMgr;
import com.lodogame.game.dao.SystemForcesDao;
import com.lodogame.game.dao.UserForcesDao;
import com.lodogame.game.server.response.Response;
import com.lodogame.game.server.session.Session;
import com.lodogame.ldsg.bo.UserForcesBO;
import com.lodogame.ldsg.constants.ForcesStatus;
import com.lodogame.ldsg.constants.ForcesType;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.handler.PushHandler;
import com.lodogame.ldsg.service.ArenaService;
import com.lodogame.ldsg.service.BlackRoomService;
import com.lodogame.ldsg.service.EmpireService;
import com.lodogame.ldsg.service.ExpeditionService;
import com.lodogame.ldsg.service.ForcesService;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.PkService;
import com.lodogame.ldsg.service.ResourceService;
import com.lodogame.ldsg.service.SceneService;
import com.lodogame.model.SystemForces;

/**
 * 关卡相关action
 * 
 * @author jacky
 * 
 */

public class BattleAction extends LogicRequestAction {

	private static final Logger LOG = Logger.getLogger(BattleAction.class);

	@Autowired
	private SceneService sceneService;

	@Autowired
	private ForcesService forcesService;

	@Autowired
	private PkService pkService;

	@Autowired
	private HeroService heroService;

	@Autowired
	private UserForcesDao userForcesDao;

	@Autowired
	private PushHandler pushHandler;

	@Autowired
	private ArenaService arenaService;

	@Autowired
	private BlackRoomService blackRoomService;

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private ExpeditionService expeditionService;
	
	@Autowired
	private EmpireService empireService;

	@Autowired
	private SystemForcesDao systemForcesDao;

	public Response fight() throws IOException {

		final int type = this.getInt("tp", 1);
		final long param = this.getInt("pa", 0);
		long ts = this.getLong("ts", 0L);
		int rType = getInt("fbType", 0);
		int rDif = getInt("fbDif", 0);
		int rG = getInt("g", 0);
		int star = getInt("star", 0);
		long exId = getLong("exId", 0L);
		int floor = getInt("floor", 0);
		int pos = getInt("pos", 0);
		this.userForcesDao.setAmendEmbattleTime(getUid(), ts);

		String pos1UserHeroId = this.getString("p1", null);
		String pos2UserHeroId = this.getString("p2", null);
		String pos3UserHeroId = this.getString("p3", null);
		String pos4UserHeroId = this.getString("p4", null);
		String pos5UserHeroId = this.getString("p5", null);
		String pos6UserHeroId = this.getString("p6", null);

		Map<Integer, String> posMap = new HashMap<Integer, String>();
		if (pos1UserHeroId != null) {
			posMap.put(1, pos1UserHeroId);
		}
		if (pos2UserHeroId != null) {
			posMap.put(2, pos2UserHeroId);
		}
		if (pos3UserHeroId != null) {
			posMap.put(3, pos3UserHeroId);
		}
		if (pos4UserHeroId != null) {
			posMap.put(4, pos4UserHeroId);
		}
		if (pos5UserHeroId != null) {
			posMap.put(5, pos5UserHeroId);
		}
		if (pos6UserHeroId != null) {
			posMap.put(6, pos6UserHeroId);
		}
		this.heroService.amendEmbattle(getUid(), posMap);

		LOG.debug("战斗请求uid[" + getUid() + "], type[" + type + "]");
		switch (type) {
		case 1:
			monsterFight(type, (int) param);
			break;
		case 2:
			pkFight(type, param);
			break;
		case 5:
			arenaFight(type, param);
			break;
		case 10:
		case 11:
			activityCopyFight(type);
			break;
		case 12:
			resourceFight(rType, rDif, rG, star);
			break;
		case 13:
			expeditionFight(exId);
			break;
		case 14:
			empireFight(posMap,floor,pos);
			break;
		default:
			LOG.debug("未知战斗类型 type[" + type + "]");
		}

		return null;
	}
	/**
	 * 帝国宝库挑战
	 * 
	 * @param type
	 * @param param
	 */
	private void empireFight(Map<Integer, String> posMap,int floor,int pos) {

		empireService.fight(getUid(), floor, pos, new EventHandle() {
			@Override
			public boolean handle(Event event) {
				String report = event.getString("report");
				int flag = event.getInt("flag");
				set("rp", report);
				set("rf", flag);
				set("uid", getUid());
			
				set("dr", event.getObject("forcesDropBO"));
				set("bgid", event.getInt("bgid"));
				set("dun", event.getString("dun"));
				set("pkt", event.getInt("pkt"));

				pushHandler.pushUser(getUid());

				Session session = ServerConnectorMgr.getInstance().getServerSession("battle");
				try {
					response = render();
					session.send(response.getMessage());
				} catch (IOException e) {
					throw new ServiceException(ServiceReturnCode.FAILD, e.getMessage());
				}

				return true;
			}
		});
	}

	/**
	 * 争霸赛挑战
	 * 
	 * @param type
	 * @param param
	 */
	private void pkFight(final int type, final long targetPid) {

		pkService.fight(getUid(), targetPid, false, new EventHandle() {
			@Override
			public boolean handle(Event event) {
				String report = event.getString("report");
				int flag = event.getInt("flag");
				set("rp", report);
				set("rf", flag);
				set("uid", getUid());
				set("tp", type);
				set("dr", event.getObject("forcesDropBO"));
				set("bgid", event.getInt("bgid"));
				set("dun", event.getString("dun"));
				set("pkt", event.getInt("pkt"));

				pushHandler.pushUser(getUid());

				Session session = ServerConnectorMgr.getInstance().getServerSession("battle");
				try {
					response = render();
					session.send(response.getMessage());
				} catch (IOException e) {
					throw new ServiceException(ServiceReturnCode.FAILD, e.getMessage());
				}

				return true;
			}
		});
	}

	/**
	 * 比武场挑战
	 * 
	 * @param type
	 * @param param
	 */
	private void arenaFight(final int type, final long targetPid) {
		arenaService.battle(getUid(), targetPid, new EventHandle() {
			@Override
			public boolean handle(Event event) {
				String report = event.getString("report");
				int flag = event.getInt("flag");
				set("rp", report);
				set("rf", flag);
				set("uid", getUid());
				set("tp", type);
				set("dr", event.getObject("forcesDropBO"));
				set("bgid", event.getInt("bgid"));
				set("dun", event.getString("dun"));
				if (flag == 1) {
					set("pls", event.getObject("pls"));
				}

				set("uaf", event.getObject("uaf"));
				set("iw", event.getString("iw"));
				set("iw3", event.getString("iw3"));
				set("iw5", event.getString("iw5"));
				set("iw8", event.getString("iw8"));

				pushHandler.pushUser(getUid());

				Session session = ServerConnectorMgr.getInstance().getServerSession("battle");
				try {
					response = render();
					session.send(response.getMessage());
				} catch (IOException e) {
					throw new ServiceException(ServiceReturnCode.FAILD, e.getMessage());
				}

				return true;
			}
		});
	}

	/**
	 * 怪物战斗
	 * 
	 * @param type
	 * @param forcesId
	 * @param forceStar
	 * @param orderInGroup
	 * @param groupId
	 */
	private void monsterFight(final int type, final int forcesId) {

		sceneService.attack(getUid(), forcesId, new EventHandle() {

			public boolean handle(Event event) {

				String report = event.getString("report");
				int flag = event.getInt("flag");

				set("rp", report);
				set("rf", flag);
				set("uid", getUid());
				set("tp", type);
				set("dr", event.getObject("forcesDropBO"));
				set("dun", event.getObject("forcesName"));
				set("bgid", event.getInt("bgid"));
				set("ifs", event.getInt("ifs", 0));
				set("fid", forcesId);
				set("dhl", event.getObject("dhl"));
				set("ps", event.getObject("ps"));
				set("co", event.getObject("co"));
				// 普通怪最后一个
				UserForcesBO userForcesBO = forcesService.getUserCurrentForces(getUid(), ForcesType.FORCES_TYPE_NORMAL);
				set("mfid", userForcesBO != null ? userForcesBO.getGroupId() : 0);

				if (userForcesBO != null && userForcesBO.getStatus() == ForcesStatus.STATUS_PASS) {
					set("adf", 1);
				} else {
					set("adf", 0);
				}

				// 精英怪最后一个
				UserForcesBO userEliteForcesBO = forcesService.getUserCurrentForces(getUid(), ForcesType.FORCES_TYPE_ELITE);
				set("mefid", userEliteForcesBO != null ? userEliteForcesBO.getGroupId() : 0);

				set("sefid", 0);

				if (userEliteForcesBO != null && userEliteForcesBO.getStatus() == ForcesStatus.STATUS_PASS) {
					set("eadf", 1);

					int groupId = userEliteForcesBO.getGroupId();
					SystemForces systemForces = systemForcesDao.getLastForcesByGroupId(groupId);

					List<SystemForces> list = systemForcesDao.getSystemForcesByPreForcesId(systemForces.getForcesId());
					if (!list.isEmpty()) {
						set("sefid", list.get(0).getGroupId());
					}

				} else {
					set("eadf", 0);
				}

				pushHandler.pushUser(getUid());

				Session session = ServerConnectorMgr.getInstance().getServerSession("battle");
				try {
					response = render();
					session.send(response.getMessage());
				} catch (IOException e) {
					throw new ServiceException(ServiceReturnCode.FAILD, e.getMessage());
				}

				return true;
			}

		});
	}

	/**
	 * 副本挑战 type: 10,修炼密室,11,萧家宝库
	 */
	private void activityCopyFight(final int type) {

		blackRoomService.fight(getUid(), type, new EventHandle() {
			@Override
			public boolean handle(Event event) {
				String report = event.getString("report");
				int flag = event.getInt("flag");
				set("rp", report);
				set("rf", flag);
				set("uid", getUid());
				set("tp", type);
				set("dr", event.getObject("forcesDropBO"));
				set("co", event.getObject("co"));
				set("dun", event.getObject("dun"));
				set("bgid", event.getInt("bgid"));
				pushHandler.pushUser(getUid());

				Session session = ServerConnectorMgr.getInstance().getServerSession("battle");
				try {
					response = render();
					session.send(response.getMessage());
				} catch (IOException e) {
					throw new ServiceException(ServiceReturnCode.FAILD, e.getMessage());
				}

				return true;
			}
		});
	}

	private void expeditionFight(long exId) {
		expeditionService.fight(getUid(), exId, new EventHandle() {
			public boolean handle(Event event) {
				int flag = event.getInt("flag");
				String report = event.getString("report");
				set("rp", report);
				set("dr", event.getObject("gift"));
				set("rf", flag);
				set("tp", 13);

				set("rp", report);
				set("rf", flag);
				set("uid", getUid());
				set("co", event.getObject("co"));
				set("dun", event.getObject("dun"));
				set("myHero", event.getObject("myHero"));
				set("vsHero", event.getObject("vsHero"));
				Session session = ServerConnectorMgr.getInstance().getServerSession("battle");
				try {
					response = render();
					session.send(response.getMessage());
				} catch (IOException e) {
					throw new ServiceException(ServiceReturnCode.FAILD, e.getMessage());
				}
				return true;
			}
		});
	}

	private void resourceFight(int fbType, int fbDif, int g, int star) {
		resourceService.fight(getUid(), fbType, fbDif, g, star, new EventHandle() {
			@Override
			public boolean handle(Event event) {
				int flag = event.getInt("flag");
				String report = event.getString("report");
				set("rp", report);
				set("dr", event.getObject("gift"));
				set("rf", flag);
				set("tp", 12);

				set("rp", report);
				set("rf", flag);
				set("uid", getUid());
				set("co", event.getObject("co"));
				set("dun", event.getObject("dun"));
				// set("bgid", event.getInt("bgid"));
				Session session = ServerConnectorMgr.getInstance().getServerSession("battle");
				try {
					response = render();
					session.send(response.getMessage());
				} catch (IOException e) {
					throw new ServiceException(ServiceReturnCode.FAILD, e.getMessage());
				}
				return true;
			}
		});
	}

	public Response pass() throws IOException {

		String userId = this.getUid();

		LOG.debug("跳过战斗.userId[" + userId + "]");

		this.forcesService.passForcesBattle(getUid());

		return this.render();
	}
}
