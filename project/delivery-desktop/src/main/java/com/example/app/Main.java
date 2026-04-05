package com.example.app;

import com.example.config.HibernateConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage enterStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/ui/choose-user.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 650);
        scene.getStylesheets().add(getClass().getResource("/styles/login-style.css").toExternalForm());
        enterStage.setTitle("hehehe");
        enterStage.setScene(scene);
        enterStage.setMinWidth(800);
        enterStage.setMinHeight(650);
        enterStage.show();
    }

    @Override
    public void stop() throws Exception {
        HibernateConfig.shutdown();
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}
