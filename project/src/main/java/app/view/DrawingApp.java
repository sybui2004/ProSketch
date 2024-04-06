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

    //MainUI mainUI = new MainUI();

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-stage.fxml")));
        Scene scene = new Scene(root, 1100, 550);
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

//module app{
//    requires javafx.fxml;
//    requires javafx.graphics;
//    requires org.jfxtras.styles.jmetro;
//    requires javafx.swing;
//
//
//    opens app.controller to javafx.fxml;
//    opens app.model.Interface to javafx.fxml;
//    opens app.model.typeEnum to javafx.fxml;
//    opens app.model.shapes to javafx.fxml;
//    opens app.model to javafx.fxml;
//    opens app.view to javafx.fxml;
//    exports app.controller;
//    exports app.model;
//    exports app.view;
//    exports app.model.typeEnum;
//    exports app.model.shapes;
//    exports app.model.Interface;
//}

// --module-path "C:\Users\MSI-PC\Documents\Java - ProPTIT\javafx-sdk-22\lib" --add-modules=javafx.controls,javafx.fxml