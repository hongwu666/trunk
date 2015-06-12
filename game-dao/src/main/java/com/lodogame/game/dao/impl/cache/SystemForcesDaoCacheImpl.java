package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.NotImplementedException;

import com.lodogame.game.dao.BasePreloadAble;
import com.lodogame.game.dao.SystemForcesDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.model.SystemForces;

public class SystemForcesDaoCacheImpl extends BasePreloadAble implements SystemForcesDao, ReloadAble {

	private SystemForcesDao systemForcesDaoMysqlImpl;

	private Map<Integer, SystemForces> systemForcesMap = new ConcurrentHashMap<Integer, SystemForces>();

	private Map<Integer, List<SystemForces>> preSystemForcesMap = new ConcurrentHashMap<Integer, List<SystemForces>>();

	// key 是部队的 groupId
	private Map<Integer, List<SystemForces>> forcesGroupMap = new ConcurrentHashMap<Integer, List<SystemForces>>();

	public void setSystemForcesDaoMysqlImpl(SystemForcesDao systemForcesDaoMysqlImpl) {
		this.systemForcesDaoMysqlImpl = systemForcesDaoMysqlImpl;
	}

	@Override
	public List<SystemForces> getSysForcesList() {
		throw new NotImplementedException();
	}

	@Override
	public SystemForces get(int forcesId) {
		return systemForcesMap.get(forcesId);
	}

	@Override
	public List<SystemForces> getSystemForcesByPreForcesId(int preForcesId) {
		return preSystemForcesMap.get(preForcesId);
	}

	protected void initData() {

		systemForcesMap.clear();
		preSystemForcesMap.clear();
		forcesGroupMap.clear();

		List<SystemForces> lis = this.systemForcesDaoMysqlImpl.getSysForcesList();
		for (SystemForces systemForces : lis) {
			systemForcesMap.put(systemForces.getForcesId(), systemForces);

			int groupId = systemForces.getGroupId();
			List<SystemForces> fs = forcesGroupMap.get(groupId);
			if (fs == null) {
				fs = new ArrayList<SystemForces>();
			}
			fs.add(systemForces);
			forcesGroupMap.put(groupId, fs);

			int preForcesId = systemForces.getPreForcesId();
			int preForcesIdB = systemForces.getPreForcesIdb();

			if (preForcesId == 0 && preForcesIdB == 0) {
				List<SystemForces> l = preSystemForcesMap.get(0);
				if (l == null) {
					l = new ArrayList<SystemForces>();
				}
				l.add(systemForces);
				preSystemForcesMap.put(0, l);
			} else {
				if (preForcesId > 0) {
					List<SystemForces> l = preSystemForcesMap.get(preForcesId);
					if (l == null) {
						l = new ArrayList<SystemForces>();
					}
					l.add(systemForces);
					preSystemForcesMap.put(preForcesId, l);
				}

				if (preForcesIdB > 0) {
					List<SystemForces> l = preSystemForcesMap.get(preForcesIdB);
					if (l == null) {
						l = new ArrayList<SystemForces>();
					}
					l.add(systemForces);
					preSystemForcesMap.put(preForcesIdB, l);
				}
			}

		}

	}

	@Override
	public List<SystemForces> getSystemForcesByGroupId(int forcesGroupId) {
		List<SystemForces> forcesList = forcesGroupMap.get(forcesGroupId);
		return forcesList;
	}

	@Override
	public SystemForces getLastForcesByGroupId(int forcesGroupId) {
		List<SystemForces> forcesList = getSystemForcesByGroupId(forcesGroupId);
		for (SystemForces forces : forcesList) {
			if (forces.getIsLast() == 1) {
				return forces;
			}
		}

		return null;
	}

}
