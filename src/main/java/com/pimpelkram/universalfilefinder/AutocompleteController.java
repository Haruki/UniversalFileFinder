package com.pimpelkram.universalfilefinder;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import org.reactfx.EventSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.pimpelkram.universalfilefinder.config.Settings;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class AutocompleteController {

	private final Logger logger = LoggerFactory.getLogger(AutocompleteController.class);

	@Inject
	private Settings settings;

	// @FXML
	// private Button dragButton;

	@Inject
	private FileWalkingTask fwt;

	@FXML
	private CustomTextField autocomplete;

	@Inject
	EventBus eventBus;

	private Thread listUpdater;

	private LinkedBlockingQueue<FileChange> fileEventQueue = new LinkedBlockingQueue<>();

	private ExecutorService pool = Executors.newCachedThreadPool();

	private class ListUpdater implements Runnable {
		private Logger logger = LoggerFactory.getLogger(ListUpdater.class);

		private LinkedBlockingQueue<FileChange> queue;
		private ObservableMap<String, String> dataMap;

		// <T> Predicate<T> distinctByName(Function<? super T, ?> nameExtractor) {
		// Set<Object> seen = ConcurrentHashMap.newKeySet();
		// return t -> seen.add(nameExtractor.apply(t));
		// }

		public ListUpdater(LinkedBlockingQueue<FileChange> queue, ObservableMap<String, String> dataMap) {
			this.queue = queue;
			this.dataMap = dataMap;
		}

		@Override
		public void run() {
			this.logger.debug("Start ListUpdater Thread");
			for (;;) {
				try {
					Thread.sleep(3000);
					this.logger.debug("ListUpdater Checking...");
					Platform.runLater(() -> {
						while (!this.queue.isEmpty()) {
							this.queue.stream().distinct().forEach(fc -> {
								switch (fc.getFileEvent()) {
								case DELETED:
									this.logger.debug("Deleted: " + fc.getShortString());
									this.dataMap.remove(fc.getShortString());
									break;
								case CREATED:
									this.logger.debug("Created: " + fc.getShortString());
									this.dataMap.put(fc.getShortString(), fc.getPath().toString());
									break;
								case MODIFIED:
									this.logger.debug("Modified: " + fc.getShortString());
									break;
								default:
									this.logger.debug("WARN: what happened here?? " + fc.getShortString());
									break;
								}
							});
							// FileChange elem = this.queue.take();
							// this.logger.debug("FileEvent: " + elem);
							this.queue.clear();
						}
					});

				} catch (InterruptedException e) {
					this.logger.debug("Stopping ListUpdater Thread.");
					return;
				}
			}

		}
	}

	public void initialize() {

		// pool test:
		for (String dir : this.settings.getRootFolderList()) {
			Path path = Paths.get(dir);
			this.pool.submit(new FileChangeDetection(this.fileEventQueue, path));
		}
		// this.eventBus.register(this);

		final EventSource<Integer> numbers = new EventSource<>();
		numbers.subscribe(i -> System.out.println(i));
		numbers.push(7);

		this.logger.debug("Start init AutocompleteController.");
		// autocomplete = TextFields.createClearableTextField();
		try {
			setupClearButtonField(this.autocomplete);
		} catch (final Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		final Thread fwtThread = new Thread(this.fwt);
		fwtThread.setDaemon(true);
		fwtThread.start();
		final AutoCompletionBinding<String> acb = TextFields.bindAutoCompletion(this.autocomplete,
				p -> this.fwt.getPackageList().keySet().stream()
						.filter(s -> s.toLowerCase().contains(p.getUserText().toLowerCase()))
						.collect(Collectors.toSet()));
		// setup drag&drop:
		this.autocomplete.setOnDragDetected(e -> {
			final Dragboard db = this.autocomplete.startDragAndDrop(TransferMode.COPY);
			final ClipboardContent cc = new ClipboardContent();
			final ArrayList<String> selectedPaths = new ArrayList<>();
			selectedPaths.add(this.fwt.getPackageList().get(this.autocomplete.getText()));
			if (selectedPaths != null && !selectedPaths.isEmpty() && (selectedPaths.get(0) != null)) {
				this.logger.debug("selectedPaths: " + selectedPaths.get(0));
				cc.putFilesByPath(selectedPaths);
				db.setContent(cc);
			}
		});

		this.listUpdater = new Thread(new ListUpdater(this.fileEventQueue, this.fwt.getPackageList()));
		this.listUpdater.setDaemon(true);
		this.listUpdater.start();
		this.logger.debug("Ende init AutocompleteController.");
	}

	private void setupClearButtonField(CustomTextField customTextField) throws Exception {
		final Method m = TextFields.class.getDeclaredMethod("setupClearButtonField", TextField.class,
				ObjectProperty.class);
		m.setAccessible(true);
		m.invoke(null, customTextField, customTextField.rightProperty());
	}

	@FXML
	public void exitApplication() {
		this.logger.debug("interrupting ListUpdater Thread...");
		this.listUpdater.interrupt();
		// Platform.exit();
		this.logger.debug("Stopping executor...");
		this.pool.shutdownNow();
	}

	@Subscribe
	private void fileEvent(String fileEvent) {
		this.logger.debug("FileChange: " + fileEvent);
	}
}
