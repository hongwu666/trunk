package com.lodogame.ldsg.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.dao.MeridianConfigDao;
import com.lodogame.game.dao.MeridianDao;
import com.lodogame.game.dao.MeridianNodeConfigDao;
import com.lodogame.game.dao.UserDao;
import com.lodogame.game.dao.UserToolDao;
import com.lodogame.ldsg.bo.MeridianMeridianBO;
import com.lodogame.ldsg.constants.ToolUseType;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.helper.BOHelper;
import com.lodogame.ldsg.service.HeroService;
import com.lodogame.ldsg.service.MeridianService;
import com.lodogame.ldsg.service.ToolService;
import com.lodogame.ldsg.service.UserService;
import com.lodogame.model.MeridianConfig;
import com.lodogame.model.MeridianNodeConfig;
import com.lodogame.model.MeridianUser;
import com.lodogame.model.MeridianUserInfo;
import com.lodogame.model.SystemHero;
import com.lodogame.model.User;
import com.lodogame.model.UserHero;

public class MeridianServiceImpl implements MeridianService {

	private static final Logger logger = Logger.getLogger(MeridianServiceImpl.class);

	@Autowired
	private MeridianDao meridianDao;

	@Autowired
	private HeroService heroService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserToolDao userToolDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private ToolService toolService;

	@Autowired
	private MeridianConfigDao meridianConfigDao;

	@Autowired
	private MeridianNodeConfigDao meridianNodeConfigDao;

	public List<MeridianMeridianBO> show(String userId) {

		MeridianUser user = meridianDao.getUserMeridian(userId);
		List<MeridianUserInfo> m = user.getAll();
		List<MeridianMeridianBO> list = BOHelper.getMeridianBO(m);

		return list;
	}

	public int[] getAddVal(UserHero hero) {

		int[] val = new int[9];
		MeridianUser user = meridianDao.getUserMeridian(hero.getUserId());
		Map<Integer, List<MeridianUserInfo>> infos = user.getHeroMeridian(hero.getUserHeroId());
		if (infos == null || infos.size() <= 0) {
			return val;
		}
		for (int temp : infos.keySet()) {
			List<MeridianUserInfo> info = infos.get(temp);
			for (MeridianUserInfo u : info) {
				if (u.getLevel() <= 0) {
					continue;
				}
				MeridianNodeConfig node = meridianNodeConfigDao.getNodeConfig(u.getMeridianNode(), u.getLevel());
				if (node == null) {
					continue;
				}
				val[temp - 1] += node.getValue();
			}
		}
		if (user.getTaiJi(hero.getUserHeroId())) {
			for (int i = 0; i < val.length; i++) {
				val[i] *= (val[i] * 1.10f);
			}
		}
		return val;
	}

	public MeridianUserInfo upLevel(String userId, int type, int nodeId, String userHeroId) {

		MeridianUser user = meridianDao.getUserMeridian(userId);
		MeridianUserInfo info = user.getInfo(type, nodeId, userHeroId);
		if (info == null || info.getLevel() < 1 || info.getLevel() >= 6) {
			String msg = "节点尚未开启或已经达到最大等级，不能升级";
			throw new ServiceException(NOT_OPEN, msg);
		}
		MeridianNodeConfig node = meridianNodeConfigDao.getNodeConfig(nodeId, info.getLevel());

		if (info.getExp() + node.getIncrement() >= node.getUpgradeRequired()) { // 即将升级
			int size = user.getOpenSize(type, userHeroId);
			if (size < info.getLevel()) {
				String msg = "节点个数不满足级别";
				throw new ServiceException(NO_NUM_NODE, msg);
			}
		}

		if (!userService.reduceSoul(userId, node.getIncrement(), ToolUseType.REDUCE_MERIDIAN_UP)) {
			String msg = "能量不足";
			throw new ServiceException(HL_NOT_HAVE, msg);
		}

		if (!userService.reduceMuhon(userId, node.getMuhonNeed(), ToolUseType.REDUCE_MERIDIAN_UP)) {
			String msg = "奶酪不足";
			throw new ServiceException(MUHON_NOT_HAVE, msg);
		}

		info.setLevel(info.getLevel() + 1);
		info.setExp(0);

		meridianDao.updateMeridianUserInfo(info);

		return info;
	}

