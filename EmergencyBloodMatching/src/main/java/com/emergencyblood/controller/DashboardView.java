package com.emergencyblood.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

/**
 * Screen 1: Dashboard View displaying summary cards and quick action navigation.
 */
public class DashboardView {

    private final NavigationController navigation;
    private final VBox root;

    public DashboardView(NavigationController navigation) {
        this.navigation = navigation;

        root = new VBox(25);
        root.setAlignment(Pos.TOP_LEFT);

        // 1. Metric Summary Cards Row
        GridPane metricsGrid = new GridPane();
        metricsGrid.setHgap(20);
        metricsGrid.setVgap(20);

        int totalDonors = 125;
        int availDonors = 82;

        VBox card1 = createMetricCard("Registered Donors", String.valueOf(totalDonors), "Active in database");
        VBox card2 = createMetricCard("Available Donors", String.valueOf(availDonors), "Ready for emergency response");
        VBox card3 = createMetricCard("Active Requests", "4", "Pending fulfillment");
        VBox card4 = createMetricCard("Critical Requests", "2", "Requires immediate match");

        metricsGrid.add(card1, 0, 0);
        metricsGrid.add(card2, 1, 0);
        metricsGrid.add(card3, 2, 0);
        metricsGrid.add(card4, 3, 0);

        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(25);
        metricsGrid.getColumnConstraints().addAll(col, col, col, col);

        // 2. Quick Action Buttons Panel
        Label actionsTitle = new Label("Quick Actions");
        actionsTitle.getStyleClass().add("card-title");
        actionsTitle.setStyle("-fx-font-size: 16px; -fx-text-fill: #ffffff;");

        Button btnEmergency = new Button("Emergency Request");
        btnEmergency.getStyleClass().add("btn-primary");
        btnEmergency.setMaxWidth(Double.MAX_VALUE);
        btnEmergency.setOnAction(e -> navigation.showEmergencyRequest());

        Button btnRegister = new Button("Register Donor");
        btnRegister.getStyleClass().add("btn-secondary");
        btnRegister.setMaxWidth(Double.MAX_VALUE);
        btnRegister.setOnAction(e -> navigation.showDonorRegistration());

        Button btnManagement = new Button("Donor Management");
        btnManagement.getStyleClass().add("btn-secondary");
        btnManagement.setMaxWidth(Double.MAX_VALUE);
        btnManagement.setOnAction(e -> navigation.showDonorManagement());

        Button btnResults = new Button("Matching Results");
        btnResults.getStyleClass().add("btn-secondary");
        btnResults.setMaxWidth(Double.MAX_VALUE);
        btnResults.setOnAction(e -> navigation.showMatchingResults());

        GridPane actionGrid = new GridPane();
        actionGrid.setHgap(15);
        actionGrid.setVgap(15);
        actionGrid.add(btnEmergency, 0, 0);
        actionGrid.add(btnRegister, 1, 0);
        actionGrid.add(btnManagement, 2, 0);
        actionGrid.add(btnResults, 3, 0);
        
        ColumnConstraints actCol = new ColumnConstraints();
        actCol.setPercentWidth(25);
        actionGrid.getColumnConstraints().addAll(actCol, actCol, actCol, actCol);

        VBox actionsPanel = new VBox(15, actionsTitle, actionGrid);
        actionsPanel.getStyleClass().add("card-panel");

        // 3. System Status / Overview Banner
        VBox banner = new VBox(8);
        banner.getStyleClass().add("card-panel");
        banner.setStyle("-fx-background-color: #1e293b; -fx-border-color: #ef4444; -fx-border-width: 0 0 0 4px;");

        Label bannerTitle = new Label("Emergency Readiness System Active");
        bannerTitle.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-font-size: 15px;");

        Label bannerDesc = new Label(
            "System is ready to take emergency blood requests, match compatible donors, and prioritize response. " +
            "Select an action above or navigate using the sidebar menu."
        );
        bannerDesc.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 13px;");
        bannerDesc.setWrapText(true);

        banner.getChildren().addAll(bannerTitle, bannerDesc);

        root.getChildren().addAll(metricsGrid, actionsPanel, banner);
    }

    private VBox createMetricCard(String title, String value, String subtitle) {
        VBox card = new VBox(8);
        card.getStyleClass().add("card-panel");

        Label lblTitle = new Label(title);
        lblTitle.getStyleClass().add("card-title");

        Label lblValue = new Label(value);
        lblValue.getStyleClass().add("card-value");

        Label lblSub = new Label(subtitle);
        lblSub.getStyleClass().add("card-subtitle");

        card.getChildren().addAll(lblTitle, lblValue, lblSub);
        return card;
    }

    public Node getView() {
        return root;
    }
}
