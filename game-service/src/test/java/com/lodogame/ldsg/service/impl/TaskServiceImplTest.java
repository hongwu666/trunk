package com.lodogame.ldsg.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.lodogame.ldsg.service.TaskService;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class TaskServiceImplTest extends AbstractTestNGSpringContextTests {
	
	@Autowired
	public TaskService taskService;
	
	@Test
	public void getTaskListTest() {
		String userId = "54422b50e2014c6db63d1039f148c27c";
		taskService.getUserTaskList(userId, 100);
	}
}