	public MeridianUserInfo open(String userId, int type, int nodeId, String userHeroId) {

		MeridianConfig config = meridianConfigDao.getConfig(type, nodeId);

		UserHero hero = heroService.get(userId, userHeroId);
		SystemHero sHero = heroService.getSysHero(hero.getSystemHeroId());
		if (config.getCareer() != sHero.getCareer()) {
			String msg = "职业不符合";
			throw new ServiceException(NO_CA, msg);
		}
		if (sHero.getHeroColor() < config.getNeedColor()) {
			String msg = "武将阶级不够";
			throw new ServiceException(COLOR_NOT, msg);
		}
		MeridianUser meridianUser = meridianDao.getUserMeridian(userId);
		if (meridianUser.isOpen(type, nodeId, userHeroId)) {
			String msg = "这个节点已经打开过";
			throw new ServiceException(IS_OPEN, msg);
		}
		if (config.getNeedNode() != -1) {
			MeridianUserInfo needInfo = meridianUser.getInfo(type, config.getNeedNode(), userHeroId);
			if (needInfo == null || needInfo.getLevel() < 1) {
				String msg = "前置节点未打开";
				throw new ServiceException(REQUIRED_NODE_NOT_OPEN, msg);
			}
		}
		int indx = nodeId % 10;
		if (!meridianUser.isOpen(type, nodeId, userHeroId) && meridianUser.isMax(userHeroId) && indx == 1) {
			String msg = "最多开4个";
			throw new ServiceException(NO_MORE, msg);
		}

		// check tool enough
		checkToolEnough(userId, config);

		// reduce tool
		reduceTool(userId, config);

		int luckAdd = config.getLuckAdd();

		MeridianUserInfo info = meridianUser.getInfo(type, nodeId, userHeroId);
		if (info == null) {
			luckAdd = config.getMaxLuck() / 2 + config.getLuckAdd();
			info = MeridianUserInfo.create(userId, userHeroId, type, nodeId);
			meridianDao.insertMeridianUserInfo(info);
		}
		boolean result = checkOpen(info, config);
		if (result) { // 成功升为一级
			info.setLevel(1);
			meridianDao.updateMeridianUserInfo(info);
		} else {// 失败累计成功率
			info.addLuck(luckAdd);
			meridianDao.updateMeridianUserInfo(info);
		}
		return info;
	}

	/**
	 * 实际的扣道具
	 * 
	 * @param userId
	 * @param config
	 */
	private void reduceTool(String userId, MeridianConfig config) {

		if (!userService.reduceSoul(userId, config.getConsume(), ToolUseType.REDUCE_MERIDIAN_OPEN)) {
			String msg = "能量不足";
			throw new ServiceException(HL_NOT_HAVE, msg);
		}

		if (!userService.reduceMuhon(userId, config.getMuhonNeed(), ToolUseType.REDUCE_MERIDIAN_OPEN)) {
			String msg = "奶酪不足";
			throw new ServiceException(MUHON_NOT_HAVE, msg);
		}

		if (!userService.reduceMingwen(userId, config.getMingwenNeed(), ToolUseType.REDUCE_MERIDIAN_OPEN)) {
			String msg = "铭文不足";
			throw new ServiceException(MINGWEN_NOT_HAVE, msg);
		}
	}

	/**
	 * 开始信仰时判断道具有没有足够
	 * 
	 * @param userId
	 * @param config
	 */
	private void checkToolEnough(String userId, MeridianConfig config) {
		User user = this.userService.get(userId);
		if (user.getSoul() < config.getConsume() && config.getConsume() > 0) {
			String msg = "能量不足";
			throw new ServiceException(HL_NOT_HAVE, msg);
		}

		if (user.getMuhon() < config.getMuhonNeed() && config.getMuhonNeed() > 0) {
			String msg = "奶酪不足";
			throw new ServiceException(MUHON_NOT_HAVE, msg);
		}

		if (user.getMingwen() < config.getMingwenNeed() && config.getMingwenNeed() > 0) {
			String msg = "铭文不足";
			throw new ServiceException(MINGWEN_NOT_HAVE, msg);
		}
	}

