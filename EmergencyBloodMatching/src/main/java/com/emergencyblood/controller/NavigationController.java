package com.emergencyblood.controller;

import com.emergencyblood.model.BloodRequest;
import com.emergencyblood.model.Donor;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

import java.util.HashMap;
import java.util.Map;

/**
 * Navigation controller managing sidebar navigation, top header,
 * dynamic screen switching, and shared application state.
 */
public class NavigationController {

    private final BorderPane mainLayout;
    private final VBox sidebar;
    private final BorderPane headerBar;
    private final Label headerTitleLabel;
    private final Label headerSubtitleLabel;
    private final Label headerPillLabel;
    private final Label sidebarStatusDesc;
    private final StackPane contentArea;

    private final Map<String, Button> navButtons = new HashMap<>();
    private final ObservableList<Donor> sharedDonorList = FXCollections.observableArrayList();
    private BloodRequest activeRequest;

    public NavigationController() {
        mainLayout = new BorderPane();

        // 1. Initialize Mock Data
        initMockData();

        // 2. Create Header
        headerTitleLabel = new Label("Dashboard");
        headerTitleLabel.getStyleClass().add("page-title");

        headerSubtitleLabel = new Label("System overview and emergency readiness status");
        headerSubtitleLabel.getStyleClass().add("page-subtitle");

        VBox titleBox = new VBox(2, headerTitleLabel, headerSubtitleLabel);

        headerPillLabel = new Label("• OPERATIONAL");
        headerPillLabel.getStyleClass().add("header-status-pill-text");

        HBox pillBox = new HBox(headerPillLabel);
        pillBox.getStyleClass().add("header-status-pill");
        pillBox.setAlignment(Pos.CENTER);

        headerBar = new BorderPane();
        headerBar.setLeft(titleBox);
        headerBar.setRight(pillBox);
        BorderPane.setAlignment(pillBox, Pos.CENTER_RIGHT);
        headerBar.getStyleClass().add("header-bar");
        mainLayout.setTop(headerBar);

        // 3. Create Sidebar Status Widgets & Sidebar
        sidebarStatusDesc = new Label();
        sidebarStatusDesc.getStyleClass().add("sidebar-status-desc");
        updateSidebarStatusText();

        sharedDonorList.addListener((ListChangeListener<Donor>) c -> updateSidebarStatusText());

        sidebar = createSidebar();
        mainLayout.setLeft(sidebar);

        // 4. Create Content Area
        contentArea = new StackPane();
        contentArea.setPadding(new Insets(25));
        mainLayout.setCenter(contentArea);

        // Default screen: Dashboard
        showDashboard();
    }

    private void initMockData() {
        sharedDonorList.add(new Donor(1, "Rahul Sharma", "O+", 20, "9876543210", "Vijayawada", "Available"));
        sharedDonorList.add(new Donor(2, "Kiran Reddy", "A+", 21, "9876543211", "Vijayawada", "Available"));
        sharedDonorList.add(new Donor(3, "Arjun Naidu", "O-", 22, "9876543212", "Guntur", "Available"));
        sharedDonorList.add(new Donor(4, "Ravi Teja", "B+", 20, "9876543213", "Mangalagiri", "Unavailable"));
        sharedDonorList.add(new Donor(5, "Priya Lakshmi", "AB+", 25, "9876543214", "Vijayawada", "Available"));
    }

    private void updateSidebarStatusText() {
        long availableCount = sharedDonorList.stream()
                .filter(d -> "Available".equalsIgnoreCase(d.getAvailability()))
                .count();
        int activeReqCount = activeRequest != null ? 1 : 0;
        sidebarStatusDesc.setText(availableCount + " donors ready · " + activeReqCount + " requests active");
    }

