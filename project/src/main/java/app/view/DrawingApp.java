package app.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;
import java.util.Objects;

public class DrawingApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-stage.fxml")));
        Scene scene = new Scene(root, 1300, 760);
        new JMetro(scene, Style.LIGHT);
        scene.getRoot().getStyleClass().add(JMetroStyleClass.BACKGROUND);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ProSketch");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("images/logo.png")).openStream()));
        primaryStage.show();
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(400);
    }
    public static void main(String[] args) {
        launch(args);
    }
}