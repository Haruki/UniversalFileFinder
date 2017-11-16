package com.pimpelkram.universalfilefinder;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;

public class FileChangeDetection implements Runnable {

	private final Logger logger = LoggerFactory.getLogger(FileChangeDetection.class);
	private EventBus eventBus;
	private Path targetPath;

	public FileChangeDetection(EventBus eventBus, Path targetPath) {
		this.eventBus = eventBus;
		this.targetPath = targetPath;
	}

	@Override
	public void run() {
		WatchService watcher = null;
		WatchKey key;
		try {
			watcher = FileSystems.getDefault().newWatchService();
			Path path = this.targetPath;

			path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.ENTRY_MODIFY);
		} catch (IOException e) {
			this.logger.error(e.getMessage());
		}
		for (;;) {
			try {
				key = watcher.take();
			} catch (InterruptedException x) {
				return;
			}

			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind<?> kind = event.kind();
				if (kind == StandardWatchEventKinds.OVERFLOW) {
					continue;
				}
				@SuppressWarnings("unchecked")
				WatchEvent<Path> ev = (WatchEvent<Path>) event;
				Path filename = ev.context();
				this.logger.debug("New Event: " + filename);
				this.eventBus.post(filename);
				continue;
			}

			// Reset the key -- this step is critical if you want to
			// receive further watch events. If the key is no longer valid,
			// the directory is inaccessible so exit the loop.
			boolean valid = key.reset();
			if (!valid) {
				break;
			}
		}

	}

}
