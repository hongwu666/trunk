package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.NotImplementedException;

import com.lodogame.game.dao.BasePreloadAble;
import com.lodogame.game.dao.TavernDropToolDao;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.model.TavernAmendDropTool;
import com.lodogame.model.TavernDropTool;

public class TavernDropToolDaoCacheImpl extends BasePreloadAble implements TavernDropToolDao, ReloadAble {

	private Map<Integer, List<TavernDropTool>> tavernDropToolMap = new ConcurrentHashMap<Integer, List<TavernDropTool>>();

	private Map<Integer, List<TavernAmendDropTool>> tavernAmendDropToolMap = new ConcurrentHashMap<Integer, List<TavernAmendDropTool>>();

	private TavernDropToolDao tavernDropToolDaoMysqlImpl;

	public void setTavernDropToolDaoMysqlImpl(TavernDropToolDao tavernDropToolDaoMysqlImpl) {
		this.tavernDropToolDaoMysqlImpl = tavernDropToolDaoMysqlImpl;
	}

	@Override
	public List<TavernDropTool> getTavernDropToolList(int type) {
		return this.tavernDropToolMap.get(type);
	}

	@Override
	public List<TavernDropTool> getTavernDropToolList() {
		throw new NotImplementedException();
	}

	@Override
	public List<TavernAmendDropTool> getTavernAmendDropToolList() {
		throw new NotImplementedException();
	}

	@Override
	public List<TavernAmendDropTool> getTavernAmendDropToolList(int type) {
		return this.tavernAmendDropToolMap.get(type);
	}

	protected void initData() {

		tavernDropToolMap.clear();
		tavernAmendDropToolMap.clear();

		List<TavernDropTool> list = this.tavernDropToolDaoMysqlImpl.getTavernDropToolList();
		for (TavernDropTool t : list) {
			List<TavernDropTool> l = this.tavernDropToolMap.get(t.getGroupId());
			if (tavernDropToolMap.containsKey(t.getGroupId())) {
				l = this.tavernDropToolMap.get(t.getGroupId());
			} else {
				l = new ArrayList<TavernDropTool>();
			}
			l.add(t);

			tavernDropToolMap.put(t.getGroupId(), l);
		}

		List<TavernAmendDropTool> alist = this.tavernDropToolDaoMysqlImpl.getTavernAmendDropToolList();
		for (TavernAmendDropTool t : alist) {
			List<TavernAmendDropTool> l = this.tavernAmendDropToolMap.get(t.getType());
			if (tavernAmendDropToolMap.containsKey(t.getType())) {
				l = this.tavernAmendDropToolMap.get(t.getType());
			} else {
				l = new ArrayList<TavernAmendDropTool>();
			}
			l.add(t);

			tavernAmendDropToolMap.put(t.getType(), l);
		}

	}

}
