package com.example.hotel;

// // JavaFX Controller
// This class handles room selection, availability, and user input for booking.

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

public class RoomSelectionController {

    // ================= SINGLE =================
    @FXML
    // Spinner to select number of single rooms.
    private Spinner<Integer> singleSpinner;

    @FXML
    // Checkbox to select AC option for single room.
    private CheckBox singleAC;

    @FXML
    // Radio buttons to select category of single room.
    private RadioButton singleStandard;
    @FXML
    private RadioButton singlePremium;
    @FXML
    private RadioButton singleDeluxe;

    // ================= DOUBLE =================
    @FXML
    // Spinner to select number of double rooms.
    private Spinner<Integer> doubleSpinner;

    @FXML
    // Checkbox for AC option in double room.
    private CheckBox doubleAC;

    @FXML
    // Radio buttons for double room category.
    private RadioButton doubleStandard;
    @FXML
    private RadioButton doublePremium;
    @FXML
    private RadioButton doubleDeluxe;

    // ================= TRIPLE =================
    @FXML
    // Spinner to select number of triple rooms.
    private Spinner<Integer> tripleSpinner;

    @FXML
    // Checkbox for AC option in triple room.
    private CheckBox tripleAC;

    @FXML
    // Radio buttons for triple room category.
    private RadioButton tripleStandard;
    @FXML
    private RadioButton triplePremium;
    @FXML
    private RadioButton tripleDeluxe;

    // ================= DATES =================
    @FXML
    // Date picker for check-in date.
    private DatePicker fromDate;

    @FXML
    // Date picker for check-out date.
    private DatePicker toDate;

    // ================= BUTTONS =================
    @FXML
    // Button to proceed to next step.
    private Button proceedButton;

    @FXML
    // Button to go back.
    private Button backButton;

    @FXML
    // Labels showing available rooms.
    private Label singleAvailability;
    @FXML
    private Label doubleAvailability;
    @FXML
    private Label tripleAvailability;

    // ================= INITIALIZE =================
    @FXML
    private void initialize() {

        // Gets current booking data.
        Booking b = AppState.booking;

        // Sets selected category for single room based on previous choice.
        if (b.singleCategory == Category.PREMIUM)
            singlePremium.setSelected(true);
        else if (b.singleCategory == Category.DELUXE)
            singleDeluxe.setSelected(true);
        else
            singleStandard.setSelected(true);

        // Sets selected category for double room.
        if (b.doubleCategory == Category.PREMIUM)
            doublePremium.setSelected(true);
        else if (b.doubleCategory == Category.DELUXE)
            doubleDeluxe.setSelected(true);
        else
            doubleStandard.setSelected(true);

        // Sets selected category for triple room.
        if (b.tripleCategory == Category.PREMIUM)
            triplePremium.setSelected(true);
        else if (b.tripleCategory == Category.DELUXE)
            tripleDeluxe.setSelected(true);
        else
            tripleStandard.setSelected(true);

        // Initializes spinners with max available rooms.
        SpinnerValueFactory.IntegerSpinnerValueFactory singleFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                0, AppState.manager.getSingleRooms(), 0);

