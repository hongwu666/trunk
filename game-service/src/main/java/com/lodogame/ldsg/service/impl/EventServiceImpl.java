package com.lodogame.ldsg.service.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.lodogame.ldsg.event.CopperUpdateEvent;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;
import com.lodogame.ldsg.event.HeroColorEvent;
import com.lodogame.ldsg.event.HeroPowerUpdateEvent;
import com.lodogame.ldsg.event.HeroStarEvent;
import com.lodogame.ldsg.event.MuhonUpdateEvent;
import com.lodogame.ldsg.event.UpdateRankEvent;
import com.lodogame.ldsg.event.UserPowerUpdateEvent;
import com.lodogame.ldsg.event.VipLevelEvent;
import com.lodogame.ldsg.factory.EventHandleFactory;
import com.lodogame.ldsg.service.EventService;

public class EventServiceImpl implements EventService {

	private static final Logger logger = Logger.getLogger(EventServiceImpl.class);

	private BlockingQueue<Event> eventQueue = new ArrayBlockingQueue<Event>(1024);

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	@Override
	public void dispatchEvent(Event event) {
		try {
			eventQueue.put(event);
		} catch (InterruptedException ie) {
			logger.error(ie.getMessage(), ie);
		}
	}

	@Override
	public void addVipLevelEvent(String userId, int vipLevel) {
		Event event = new VipLevelEvent(userId, vipLevel);
		dispatchEvent(event);
	}

	@Override
	public void addCopperUpdateEvent(String userId, int copperNum) {
		CopperUpdateEvent event = new CopperUpdateEvent(userId, copperNum);
		dispatchEvent(event);
	}

	@Override
	public void addMuhonUpdateEvent(String userId, int muhonNum) {
		Event event = new MuhonUpdateEvent(userId, muhonNum);
		dispatchEvent(event);
	}

	@Override
	public void addHeroPowerUpdateEvent(String userId, String userHeroId) {
		Event event = new HeroPowerUpdateEvent(userId, userHeroId);
		dispatchEvent(event);
	}

	@Override
	public void addUserPowerUpdateEvent(String userId) {
		Event event = new UserPowerUpdateEvent(userId);
		dispatchEvent(event);
	}

	/**
	 * 处理事件
	 * 
	 * @param event
	 */
	private void handle(final Event event) {

		Runnable task = new Runnable() {

			@Override
			public void run() {
				EventHandle handle = EventHandleFactory.getInstance().getEventHandle(event.getClass().getSimpleName());
				if (handle != null) {
					handle.handle(event);
				}
			}
		};

		taskExecutor.execute(task);

	}

	@Override
	public void init() {

		new Thread(new Runnable() {

			public void run() {
				while (true) {

					logger.debug("事件处理线程");

					try {
						Event event = eventQueue.take();
						handle(event);

					} catch (Throwable t) {
						logger.error(t.getMessage(), t);

						try {
							Thread.sleep(1000);
						} catch (InterruptedException ie) {
							logger.error(ie.getMessage(), ie);
						}

					}

				}
			}

		}).start();

	}

	@Override
	public void addHeroStarEvent(String userId, int heroStar) {
		HeroStarEvent event = new HeroStarEvent(userId, heroStar);
		dispatchEvent(event);
	}

	@Override
	public void addHeroColorEvent(String userId, int heroColor) {
		HeroColorEvent event = new HeroColorEvent(userId, heroColor);
		dispatchEvent(event);
	}
	@Override
	public void addUpdateRankEvent(String userId,int type,Object obj){
		UpdateRankEvent event=new UpdateRankEvent(userId, type, obj);
		dispatchEvent(event);
	}

}
