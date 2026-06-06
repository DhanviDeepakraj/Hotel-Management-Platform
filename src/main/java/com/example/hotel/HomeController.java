package com.example.hotel;

// // JavaFX Controller
// This class handles user interactions on the home screen like login, booking, and contact.

//used when button is clicked
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Node;

public class HomeController {

    public void handleLoginClick(ActionEvent event) {
        try {
            // Loads login screen when user clicks login.
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/example/hotel/login.fxml"));

            // Gets current window (stage) from event source.
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                    .getScene()
                    .getWindow();

            // Switches to login scene and adjusts size.
            stage.setScene(new Scene(root));
            stage.sizeToScene();

        } catch (Exception e) {
            // Handles any errors during loading.
            e.printStackTrace();
        }

    }

    @FXML
    private void handleBookClick(ActionEvent event) {
        try {
            // Loads room selection screen when booking is clicked.
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/example/hotel/room-selection.fxml"));

            // Gets current window.
            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            // Switches to room selection screen.( replaces old screen with new one)
            stage.setScene(new Scene(root));
            stage.sizeToScene();// resize window automatically 

        } catch (Exception e) {
            // Handles any errors during navigation.
            e.printStackTrace();
        }
    }

    @FXML
    private void handleContactClick(ActionEvent event) {

        // Displays contact information in popup.
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Contact Us");
        alert.setHeaderText("Reach us anytime ");

        // Shows phone numbers for support.
        alert.setContentText(
                "Call us at:\n\n" +
                        "Front Desk: +91 98765 43210\n" +
                        "Support: +91 91234 56789");
// show popup and wait until user click ok
        alert.showAndWait();
    }

}