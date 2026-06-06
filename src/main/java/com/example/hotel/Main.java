package com.example.hotel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Database.initialize();


        // ================= LOAD HOME PAGE =================
        // Loads the home screen layout from FXML file.
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/hotel/home.fxml"));

        // Creates scene using loaded UI.
        Scene scene = new Scene(loader.load());

        // ================= STAGE SETTINGS =================
        // Sets title and attaches scene to main window.
        stage.setTitle("Hospitality Management Organisation Platform");
        stage.setScene(scene);

        // Disables resizing and centers window on screen.
        stage.setResizable(false);
        stage.centerOnScreen();

        // Displays the application window.
        stage.show();
    }

    public static void main(String[] args) {
        // Launches the JavaFX application.
        launch();
    }
}