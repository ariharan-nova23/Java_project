package com.emergencyblood.controller;

import com.emergencyblood.model.Donor;
import com.emergencyblood.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Screen 3: Donor Registration Form with 2-Column Grid and Segmented Availability Toggle.
 */
public class DonorRegistrationView {

    private final NavigationController navigation;
    private final VBox root;
    private String selectedAvailability = "Available";

    public DonorRegistrationView(NavigationController navigation) {
        this.navigation = navigation;

        root = new VBox(20);
        root.setAlignment(Pos.TOP_LEFT);

        VBox formPanel = new VBox(18);
        formPanel.getStyleClass().add("card-panel");
        formPanel.setMaxWidth(680);

        Label sectionTitle = new Label("DONOR INFORMATION");
        sectionTitle.getStyleClass().add("card-title-muted");

        VBox formFields = new VBox(16);

        // Full Name
        Label lblName = new Label("FULL NAME *");
        lblName.getStyleClass().add("form-label");
        TextField txtName = new TextField();
        txtName.setPromptText("e.g. Rahul Sharma");

        // Row 1: Age & Blood Group (2 columns)
        GridPane row1 = new GridPane();
        row1.setHgap(16);
        row1.setVgap(16);

        Label lblAge = new Label("AGE (18-65) *");
        lblAge.getStyleClass().add("form-label");
        TextField txtAge = new TextField();
        txtAge.setPromptText("e.g. 22");

        Label lblBloodGroup = new Label("BLOOD GROUP *");
        lblBloodGroup.getStyleClass().add("form-label");
        ComboBox<String> comboBloodGroup = new ComboBox<>(FXCollections.observableArrayList(
            "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
        ));
        comboBloodGroup.setPromptText("Select group");
        comboBloodGroup.setMaxWidth(Double.MAX_VALUE);

        VBox ageBox = new VBox(6, lblAge, txtAge);
        VBox bgBox = new VBox(6, lblBloodGroup, comboBloodGroup);

        row1.add(ageBox, 0, 0);
        row1.add(bgBox, 1, 0);

        ColumnConstraints col1 = new ColumnConstraints(); col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints(); col2.setPercentWidth(50);
        row1.getColumnConstraints().addAll(col1, col2);

        // Row 2: Phone & Location (2 columns)
        GridPane row2 = new GridPane();
        row2.setHgap(16);
        row2.setVgap(16);

        Label lblPhone = new Label("PHONE NUMBER *");
        lblPhone.getStyleClass().add("form-label");
        TextField txtPhone = new TextField();
        txtPhone.setPromptText("e.g. 9876543210");

        Label lblLocation = new Label("CITY / LOCATION *");
        lblLocation.getStyleClass().add("form-label");
        TextField txtLocation = new TextField();
        txtLocation.setPromptText("e.g. Vijayawada");

        VBox phoneBox = new VBox(6, lblPhone, txtPhone);
        VBox locBox = new VBox(6, lblLocation, txtLocation);

        row2.add(phoneBox, 0, 0);
        row2.add(locBox, 1, 0);
        row2.getColumnConstraints().addAll(col1, col2);

        // Availability Segmented Buttons
        Label lblAvailability = new Label("AVAILABILITY STATUS");
        lblAvailability.getStyleClass().add("form-label");

        Button btnAvailable = new Button("Available");
        btnAvailable.getStyleClass().addAll("segmented-btn", "segmented-btn-left", "segmented-active-green");

        Button btnUnavailable = new Button("Unavailable");
        btnUnavailable.getStyleClass().addAll("segmented-btn", "segmented-btn-right");

        btnAvailable.setOnAction(e -> {
            selectedAvailability = "Available";
            btnAvailable.getStyleClass().add("segmented-active-green");
            btnUnavailable.getStyleClass().remove("segmented-active-green");
        });

        btnUnavailable.setOnAction(e -> {
            selectedAvailability = "Unavailable";
            btnUnavailable.getStyleClass().add("segmented-active-green");
            btnAvailable.getStyleClass().remove("segmented-active-green");
        });

        HBox availGroup = new HBox(btnAvailable, btnUnavailable);
        btnAvailable.setPrefWidth(220);
        btnUnavailable.setPrefWidth(220);

        formFields.getChildren().addAll(
            lblName, txtName,
            row1,
            row2,
            lblAvailability, availGroup
        );

        // Action Buttons
        Button btnClear = new Button("Clear");
        btnClear.getStyleClass().add("btn-dark-action");
        btnClear.setOnAction(e -> {
            txtName.clear();
            txtAge.clear();
            comboBloodGroup.setValue(null);
            txtPhone.clear();
            txtLocation.clear();
            selectedAvailability = "Available";
            btnAvailable.getStyleClass().add("segmented-active-green");
            btnUnavailable.getStyleClass().remove("segmented-active-green");
        });

        Button btnRegister = new Button("+   Register Donor");
        btnRegister.getStyleClass().add("btn-blue-primary");
        btnRegister.setOnAction(e -> {
            String name = txtName.getText().trim();
            String ageStr = txtAge.getText().trim();
            String group = comboBloodGroup.getValue();
            String phone = txtPhone.getText().trim();
            String location = txtLocation.getText().trim();

            if (name.isEmpty()) {
                AlertUtil.showError("Validation Error", "Missing Name", "Please enter the donor's full name.");
                return;
            }

            if (ageStr.isEmpty()) {
                AlertUtil.showError("Validation Error", "Missing Age", "Please enter the donor's age.");
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageStr);
                if (age < 18 || age > 65) {
                    AlertUtil.showError("Validation Error", "Age Out of Range", "Donor age must be between 18 and 65 years.");
                    return;
                }
            } catch (NumberFormatException ex) {
                AlertUtil.showError("Validation Error", "Invalid Age Format", "Age must be a numeric integer.");
                return;
            }

            if (group == null || group.isEmpty()) {
                AlertUtil.showError("Validation Error", "Missing Blood Group", "Please select a valid blood group.");
                return;
            }

            if (phone.isEmpty()) {
                AlertUtil.showError("Validation Error", "Missing Phone Number", "Please enter a contact phone number.");
                return;
            }

            if (location.isEmpty()) {
                AlertUtil.showError("Validation Error", "Missing Location", "Please enter the donor's location.");
                return;
            }

            int newId = navigation.getSharedDonorList().size() + 1;
            Donor donor = new Donor(newId, name, group, age, phone, location, selectedAvailability);
            navigation.getSharedDonorList().add(donor);

            AlertUtil.showInfo(
                "Registration Successful", 
                "Donor Registered", 
                "Donor '" + name + "' (ID: " + newId + ") registered successfully."
            );

            // Clear inputs
            txtName.clear();
            txtAge.clear();
            comboBloodGroup.setValue(null);
            txtPhone.clear();
            txtLocation.clear();
            selectedAvailability = "Available";
            btnAvailable.getStyleClass().add("segmented-active-green");
            btnUnavailable.getStyleClass().remove("segmented-active-green");
        });

        HBox btnBox = new HBox(12, btnClear, btnRegister);
        btnBox.setAlignment(Pos.CENTER_RIGHT);
        btnBox.setPadding(new Insets(10, 0, 0, 0));

        formPanel.getChildren().addAll(sectionTitle, formFields, btnBox);
        root.getChildren().add(formPanel);
    }

    public Node getView() {
        return root;
    }
}
