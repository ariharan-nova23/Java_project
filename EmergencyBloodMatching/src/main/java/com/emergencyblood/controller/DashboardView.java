package com.emergencyblood.controller;

import com.emergencyblood.model.Donor;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Screen 1: Dashboard View displaying Executive Light KPI cards, Quick Actions,
 * Emergency Readiness Stock Grid, and a Filterable Recent Donors List inside a ScrollPane.
 * Connected reactively to MySQL-backed shared data.
 */
public class DashboardView {

    private final NavigationController navigation;
    private final VBox root;
    private final ScrollPane scrollPane;

    private Label lblTotalDonors;
    private Label lblAvailDonors;
    private Label lblActiveRequests;
    private Label lblCriticalRequests;
    private Label lblRatePct;
    private ProgressBar progressBar;
    private final Map<String, Label> stockCountLabels = new HashMap<>();

    public DashboardView(NavigationController navigation) {
        this.navigation = navigation;

        root = new VBox(22);
        root.setAlignment(Pos.TOP_LEFT);
        root.setPadding(new Insets(4, 4, 20, 4));

        // 1. Metric KPI Cards (Top 4-Column Grid)
        GridPane metricsGrid = new GridPane();
        metricsGrid.setHgap(16);
        metricsGrid.setVgap(16);

        VBox card1 = createMetricCard("Registered Donors", "0", "Total in system", "blue", "metric-value-text");
        lblTotalDonors = (Label) card1.getChildren().get(0);

        VBox card2 = createMetricCard("Available Donors", "0", "Ready to respond", "green", "metric-value-green");
        lblAvailDonors = (Label) card2.getChildren().get(0);

        VBox card3 = createMetricCard("Active Requests", "0", "Awaiting match", "orange", "metric-value-orange");
        lblActiveRequests = (Label) card3.getChildren().get(0);

        VBox card4 = createMetricCard("Critical Requests", "0", "Immediate need", "red", "metric-value-red");
        lblCriticalRequests = (Label) card4.getChildren().get(0);

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

        // Left Panel - Quick Actions
        VBox quickActionsBox = new VBox(12);
        quickActionsBox.getStyleClass().add("card-panel");

        Label actionsTitle = new Label("QUICK ACTIONS");
        actionsTitle.getStyleClass().add("card-title-muted");

        Button btnEmergency = new Button("🚨   Create Emergency Request");
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

        // Right Panel - Emergency Readiness & Stock Grid
        VBox readinessBox = new VBox(14);
        readinessBox.getStyleClass().add("card-panel");

        Label readinessTitle = new Label("EMERGENCY READINESS");
        readinessTitle.getStyleClass().add("card-title-muted");

        Label rateLabel = new Label("Availability Rate");
        rateLabel.setStyle("-fx-text-fill: #4B5563; -fx-font-size: 13px; -fx-font-weight: bold;");

        lblRatePct = new Label("0%");
        lblRatePct.setStyle("-fx-text-fill: #10B981; -fx-font-size: 20px; -fx-font-weight: bold;");

        BorderPane rateHeader = new BorderPane();
        rateHeader.setLeft(rateLabel);
        rateHeader.setRight(lblRatePct);

        progressBar = new ProgressBar(0.0);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.getStyleClass().add("readiness-progress");

        Label stockTitle = new Label("BLOOD GROUP STOCK");
        stockTitle.getStyleClass().add("card-title-muted");

        // Blood Group Stock Grid (8 boxes)
        GridPane stockGrid = new GridPane();
        stockGrid.setHgap(10);
        stockGrid.setVgap(10);

        String[] groups = {"O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-"};
        for (int i = 0; i < groups.length; i++) {
            String grp = groups[i];

            VBox sBox = new VBox(2);
            sBox.getStyleClass().add("stock-box");

            Label lblGrp = new Label(grp);
            lblGrp.getStyleClass().add("stock-group-text");

            Label lblCnt = new Label("0");
            lblCnt.getStyleClass().add("stock-count-text");
            stockCountLabels.put(grp, lblCnt);

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

        // 3. Bottom Section - Filterable Recent Donors Table
        VBox recentPanel = new VBox(14);
        recentPanel.getStyleClass().add("card-panel");

        Label recentTitle = new Label("RECENT DONORS");
        recentTitle.getStyleClass().add("card-title-muted");

        // Search Bar & Filter Controls
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Search donors by name, blood group, or city...");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        ComboBox<String> statusFilter = new ComboBox<>(FXCollections.observableArrayList(
            "All Statuses", "Available", "Unavailable"
        ));
        statusFilter.setValue("All Statuses");

        HBox filterBar = new HBox(12, searchField, statusFilter);
        filterBar.setAlignment(Pos.CENTER_LEFT);

        // Filtered List & Rows Generator
        FilteredList<Donor> filteredData = new FilteredList<>(navigation.getSharedDonorList(), p -> true);

        VBox donorRows = new VBox(8);

