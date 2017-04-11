package com.pimpelkram.universalfilefinder;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.pimpelkram.universalfilefinder.config.Settings;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;

public class FileWalkingTask extends Task<ObservableMap<String, String>> {

	private final Logger logger = LoggerFactory.getLogger(FileWalkingTask.class);
	private final ObservableMap<String, String> packageList = FXCollections.observableHashMap();

	@Inject
	Settings settings;

	@Override
	protected ObservableMap<String, String> call() throws Exception {
		logger.debug("FileWalkerTask call() start...");
		for (final String rootPathString : settings.getRootFolderList()) {
			try {
				logger.debug("rootPathString : " + rootPathString);
				logger.debug(String.valueOf(Files.exists(Paths.get(rootPathString))));
				Files.walk(Paths.get(rootPathString)).filter(p -> !Files.isDirectory(p, LinkOption.NOFOLLOW_LINKS))
						// .filter(p -> p.toString().matches(getPositiveRegex())
						// && !p.toString().matches(getNegativeRegex()))
						.forEach(p -> handlePathFile(p));
				logger.debug("finished for loop");
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		logger.debug("FileWalkerTask call() end...");
		return getPackageList();
	}

	public ObservableMap<String, String> getPackageList() {
		return packageList;
	}

	private void handlePathFile(Path p) {
		logger.debug(p.toString());
		final String[] splits = p.toString().split("\\\\");
		final String shortString = "(" + splits[5] + ") " + splits[7];
		Platform.runLater(() -> getPackageList().put(shortString, p.toString()));
	}

}
