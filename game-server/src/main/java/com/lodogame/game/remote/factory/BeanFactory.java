package com.lodogame.game.remote.factory;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanFactory {

	private static Logger logger = Logger.getLogger(BeanFactory.class);

	private static BeanFactory instance;

	private ApplicationContext applicationContext = null;

	private BeanFactory(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public static synchronized BeanFactory getInstance() {

		if (instance == null) {

			logger.info("初始化游戏服务器上下文相关数据");
			String[] locations = { "applicationContext.xml" };
			ApplicationContext applicationContext = new ClassPathXmlApplicationContext(locations);

			instance = new BeanFactory(applicationContext);
		}
		return instance;
	}

	public static synchronized BeanFactory getInstance(ApplicationContext applicationContext) {

		if (instance == null) {
			instance = new BeanFactory(applicationContext);
		}
		return instance;
	}

	public Object getBean(String name) {
		return applicationContext.getBean(name);
	}

	public boolean containsBean(String name) {
		return applicationContext.containsBean(name);
	}

}
