package com.lodogame.game.dao.impl.cache;

import java.util.ArrayList;
import java.util.List;
import com.lodogame.game.dao.GiftDropDao;
import com.lodogame.game.dao.impl.mysql.GiftDropDaoMysqlImpl;
import com.lodogame.game.dao.reload.ReloadAble;
import com.lodogame.game.dao.reload.ReloadManager;
import com.lodogame.model.GiftDrop;

public class GiftDropDaoCacheImpl implements GiftDropDao, ReloadAble {

	private GiftDropDaoMysqlImpl giftDropDaoMysqlImpl;
	private List<GiftDrop> cache = new ArrayList<GiftDrop>();

	public void setGiftDropDaoMysqlImpl(GiftDropDaoMysqlImpl giftDropDaoMysqlImpl) {
		this.giftDropDaoMysqlImpl = giftDropDaoMysqlImpl;
	}

	@Override
	public GiftDrop get(int giftType, int giftBagType) {
		for (GiftDrop giftDrop : cache) {
			if (giftDrop.getGiftType() == giftType && giftDrop.getGiftBagType() == giftBagType) {
				return giftDrop;
			}
		}
		return null;
	}

	@Override
	public void reload() {
		initCache();
	}

	private void initCache() {
		cache = this.giftDropDaoMysqlImpl.getAllDrops();
	}

	@Override
	public void init() {
		initCache();
		ReloadManager.getInstance().register(getClass().getSimpleName(), this);
	}

	@Override
	public List<GiftDrop> getAllDrops() {
		return this.giftDropDaoMysqlImpl.getAllDrops();
	}

}
