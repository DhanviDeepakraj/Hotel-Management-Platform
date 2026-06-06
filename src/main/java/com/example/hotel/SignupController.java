package com.example.hotel;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

public class SignupController {

    @FXML
    // Input field for user name.
    private TextField nameField;

    @FXML
    // Date picker for date of birth.
    private DatePicker dobField;

    @FXML
    // Input field for phone number.
    private TextField phoneField;

    @FXML
    // Text area for address input.
    private TextArea addressField;

    @FXML
    // Password input field.
    private PasswordField passwordField;

    @FXML
    // Confirm password input field.
    private PasswordField confirmPasswordField;

    @FXML
    // Button to continue booking process.
    private Button continueButton;

    @FXML
    // Button to go back.
    private Button backButton;

    // ================= BOOK BUTTON =================
    @FXML
    private void initialize() {

        // Sets action for continue button.
        continueButton.setOnAction(e -> handleBooking());

        // Sets action for back button.
        backButton.setOnAction(e -> goBack());

        // Sets placeholder text for phone field.
        phoneField.setPromptText("Enter 10-digit phone number");

        // Restricts phone input to digits and max 10 length.
        phoneField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                phoneField.setText(newVal.replaceAll("[^\\d]", ""));
            }
            if (newVal.length() > 10) {
                phoneField.setText(newVal.substring(0, 10));
            }
        });
    }

    private void handleBooking() {

        // ================= VALIDATION =================
        // Checks if all fields are filled.
        if (nameField.getText().trim().isEmpty() ||
                dobField.getValue() == null ||
                phoneField.getText().trim().isEmpty() ||
                addressField.getText().trim().isEmpty() ||
                passwordField.getText().isEmpty() ||
                confirmPasswordField.getText().isEmpty()) {

            showError("Please fill all fields.");
            return;
        }

        // Checks if passwords match.
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            showError("Passwords do not match.");
            return;
        }

        // // Regex Validation
        // Ensures phone number is exactly 10 digits.
        if (phoneField.getText().length() != 10) {
            showError("Phone number must be exactly 10 digits.");
            return;
        }


        // Checks if DOB is in the future (invalid)
        java.time.LocalDate dob = dobField.getValue();
        java.time.LocalDate today = java.time.LocalDate.now();

        if (dob.isAfter(today)) {
            showError("Invalid date of birth.");
            return;
        }

        // Checks if user is at least 18 years old
        int age = java.time.Period.between(dob, today).getYears();

        if (age < 18) {
            showError("You must be at least 18 years old to book.");
            return;
        }

        // Stores entered phone number.
        String enteredPhone = phoneField.getText();

        try {

    if (BookingDAO.phoneExists(enteredPhone)) {
        showError("User already exists with this phone number!");
        return;
    }

} catch (Exception e) {
    e.printStackTrace();
    showError("Database error.");
    return;
}


        try {

            // ================= OBJECT CREATION =================
            // Uses shared booking object to store user data.
            Booking b = AppState.booking;

            // Sets user details.
            b.setName(nameField.getText());
            b.setPhone(phoneField.getText());
            b.dob = dobField.getValue().toString();
            b.address = addressField.getText();

            // ================= RANDOM DATA =================
            // Sets password for booking.
            b.setPassword(passwordField.getText());

            // Generates unique booking ID.
            String bookingId = BookingDAO.generateBookingId();


            // Sets generated booking ID.
            b.setBookingId(bookingId);

            // Stores booking in global map.
            AppState.bookingMap.put(bookingId, b);

            // ================= FINAL ROOM BOOKING =================

            // Clears previous room list.
            b.bookedRooms.clear();

            // SINGLE
            // Books required number of single rooms.
            for (int i = 0; i < b.singleQty; i++) {
                int room = AppState.manager.bookRoom(b.getBookingId(), BedType.SINGLE);
                b.bookedRooms.add(room);
            }

            // DOUBLE
            // Books required number of double rooms.
            for (int i = 0; i < b.doubleQty; i++) {
                int room = AppState.manager.bookRoom(b.getBookingId(), BedType.DOUBLE);
                b.bookedRooms.add(room);
            }

            // TRIPLE
            // Books required number of triple rooms.
            for (int i = 0; i < b.tripleQty; i++) {
                int room = AppState.manager.bookRoom(b.getBookingId(), BedType.TRIPLE);
                b.bookedRooms.add(room);

            }
            // ================= WRAPPER + AUTOBOXING =================
            // // Wrapper Class
            // Demonstrates use of wrapper classes and autoboxing.

            int total = 0;

// SINGLE rooms
if (b.singleQty > 0) {
    int base = BedType.SINGLE.getBasePrice();
    total += b.singleQty * base;
}

// DOUBLE rooms
if (b.doubleQty > 0) {
    int base = BedType.DOUBLE.getBasePrice();
    total += b.doubleQty * base;
}

// TRIPLE rooms
if (b.tripleQty > 0) {
    int base = BedType.TRIPLE.getBasePrice();
    total += b.tripleQty * base;
}

// print result
int totalRooms = b.singleQty + b.doubleQty + b.tripleQty;

System.out.println("Total Rooms: " + totalRooms);
System.out.println("Total Price: ₹" + total);

BookingDAO.saveBooking(b);

BookingDAO.saveBookedRooms(
        b.getBookingId(),
        b.bookedRooms
);
            // Prints booking details.
            System.out.println("=== NEW BOOKING CREATED ===");
            System.out.println("Name: " + b.getName());
            System.out.println("Phone: " + b.getPhone());
            System.out.println("Booking ID: " + b.getBookingId());
            System.out.println("Rooms: " + b.bookedRooms);
            System.out.println("===========================");


            // ================= GLOBAL STATE =================
            // Updates global booking reference.
            AppState.booking = b;

            // ================= UI =================
            // Shows booking confirmation popup.
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Booking Confirmed");
            alert.setHeaderText("Reservation Successful");

            // Gets booked rooms for display.
            java.util.List<Integer> roomList = AppState.manager.getRoomsByBookingId(b.getBookingId());

            StringBuilder rooms = new StringBuilder();

            // Builds string of room numbers.
            for (int room : roomList) {
                rooms.append(room).append(", ");
            }

            // Removes last comma.
            if (rooms.length() > 0) {
                rooms.setLength(rooms.length() - 2);
            }

            // Displays booking details in popup.
            alert.setContentText(
                    "Your booking has been confirmed!\n\n" +
                            "Booking ID: " + b.getBookingId() + "\n" +
                            "Room Numbers: " + rooms.toString());

            alert.showAndWait();


            // Marks user as logged in.
            AppState.isLoggedIn = true;

            // ================= SCENE SWITCH =================
            // Navigates to dashboard after booking.
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/hotel/dashboard.fxml"));

            Parent root = loader.load();
            Stage stage = (Stage) continueButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.sizeToScene();

        } catch (Exception e) {
            // Handles errors during booking.
            e.printStackTrace();
            showError("Booking failed!");
        }
    }

    // ================= BACK BUTTON =================
    private void goBack() {
        try {
            // Loads summary screen when going back.
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/hotel/summary.fxml"));
            Parent root = loader.load();

            // Switches to summary screen.
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (Exception e) {
            // Handles navigation errors.
            e.printStackTrace();
        }
    }

    // ================= ERROR POPUP =================
    // Displays error message in popup.
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}