package com.pimpelkram.universalfilefinder;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.pimpelkram.universalfilefinder.config.Settings;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AutocompleteController {

	private final Logger logger = LoggerFactory.getLogger(AutocompleteController.class);

	@Inject
	private Settings settings;

	@Inject
	private FileWalkingTask fwt;

	@FXML
	private TextField autocomplete;

	private final ObservableList<String> testList = FXCollections.observableArrayList();

	public void initialize() {
		logger.debug("Start init AutocompleteController.");
		testList.add("wurst");
		testList.add("käse");
		final Thread fwtThread = new Thread(fwt);
		fwtThread.setDaemon(true);
		fwtThread.start();
		final AutoCompletionBinding<String> acb = TextFields.bindAutoCompletion(autocomplete,
				p -> fwt.getPackageList().filtered(s -> s.contains(p.getUserText())));
		logger.debug("Ende init AutocompleteController.");
	}
}
