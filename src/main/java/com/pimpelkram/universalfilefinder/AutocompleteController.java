package com.pimpelkram.universalfilefinder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.pimpelkram.universalfilefinder.config.Settings;

import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class AutocompleteController {

	private final Logger logger = LoggerFactory.getLogger(AutocompleteController.class);

	@Inject
	private Settings settings;

	@FXML
	private Button dragButton;

	@Inject
	private FileWalkingTask fwt;

	@FXML
	private CustomTextField autocomplete;

	public void initialize() {
		logger.debug("Start init AutocompleteController.");
		// autocomplete = TextFields.createClearableTextField();
		try {
			setupClearButtonField(autocomplete);
		} catch (final Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		final Thread fwtThread = new Thread(fwt);
		fwtThread.setDaemon(true);
		fwtThread.start();
		final AutoCompletionBinding<String> acb = TextFields.bindAutoCompletion(autocomplete,
				p -> fwt.getPackageList().keySet().stream()
						.filter(s -> s.toLowerCase().contains(p.getUserText().toLowerCase()))
						.collect(Collectors.toSet()));
		// setup drag&drop:
		autocomplete.setOnDragDetected(e -> {
			final Dragboard db = autocomplete.startDragAndDrop(TransferMode.COPY);
			final ClipboardContent cc = new ClipboardContent();
			final ArrayList<String> selectedPaths = new ArrayList<>();
			selectedPaths.add(fwt.getPackageList().get(autocomplete.getText()));
			if (selectedPaths != null && !selectedPaths.isEmpty() && (selectedPaths.get(0) != null)) {
				logger.debug("selectedPaths: " + selectedPaths.get(0));
				cc.putFilesByPath(selectedPaths);
				db.setContent(cc);
			}
		});
		logger.debug("Ende init AutocompleteController.");
	}

	private void setupClearButtonField(CustomTextField customTextField) throws Exception {
		final Method m = TextFields.class.getDeclaredMethod("setupClearButtonField", TextField.class,
				ObjectProperty.class);
		m.setAccessible(true);
		m.invoke(null, customTextField, customTextField.rightProperty());
	}
}
