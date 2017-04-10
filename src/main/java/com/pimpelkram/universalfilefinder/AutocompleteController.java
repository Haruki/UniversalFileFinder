package com.pimpelkram.universalfilefinder;

import java.util.ArrayList;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.pimpelkram.universalfilefinder.config.Settings;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class AutocompleteController {

	private final Logger logger = LoggerFactory.getLogger(AutocompleteController.class);

	@Inject
	private Settings settings;

	@Inject
	private FileWalkingTask fwt;

	@FXML
	private TextField autocomplete;

	public void initialize() {
		logger.debug("Start init AutocompleteController.");
		final Thread fwtThread = new Thread(fwt);
		fwtThread.setDaemon(true);
		fwtThread.start();
		final AutoCompletionBinding<String> acb = TextFields.bindAutoCompletion(autocomplete,
				p -> fwt.getPackageList().filtered(s -> s.contains(p.getUserText())));
		// setup drag&drop:
		autocomplete.setOnDragDetected(e -> {
			final Dragboard db = autocomplete.startDragAndDrop(TransferMode.COPY);
			final ClipboardContent cc = new ClipboardContent();
			final ArrayList<String> selectedPaths = new ArrayList<>();
			selectedPaths.add(autocomplete.getText());
			cc.putFilesByPath(selectedPaths);
			db.setContent(cc);
		});
		logger.debug("Ende init AutocompleteController.");
	}
}
