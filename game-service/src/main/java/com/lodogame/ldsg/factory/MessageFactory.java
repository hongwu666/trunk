package com.lodogame.ldsg.factory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.lodogame.ldsg.bo.Color;
import com.lodogame.ldsg.bo.Message;
import com.lodogame.ldsg.config.Config;
import com.lodogame.ldsg.helper.MessageHelper;

/**
 * 跑马灯消息工厂类
 * 
 * @author jacky
 * 
 */
public class MessageFactory {

	public static final Color WHITE = new Color(0, 0, 0);

	public static final Color GREEN = new Color(180, 255, 0);

	public static final Color BLUE = new Color(0, 183, 238);

	public static final Color PURPLE = new Color(197, 5, 176);

	public static final Color ORANGE = new Color(235, 99, 4);

	public static final Color YELLOW = new Color(255, 216, 0);

	public static final Color RED = new Color(228, 32, 32);

	public static Logger LOG = Logger.getLogger(MessageFactory.class);

	private static Map<String, List<Message>> templates = new HashMap<String, List<Message>>();

	private static MessageFactory messageFactory;

	private static Properties prop;

	private boolean inited;

	public List<Message> getMessage(String msgType, Map<String, String> params) {

		if (templates.containsKey(msgType)) {
			List<Message> msgList = templates.get(msgType);
			return render(msgList, params);
		}
		return null;
	}

	private List<Message> render(List<Message> msgList, Map<String, String> params) {

		List<Message> ml = new ArrayList<Message>();
		for (Message message : msgList) {

			Message msg = new Message();
			String text = message.getTxt();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				if (entry.getValue() == null) {
					LOG.error("参数为空.key[" + entry.getKey() + "]");
					text = text.replace("#" + entry.getKey() + "#", "");
				} else {
					text = text.replace("#" + entry.getKey() + "#", entry.getValue());
				}
			}

			if (text.length() > 0 && text.charAt(0) == '$') {
				text = text.substring(1, text.length());
				Color color = this.getColor(text);
				msg.setColor(color);
			} else {
				msg.setColor(message.getColor());
			}

			msg.setTxt(text);

			ml.add(msg);
		}

		return ml;

	}

	/**
	 * 获取颜色
	 * 
	 * @param text
	 * @return
	 */
	public Color getColor(String text) {

		if (StringUtils.equals(text, "白色")) {
			return WHITE;
		} else if (StringUtils.equals(text, "绿色")) {
			return GREEN;
		} else if (StringUtils.equals(text, "蓝色")) {
			return BLUE;
		} else if (StringUtils.equals(text, "紫色")) {
			return PURPLE;
		} else if (StringUtils.equals(text, "橙色")) {
			return ORANGE;
		} else if (StringUtils.equals(text, "黄色")) {
			return RED;
		} else if (StringUtils.equals(text, "攻击")) {
			return RED;
		} else if (StringUtils.equals(text, "防御")) {
			return ORANGE;
		} else if (StringUtils.equals(text, "生命")) {
			return GREEN;
		}

		return null;
	}

	public static MessageFactory ins() {
		synchronized (MessageFactory.class) {
			if (messageFactory == null) {
				messageFactory = new MessageFactory();
			}
		}
		return messageFactory;
	}

	private MessageFactory() {

		try {
			initConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private synchronized void initConfig() throws IOException {

		if (inited) {
			return;
		}
		inited = true;

		int isHK = Config.ins().getIsHK();

		if (isHK == 1) {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("message_ZH_HK.properties"));
		} else if (isHK == 0) {
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("message_ZH_CN.properties"));

		}

		for (Object key : prop.keySet()) {

			String s = key.toString();
			List<Message> messageList = MessageHelper.parse(new String(prop.getProperty(s).getBytes("ISO8859-1"), "utf8"));
			templates.put(s, messageList);
		}
	}

	public static void main(String[] args) {
		System.out.println("$".substring(0, 1));
	}
}
