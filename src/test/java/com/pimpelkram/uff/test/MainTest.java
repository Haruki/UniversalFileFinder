package com.pimpelkram.uff.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Homer on 05.06.2017.
 */
public class MainTest {

	Logger logger = LoggerFactory.getLogger(MainTest.class);

	public static void main(String[] args) {
		new MainTest().start();
	}

	private void start() {
		final String pathToHome = System.getProperty("user.home");
		logger.debug("User home directory: " + pathToHome);

		final Observable<DirectoryEvent> directoryStream = Observable.create(o -> {
			final Path configPath = Paths.get(pathToHome + File.separator + "testConfig.yml");
			logger.debug(configPath.toString());
			Config config;
			final Yaml yaml = new Yaml();
			config = yaml.loadAs(new FileInputStream(configPath.toString()), Config.class);
			System.out.println(config);
			for (final String dir : config.directories) {
				try {
					System.out.println(dir);
					o.onNext(new DirectoryEvent(ChangeTyp.I, dir));
				} catch (final Exception e) {
					o.onError(e.fillInStackTrace());
				}
			}

			final WatchService watcher = configPath.getFileSystem().newWatchService();

			final WatchKey key = configPath.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);

			for (;;) {
				watcher.take();
				for (final WatchEvent<?> event : key.pollEvents()) {
					final WatchEvent.Kind<?> kind = event.kind();

					// This key is registered only
					// for ENTRY_CREATE events,
					// but an OVERFLOW event can
					// occur regardless if events
					// are lost or discarded.
					if (kind == StandardWatchEventKinds.OVERFLOW) {
						continue;
					}

					// The filename is the
					// context of the event.
					final WatchEvent<Path> ev = (WatchEvent<Path>) event;
					final Path filename = ev.context();

					// Verify that the new
					// file is a text file.
					try {
						// Resolve the filename against the directory.
						// If the filename is "test" and the directory is "foo",
						// the resolved name is "test/foo".
						// final Path child = Paths.resolve(filename);
						if (!Files.probeContentType(filename).equals("text/plain")) {
							System.err.format("New file '%s'" + " is not a plain text file.%n", filename);
							continue;
						}
					} catch (final IOException x) {
						System.err.println(x);
						continue;
					}

					// Email the file to the
					// specified email alias.
					System.out.format("Emailing file %s%n", filename);
					// Details left to reader....
				}

				// Reset the key -- this step is critical if you want to
				// receive further watch events. If the key is no longer valid,
				// the directory is inaccessible so exit the loop.
				final boolean valid = key.reset();
				if (!valid) {
					break;
				}
			}

		});

		// directoryStream.subscribe(event ->
		// System.out.println(event.getChangeTyp() + " " +
		// event.getPathName()));

		directoryStream.flatMap(directoryEvent -> Observable.just(directoryEvent).subscribeOn(Schedulers.computation()))
				.blockingSubscribe(s -> System.out.println(s));

	}

	private void createFileWatcher(DirectoryEvent directoryEvent) {
		Observable.create(o -> {
			// WatchKey key = configPath.register(watcher, ENTRY_CREATE,
			// StandardWatchEventKinds.ENTRY_DELETE,
			// StandardWatchEventKinds.ENTRY_MODIFY);

		});
	}
}
