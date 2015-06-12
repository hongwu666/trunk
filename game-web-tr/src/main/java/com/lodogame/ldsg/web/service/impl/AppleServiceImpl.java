package com.lodogame.ldsg.web.service.impl;

import com.lodogame.ldsg.web.sdk.AppleSdk;
import com.lodogame.ldsg.web.service.BasePartnerService;


public class AppleServiceImpl extends BasePartnerService {

	@Override
	public String getPayBackUrl() {
		return AppleSdk.instance().getPayBackUrl();
	}


}
