package com.lodogame.ldsg.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.lodogame.game.dao.ContestDao;
import com.lodogame.game.remote.callback.Callback;
import com.lodogame.game.remote.factory.BeanFactory;
import com.lodogame.game.remote.handle.RemoteCallHandle;
import com.lodogame.game.remote.request.Request;
import com.lodogame.game.remote.response.Response;
import com.lodogame.ldsg.service.ContestService;
import com.lodogame.model.ContestUser;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class ContestServiceImplTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private ContestService service;

	@Autowired
	private ContestDao contestDao;

	@Autowired
	private RemoteCallHandle remoteCallHandle;

	@Test
	public void getBattleBO() {

		BeanFactory.getInstance(this.applicationContext);

		List<ContestUser> list = this.contestDao.getList();
		for (ContestUser user : list) {
			// BattleBO battleBO = this.service.getBattleBO(user.getUserId(),
			// true);
			// Response response = new Response();
			// response.put("battleBO", battleBO);
			// System.out.println(JSON.toJSON(response));

			Request request = new Request();
			request.setAction("ContestRemote");
			request.setMethod("getBattleBO");

			request.put("userId", user.getUserId());

			remoteCallHandle.call("d1", request, new Callback() {

				@Override
				public void handle(Response resp) {
					System.out.println(JSON.toJSON(resp.getObject("battleBO")));
				}
			});

		}
	}
}
