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
 * Screen 2: Emergency Blood Request Form with ScrollPane wrapper.
 */
public class EmergencyRequestView {

    private final NavigationController navigation;
    private final VBox root;
    private final ScrollPane scrollPane;
    private String selectedUrgency = "CRITICAL";

    public EmergencyRequestView(NavigationController navigation) {
        this.navigation = navigation;

        root = new VBox(20);
        root.setAlignment(Pos.TOP_LEFT);
        root.setPadding(new Insets(4, 4, 20, 4));

        // 1. Top Emergency Alert Banner
        HBox banner = new HBox(10);
        banner.setStyle("-fx-background-color: #FEF2F2; -fx-border-color: #FCA5A5; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 14px 18px;");
        banner.setAlignment(Pos.CENTER_LEFT);

        Label bannerText = new Label("⚠   Immediate response required — all compatible donors alerted");
        bannerText.setStyle("-fx-text-fill: #DC2626; -fx-font-weight: bold; -fx-font-size: 13px;");
        banner.getChildren().add(bannerText);

        // 2. Request Details Card Panel
        VBox formPanel = new VBox(18);
        formPanel.getStyleClass().add("card-panel");
        formPanel.setMaxWidth(680);

        Label sectionTitle = new Label("REQUEST DETAILS");
        sectionTitle.getStyleClass().add("card-title-muted");

        VBox formFields = new VBox(16);

        // Required Blood Group
        Label lblBloodGroup = new Label("REQUIRED BLOOD GROUP *");
        lblBloodGroup.getStyleClass().add("form-label");

        ComboBox<String> comboBloodGroup = new ComboBox<>(FXCollections.observableArrayList(
            "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
        ));
        comboBloodGroup.setPromptText("Select blood group");
        comboBloodGroup.setMaxWidth(Double.MAX_VALUE);

        // Units Required
        Label lblUnits = new Label("UNITS REQUIRED *");
        lblUnits.getStyleClass().add("form-label");

        TextField txtUnits = new TextField();
        txtUnits.setPromptText("e.g. 2");

        // Hospital / Location
        Label lblLocation = new Label("HOSPITAL / EMERGENCY LOCATION *");
        lblLocation.getStyleClass().add("form-label");

        TextField txtLocation = new TextField();
        txtLocation.setPromptText("e.g. Vijayawada Government Hospital");

        // Urgency Level Segmented Buttons
        Label lblUrgency = new Label("URGENCY LEVEL");
        lblUrgency.getStyleClass().add("form-label");

        Button btnCritical = new Button("CRITICAL");
        btnCritical.getStyleClass().addAll("segmented-btn", "segmented-btn-left", "segmented-active-red");

        Button btnHigh = new Button("HIGH");
        btnHigh.getStyleClass().addAll("segmented-btn", "segmented-btn-middle");

        Button btnMedium = new Button("MEDIUM");
        btnMedium.getStyleClass().addAll("segmented-btn", "segmented-btn-right");

        btnCritical.setOnAction(e -> {
            selectedUrgency = "CRITICAL";
            btnCritical.getStyleClass().add("segmented-active-red");
            btnHigh.getStyleClass().remove("segmented-active-red");
            btnMedium.getStyleClass().remove("segmented-active-red");
        });

        btnHigh.setOnAction(e -> {
            selectedUrgency = "HIGH";
            btnHigh.getStyleClass().add("segmented-active-red");
            btnCritical.getStyleClass().remove("segmented-active-red");
            btnMedium.getStyleClass().remove("segmented-active-red");
        });

        btnMedium.setOnAction(e -> {
            selectedUrgency = "MEDIUM";
            btnMedium.getStyleClass().add("segmented-active-red");
            btnCritical.getStyleClass().remove("segmented-active-red");
            btnHigh.getStyleClass().remove("segmented-active-red");
        });

        HBox urgencyGroup = new HBox(btnCritical, btnHigh, btnMedium);

        formFields.getChildren().addAll(
            lblBloodGroup, comboBloodGroup,
            lblUnits, txtUnits,
            lblLocation, txtLocation,
            lblUrgency, urgencyGroup
        );

        // Submit Button
        Button btnFindDonors = new Button("🚨   Find Matching Donors");
        btnFindDonors.getStyleClass().add("btn-danger-primary");
        btnFindDonors.setOnAction(e -> {
            String group = comboBloodGroup.getValue();
            String unitsStr = txtUnits.getText().trim();
            String location = txtLocation.getText().trim();

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
            BloodRequest newRequest = new BloodRequest(102, group, units, location, selectedUrgency);
            navigation.setActiveRequest(newRequest);

            AlertUtil.showInfo(
                "Request Created", 
                "Emergency Blood Request Submitted", 
                "Emergency request for " + group + " (" + units + " units) created. Proceeding to donor matching results."
            );

            navigation.showMatchingResults();
        });

        HBox actionBox = new HBox(btnFindDonors);
        actionBox.setAlignment(Pos.CENTER_RIGHT);
        actionBox.setPadding(new Insets(10, 0, 0, 0));

        formPanel.getChildren().addAll(sectionTitle, formFields, actionBox);

        // 3. Footer Algorithm Info Card
        VBox infoCard = new VBox(8);
        infoCard.setMaxWidth(680);
        infoCard.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 14px 18px;");

        Label infoText = new Label(
            "Matching algorithm: Donors are ranked by blood compatibility, geographic proximity, current availability, " +
            "and historical response rate. O- universal donors are included for critical requests."
        );
        infoText.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 12px;");
        infoText.setWrapText(true);

        infoCard.getChildren().add(infoText);

        root.getChildren().addAll(banner, formPanel, infoCard);

        scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
    }

    public Node getView() {
        return scrollPane;
    }
}
