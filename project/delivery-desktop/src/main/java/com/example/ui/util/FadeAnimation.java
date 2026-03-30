package com.example.ui.util;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class FadeAnimation {
    public static void fadeAnimation(Node node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(800), node);
        tt.setFromX(100);
        tt.setToX(0);
        FadeTransition ft = new FadeTransition(Duration.millis(800), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        tt.play();
        ft.play();
    }
}