    private VBox createSidebar() {
        VBox box = new VBox(10);
        box.getStyleClass().add("sidebar");
        box.setPrefWidth(250);

        // Brand Icon & Titles
        StackPane iconBox = new StackPane();
        iconBox.getStyleClass().add("brand-icon-box");
        Label iconLabel = new Label("🩸");
        iconLabel.setStyle("-fx-font-size: 18px;");
        iconBox.getChildren().add(iconLabel);

        Label logoTitle = new Label("Emergency Blood");
        logoTitle.getStyleClass().add("sidebar-title");

        Label logoSubtitle = new Label("v1.0 · SRM University-AP");
        logoSubtitle.getStyleClass().add("sidebar-subtitle");

        VBox brandText = new VBox(1, logoTitle, logoSubtitle);

        HBox brandHeader = new HBox(12, iconBox, brandText);
        brandHeader.setAlignment(Pos.CENTER_LEFT);
        brandHeader.setPadding(new Insets(0, 0, 15, 4));

        Label sectionNav = new Label("NAVIGATION");
        sectionNav.getStyleClass().add("sidebar-section-header");

        Button btnDashboard = createNavButton("∷  Dashboard", () -> showDashboard());
        Button btnRequest = createNavButton("⚠  Emergency Request", () -> showEmergencyRequest());
        Button btnRegister = createNavButton("+  Register Donor", () -> showDonorRegistration());
        Button btnManagement = createNavButton("≡  Donor Management", () -> showDonorManagement());
        Button btnResults = createNavButton("◎  Matching Results", () -> showMatchingResults());

        navButtons.put("Dashboard", btnDashboard);
        navButtons.put("Emergency Request", btnRequest);
        navButtons.put("Register Donor", btnRegister);
        navButtons.put("Donor Management", btnManagement);
        navButtons.put("Matching Results", btnResults);

        VBox menuBox = new VBox(6, sectionNav, btnDashboard, btnRequest, btnRegister, btnManagement, btnResults);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Sidebar Bottom Status Card
        VBox statusCard = new VBox(6);
        statusCard.getStyleClass().add("sidebar-status-card");

        Circle dot = new Circle(4);
        dot.getStyleClass().add("status-dot-green");

        Label statusTitle = new Label("System Operational");
        statusTitle.getStyleClass().add("sidebar-status-title");

        HBox statusHeader = new HBox(8, dot, statusTitle);
        statusHeader.setAlignment(Pos.CENTER_LEFT);

        statusCard.getChildren().addAll(statusHeader, sidebarStatusDesc);

        box.getChildren().addAll(brandHeader, menuBox, spacer, statusCard);
        return box;
    }

    private Button createNavButton(String text, Runnable action) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.getStyleClass().add("nav-button");
        btn.setOnAction(e -> action.run());
        return btn;
    }

    private void setActiveNavButton(String screenName) {
        for (Map.Entry<String, Button> entry : navButtons.entrySet()) {
            if (entry.getKey().equals(screenName)) {
                if (!entry.getValue().getStyleClass().contains("nav-button-active")) {
                    entry.getValue().getStyleClass().add("nav-button-active");
                }
            } else {
                entry.getValue().getStyleClass().remove("nav-button-active");
            }
        }
    }

    public void setView(String title, String subtitle, Node viewNode, String screenName) {
        headerTitleLabel.setText(title);
        headerSubtitleLabel.setText(subtitle);
        contentArea.getChildren().setAll(viewNode);
        setActiveNavButton(screenName);
    }

    public void showDashboard() {
        DashboardView view = new DashboardView(this);
        setView("Dashboard", "System overview and emergency readiness status", view.getView(), "Dashboard");
    }

    public void showEmergencyRequest() {
        EmergencyRequestView view = new EmergencyRequestView(this);
        setView("Emergency Blood Request", "Submit an urgent blood requirement — the system will identify and rank compatible donors", view.getView(), "Emergency Request");
    }

    public void showDonorRegistration() {
        DonorRegistrationView view = new DonorRegistrationView(this);
        setView("Register Donor", "Enroll a new voluntary blood donor into the emergency response network", view.getView(), "Register Donor");
    }

    public void showDonorManagement() {
        DonorManagementView view = new DonorManagementView(this);
        setView("Donor Management", "View, filter, and update donor records", view.getView(), "Donor Management");
    }

    public void showMatchingResults() {
        MatchingResultsView view = new MatchingResultsView(this);
        setView("Matching Results", "Ranked compatible donors for active emergency blood request", view.getView(), "Matching Results");
    }

    public BorderPane getMainLayout() {
        return mainLayout;
    }

    public ObservableList<Donor> getSharedDonorList() {
        return sharedDonorList;
    }

    public BloodRequest getActiveRequest() {
        return activeRequest;
    }

    public void setActiveRequest(BloodRequest request) {
        this.activeRequest = request;
        updateSidebarStatusText();
    }
}
