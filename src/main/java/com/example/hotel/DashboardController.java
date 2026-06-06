package com.example.hotel;

//JavaFX Controller
// This class controls the dashboard screen and handles user interactions and UI updates.

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

//Collections Framework
// These are used to store and manage groups of data like lists of items or prices.
import java.util.*;

// Functional Interface (Lambda)
// These help create reusable logic blocks like functions inside code.
import java.util.function.BiFunction;
import java.util.function.Function;

public class DashboardController {

    @FXML
    // This is the main area where different UI screens (services, food, etc.) are displayed.
    private StackPane contentArea;

    @FXML
    // This label shows a welcome message to the logged-in user.
    private Label welcomeLabel;

    @FXML
    //yhis block fetched names from the booking obj b to display hello and name
    public void initialize() {

        // This gets the current booking details from shared AppState.
        Booking b = AppState.booking;

        // If user name exists, show personalized welcome message.
        if (b != null && b.getName() != null) {
            welcomeLabel.setText("Welcome, " + b.getName());
        } else {
            // Otherwise show default welcome message.
            welcomeLabel.setText("Welcome");
        }
    }

    // ================= VIEW SERVICES =================
    @FXML
    //inside vuew services 
    private void showServices() {

        // Creates a vertical layout with spacing between elements.
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        // Title label for services section.
        Label title = new Label("Available Services");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Buttons for different services.
        Button laundry = new Button("Laundry");
        Button room = new Button("Call Room Service");
        Button wifi = new Button("WiFi / LAN");

        // Loop applies same size and style to all buttons.
        for (Button b : new Button[] { laundry, room, wifi }) {
            b.setPrefWidth(220);
            b.setPrefHeight(45);
            b.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        }

        // POPUPS
        
        // LAUNDRY 
        // Opens a new UI to schedule laundry pickup.
        laundry.setOnAction(e -> {

            // Creates layout for laundry scheduling screen.
            VBox layout2 = new VBox(15);
            layout2.setAlignment(Pos.CENTER);
            layout2.setStyle("-fx-padding: 20;");

            // Title for laundry section.
            Label title2 = new Label("Schedule Laundry Pickup");
            title2.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

            // Spinners to select time (hour and minutes).
            Spinner<Integer> hour = new Spinner<>(0, 23, 12);
            Spinner<Integer> min = new Spinner<>(0, 59, 0);

            // Layout for time selection.
            HBox timeBox = new HBox(10,
                    new Label("Time:"),
                    hour,
                    new Label(":"),
                    min);
            timeBox.setAlignment(Pos.CENTER);

            // Button to confirm laundry pickup.
            Button scheduleBtn = new Button("Schedule Pickup");
            scheduleBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");

            // Shows confirmation popup when scheduled.
            // alert is used to create those popups 
            scheduleBtn.setOnAction(ev -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Laundry Scheduled");
                alert.setHeaderText(null);
                alert.setContentText(
                        "Your laundry will be picked up at selected time\n" +
                                "and delivered back as soon as possible.\nThank you!");
                alert.showAndWait();
            });

            // Back button to return to services menu.
            Button backBtn = new Button("Back");
            backBtn.setOnAction(ev -> showServices());

            // Adds all UI elements into layout.
              layout2.getChildren().addAll(title2, timeBox, scheduleBtn, backBtn);
            // Displays this layout in main content area.
            contentArea.getChildren().setAll(layout2);
        });

