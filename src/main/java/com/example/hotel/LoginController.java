package com.example.hotel;

// // JavaFX Controller
// This class handles login functionality and user authentication.

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

public class LoginController {

    @FXML
    // Input field for phone number.
    private TextField phoneField;

    @FXML
    // Input field for booking ID.
    private TextField bookingIdField;

    @FXML
    // Input field for password (hidden text).
    private PasswordField passcodeField;

    // --------- LOGIN --------

    @FXML
    private void handleLoginClick() {

        // -------- INPUT--------
        // Retrieves user input from text fields.
        String phone = phoneField.getText();
        String bookingId = bookingIdField.getText();
        String passcode = passcodeField.getText();

        // Checks if any field is empty.
        if (phone.isEmpty() || bookingId.isEmpty() || passcode.isEmpty()) {
            showError("Please fill all fields.");
            return;
        }

        //Regex Validation
        // Ensures phone number is exactly 10 digits.
        if (!phone.matches("\\d{10}")) {
            showError("Enter a valid 10-digit phone number.");
            return;
        }

        try {

    Booking b = BookingDAO.login(
            phone,
            bookingId,
            passcode
    );

    if (b == null) {
        showError("Invalid Phone, Booking ID, or Password.");
        return;
    }

    AppState.booking = b;
    AppState.bookingMap.put(
            b.getBookingId(),
            b
    );

} catch (Exception e) {
    e.printStackTrace();
    showError("Database error.");
    return;
}

        // Shows success message after successful login.
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login Successful");
        alert.setHeaderText("Login Successful");
        alert.setContentText("Welcome back " + AppState.booking.getName());
        alert.showAndWait();

        // Prints login details for debugging.
        System.out.println("=== LOGIN SUCCESS ===");
        System.out.println("User: " + AppState.booking.getName());
        System.out.println("Booking ID: " + AppState.booking.getBookingId());
        System.out.println("=====================");

        // Marks user as logged in.
        AppState.isLoggedIn = true;

        // Loads dashboard screen after login.
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/hotel/dashboard.fxml"));

            Parent root = loader.load();

            // Gets current window and switches to dashboard.
            Stage stage = (Stage) phoneField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.sizeToScene();

        } catch (Exception e) {
            // Handles errors during screen loading.
            e.printStackTrace();
            showError("Error loading dashboard.");
        }
    }

    //---------BACK--------
    @FXML
    private void handleBackClick() {

        try {
            // Loads home screen when back button is clicked.
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/hotel/home.fxml"));
            Parent root = loader.load();

            // Switches back to home screen.
            Stage stage = (Stage) phoneField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.sizeToScene();

        } catch (Exception e) {
            // Handles errors during navigation.
            e.printStackTrace();
        }
    }

    // Displays error messages in popup dialog.
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void initialize() {

        // Adds listener to restrict phone input to digits only.
        phoneField.textProperty().addListener((obs, oldVal, newVal) -> {

            // Removes any non-digit characters.
            if (!newVal.matches("\\d*")) {
                phoneField.setText(newVal.replaceAll("[^\\d]", ""));
            }

            // Limits input to maximum 10 digits.
            if (newVal.length() > 10) {
                phoneField.setText(newVal.substring(0, 10));
            }
        });
    }

}