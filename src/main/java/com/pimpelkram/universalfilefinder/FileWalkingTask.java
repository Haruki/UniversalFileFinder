package com.pimpelkram.universalfilefinder;

import com.google.inject.Inject;
import com.pimpelkram.universalfilefinder.config.Settings;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.*;

public class FileWalkingTask extends Task<ObservableMap<String, String>> {

    private final Logger logger = LoggerFactory.getLogger(FileWalkingTask.class);
    private final ObservableMap<String, String> packageList = FXCollections.observableHashMap();

    @Inject
    Settings settings;

    @Override
    protected ObservableMap<String, String> call() throws Exception {
        this.logger.debug("FileWalkerTask call() start...");
        for (final String rootPathString : this.settings.getRoot()) {
            try {
                this.logger.debug("rootPathString : " + rootPathString);
                this.logger.debug(String.valueOf(Files.exists(Paths.get(rootPathString))));
                Files.walk(Paths.get(rootPathString), 1, FileVisitOption.FOLLOW_LINKS)
                        .filter(p -> !Files.isDirectory(p, LinkOption.NOFOLLOW_LINKS))
                        .peek(p -> System.out.println("Before Filter!: " + p.toString()))
                        .forEach(this::handlePathFile);
                this.logger.debug("finished for loop");
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        this.logger.debug("FileWalkerTask call() end...");
        return this.getPackageList();
    }

    ObservableMap<String, String> getPackageList() {
        return this.packageList;
    }

    private void handlePathFile(Path p) {
        this.logger.debug(p.toString());
        final String[] splits = p.toString().split("\\\\");
        final int lowIndex = splits.length - 3;
        final int useIndex = lowIndex < 0 ? 0 : lowIndex;
        //filter global regex:
        if (this.settings.getExcludeRegex() != null) {
            for (String rx : this.settings.getExcludeRegex()) {
                if (p.toString().matches(rx)) {
                    return;
                }
            }
        }
        // logger.debug("Splitlength: " + splits.length);
        final String shortString = "(" + splits[useIndex] + ") " + splits[splits.length - 1];
        // logger.debug("handlePathFile shortString: " + shortString);
        Platform.runLater(() -> this.getPackageList().put(shortString, p.toString()));
    }

}
