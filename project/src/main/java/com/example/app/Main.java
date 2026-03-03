package com.example.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("choose-user.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 650);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("hehehe");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(650);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
