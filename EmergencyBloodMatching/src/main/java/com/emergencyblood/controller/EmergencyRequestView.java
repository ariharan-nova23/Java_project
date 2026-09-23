package com.emergencyblood.controller;

import com.emergencyblood.model.BloodRequest;
import com.emergencyblood.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Screen 2: Emergency Blood Request Form
 */
public class EmergencyRequestView {

    private final NavigationController navigation;
    private final VBox root;

    public EmergencyRequestView(NavigationController navigation) {
        this.navigation = navigation;

        root = new VBox(20);
        root.setAlignment(Pos.TOP_LEFT);

        VBox formPanel = new VBox(20);
        formPanel.getStyleClass().add("card-panel");
        formPanel.setMaxWidth(650);

        Label header = new Label("Create Emergency Blood Request");
        header.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        // Required Blood Group Dropdown
        Label lblBloodGroup = new Label("Required Blood Group:");
        lblBloodGroup.getStyleClass().add("form-label");

        ComboBox<String> comboBloodGroup = new ComboBox<>(FXCollections.observableArrayList(
            "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
        ));
        comboBloodGroup.setPromptText("Select Blood Group");
        comboBloodGroup.setMaxWidth(Double.MAX_VALUE);

        // Units Required Input
        Label lblUnits = new Label("Units Required:");
        lblUnits.getStyleClass().add("form-label");

        TextField txtUnits = new TextField();
        txtUnits.setPromptText("e.g., 2");

        // Location Input
        Label lblLocation = new Label("Hospital / Emergency Location:");
        lblLocation.getStyleClass().add("form-label");

        TextField txtLocation = new TextField();
        txtLocation.setPromptText("e.g., Vijayawada");

        // Urgency Level Dropdown
        Label lblUrgency = new Label("Urgency Level:");
        lblUrgency.getStyleClass().add("form-label");

        ComboBox<String> comboUrgency = new ComboBox<>(FXCollections.observableArrayList(
            "LOW", "MEDIUM", "CRITICAL"
        ));
        comboUrgency.setValue("CRITICAL");
        comboUrgency.setMaxWidth(Double.MAX_VALUE);

        grid.add(lblBloodGroup, 0, 0);
        grid.add(comboBloodGroup, 1, 0);

        grid.add(lblUnits, 0, 1);
        grid.add(txtUnits, 1, 1);

        grid.add(lblLocation, 0, 2);
        grid.add(txtLocation, 1, 2);

        grid.add(lblUrgency, 0, 3);
        grid.add(comboUrgency, 1, 3);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(40);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(60);
        grid.getColumnConstraints().addAll(col1, col2);

        // Action Buttons
        Button btnFindDonors = new Button("FIND DONORS");
        btnFindDonors.getStyleClass().add("btn-primary");
        btnFindDonors.setOnAction(e -> {
            String group = comboBloodGroup.getValue();
            String unitsStr = txtUnits.getText().trim();
            String location = txtLocation.getText().trim();
            String urgency = comboUrgency.getValue();

            if (group == null || group.isEmpty()) {
                AlertUtil.showError("Validation Error", "Missing Blood Group", "Please select a required blood group.");
                return;
            }

            if (unitsStr.isEmpty()) {
                AlertUtil.showError("Validation Error", "Missing Units", "Please enter the number of blood units required.");
                return;
            }

            int units;
            try {
                units = Integer.parseInt(unitsStr);
                if (units <= 0) {
                    AlertUtil.showError("Validation Error", "Invalid Units", "Units required must be greater than 0.");
                    return;
                }
            } catch (NumberFormatException ex) {
                AlertUtil.showError("Validation Error", "Invalid Input Format", "Units required must be a numeric integer.");
                return;
            }

            if (location.isEmpty()) {
                AlertUtil.showError("Validation Error", "Missing Location", "Please specify the emergency location.");
                return;
            }

            // Save Active Request & Navigate
            BloodRequest newRequest = new BloodRequest(102, group, units, location, urgency);
            navigation.setActiveRequest(newRequest);

            AlertUtil.showInfo(
                "Request Created", 
                "Emergency Blood Request Submitted", 
                "Emergency request for " + group + " (" + units + " units) created. Proceeding to donor matching results."
            );

            navigation.showMatchingResults();
        });

        HBox btnBox = new HBox(15, btnFindDonors);
        btnBox.setAlignment(Pos.CENTER_RIGHT);

        formPanel.getChildren().addAll(header, grid, new Separator(), btnBox);
        root.getChildren().add(formPanel);
    }

    public Node getView() {
        return root;
    }
}
