package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.NotImplementedException;

import com.lodogame.game.dao.SystemHeroSkillDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.SystemHeroSkill;

public class SystemHeroSkillDaoCacheImpl implements SystemHeroSkillDao, ReloadAble {

	private SystemHeroSkillDao systemHeroSkillDaoMysqlImpl;

	private Map<Integer, List<SystemHeroSkill>> heroSkillMap = new ConcurrentHashMap<Integer, List<SystemHeroSkill>>();

	public void setSystemHeroSkillDaoMysqlImpl(SystemHeroSkillDao systemHeroSkillDaoMysqlImpl) {
		this.systemHeroSkillDaoMysqlImpl = systemHeroSkillDaoMysqlImpl;
	}

	@Override
	public List<SystemHeroSkill> getHeroSkillList(int heroId) {

		if (heroSkillMap.containsKey(heroId)) {
			return heroSkillMap.get(heroId);
		}
		List<SystemHeroSkill> systemHeroSkillList;

		synchronized (SystemHeroSkillDaoCacheImpl.class) {

			systemHeroSkillList = this.systemHeroSkillDaoMysqlImpl.getHeroSkillList(heroId);
			if (systemHeroSkillList != null) {
				heroSkillMap.put(heroId, systemHeroSkillList);
			}
		}

		return systemHeroSkillList;
	}

	private void initData() {

		List<SystemHeroSkill> list = this.systemHeroSkillDaoMysqlImpl.getList();
		for (SystemHeroSkill systemHeroSkill : list) {
			int heroId = systemHeroSkill.getHeroId();
			List<SystemHeroSkill> l = null;
			if (heroSkillMap.containsKey(heroId)) {
				l = heroSkillMap.get(heroId);
			} else {
				l = new ArrayList<SystemHeroSkill>();
			}
			l.add(systemHeroSkill);
			heroSkillMap.put(heroId, l);

		}
	}

	@Override
	public List<SystemHeroSkill> getList() {
		throw new NotImplementedException();
	}

	@Override
	public void reload() {
		heroSkillMap.clear();
	}

	@Override
	public void init() {
		initData();
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

}
