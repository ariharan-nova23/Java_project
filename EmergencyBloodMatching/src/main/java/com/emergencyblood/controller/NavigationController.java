package com.emergencyblood.controller;

import com.emergencyblood.model.BloodRequest;
import com.emergencyblood.model.Donor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Navigation controller managing sidebar navigation, header titles, 
 * dynamic screen switching, and shared mock data across the application.
 */
public class NavigationController {

    private final BorderPane mainLayout;
    private final VBox sidebar;
    private final VBox headerBar;
    private final Label headerTitleLabel;
    private final Label headerSubtitleLabel;
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

        headerSubtitleLabel = new Label("Find compatible blood donors quickly during emergencies.");
        headerSubtitleLabel.getStyleClass().add("page-subtitle");

        headerBar = new VBox(4, headerTitleLabel, headerSubtitleLabel);
        headerBar.getStyleClass().add("header-bar");
        mainLayout.setTop(headerBar);

        // 3. Create Sidebar
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
        sharedDonorList.add(new Donor(1, "Rahul", "O+", 20, "9876543210", "Vijayawada", "Available"));
        sharedDonorList.add(new Donor(2, "Kiran", "A+", 21, "9876543211", "Vijayawada", "Available"));
        sharedDonorList.add(new Donor(3, "Arjun", "O-", 22, "9876543212", "Guntur", "Available"));
        sharedDonorList.add(new Donor(4, "Ravi", "B+", 20, "9876543213", "Mangalagiri", "Unavailable"));

        activeRequest = new BloodRequest(101, "O+", 2, "Vijayawada", "CRITICAL");
    }

    private VBox createSidebar() {
        VBox box = new VBox(15);
        box.getStyleClass().add("sidebar");
        box.setPrefWidth(240);

        Label logoTitle = new Label("Emergency Blood");
        logoTitle.getStyleClass().add("sidebar-title");

        Label logoSubtitle = new Label("Matching System v1.0");
        logoSubtitle.getStyleClass().add("sidebar-subtitle");

        VBox brandBox = new VBox(2, logoTitle, logoSubtitle);
        brandBox.setPadding(new Insets(0, 0, 15, 10));

        Button btnDashboard = createNavButton("Dashboard", () -> showDashboard());
        Button btnRequest = createNavButton("Emergency Request", () -> showEmergencyRequest());
        Button btnRegister = createNavButton("Register Donor", () -> showDonorRegistration());
        Button btnManagement = createNavButton("Donor Management", () -> showDonorManagement());
        Button btnResults = createNavButton("Matching Results", () -> showMatchingResults());

        navButtons.put("Dashboard", btnDashboard);
        navButtons.put("Emergency Request", btnRequest);
        navButtons.put("Register Donor", btnRegister);
        navButtons.put("Donor Management", btnManagement);
        navButtons.put("Matching Results", btnResults);

        VBox menuBox = new VBox(8, btnDashboard, btnRequest, btnRegister, btnManagement, btnResults);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label footer = new Label("SRM University-AP\nCSE 213 Project");
        footer.getStyleClass().add("sidebar-subtitle");
        footer.setPadding(new Insets(10));

        box.getChildren().addAll(brandBox, menuBox, spacer, footer);
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
        setView("Dashboard Overview", "Quick metrics and emergency actions", view.getView(), "Dashboard");
    }

    public void showEmergencyRequest() {
        EmergencyRequestView view = new EmergencyRequestView(this);
        setView("Emergency Blood Request", "Submit urgent blood requirements for matching", view.getView(), "Emergency Request");
    }

    public void showDonorRegistration() {
        DonorRegistrationView view = new DonorRegistrationView(this);
        setView("Donor Registration", "Register new voluntary blood donors into the system", view.getView(), "Register Donor");
    }

    public void showDonorManagement() {
        DonorManagementView view = new DonorManagementView(this);
        setView("Donor Management", "View, filter, edit, and update donor records", view.getView(), "Donor Management");
    }

    public void showMatchingResults() {
        MatchingResultsView view = new MatchingResultsView(this);
        setView("Matching Results", "Ranked compatible donors for active emergency request", view.getView(), "Matching Results");
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
    }
}
