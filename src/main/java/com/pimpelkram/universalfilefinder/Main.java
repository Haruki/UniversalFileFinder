package com.pimpelkram.universalfilefinder;

import java.util.Arrays;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gluonhq.ignite.guice.GuiceContext;
import com.pimpelkram.universalfilefinder.utils.EffectUtilities;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
		primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/search.png")));
		logger.debug("Main FXApplication 'start' start.");
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setAlwaysOnTop(true);
		context.init();
		fxmlLoader.setLocation(getClass().getResource("/UniversalFileFinderMain.fxml"));
		fxmlLoader.load();
		final Parent view = fxmlLoader.getRoot();
		final Node node = view.lookup("#dragButton");
		node.setStyle("-fx-background-image: url('/search.png');" + "-fx-background-color: #FFFFFF;"
				+ "-fx-background-size: 20 20; " + "-fx-background-position: center;"
				+ "-fx-background-repeat: no-repeat;" + "-fx-background: transparent;");
		logger.debug("Found DragButton: " + (node != null));
		EffectUtilities.makeDraggable(primaryStage, node);
		primaryStage.setTitle("UniversalFileFinder");
		primaryStage.setScene(new Scene(view));
		primaryStage.show();
		logger.debug("Main FXApplication 'start' end.");
	}

}
