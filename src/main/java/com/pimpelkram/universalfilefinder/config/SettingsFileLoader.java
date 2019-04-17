package com.pimpelkram.universalfilefinder.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Lädt JSON Properties als Einstellungen für das Programm. Es wird
 * standardmäßig im Home Verzeichnis des users erwartet.
 *
 * @author borsutzha
 *
 */
public class SettingsFileLoader {

	private final String homePath = System.getProperty("user.home");

	private final String expectedFileName = "universalfilefinder_properties.json";

	private final Path settingsPath = Paths.get(homePath + "\\" + expectedFileName);

	private ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

	private Logger logger = LoggerFactory.getLogger(Settings.class);
	/*
	 * Lädt Settings aus einer Datei im Home-Verzeichnis. Namen sind fest
	 * definiert. (Convention over Configuration)
	 *
	 */
	public Settings getSettings() {
		try {
			Settings settings = mapper.readValue(settingsPath.toFile(), Settings.class);
			if (settings != null) {
				return settings;
			} else {
				logger.error("Could not not load configuration file.");
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Could not not load configuration file.");
			return null;
		}
	}

}
