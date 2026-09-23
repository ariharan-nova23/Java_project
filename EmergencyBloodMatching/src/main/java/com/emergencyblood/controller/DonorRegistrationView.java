package com.emergencyblood.controller;

import com.emergencyblood.model.Donor;
import com.emergencyblood.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Screen 3: Donor Registration Form
 */
public class DonorRegistrationView {

    private final NavigationController navigation;
    private final VBox root;

    public DonorRegistrationView(NavigationController navigation) {
        this.navigation = navigation;

        root = new VBox(20);
        root.setAlignment(Pos.TOP_LEFT);

        VBox formPanel = new VBox(20);
        formPanel.getStyleClass().add("card-panel");
        formPanel.setMaxWidth(650);

        Label header = new Label("Register Voluntary Blood Donor");
        header.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        // Name
        Label lblName = new Label("Full Name:");
        lblName.getStyleClass().add("form-label");
        TextField txtName = new TextField();
        txtName.setPromptText("e.g., Rahul Sharma");

        // Age
        Label lblAge = new Label("Age (18-65):");
        lblAge.getStyleClass().add("form-label");
        TextField txtAge = new TextField();
        txtAge.setPromptText("e.g., 22");

        // Blood Group
        Label lblBloodGroup = new Label("Blood Group:");
        lblBloodGroup.getStyleClass().add("form-label");
        ComboBox<String> comboBloodGroup = new ComboBox<>(FXCollections.observableArrayList(
            "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
        ));
        comboBloodGroup.setPromptText("Select Blood Group");
        comboBloodGroup.setMaxWidth(Double.MAX_VALUE);

        // Phone Number
        Label lblPhone = new Label("Phone Number:");
        lblPhone.getStyleClass().add("form-label");
        TextField txtPhone = new TextField();
        txtPhone.setPromptText("e.g., 9876543210");

        // Location
        Label lblLocation = new Label("City / Location:");
        lblLocation.getStyleClass().add("form-label");
        TextField txtLocation = new TextField();
        txtLocation.setPromptText("e.g., Vijayawada");

        // Availability Status
        Label lblAvailability = new Label("Availability Status:");
        lblAvailability.getStyleClass().add("form-label");
        ComboBox<String> comboAvailability = new ComboBox<>(FXCollections.observableArrayList(
            "Available", "Unavailable"
        ));
        comboAvailability.setValue("Available");
        comboAvailability.setMaxWidth(Double.MAX_VALUE);

        grid.add(lblName, 0, 0);
        grid.add(txtName, 1, 0);

        grid.add(lblAge, 0, 1);
        grid.add(txtAge, 1, 1);

        grid.add(lblBloodGroup, 0, 2);
        grid.add(comboBloodGroup, 1, 2);

        grid.add(lblPhone, 0, 3);
        grid.add(txtPhone, 1, 3);

        grid.add(lblLocation, 0, 4);
        grid.add(txtLocation, 1, 4);

        grid.add(lblAvailability, 0, 5);
        grid.add(comboAvailability, 1, 5);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(40);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(60);
        grid.getColumnConstraints().addAll(col1, col2);

        // Buttons
        Button btnRegister = new Button("REGISTER DONOR");
        btnRegister.getStyleClass().add("btn-primary");
        btnRegister.setOnAction(e -> {
            String name = txtName.getText().trim();
            String ageStr = txtAge.getText().trim();
            String group = comboBloodGroup.getValue();
            String phone = txtPhone.getText().trim();
            String location = txtLocation.getText().trim();
            String availability = comboAvailability.getValue();

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

            // Create Donor & Add to Shared List
            int newId = navigation.getSharedDonorList().size() + 1;
            Donor donor = new Donor(newId, name, group, age, phone, location, availability);
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
            comboAvailability.setValue("Available");
        });

        Button btnClear = new Button("CLEAR");
        btnClear.getStyleClass().add("btn-secondary");
        btnClear.setOnAction(e -> {
            txtName.clear();
            txtAge.clear();
            comboBloodGroup.setValue(null);
            txtPhone.clear();
            txtLocation.clear();
            comboAvailability.setValue("Available");
        });

        HBox btnBox = new HBox(15, btnClear, btnRegister);
        btnBox.setAlignment(Pos.CENTER_RIGHT);

        formPanel.getChildren().addAll(header, grid, new Separator(), btnBox);
        root.getChildren().add(formPanel);
    }

    public Node getView() {
        return root;
    }
}
