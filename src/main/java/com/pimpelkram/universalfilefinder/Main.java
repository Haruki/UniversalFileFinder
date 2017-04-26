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
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
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
		// logger.debug("init SvgImageLoader Factory...");
		// SvgImageLoaderFactory.install();
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
		final Node image = view.lookup("#image");
		final Node imageStackPane = view.lookup("#imageStackPane");
		final Node mainHBox = view.lookup("#mainHBox");
		((HBox) mainHBox).setBackground(Background.EMPTY);
		((StackPane) imageStackPane).setBackground(Background.EMPTY);
		// node.setStyle("-fx-background-image: url('/search.png');" +
		// "-fx-background-color: #FFFFFF;"
		// + "-fx-background-size: 20 20; " + "-fx-background-position: center;"
		// + "-fx-background-repeat: no-repeat;" + "-fx-background:
		// transparent;");
		logger.debug("Found DragButton: " + (node != null));
		EffectUtilities.makeDraggable(primaryStage, image);
		primaryStage.setTitle("UniversalFileFinder");
		final Scene scene = new Scene(view);
		logger.debug(scene.getAntiAliasing().toString());
		scene.setFill(Color.TRANSPARENT);
		primaryStage.setScene(scene);
		primaryStage.show();
		logger.debug("Main FXApplication 'start' end.");
	}

}
