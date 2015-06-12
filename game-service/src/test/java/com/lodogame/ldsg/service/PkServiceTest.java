package com.lodogame.ldsg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.model.User;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class PkServiceTest extends AbstractTestNGSpringContextTests {
	String userId = "711615315fbd495a84bebb3f6f0d01da";
	int awardId = 1;
	int num = 2;

	@Autowired
	private PkService pkService;

	@Autowired
	private UserService userService;

	@Test
	public void exchangeTest() {
		// pkService.exchange(userId, awardId);

	}

	@Test
	public void batchExchangeTest() {
		// pkService.batchExchange(userId, awardId, num);
	}

	@Test
	public void enterPkTest() {

		User user = userService.getRandomUserFromDB();

		pkService.enter(user.getUserId(), new EventHandle() {

			@Override
			public boolean handle(Event event) {

				return true;
			}
		});

	}

	@Test
	public void getGrankTen() {
		// System.out.println(pkService.getGrankTen(10).size());
		// System.out.println();
		// System.out.println();
		// System.out.println();
		// System.out.println();
	}

	@Test
	public void getGrankFirst() {
		// System.out.println(pkService.getGrankFirst().size());
		// System.out.println();
		// System.out.println();
		// System.out.println();
		// System.out.println();
	}
}
