package com.lodogame.ldsg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.lodogame.game.dao.impl.mysql.SystemForcesDaoMysqlImpl;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class SceneServiceTest extends AbstractTestNGSpringContextTests {

	String userId = "9052022d2700422eabf13e4e7eeb6bf9";

	@Autowired
	private SceneService sceneService;

	
	@Autowired
	private SystemForcesDaoMysqlImpl systemForcesDaoMysqlImpl;
	
	@Test
	public void attackTest() {

	}
}
