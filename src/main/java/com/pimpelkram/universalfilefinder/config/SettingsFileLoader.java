package com.pimpelkram.universalfilefinder.config;

import javax.json.Json;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Lädt JSON Properties als Einstellungen für das Programm. Es wird
 * standardmäßig im Home Verzeichnis des users erwartet.
 *
 * @author borsutzha
 *
 */
public class SettingsFileLoader implements SettingsLoader {

	private final String homePath = System.getProperty("user.home");

	private final String expectedFileName = "universalfilefinder_properties.json";

	private final Path settingsPath = Paths.get(homePath + "\\" + expectedFileName);

	private boolean checkConfigFile() {
		return Files.exists(settingsPath);
	}

	/*
	 * Lädt Settings aus einer Datei im Home-Verzeichnis. Namen sind fest
	 * definiert. (Convention over Configuration)
	 *
	 * @see com.pimpelkram.deliverytool.config.SettingsLoader#getSettings()
	 */
	@Override
	public Settings getSettings() {
		Settings settings = new Settings();
		if (!checkConfigFile()) {
			settings.getErrors().put("Settingsdatei \"" + settingsPath.toString() + "\" konnte nicht gefunden werden!.",
					ErrorTypes.FileNotFound);
		} else {
			final Charset charset = Charset.forName("UTF-8");
			BufferedReader fileReader;
			try {
				fileReader = Files.newBufferedReader(settingsPath, charset);
				final JsonReader reader = Json.createReader(fileReader);
				settings = new JsonSettingsBuilder().build(settings, reader);
			} catch (final IOException e) {
				settings.getErrors().put(
						"Settingsdatei \"" + settingsPath.toString() + "\" konnte nicht geladen werden!",
						ErrorTypes.InvalidJson);
			}
		}
		return settings;
	}

}