	public int getSellSoul(String userId, String userHeroId) {
		List<MeridianUserInfo> infoList = meridianDao.getMeridianUserInfo(userId);
		int soul = 0;
		for (MeridianUserInfo meridianUserInfo : infoList) {
			if (!meridianUserInfo.getUserHeroId().equals(userHeroId)) {
				continue;
			}
			if (meridianUserInfo.getLevel() > 1) {
				for (int i = 1; i < meridianUserInfo.getLevel(); i++) {
					MeridianNodeConfig config = meridianNodeConfigDao.getNodeConfig(meridianUserInfo.getMeridianNode(), i);
					soul += config.getUpgradeRequired();
				}
			}
		}
		return soul;

	}

	public int getSellMingwen(String userId, String userHeroId) {
		List<MeridianUserInfo> infoList = meridianDao.getMeridianUserInfo(userId);
		int mingwen = 0;
		for (MeridianUserInfo meridianUserInfo : infoList) {
			if (!meridianUserInfo.getUserHeroId().equals(userHeroId)) {
				continue;
			}
			MeridianConfig config = meridianConfigDao.getConfig(meridianUserInfo.getMeridianType(), meridianUserInfo.getMeridianNode());
			// 已点亮的节点
			if (meridianUserInfo.getLevel() > 0) {
				// 節點是101或201.。。x01
				if (meridianUserInfo.getMeridianNode() % 10 == 1) {
					mingwen += config.getMingwenNeed() * 2;
				} else {
					mingwen += ((config.getMinLuck() - config.getMaxLuck() / 2) / config.getLuckAdd() + 2) * config.getMingwenNeed();
				}
			} else {
				mingwen += ((meridianUserInfo.getLuck() - config.getMaxLuck() / 2) / config.getLuckAdd()) * config.getMingwenNeed();
			}

		}
		return mingwen;

	}

	@Override
	public int getSellMuhon(String userId, String userHeroId) {

		List<MeridianUserInfo> infoList = meridianDao.getMeridianUserInfo(userId);
		int muhon = 0;
		for (MeridianUserInfo meridianUserInfo : infoList) {

			if (!meridianUserInfo.getUserHeroId().equals(userHeroId)) {
				continue;
			}

			MeridianConfig config = meridianConfigDao.getConfig(meridianUserInfo.getMeridianType(), meridianUserInfo.getMeridianNode());
			// 已点亮的节点
			if (meridianUserInfo.getLevel() > 0) {
				// 節點是101或201.。。x01
				if (meridianUserInfo.getMeridianNode() % 10 == 1) {
					muhon += config.getMuhonNeed();
				} else {
					muhon += ((config.getMinLuck() - config.getMaxLuck() / 2) / config.getLuckAdd() + 1) * config.getMuhonNeed();
				}
			} else {
				muhon += ((meridianUserInfo.getLuck() - config.getMaxLuck() / 2) / config.getLuckAdd()) * config.getMuhonNeed();
			}

			if (meridianUserInfo.getLevel() > 1) {
				for (int i = 1; i < meridianUserInfo.getLevel(); i++) {
					MeridianNodeConfig meridianNodeConfig = meridianNodeConfigDao.getNodeConfig(meridianUserInfo.getMeridianNode(), i);
					muhon += meridianNodeConfig.getMuhonNeed();
				}
			}

		}

		return (int) (muhon * 0.9);
	}

	// 开启
	private boolean checkOpen(MeridianUserInfo info, MeridianConfig config) {
		if (info.getLuck() < config.getMinLuck()) { // 小于最低值，不成功
			return false;
		}
		if (info.getLuck() >= config.getMaxLuck()) { // 大于最大值，成功
			return true;
		}
		int v = info.getLuck() - config.getMinLuck();
		int rate = (v / config.getLuckAdd()) * config.getChance();
		int rand = RandomUtils.nextInt(100);
		return rand < rate;
	}

}
