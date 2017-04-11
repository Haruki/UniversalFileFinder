package com.pimpelkram.universalfilefinder;

import java.util.Arrays;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gluonhq.ignite.guice.GuiceContext;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	Logger logger = LoggerFactory.getLogger(Main.class);

	private final GuiceContext context = new GuiceContext(this, () -> Arrays.asList(new DefaultModule()));
	@Inject
	FXMLLoader fxmlLoader;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() {
		logger.debug("Main FXApplication 'init' start.");
		logger.debug("Main FXApplication 'init' end.");
	}

	@Override
	public void stop() {
		logger.debug("Main FXApplication 'stop' start.");
		logger.debug("Main FXApplication 'stop' end.");
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		logger.debug("Main FXApplication 'start' start.");
		// primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setAlwaysOnTop(true);
		context.init();
		fxmlLoader.setLocation(getClass().getResource("/UniversalFileFinderMain.fxml"));
		fxmlLoader.load();
		final Parent view = fxmlLoader.getRoot();
		primaryStage.setTitle("UniversalFileFinder");
		primaryStage.setScene(new Scene(view));
		primaryStage.show();
		logger.debug("Main FXApplication 'start' end.");
	}

}
