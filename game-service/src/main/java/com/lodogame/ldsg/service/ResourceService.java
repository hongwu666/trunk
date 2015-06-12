package com.lodogame.ldsg.service;

import java.util.List;

import com.lodogame.ldsg.bo.CommonDropBO;
import com.lodogame.ldsg.bo.ResourceJdBo;
import com.lodogame.ldsg.bo.ResourceSsBo;
import com.lodogame.ldsg.bo.ResourceViewBO;
import com.lodogame.ldsg.event.EventHandle;

public interface ResourceService {
	int YB_NOT_HAVE = 1001;
	int NUM_NOT_HAVE = 1002;
	int PK_ERR = 1003;
	int START_MAX = 1004;
	int NO_OPEN = 2012;
	int NO_BUY = 1006;
	
	int NO_GUAI =5000;

	ResourceViewBO show(String userId, int fbType);

	List<ResourceJdBo> showJd(String userId, int fbType, int fbDif);

	int buyNum(String userId, int num,int type);

	int startShow(String userId, int fbType, int fbDif, int g);

	int startOneKey(String userId, int fbType, int fbDif, int g);

	int startUp(String userId, int fbType, int fbDif, int g);

	void fight(String userId, int fbType, int fbDif, int g,int star, EventHandle eventHandle);

	CommonDropBO pkEnd(String userId, int fbType, int fbDif, int g);

	List<ResourceSsBo> showSs(String userId, int fbType, int fbDif);

	int getBuyPrice(String userId);

	int reset(String user, int fbType, int fbDif);
}
