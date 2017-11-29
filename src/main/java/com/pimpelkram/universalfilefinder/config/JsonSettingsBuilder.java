package com.pimpelkram.universalfilefinder.config;

import java.util.stream.Collectors;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Baut aus einer JSON-Datei das Settings-Objekt. Alle JsonStrings werden ohne
 * umschließende Anführungszeichen übernommen.
 *
 * @author borsutzha
 */
public class JsonSettingsBuilder {

	public Settings build(Settings settings, JsonReader reader) {
		if (settings == null) {
			settings = new Settings();
		}
		try {
			final JsonObject jobject = reader.readObject();
			// Property 1: Wurzelverzeichnis(se) für das Einlesen von
			// Lieferungen:
			final JsonArray jsonRootDirList = jobject.getJsonArray("root");
			final ObservableList<String> rootDirStringList = FXCollections.observableArrayList(jsonRootDirList.stream()
					.map(value -> value.toString().replaceAll("\"", "")).collect(Collectors.toList()));
			settings.setRootFolderList(new SimpleListProperty<>(rootDirStringList));
		} catch (final Exception e) {

			// settings.getErrors().put(e.getMessage(), ErrorTypes.InvalidJson);
			e.printStackTrace();
		}
		return settings;
	}
}
