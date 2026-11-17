package com.emergencyblood.controller;

import com.emergencyblood.model.Donor;
import com.emergencyblood.util.AlertUtil;
import com.emergencyblood.util.DatabaseExecutor;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.util.Optional;

/**
 * Screen 4: Donor Management TableView Screen with MySQL Integration,
 * Asynchronous Background CRUD, Custom Cell Badges, and Dynamic Status Toolbar.
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

        BorderPane topBar = new BorderPane();
        topBar.setRight(topBadgeBox);

        // 2. Action Toolbar Buttons
        Button btnAdd = new Button("+  Add Donor");
        btnAdd.getStyleClass().add("btn-blue-primary");
        btnAdd.setOnAction(e -> navigation.showDonorRegistration());

        Button btnEditDetails = new Button("✏  Edit Details");
        btnEditDetails.getStyleClass().add("btn-dark-action");
        btnEditDetails.setOnAction(e -> {
            Donor selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                AlertUtil.showError("Selection Error", "No Donor Selected", "Please select a donor from the table to edit.");
                return;
            }
            showEditDonorDialog(selected);
        });

        Button btnToggle = new Button("Toggle Availability");
        btnToggle.getStyleClass().add("btn-dark-action");
        btnToggle.setOnAction(e -> {
            Donor selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                AlertUtil.showError("Selection Error", "No Donor Selected", "Please select a donor from the table to toggle availability.");
                return;
            }

            String current = selected.getAvailability();
            String newStatus = current.equalsIgnoreCase("Available") ? "Unavailable" : "Available";

            btnToggle.setDisable(true);
            Task<Boolean> task = new Task<>() {
                @Override
                protected Boolean call() throws Exception {
                    selected.setAvailability(newStatus);
                    boolean ok = navigation.getDonorDAO().updateDonor(selected);
                    if (!ok) {
                        selected.setAvailability(current);
                    }
                    return ok;
                }
            };

            task.setOnSucceeded(ev -> {
                btnToggle.setDisable(false);
                table.refresh();
                updateFooterAndBadge();

                AlertUtil.showInfo(
                    "Status Updated", 
                    "Donor Availability Updated", 
                    "Donor " + selected.getName() + " is now set to '" + newStatus + "' in MySQL."
                );
            });

            task.setOnFailed(ev -> {
                btnToggle.setDisable(false);
                selected.setAvailability(current);
                table.refresh();
                Throwable ex = task.getException();
                AlertUtil.showError("Database Error", "Update Failed", ex != null ? ex.getMessage() : "Unknown error");
            });

            DatabaseExecutor.executeTask(task);
        });

        Button btnDelete = new Button("Delete");
        btnDelete.getStyleClass().add("btn-dark-action");
        btnDelete.setOnAction(e -> {
            Donor selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                AlertUtil.showError("Selection Error", "No Donor Selected", "Please select a donor from the table to delete.");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Deletion");
            confirm.setHeaderText("Delete Donor from Database");
            confirm.setContentText("Are you sure you want to delete donor '" + selected.getName() + "' (ID: #" + selected.getId() + ")? This action is permanent.");

            Optional<ButtonType> opt = confirm.showAndWait();
            if (opt.isEmpty() || opt.get() != ButtonType.OK) {
                return;
            }

            btnDelete.setDisable(true);
            Task<Boolean> task = new Task<>() {
                @Override
                protected Boolean call() throws Exception {
                    return navigation.getDonorDAO().deleteDonor(selected.getId());
                }
            };

            task.setOnSucceeded(ev -> {
                btnDelete.setDisable(false);
                navigation.getSharedDonorList().remove(selected);
                table.refresh();
                updateFooterAndBadge();
                AlertUtil.showInfo(
                    "Donor Deleted", 
                    "Record Removed", 
                    "Donor '" + selected.getName() + "' was permanently deleted from MySQL."
                );
            });

            task.setOnFailed(ev -> {
                btnDelete.setDisable(false);
                Throwable ex = task.getException();
                AlertUtil.showError("Database Error", "Delete Failed", ex != null ? ex.getMessage() : "Unknown error");
            });

            DatabaseExecutor.executeTask(task);
        });

        Button btnRefresh = new Button("↻  Refresh");
        btnRefresh.getStyleClass().add("btn-dark-action");
        btnRefresh.setOnAction(e -> {
            btnRefresh.setDisable(true);
            navigation.refreshDonorsFromDatabase(() -> {
                btnRefresh.setDisable(false);
                table.refresh();
                updateFooterAndBadge();
            });
        });

        HBox leftToolbar = new HBox(12, btnAdd, btnEditDetails, btnToggle, btnDelete);
        leftToolbar.setAlignment(Pos.CENTER_LEFT);

        BorderPane toolbar = new BorderPane();
        toolbar.setLeft(leftToolbar);
        toolbar.setRight(btnRefresh);

        // 3. TableView Configuration
        table.setItems(navigation.getSharedDonorList());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("No donors found in the database. Use '+ Add Donor' to register one."));

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

    private void showEditDonorDialog(Donor donor) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Donor Details");
        dialog.setHeaderText("Update database record for " + donor.getName() + " (ID: #" + donor.getId() + ")");

        ButtonType saveBtnType = new ButtonType("Save Changes", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtnType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);
        grid.setPadding(new Insets(16, 20, 16, 20));

        TextField txtName = new TextField(donor.getName());
        ComboBox<String> comboBlood = new ComboBox<>(FXCollections.observableArrayList(
            "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
        ));
        comboBlood.setValue(donor.getBloodGroup());

        TextField txtAge = new TextField(String.valueOf(donor.getAge()));
        TextField txtPhone = new TextField(donor.getPhoneNumber());
        TextField txtLocation = new TextField(donor.getLocation());

        ComboBox<String> comboStatus = new ComboBox<>(FXCollections.observableArrayList("Available", "Unavailable"));
        comboStatus.setValue(donor.getAvailability());

        grid.add(new Label("Full Name:"), 0, 0);
        grid.add(txtName, 1, 0);
        grid.add(new Label("Blood Group:"), 0, 1);
        grid.add(comboBlood, 1, 1);
        grid.add(new Label("Age (18-65):"), 0, 2);
        grid.add(txtAge, 1, 2);
        grid.add(new Label("Phone Number:"), 0, 3);
        grid.add(txtPhone, 1, 3);
        grid.add(new Label("Location:"), 0, 4);
        grid.add(txtLocation, 1, 4);
        grid.add(new Label("Availability:"), 0, 5);
        grid.add(comboStatus, 1, 5);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> opt = dialog.showAndWait();
        if (opt.isPresent() && opt.get() == saveBtnType) {
            String name = txtName.getText().trim();
            String group = comboBlood.getValue();
            String ageStr = txtAge.getText().trim();
            String phone = txtPhone.getText().trim();
            String loc = txtLocation.getText().trim();
            String status = comboStatus.getValue();

            if (name.isEmpty() || ageStr.isEmpty() || phone.isEmpty() || loc.isEmpty() || group == null) {
                AlertUtil.showError("Validation Error", "Missing Fields", "Please complete all fields.");
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageStr);
                if (age < 18 || age > 65) {
                    AlertUtil.showError("Validation Error", "Invalid Age", "Age must be between 18 and 65.");
                    return;
                }
            } catch (NumberFormatException e) {
                AlertUtil.showError("Validation Error", "Invalid Age", "Age must be an integer number.");
                return;
            }

            Donor updated = new Donor(donor.getId(), name, group, age, phone, loc, status);

            Task<Boolean> task = new Task<>() {
                @Override
                protected Boolean call() throws Exception {
                    return navigation.getDonorDAO().updateDonor(updated);
                }
            };

            task.setOnSucceeded(ev -> {
                donor.setName(name);
                donor.setBloodGroup(group);
                donor.setAge(age);
                donor.setPhoneNumber(phone);
                donor.setLocation(loc);
                donor.setAvailability(status);
                table.refresh();
                updateFooterAndBadge();

                AlertUtil.showInfo(
                    "Update Successful", 
                    "Donor Details Saved", 
                    "Donor #" + donor.getId() + " (" + name + ") updated successfully in MySQL."
                );
            });

            task.setOnFailed(ev -> {
                Throwable ex = task.getException();
                AlertUtil.showError("Database Error", "Update Failed", ex != null ? ex.getMessage() : "Unknown error");
            });

            DatabaseExecutor.executeTask(task);
        }
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
