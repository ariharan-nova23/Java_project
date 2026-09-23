package com.emergencyblood.controller;

import com.emergencyblood.model.Donor;
import com.emergencyblood.util.AlertUtil;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

/**
 * Screen 4: Donor Management TableView Screen
 */
public class DonorManagementView {

    private final NavigationController navigation;
    private final VBox root;
    private final TableView<Donor> table;

    public DonorManagementView(NavigationController navigation) {
        this.navigation = navigation;

        root = new VBox(20);
        root.setAlignment(Pos.TOP_LEFT);

        VBox container = new VBox(15);
        container.getStyleClass().add("card-panel");

        // TableView Configuration
        table = new TableView<>();
        table.setItems(navigation.getSharedDonorList());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("No donors currently registered in memory."));

        TableColumn<Donor, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(60);

        TableColumn<Donor, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colName.setPrefWidth(150);

        TableColumn<Donor, String> colGroup = new TableColumn<>("Blood Group");
        colGroup.setCellValueFactory(new PropertyValueFactory<>("bloodGroup"));
        colGroup.setPrefWidth(100);

        TableColumn<Donor, Integer> colAge = new TableColumn<>("Age");
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colAge.setPrefWidth(70);

        TableColumn<Donor, String> colPhone = new TableColumn<>("Phone Number");
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colPhone.setPrefWidth(130);

        TableColumn<Donor, String> colLocation = new TableColumn<>("Location");
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colLocation.setPrefWidth(130);

        TableColumn<Donor, String> colStatus = new TableColumn<>("Availability");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("availability"));
        colStatus.setPrefWidth(120);

        table.getColumns().addAll(colId, colName, colGroup, colAge, colPhone, colLocation, colStatus);
        VBox.setVgrow(table, Priority.ALWAYS);

        // Action Toolbar
        Button btnAdd = new Button("+ Add Donor");
        btnAdd.getStyleClass().add("btn-primary");
        btnAdd.setOnAction(e -> navigation.showDonorRegistration());

        Button btnEdit = new Button("Toggle Availability");
        btnEdit.getStyleClass().add("btn-secondary");
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

            AlertUtil.showInfo(
                "Status Updated", 
                "Donor Availability Updated", 
                "Donor " + selected.getName() + " is now set to '" + newStatus + "'."
            );
        });

        Button btnDelete = new Button("Delete");
        btnDelete.getStyleClass().add("btn-danger");
        btnDelete.setOnAction(e -> {
            Donor selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                AlertUtil.showError("Selection Error", "No Donor Selected", "Please select a donor from the table to delete.");
                return;
            }

            navigation.getSharedDonorList().remove(selected);
            AlertUtil.showInfo(
                "Donor Deleted", 
                "Record Removed", 
                "Donor '" + selected.getName() + "' was deleted successfully."
            );
        });

        Button btnRefresh = new Button("Refresh");
        btnRefresh.getStyleClass().add("btn-secondary");
        btnRefresh.setOnAction(e -> table.refresh());

        HBox toolbar = new HBox(12, btnAdd, btnEdit, btnDelete, btnRefresh);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        container.getChildren().addAll(toolbar, table);
        root.getChildren().add(container);
    }

    public Node getView() {
        return root;
    }
}
