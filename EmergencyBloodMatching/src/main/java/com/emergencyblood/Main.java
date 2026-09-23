package com.emergencyblood;

import com.emergencyblood.controller.NavigationController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Entry point for the Emergency Blood Matching System JavaFX Application.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        NavigationController navigationController = new NavigationController();
        Scene scene = new Scene(navigationController.getMainLayout(), 1150, 720);

        // Load CSS stylesheet
        URL cssUrl = getClass().getResource("/css/styles.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("Warning: /css/styles.css could not be found!");
        }

        primaryStage.setTitle("Emergency Blood Matching System - Checkpoint 1 (JavaFX)");
        primaryStage.setMinWidth(950);
        primaryStage.setMinHeight(600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