        room.setOnAction(e -> {

            VBox layout2 = new VBox(15);
            layout2.setAlignment(Pos.CENTER);

            Label title2 = new Label("Room Service");
            title2.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

            Button clean = new Button("Room cleaning");
            Button bedlinen = new Button("Change bed linen");
            Button ExtraBed = new Button("Provide Extra Bed");
            Button Toilerteries = new Button("Re-fill Toilerteries / Fresh Towels");
            Button Trash = new Button("Trash Removal");

            // Apply same styling as main dashboard buttons
            for (Button b : new Button[] { clean, bedlinen, ExtraBed, Toilerteries, Trash }) {
                b.setPrefWidth(220);
                b.setPrefHeight(45);
                b.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
            }

            clean.setOnAction(ev -> showMsg("Room cleaning scheduled, Thank you for youre patience"));
            bedlinen.setOnAction(ev -> showMsg(
                    "Bed and pillow linen will be replaced with a fresh set soon, Thank you for youre patiencce"));
            ExtraBed.setOnAction(ev -> showMsg("Extra bed shall be provided in a while, Thank you"));
            Toilerteries.setOnAction(
                    ev -> showMsg("Toiletries/Towels will be replaced and replenished , Thank you for youre patience"));
            Trash.setOnAction(ev -> showMsg("Trash will be collected soon , Thank you for youre patience"));

            Button backBtn = new Button("Back");
            backBtn.setOnAction(ev -> showServices());

            layout2.getChildren().addAll(
                    title2,
                    clean,
                    bedlinen,
                    ExtraBed,
                    Toilerteries,
                    Trash,
                    backBtn);

            contentArea.getChildren().setAll(layout2);
        });

