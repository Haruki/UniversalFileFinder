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
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class FileWalkingTask extends Task<ObservableList<String>> {

	private final Logger logger = LoggerFactory.getLogger(FileWalkingTask.class);
	private final ObservableList<String> packageList = FXCollections.observableArrayList();

	@Inject
	Settings settings;

	@Override
	protected ObservableList<String> call() throws Exception {
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

	public ObservableList<String> getPackageList() {
		return packageList;
	}

	private void handlePathFile(Path p) {
		logger.debug(p.toString());
		Platform.runLater(() -> getPackageList().add(p.toString()));
	}

}
