package com.example.hotel;

// // JavaFX Controller
// This class displays booking summary and calculates total cost.

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SummaryController {

    @FXML
    // Container to display room summary items.
    private VBox itemsContainer;

    @FXML
    // Label to display total price.
    private Label totalLabel;

    @FXML
    // Button to proceed further.
    private Button proceedButton;

    @FXML
    // Button to go back.
    private Button backButton;

    // ================= INITIALIZE =================
    @FXML
    public void initialize() {

        // Gets current booking details.
        Booking b = AppState.booking;

        // Calculates number of days between check-in and check-out.
        long days = java.time.temporal.ChronoUnit.DAYS.between(b.fromDate, b.toDate);

        // Ensures at least 1 day is counted.
        if (days <= 0)
            days = 1;

        // Variable to store total cost.
        int total = 0;

        // ================= SINGLE =================
        // Calculates cost for single rooms if selected.
        if (b.singleQty > 0) {

            // Base price for single room.
            int base = BedType.SINGLE.getBasePrice();

            // Adds AC cost if selected.
            int acCost = b.singleAC ? 500 : 0;

            // Adds category cost if selected.
            int categoryCost = b.singleCategory != null ? b.singleCategory.getExtra() : 0;

            // Calculates price per day.
            int pricePerDay = base + acCost + categoryCost;

            // Calculates total price for selected quantity and days.
            int price = pricePerDay * b.singleQty * (int) days;

            // Adds to total.
            total += price;

            // Adds UI card showing details.
            itemsContainer.getChildren().add(
                    createCard("Single Room", b.singleQty, price, days, base, acCost + categoryCost));
        }

        // ================= DOUBLE =================
        // Calculates cost for double rooms.
        if (b.doubleQty > 0) {

            int base = BedType.DOUBLE.getBasePrice();
            int acCost = b.doubleAC ? 500 : 0;
            int categoryCost = b.doubleCategory != null ? b.doubleCategory.getExtra() : 0;

            int pricePerDay = base + acCost + categoryCost;
            int price = pricePerDay * b.doubleQty * (int) days;

            total += price;

            itemsContainer.getChildren().add(
                    createCard("Double Room", b.doubleQty, price, days, base, acCost + categoryCost));
        }

        // ================= TRIPLE =================
        // Calculates cost for triple rooms.
        if (b.tripleQty > 0) {

            int base = BedType.TRIPLE.getBasePrice();
            int acCost = b.tripleAC ? 500 : 0;
            int categoryCost = b.tripleCategory != null ? b.tripleCategory.getExtra() : 0;

            int pricePerDay = base + acCost + categoryCost;
            int price = pricePerDay * b.tripleQty * (int) days;

            total += price;

            itemsContainer.getChildren().add(
                    createCard("Triple Room", b.tripleQty, price, days, base, acCost + categoryCost));
        }

        // Displays total cost.
        totalLabel.setText("Total: ₹" + total);

        // Sets button actions.
        proceedButton.setOnAction(e -> goToSignup());
        backButton.setOnAction(e -> goBack());

    }

    private HBox createCard(String type, int qty, int price, long days, int base, int acCost) {

        // Determines AC and category text based on cost.
        String acText = "";

        if (acCost >= 500)
            acText += "AC ";
        if (acCost > 500)
            acText += "+ Category";

        // If no extra features, set as standard.
        if (acText.isEmpty())
            acText = "Standard";

        // Title showing room type, features, and quantity.
        Label title = new Label(type + " • " + acText + " • Qty: " + qty);
        title.setStyle("-fx-font-size: 13px; -fx-font-weight: 700;");

        // Details showing price calculation.
        Label details = new Label(
                "₹" + (base + acCost) + " × " + qty + " × " + days + " day(s)");
        details.setStyle("-fx-font-size: 11px; -fx-text-fill: #64748b;");

        // Label showing total price.
        Label priceLabel = new Label("₹" + price);
        priceLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: 700; -fx-text-fill: #2563eb;");

        // Left section containing title and details.
        VBox left = new VBox(title, details);
        left.setSpacing(3);

        // Main card layout combining left info and price.
        HBox card = new HBox(left, priceLabel);
        card.setSpacing(10);
        card.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        HBox.setHgrow(left, javafx.scene.layout.Priority.ALWAYS);

        // Returns constructed UI card.
        return card;
    }

    private void goToSignup() {

        // Gets current booking details.
        Booking b = AppState.booking;

        // LOGGED-IN USER FLOW
        // If user is already logged in, directly books rooms.
        if (AppState.isLoggedIn) {

            // BOOK ROOMS

            // Books single rooms.
            for (int i = 0; i < b.singleQty; i++) {
                int room = AppState.manager.bookRoom(b.getBookingId(), BedType.SINGLE);
                b.bookedRooms.add(room);
            }

            // Books double rooms.
            for (int i = 0; i < b.doubleQty; i++) {
                int room = AppState.manager.bookRoom(b.getBookingId(), BedType.DOUBLE);
                b.bookedRooms.add(room);
            }

            // Books triple rooms.
            for (int i = 0; i < b.tripleQty; i++) {
                int room = AppState.manager.bookRoom(b.getBookingId(), BedType.TRIPLE);
                b.bookedRooms.add(room);
            }

            // SHOW ROOMS
            // Builds string of booked room numbers.
            StringBuilder rooms = new StringBuilder();
            java.util.List<Integer> roomList = AppState.manager.getRoomsByBookingId(b.getBookingId());

            for (int room : roomList) {
                rooms.append(room).append(", ");
            }

            // Removes trailing comma.
            if (rooms.length() > 0) {
                rooms.setLength(rooms.length() - 2);
            }

            // Shows booking confirmation popup.
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Booking Successful");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Rooms successfully added!\n\n" +
                            "Booking ID: " + b.getBookingId() + "\n" +
                            "Rooms: " + rooms.toString());

            alert.showAndWait();

            if (AppState.fromDashboard) {
    try {
        BookingDAO.saveBookedRooms(
                b.getBookingId(),
                roomList
        );
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

            // reset flag after use
            AppState.fromDashboard = false;

            try {
                // Navigates back to dashboard.
                Parent root = FXMLLoader.load(
                        getClass().getResource("/com/example/hotel/dashboard.fxml"));

                Stage stage = (Stage) itemsContainer.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.sizeToScene();

            } catch (Exception e) {
                // Handles navigation errors.
                e.printStackTrace();
            }

            return;
        }

        // If user is not logged in, navigate to signup screen.
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/example/hotel/signup.fxml"));

            Stage stage = (Stage) itemsContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.sizeToScene();

        } catch (Exception e) {
            // Handles navigation errors.
            e.printStackTrace();
        }
    }

    // Navigates back to room selection screen.
    private void goBack() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/example/hotel/room-selection.fxml"));

            Stage stage = (Stage) itemsContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.sizeToScene();

        } catch (Exception e) {
            // Handles navigation errors.
            e.printStackTrace();
        }
    }
}