package com.emergencyblood.controller;

import com.emergencyblood.model.Donor;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Screen 1: Dashboard View displaying live metric cards, quick actions,
 * emergency readiness stock grid, and recent donors list.
 * Styled with Premium Cream Theme (#FFFDD0).
 */
public class DashboardView {

    private final NavigationController navigation;
    private final VBox root;

    public DashboardView(NavigationController navigation) {
        this.navigation = navigation;

        root = new VBox(22);
        root.setAlignment(Pos.TOP_LEFT);

        int totalDonors = navigation.getSharedDonorList().size();
        long availableDonors = navigation.getSharedDonorList().stream()
                .filter(d -> "Available".equalsIgnoreCase(d.getAvailability()))
                .count();

        boolean hasActiveRequest = navigation.getActiveRequest() != null;
        int activeRequests = hasActiveRequest ? 1 : 0;
        int criticalRequests = (hasActiveRequest && "CRITICAL".equalsIgnoreCase(navigation.getActiveRequest().getUrgency())) ? 1 : 0;

        // 1. Metric Summary Cards Row (4 Cards)
        GridPane metricsGrid = new GridPane();
        metricsGrid.setHgap(16);
        metricsGrid.setVgap(16);

        VBox card1 = createMetricCard("Registered Donors", String.valueOf(totalDonors), "Total in system", "blue", "metric-value-text");
        VBox card2 = createMetricCard("Available Donors", String.valueOf(availableDonors), "Ready to respond", "green", "metric-value-green");
        VBox card3 = createMetricCard("Active Requests", String.valueOf(activeRequests), "Awaiting match", "orange", "metric-value-orange");
        VBox card4 = createMetricCard("Critical Requests", String.valueOf(criticalRequests), "Immediate need", "red", "metric-value-red");

        metricsGrid.add(card1, 0, 0);
        metricsGrid.add(card2, 1, 0);
        metricsGrid.add(card3, 2, 0);
        metricsGrid.add(card4, 3, 0);

        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(25);
        metricsGrid.getColumnConstraints().addAll(col, col, col, col);

        // 2. Middle Grid (Quick Actions & Emergency Readiness)
        GridPane middleGrid = new GridPane();
        middleGrid.setHgap(20);
        middleGrid.setVgap(20);

        // Left Column: Quick Actions
        VBox quickActionsBox = new VBox(12);
        quickActionsBox.getStyleClass().add("card-panel");

        Label actionsTitle = new Label("QUICK ACTIONS");
        actionsTitle.getStyleClass().add("card-title-muted");

        Button btnEmergency = new Button("⚠   Create Emergency Request");
        btnEmergency.getStyleClass().add("btn-danger-primary");
        btnEmergency.setMaxWidth(Double.MAX_VALUE);
        btnEmergency.setOnAction(e -> navigation.showEmergencyRequest());

        Button btnRegister = new Button("+   Register New Donor");
        btnRegister.getStyleClass().add("btn-blue-primary");
        btnRegister.setMaxWidth(Double.MAX_VALUE);
        btnRegister.setOnAction(e -> navigation.showDonorRegistration());

        Button btnManagement = new Button("≡   Manage Donor Records");
        btnManagement.getStyleClass().add("btn-dark-action");
        btnManagement.setMaxWidth(Double.MAX_VALUE);
        btnManagement.setOnAction(e -> navigation.showDonorManagement());

        Button btnResults = new Button("◎   View Matching Results");
        btnResults.getStyleClass().add("btn-dark-action");
        btnResults.setMaxWidth(Double.MAX_VALUE);
        btnResults.setOnAction(e -> navigation.showMatchingResults());

        quickActionsBox.getChildren().addAll(actionsTitle, btnEmergency, btnRegister, btnManagement, btnResults);

        // Right Column: Emergency Readiness
        VBox readinessBox = new VBox(14);
        readinessBox.getStyleClass().add("card-panel");

        Label readinessTitle = new Label("EMERGENCY READINESS");
        readinessTitle.getStyleClass().add("card-title-muted");

        int ratePct = totalDonors > 0 ? (int) Math.round(((double) availableDonors / totalDonors) * 100) : 0;
        Label rateLabel = new Label("Availability Rate");
        rateLabel.setStyle("-fx-text-fill: #5C544B; -fx-font-size: 13px; -fx-font-weight: bold;");

        Label ratePctLabel = new Label(ratePct + "%");
        ratePctLabel.setStyle("-fx-text-fill: #15803D; -fx-font-size: 20px; -fx-font-weight: bold;");

        BorderPane rateHeader = new BorderPane();
        rateHeader.setLeft(rateLabel);
        rateHeader.setRight(ratePctLabel);

        ProgressBar progressBar = new ProgressBar((double) ratePct / 100.0);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.getStyleClass().add("readiness-progress");

        Label stockTitle = new Label("BLOOD GROUP STOCK");
        stockTitle.getStyleClass().add("card-title-muted");

        // Blood Group Stock Grid (8 boxes)
        Map<String, Integer> stockMap = calculateStockMap();
        GridPane stockGrid = new GridPane();
        stockGrid.setHgap(10);
        stockGrid.setVgap(10);

        String[] groups = {"0+", "0-", "A+", "A-", "B+", "B-", "AB+", "AB-"};
        for (int i = 0; i < groups.length; i++) {
            String grp = groups[i];
            int count = stockMap.getOrDefault(grp, 0);

            VBox sBox = new VBox(2);
            sBox.getStyleClass().add("stock-box");
            if (count > 0) {
                sBox.getStyleClass().add("stock-box-active");
            }

            Label lblGrp = new Label(grp);
            lblGrp.getStyleClass().add("stock-group-text");

            Label lblCnt = new Label(String.valueOf(count));
            lblCnt.getStyleClass().add("stock-count-text");

            sBox.getChildren().addAll(lblGrp, lblCnt);

            int rowIdx = i / 4;
            int colIdx = i % 4;
            stockGrid.add(sBox, colIdx, rowIdx);
        }

        ColumnConstraints sCol = new ColumnConstraints();
        sCol.setPercentWidth(25);
        stockGrid.getColumnConstraints().addAll(sCol, sCol, sCol, sCol);

        readinessBox.getChildren().addAll(readinessTitle, rateHeader, progressBar, stockTitle, stockGrid);

        middleGrid.add(quickActionsBox, 0, 0);
        middleGrid.add(readinessBox, 1, 0);

        ColumnConstraints midCol = new ColumnConstraints();
        midCol.setPercentWidth(50);
        middleGrid.getColumnConstraints().addAll(midCol, midCol);

        // 3. Bottom Recent Donors Table
        VBox recentPanel = new VBox(12);
        recentPanel.getStyleClass().add("card-panel");

        Label recentTitle = new Label("RECENT DONORS");
        recentTitle.getStyleClass().add("card-title-muted");

        GridPane tableHeader = new GridPane();
        tableHeader.setHgap(10);
        tableHeader.setPadding(new Insets(0, 0, 6, 0));

        Label thName = new Label("NAME"); thName.getStyleClass().add("card-title-muted");
        Label thGroup = new Label("BLOOD GROUP"); thGroup.getStyleClass().add("card-title-muted");
        Label thLoc = new Label("LOCATION"); thLoc.getStyleClass().add("card-title-muted");
        Label thStat = new Label("STATUS"); thStat.getStyleClass().add("card-title-muted");

        tableHeader.add(thName, 0, 0);
        tableHeader.add(thGroup, 1, 0);
        tableHeader.add(thLoc, 2, 0);
        tableHeader.add(thStat, 3, 0);

        ColumnConstraints tc1 = new ColumnConstraints(); tc1.setPercentWidth(30);
        ColumnConstraints tc2 = new ColumnConstraints(); tc2.setPercentWidth(20);
        ColumnConstraints tc3 = new ColumnConstraints(); tc3.setPercentWidth(30);
        ColumnConstraints tc4 = new ColumnConstraints(); tc4.setPercentWidth(20);
        tableHeader.getColumnConstraints().addAll(tc1, tc2, tc3, tc4);

        VBox donorRows = new VBox(8);

        int maxRows = Math.min(3, navigation.getSharedDonorList().size());
        for (int i = 0; i < maxRows; i++) {
            Donor d = navigation.getSharedDonorList().get(i);

            GridPane row = new GridPane();
            row.setHgap(10);
            row.setPadding(new Insets(6, 0, 6, 0));

            Label lblName = new Label(d.getName());
            lblName.setStyle("-fx-text-fill: #2C2621; -fx-font-weight: bold; -fx-font-size: 13px;");

            Label lblGroup = new Label(d.getBloodGroup());
            lblGroup.getStyleClass().add("blood-badge");

            Label lblLoc = new Label(d.getLocation());
            lblLoc.setStyle("-fx-text-fill: #7A7067; -fx-font-size: 13px;");

            boolean isAvail = "Available".equalsIgnoreCase(d.getAvailability());
            Label lblStatus = new Label(d.getAvailability());
            lblStatus.getStyleClass().add(isAvail ? "status-badge-available" : "status-badge-unavailable");

            row.add(lblName, 0, 0);
            row.add(lblGroup, 1, 0);
            row.add(lblLoc, 2, 0);
            row.add(lblStatus, 3, 0);

            row.getColumnConstraints().addAll(tc1, tc2, tc3, tc4);
            donorRows.getChildren().add(row);
        }

        recentPanel.getChildren().addAll(recentTitle, tableHeader, donorRows);

        root.getChildren().addAll(metricsGrid, middleGrid, recentPanel);
    }

    private VBox createMetricCard(String title, String value, String subtitle, String colorClass, String valueClass) {
        VBox card = new VBox(6);
        card.getStyleClass().addAll("metric-card", "metric-card-" + colorClass);

        Label lblValue = new Label(value);
        lblValue.getStyleClass().add(valueClass);

        Label lblTitle = new Label(title);
        lblTitle.getStyleClass().add("card-title-muted");

        Label lblSub = new Label(subtitle);
        lblSub.getStyleClass().add("metric-subtext");

        card.getChildren().addAll(lblValue, lblTitle, lblSub);
        return card;
    }

    private Map<String, Integer> calculateStockMap() {
        Map<String, Integer> map = new HashMap<>();
        for (Donor d : navigation.getSharedDonorList()) {
            if ("Available".equalsIgnoreCase(d.getAvailability())) {
                String bg = d.getBloodGroup();
                map.put(bg, map.getOrDefault(bg, 0) + 1);
            }
        }
        return map;
    }

    public Node getView() {
        return root;
    }
}