        // WiFi / LAN
        // Shows popup with WiFi information.
        wifi.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("WiFi / LAN");
            alert.setHeaderText(null);
            alert.setContentText("WiFi access is available.\nNetwork details will be shared with you shortly.");
            alert.showAndWait();
        });

        // Adds all elements to layout.
        layout.getChildren().addAll(title, laundry, room, wifi);

        // Displays this layout inside main content area.
        contentArea.getChildren().setAll(layout);
    }

    private void showMsg(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Room Service");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // -----------FOOD-----------
    @FXML
    private void showFood() {

        // Creates main vertical container for food menu.
        VBox container = new VBox(15);
        container.setAlignment(Pos.TOP_CENTER);
        container.setStyle("-fx-padding: 15;");

        // Title for food section.
        Label title = new Label("Food Menu");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Displays total bill amount, initially zero.
        Label totalLabel = new Label("Total: ₹0");
        totalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Lists to store quantity selectors and corresponding prices.
        List<Spinner<Integer>> spinners = new ArrayList<>();
        List<Integer> prices = new ArrayList<>();

        // Container for all menu items.
        VBox itemsBox = new VBox(20);

        // This creates a reusable function to generate section headings.
        Function<String, Label> sectionTitle = (text) -> {
            Label lbl = new Label(text);
            lbl.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
            return lbl;
        };

        // This creates each food item with name, price, and quantity selector.
        BiFunction<String, Integer, HBox> createItem = (name, price) -> {

            // Spinner allows user to select quantity (0 to 10).
            Spinner<Integer> sp = new Spinner<>(0, 10, 0);
            sp.setPrefWidth(70);

            // Store spinner and price for total calculation.
            spinners.add(sp);
            prices.add(price);

            // Label shows item name with price.
            Label label = new Label(name + " ₹" + price);
            label.setPrefWidth(220);

            // Spacer pushes spinner to right side.
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // Returns one row of item UI.
            return new HBox(10, label, spacer, sp);
        };

        // ===== SOUPS =====
        // Section for soups with items.
        VBox soups = new VBox(8,
                sectionTitle.apply("Soups"),
                createItem.apply("Tomato Soup", 120),
                createItem.apply("Sweet Corn Soup", 130),
                createItem.apply("Hot & Sour Soup", 140));

        // ===== STARTERS =====
        // Section for starters with items.
        VBox starters = new VBox(8,
                sectionTitle.apply("Starters"),
                createItem.apply("Paneer Tikka", 220),
                createItem.apply("Veg Spring Rolls", 160),
                createItem.apply("Hara Bhara Kabab", 180));

        // ===== MAIN COURSE =====
        // Section for main dishes.
        VBox main = new VBox(8,
                sectionTitle.apply("Main Course"),
                createItem.apply("Paneer Butter Masala", 260),
                createItem.apply("Kadai Paneer", 250),
                createItem.apply("Palak Paneer", 240));

        // ===== BREADS =====
        // Section for breads.
        VBox breads = new VBox(8,
                sectionTitle.apply("Breads"),
                createItem.apply("Butter Naan", 50),
                createItem.apply("Garlic Naan", 60));

        // ===== BEVERAGES =====
        // Section for drinks.
        VBox drinks = new VBox(8,
                sectionTitle.apply("Beverages"),
                createItem.apply("Lime Soda", 60),
                createItem.apply("Orange Juice", 80));

        // Adds all sections into main items container.
        itemsBox.getChildren().addAll(soups, starters, main, breads, drinks);

        // Calculates total bill based on selected quantities.
        Runnable updateTotal = () -> {
            int sum = 0;
            for (int i = 0; i < spinners.size(); i++) {
                sum += spinners.get(i).getValue() * prices.get(i);
            }
            totalLabel.setText("Total: ₹" + sum);
        };

        // Adds listener to each spinner to update total when value changes.
        for (Spinner<Integer> sp : spinners) {
            sp.valueProperty().addListener((obs, oldVal, newVal) -> updateTotal.run());
        }

        // Button to place food order.
        Button orderBtn = new Button("Place Order");
        orderBtn.setPrefWidth(200);
        orderBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");

        // Shows confirmation popup when order is placed.
        orderBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Order Placed");
            alert.setHeaderText(null);
            alert.setContentText("Your food order has been placed successfully.\nIt will be served shortly.");
            alert.showAndWait();
        });

        // Back button to return to home dashboard view.
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> resetHome());

        // Bottom section with total and buttons.
        VBox bottomBox = new VBox(10, totalLabel, orderBtn, backBtn);
        bottomBox.setAlignment(Pos.CENTER);

        // Adds everything to main container.
        container.getChildren().addAll(title, itemsBox, bottomBox);

        // Scroll pane allows viewing full menu if content overflows.
        ScrollPane scroll = new ScrollPane(container);
        scroll.setFitToWidth(true);

        // Displays food menu in content area.
        contentArea.getChildren().setAll(scroll);
    }

    private void resetHome() {

        // Creates a home layout with vertical alignment.
        VBox home = new VBox(10);
        home.setAlignment(Pos.CENTER);

        // Main welcome title shown on dashboard.
        Label title = new Label("Welcome to The Pearl Hotel");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        // Subtext explaining purpose of dashboard.
        Label sub = new Label("Use this dashboard to access and manage hotel services with ease.");
        sub.setStyle("-fx-text-fill: #7f8c8d;");

        // Adds title and subtitle to layout.
        home.getChildren().addAll(title, sub);

        // Displays this layout in main content area.
        contentArea.getChildren().setAll(home);
    }

    //--------BOOK ROOM------
    @FXML
    private void showBooking() {
        try {
            // Sets flag to indicate navigation came from dashboard.
            AppState.fromDashboard = true;

            // Loads room selection screen.
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/hotel/room-selection.fxml"));

            Parent root = loader.load();

            // Gets controller and refreshes data (like room availability).
            RoomSelectionController controller = loader.getController();
            controller.refreshAll();

            // Switches current screen to room selection page.
            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.sizeToScene();

        } catch (Exception e) {
            // Handles any loading or UI errors.
            e.printStackTrace();
        }
    }

    // ================= HELP =================
    @FXML
    private void showHelp() {

        // Creates layout for help section.
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20;");

        // Button to quickly contact front desk.
        Button callService = new Button("Call Front-Desk");
        callService.setPrefWidth(220);
        callService.setPrefHeight(40);
        callService.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");

        // Shows popup when front desk is called.
        callService.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Room Service");
            alert.setHeaderText(null);
            alert.setContentText("Calling Front-Desk. kindly be patient");
            alert.showAndWait();
        });

        // Heading asking user to describe issue.
        Label heading = new Label("Enter the issue you're facing:");
        heading.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Text area for user to type issue.
        TextArea issueBox = new TextArea();
        issueBox.setPromptText("Describe your issue here...");
        issueBox.setPrefWidth(350);
        issueBox.setPrefHeight(150);

        // Button to submit issue.
        Button submitBtn = new Button("Submit");
        submitBtn.setPrefWidth(150);
        submitBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");

        // Shows confirmation and clears input after submission.
        submitBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Request Submitted");
            alert.setHeaderText(null);
            alert.setContentText("Your issue has been submitted.\nOur staff will assist you shortly.");
            alert.showAndWait();

            issueBox.clear();
        });

        // Back button returns to dashboard home view.
        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(120);
        backBtn.setOnAction(e -> resetHome());

        // Adds all elements into layout.
        layout.getChildren().addAll(
                callService,
                heading,
                issueBox,
                submitBtn,
                backBtn);

        // Displays help layout in content area.
        contentArea.getChildren().setAll(layout);
    }

    // ================= CHECKOUT =================
    @FXML
    private void showCheckout() {

        // Creates layout for checkout section.
        VBox layout = new VBox(18);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20;");

        // 🔹 TITLE (NEW)
        Label title = new Label("Are you sure you want to check out?");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Button for immediate checkout.
        Button checkoutNow = new Button("Checkout Now");
        checkoutNow.setPrefWidth(200);
        checkoutNow.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

        // Handles instant checkout process.
        checkoutNow.setOnAction(e -> {

            // Gets current booking details.
            Booking b = AppState.booking;

            // Prints debug information about checkout.
            System.out.println("=== CHECKOUT STARTED ===");
            System.out.println("User: " + b.getName());
            System.out.println("Rooms before checkout: " + b.bookedRooms);
            System.out.println("Booking ID: " + b.getBookingId());

            // Stores booking ID for lookup.
            String bookingId = b.getBookingId();

            // Gets actual rooms from manager using booking ID.
            java.util.List<Integer> rooms = AppState.manager.getRoomsByBookingId(bookingId);

            System.out.println("Rooms from manager: " + rooms);

            // Releases each booked room back to availability.
            for (int room : rooms) {
                //synchronisation call 
                AppState.manager.releaseRoom(room);

            }
            try {
                BookingDAO.checkoutBooking(
                b.getBookingId(),
                rooms);
            } catch (Exception ex) {
            ex.printStackTrace();
        }
        

            // Clears user's booked room list after checkout.
            b.bookedRooms.clear();

            System.out.println("All rooms released successfully.");
            System.out.println("Rooms after checkout: " + b.bookedRooms);
            System.out.println("===========================");

            // ================= SUCCESS MESSAGE =================
            // Shows checkout success popup.
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Checked Out");
            alert.setHeaderText(null);
            alert.setContentText("You have successfully checked out.\nThank you for staying with us!");
            alert.showAndWait();

            // ================= GO TO HOME =================
            // Redirects user back to home screen.
            try {
                Parent root = FXMLLoader.load(
                        getClass().getResource("/com/example/hotel/home.fxml"));

                Stage stage = (Stage) contentArea.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.sizeToScene();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Back button returns to dashboard home view.
        Button backBtn = new Button("Back");
        backBtn.setPrefWidth(150);
        backBtn.setOnAction(e -> resetHome());

        //  ADD ALL
        // Adds all UI elements into layout.
        layout.getChildren().addAll(
                title,
                checkoutNow,
                backBtn);

        // Displays checkout layout.
        contentArea.getChildren().setAll(layout);
    }

    // ================= LOGOUT =================
    @FXML
    private void handleLogout() {
        try {

            // Resets booking data when user logs out.
            AppState.booking = new Booking();

            // Marks user as logged out.
            AppState.isLoggedIn = false;

            // Loads home screen after logout.
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/example/hotel/home.fxml"));

            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.sizeToScene();

        } catch (Exception e) {
            // Handles any errors during logout.
            e.printStackTrace();
        }
    }
}