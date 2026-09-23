package com.emergencyblood.controller;

import com.emergencyblood.model.Donor;
import com.emergencyblood.util.AlertUtil;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

/**
 * Screen 4: Donor Management TableView Screen with Custom Cell Badges
 * and Dynamic Status Toolbar.
 */
public class DonorManagementView {

    private final NavigationController navigation;
    private final VBox root;
    private final TableView<Donor> table;
    private final Label footerLabel;
    private final Label topBadgeLabel;

    public DonorManagementView(NavigationController navigation) {
        this.navigation = navigation;

        root = new VBox(20);
        root.setAlignment(Pos.TOP_LEFT);

        // Instantiate TableView first
        table = new TableView<>();

        // 1. Header Right Badge Container
        long availCount = navigation.getSharedDonorList().stream()
                .filter(d -> "Available".equalsIgnoreCase(d.getAvailability()))
                .count();

        topBadgeLabel = new Label("• " + availCount + " AVAILABLE");
        topBadgeLabel.getStyleClass().add("header-status-pill-text");

        HBox topBadgeBox = new HBox(topBadgeLabel);
        topBadgeBox.getStyleClass().add("header-status-pill");
        topBadgeBox.setAlignment(Pos.CENTER);

        // Top bar right side badge overlay
        BorderPane topBar = new BorderPane();
        topBar.setRight(topBadgeBox);

        // 2. Action Toolbar
        Button btnAdd = new Button("+  Add Donor");
        btnAdd.getStyleClass().add("btn-blue-primary");
        btnAdd.setOnAction(e -> navigation.showDonorRegistration());

        Button btnEdit = new Button("Toggle Availability");
        btnEdit.getStyleClass().add("btn-dark-action");
        btnEdit.setOnAction(e -> {
            Donor selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                AlertUtil.showError("Selection Error", "No Donor Selected", "Please select a donor from the table to toggle availability.");
                return;
            }

            String current = selected.getAvailability();
            String newStatus = current.equalsIgnoreCase("Available") ? "Unavailable" : "Available";
            selected.setAvailability(newStatus);
            table.refresh();
            updateFooterAndBadge();

            AlertUtil.showInfo(
                "Status Updated", 
                "Donor Availability Updated", 
                "Donor " + selected.getName() + " is now set to '" + newStatus + "'."
            );
        });

        Button btnDelete = new Button("Delete");
        btnDelete.getStyleClass().add("btn-dark-action");
        btnDelete.setOnAction(e -> {
            Donor selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                AlertUtil.showError("Selection Error", "No Donor Selected", "Please select a donor from the table to delete.");
                return;
            }

            navigation.getSharedDonorList().remove(selected);
            updateFooterAndBadge();
            AlertUtil.showInfo(
                "Donor Deleted", 
                "Record Removed", 
                "Donor '" + selected.getName() + "' was deleted successfully."
            );
        });

        Button btnRefresh = new Button("↻  Refresh");
        btnRefresh.getStyleClass().add("btn-dark-action");
        btnRefresh.setOnAction(e -> {
            table.refresh();
            updateFooterAndBadge();
        });

        HBox leftToolbar = new HBox(12, btnAdd, btnEdit, btnDelete);
        leftToolbar.setAlignment(Pos.CENTER_LEFT);

        BorderPane toolbar = new BorderPane();
        toolbar.setLeft(leftToolbar);
        toolbar.setRight(btnRefresh);

        // 3. TableView Configuration
        table.setItems(navigation.getSharedDonorList());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("No donors currently registered in memory."));

        // Column # (ID)
        TableColumn<Donor, Integer> colId = new TableColumn<>("#");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(50);

        // Column Name
        TableColumn<Donor, String> colName = new TableColumn<>("NAME");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colName.setPrefWidth(160);

        // Column Blood Group (Custom Blue Badge)
        TableColumn<Donor, String> colGroup = new TableColumn<>("BLOOD GROUP");
        colGroup.setCellValueFactory(new PropertyValueFactory<>("bloodGroup"));
        colGroup.setPrefWidth(110);
        colGroup.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Label badge = new Label(item);
                    badge.getStyleClass().add("blood-badge");
                    setGraphic(badge);
                    setText(null);
                }
            }
        });

        // Column Age
        TableColumn<Donor, Integer> colAge = new TableColumn<>("AGE");
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colAge.setPrefWidth(70);

        // Column Phone
        TableColumn<Donor, String> colPhone = new TableColumn<>("PHONE");
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colPhone.setPrefWidth(130);

        // Column Location
        TableColumn<Donor, String> colLocation = new TableColumn<>("LOCATION");
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colLocation.setPrefWidth(140);

        // Column Availability Status (Custom Rounded Pill Badges)
        TableColumn<Donor, String> colStatus = new TableColumn<>("STATUS");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("availability"));
        colStatus.setPrefWidth(130);
        colStatus.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Label badge = new Label(item);
                    boolean isAvail = "Available".equalsIgnoreCase(item);
                    badge.getStyleClass().add(isAvail ? "status-badge-available" : "status-badge-unavailable");
                    setGraphic(badge);
                    setText(null);
                }
            }
        });

        table.getColumns().addAll(colId, colName, colGroup, colAge, colPhone, colLocation, colStatus);
        VBox.setVgrow(table, Priority.ALWAYS);

        // 4. Footer Subtext
        footerLabel = new Label();
        footerLabel.setStyle("-fx-text-fill: #64748b; -fx-font-size: 12px;");
        updateFooterAndBadge();

        VBox container = new VBox(16);
        container.getStyleClass().add("card-panel");
        container.getChildren().addAll(toolbar, table, footerLabel);

        root.getChildren().addAll(topBar, container);
    }

    private void updateFooterAndBadge() {
        int total = navigation.getSharedDonorList().size();
        long avail = navigation.getSharedDonorList().stream()
                .filter(d -> "Available".equalsIgnoreCase(d.getAvailability()))
                .count();
        footerLabel.setText(total + " donors · " + avail + " available · Click a row to select");
        topBadgeLabel.setText("• " + avail + " AVAILABLE");
    }

    public Node getView() {
        return root;
    }
}