        Runnable updateListRows = () -> {
            donorRows.getChildren().clear();
            int count = 0;
            for (Donor d : filteredData) {
                GridPane row = new GridPane();
                row.setHgap(10);
                row.setPadding(new Insets(6, 0, 6, 0));

                Label lblName = new Label(d.getName());
                lblName.setStyle("-fx-text-fill: #111827; -fx-font-weight: bold; -fx-font-size: 13px;");

                Label lblGroup = new Label(d.getBloodGroup());
                lblGroup.getStyleClass().add("blood-badge");

                Label lblLoc = new Label(d.getLocation());
                lblLoc.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 13px;");

                boolean isAvail = "Available".equalsIgnoreCase(d.getAvailability());
                Label lblStatus = new Label(d.getAvailability());
                lblStatus.getStyleClass().add(isAvail ? "status-badge-available" : "status-badge-unavailable");

                row.add(lblName, 0, 0);
                row.add(lblGroup, 1, 0);
                row.add(lblLoc, 2, 0);
                row.add(lblStatus, 3, 0);

                ColumnConstraints tc1 = new ColumnConstraints(); tc1.setPercentWidth(30);
                ColumnConstraints tc2 = new ColumnConstraints(); tc2.setPercentWidth(20);
                ColumnConstraints tc3 = new ColumnConstraints(); tc3.setPercentWidth(30);
                ColumnConstraints tc4 = new ColumnConstraints(); tc4.setPercentWidth(20);
                row.getColumnConstraints().addAll(tc1, tc2, tc3, tc4);

                donorRows.getChildren().add(row);
                count++;
                if (count >= 5) break; // Display top matching items
            }

            if (donorRows.getChildren().isEmpty()) {
                Label noResults = new Label("No matching donors found.");
                noResults.setStyle("-fx-text-fill: #9CA3AF; -fx-font-size: 13px; -fx-padding: 8px 0;");
                donorRows.getChildren().add(noResults);
            }
        };

        // Filter Logic Handler
        Runnable applyFilters = () -> {
            String query = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
            String st = statusFilter.getValue();

            filteredData.setPredicate(donor -> {
                boolean matchesSearch = query.isEmpty()
                        || donor.getName().toLowerCase().contains(query)
                        || donor.getBloodGroup().toLowerCase().contains(query)
                        || donor.getLocation().toLowerCase().contains(query);

                boolean matchesStatus = "All Statuses".equals(st)
                        || donor.getAvailability().equalsIgnoreCase(st);

                return matchesSearch && matchesStatus;
            });
            updateListRows.run();
        };

        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters.run());
        statusFilter.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters.run());

        // Refresh metrics and initial rows
        refreshDashboardMetrics();
        updateListRows.run();

        // Listen for live database/model updates
        navigation.getSharedDonorList().addListener((ListChangeListener<Donor>) c -> {
            refreshDashboardMetrics();
            updateListRows.run();
        });

        // Table Column Headers
        GridPane tableHeader = new GridPane();
        tableHeader.setHgap(10);
        tableHeader.setPadding(new Insets(4, 0, 6, 0));

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

        recentPanel.getChildren().addAll(recentTitle, filterBar, tableHeader, donorRows);

        root.getChildren().addAll(metricsGrid, middleGrid, recentPanel);

        // Wrap root in ScrollPane for full vertical scrolling
        scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
    }

    private void refreshDashboardMetrics() {
        int totalDonors = navigation.getSharedDonorList().size();
        long availableDonors = navigation.getSharedDonorList().stream()
                .filter(d -> "Available".equalsIgnoreCase(d.getAvailability()))
                .count();

        boolean hasActiveRequest = navigation.getActiveRequest() != null;
        int activeRequests = hasActiveRequest ? 1 : 0;
        int criticalRequests = (hasActiveRequest && "CRITICAL".equalsIgnoreCase(navigation.getActiveRequest().getUrgency())) ? 1 : 0;

        lblTotalDonors.setText(String.valueOf(totalDonors));
        lblAvailDonors.setText(String.valueOf(availableDonors));
        lblActiveRequests.setText(String.valueOf(activeRequests));
        lblCriticalRequests.setText(String.valueOf(criticalRequests));

        int ratePct = totalDonors > 0 ? (int) Math.round(((double) availableDonors / totalDonors) * 100) : 0;
        lblRatePct.setText(ratePct + "%");
        progressBar.setProgress((double) ratePct / 100.0);

        Map<String, Integer> stockMap = calculateStockMap();
        for (Map.Entry<String, Label> entry : stockCountLabels.entrySet()) {
            int count = stockMap.getOrDefault(entry.getKey(), 0);
            entry.getValue().setText(String.valueOf(count));
            VBox parentBox = (VBox) entry.getValue().getParent();
            if (parentBox != null) {
                if (count > 0) {
                    if (!parentBox.getStyleClass().contains("stock-box-active")) {
                        parentBox.getStyleClass().add("stock-box-active");
                    }
                } else {
                    parentBox.getStyleClass().remove("stock-box-active");
                }
            }
        }
    }

    private VBox createMetricCard(String title, String value, String subtitle, String colorClass, String valueClass) {
        VBox card = new VBox(6);
        card.getStyleClass().addAll("metric-card", "metric-card-" + colorClass);

        Label lblValue = new Label(value);
        lblValue.getStyleClass().addAll("metric-value", valueClass);

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
        return scrollPane;
    }
}
