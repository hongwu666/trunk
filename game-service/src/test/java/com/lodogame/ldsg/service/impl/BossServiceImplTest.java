package com.lodogame.ldsg.service.impl;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class BossServiceImplTest extends AbstractTestNGSpringContextTests {

	// @Autowired
	// private BossService bossService;
	//
	// @Test
	// public void getUserBossListTest() {
	// String uid = "1085d7bf3cd74d0db3c70d2e7a5052e0";
	// List<UserBossBO> userBossList = bossService.getUserBossList(uid);
	// System.out.println(userBossList);
	// }
}
