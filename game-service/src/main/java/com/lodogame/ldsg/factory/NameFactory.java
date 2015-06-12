package com.lodogame.ldsg.factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

public class NameFactory {

	private static final Logger logger = Logger.getLogger(NameFactory.class);

	private static NameFactory instance = null;

	private int index = 0;

	private List<String> names = null;

	private NameFactory() {

		index = RandomUtils.nextInt(3500);

		init();
	}

	private void init() {

		names = new ArrayList<String>();
		BufferedReader reader = null;
		String line = "";
		try {
			reader = new BufferedReader(new InputStreamReader((new ClassPathResource("name.properties")).getInputStream()));
			while ((line = reader.readLine()) != null) {
				names.add(line);
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

	public static NameFactory getInstance() {

		if (instance == null) {
			instance = new NameFactory();
		}
		return instance;
	}

	public String getName() {
		String name = this.names.get(index++);
		if (index >= this.names.size()) {
			index = 0;
		}
		return name;
	}

}
