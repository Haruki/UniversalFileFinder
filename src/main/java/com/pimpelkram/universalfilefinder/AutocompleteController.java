package com.pimpelkram.universalfilefinder;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.pimpelkram.universalfilefinder.config.Settings;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AutocompleteController {

	Logger logger = LoggerFactory.getLogger(AutocompleteController.class);

	@Inject
	Settings settings;

	@Inject
	FileWalkingTask fwt;

	@FXML
	TextField autocomplete;

	public void initialize() {
		logger.debug("Start init AutocompleteController.");
		final Thread fwtThread = new Thread(fwt);
		fwtThread.setDaemon(true);
		fwtThread.start();
		final AutoCompletionBinding<String> acb = TextFields.bindAutoCompletion(autocomplete, fwt.getPackageList());
		logger.debug("Ende init AutocompleteController.");

	}
}
