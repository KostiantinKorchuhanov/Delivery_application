package com.example.app;

import com.example.service.UserService;
import com.example.ui.controller.ChooseUserController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage enterStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/example/app/ui/choose-user.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 650);
        scene.getStylesheets().add(getClass().getResource("/com/example/app/styles/login-style.css").toExternalForm());
        enterStage.setTitle("hehehe");
        enterStage.setScene(scene);
        enterStage.setMinWidth(800);
        enterStage.setMinHeight(650);
        enterStage.show();

        // This is soooo stupid DO NOT FORGET TO DELETE THIS DISGUSTING THING
        //ChooseUserController controller = fxmlLoader.<ChooseUserController>getController();
        //controller.dirtyOperation();

//        UserService userService = new UserService();
//
//        for (int i = 1; i <= 50; i++) {
//            userService.registerUser(
//                    "NameRestaurant" + i,
//                    "SurnameRestaurant" + i,
//                    "usernameRestaurant" + i,
//                    "userRestaurant" + i + "@example.com",
//                    "Password" + i,
//                    "Password" + i,
//                    "123456789",
//                    "Restaurant"
//            );
//        }
    }

    public static void main(String[] args) {
        launch();
    }
}