        SpinnerValueFactory.IntegerSpinnerValueFactory doubleFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                0, AppState.manager.getDoubleRooms(), 0);

        SpinnerValueFactory.IntegerSpinnerValueFactory tripleFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                0, AppState.manager.getTripleRooms(), 0);

        // Assigns value factories to spinners.
        singleSpinner.setValueFactory(singleFactory);
        doubleSpinner.setValueFactory(doubleFactory);
        tripleSpinner.setValueFactory(tripleFactory);

        // Restores previously selected quantities if user navigates back.

        if (b.singleQty > 0)
            singleSpinner.getValueFactory().setValue(
                    Math.min(b.singleQty, AppState.manager.getSingleRooms()));

        if (b.doubleQty > 0)
            doubleSpinner.getValueFactory().setValue(
                    Math.min(b.doubleQty, AppState.manager.getDoubleRooms()));

        if (b.tripleQty > 0)
            tripleSpinner.getValueFactory().setValue(
                    Math.min(b.tripleQty, AppState.manager.getTripleRooms()));


        // Ensures user cannot select more rooms than available.

        singleSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal > AppState.manager.getSingleRooms()) {
                singleSpinner.getValueFactory().setValue(AppState.manager.getSingleRooms());
            }
        });

        doubleSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal > AppState.manager.getDoubleRooms()) {
                doubleSpinner.getValueFactory().setValue(AppState.manager.getDoubleRooms());
            }
        });

        tripleSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal > AppState.manager.getTripleRooms()) {
                tripleSpinner.getValueFactory().setValue(AppState.manager.getTripleRooms());
            }

        });

        // Back button navigates to previous screen.
        backButton.setOnAction(e -> goBack());

        // Displays current room availability.
        singleAvailability.setText(AppState.manager.getSingleRooms() + " available");
        doubleAvailability.setText(AppState.manager.getDoubleRooms() + " available");
        tripleAvailability.setText(AppState.manager.getTripleRooms() + " available");
    }

    
    private void goBack() {
        try {
            // Variable to store next screen.
            Parent root;

            // Checks if user came from dashboard or home.
            if (AppState.fromDashboard) {
                root = FXMLLoader.load(
                        getClass().getResource("/com/example/hotel/dashboard.fxml"));
            } else {
                root = FXMLLoader.load(
                        getClass().getResource("/com/example/hotel/home.fxml"));
            }

            // Resets navigation flag after use.
            AppState.fromDashboard = false;

            // Switches to selected previous screen.
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.sizeToScene();

        } catch (Exception e) {
            // Handles navigation errors.
            e.printStackTrace();
        }
    }

    // Displays error messages in popup.
    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void handleProceed() {

        // Gets current booking object.
        Booking b = AppState.booking;


        // Sets selected category for single room.
        if (singleStandard.isSelected())
            b.singleCategory = Category.STANDARD;
        else if (singlePremium.isSelected())
            b.singleCategory = Category.PREMIUM;
        else if (singleDeluxe.isSelected())
            b.singleCategory = Category.DELUXE;

        // Sets selected category for double room.
        if (doubleStandard.isSelected())
            b.doubleCategory = Category.STANDARD;
        else if (doublePremium.isSelected())
            b.doubleCategory = Category.PREMIUM;
        else if (doubleDeluxe.isSelected())
            b.doubleCategory = Category.DELUXE;

        // Sets selected category for triple room.
        if (tripleStandard.isSelected())
            b.tripleCategory = Category.STANDARD;
        else if (triplePremium.isSelected())
            b.tripleCategory = Category.PREMIUM;
        else if (tripleDeluxe.isSelected())
            b.tripleCategory = Category.DELUXE;

        // Stores selected dates.
        b.fromDate = fromDate.getValue();
        b.toDate = toDate.getValue();

        // Stores AC selections.
        b.singleAC = singleAC.isSelected();
        b.doubleAC = doubleAC.isSelected();
        b.tripleAC = tripleAC.isSelected();

        // Gets selected room quantities.
        int singleQty = singleSpinner.getValue();
        int doubleQty = doubleSpinner.getValue();
        int tripleQty = tripleSpinner.getValue();

        // VALIDATION
        // Ensures at least one room is selected.
        if (singleQty == 0 && doubleQty == 0 && tripleQty == 0) {
            showError("Please select at least one room.");
            return;
        }

        // Ensures dates are selected.
        if (b.fromDate == null || b.toDate == null) {
            showError("Please select stay duration.");
            return;
        }

        // Ensures checkout date is after check-in.
        if (b.toDate.isBefore(b.fromDate)) {
            showError("Checkout date cannot be before check-in date.");
            return;
        }

        try {
            // Clears previous summary data.
            b.summaryLines.clear();

            // Stores selected quantities in booking.
            b.singleQty = singleQty;
            b.doubleQty = doubleQty;
            b.tripleQty = tripleQty;

            // Loads summary screen.
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/hotel/summary.fxml"));

            Parent root = loader.load();

            // Switches to summary screen.
            Stage stage = (Stage) proceedButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.sizeToScene();

        } catch (Exception e) {
            // Handles errors during navigation.
            e.printStackTrace();
            showError("Something went wrong!");
        }

    }

    public void refreshAll() {

        // Gets latest available room counts.
        int singleMax = AppState.manager.getSingleRooms();
        int doubleMax = AppState.manager.getDoubleRooms();
        int tripleMax = AppState.manager.getTripleRooms();

        // Gets current spinner values safely.
        int singleVal = singleSpinner.getValue() == null ? 0 : singleSpinner.getValue();
        int doubleVal = doubleSpinner.getValue() == null ? 0 : doubleSpinner.getValue();
        int tripleVal = tripleSpinner.getValue() == null ? 0 : tripleSpinner.getValue();

        // Updates spinner limits and values based on availability.
        singleSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, singleMax, Math.min(singleVal, singleMax)));

        doubleSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, doubleMax, Math.min(doubleVal, doubleMax)));

        tripleSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, tripleMax, Math.min(tripleVal, tripleMax)));

        // Updates availability labels.
        singleAvailability.setText(singleMax + " available");
        doubleAvailability.setText(doubleMax + " available");
        tripleAvailability.setText(tripleMax + " available");
    }
}