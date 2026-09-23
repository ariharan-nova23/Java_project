package com.emergencyblood.controller;

import com.emergencyblood.model.BloodRequest;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;

/**
 * Screen 5: Matching Results Screen (Mock Ranked Donor Layout)
 */
public class MatchingResultsView {

    private final NavigationController navigation;
    private final VBox root;

    public MatchingResultsView(NavigationController navigation) {
        this.navigation = navigation;

        root = new VBox(20);
        root.setAlignment(Pos.TOP_LEFT);

        BloodRequest request = navigation.getActiveRequest();

        // 1. Active Request Banner
        VBox requestBanner = new VBox(12);
        requestBanner.getStyleClass().add("card-panel");
        requestBanner.setStyle("-fx-border-color: #dc2626; -fx-border-width: 0 0 0 4px;");

        Label bannerTitle = new Label("Active Emergency Request Summary");
        bannerTitle.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 16px; -fx-font-weight: bold;");

        GridPane reqGrid = new GridPane();
        reqGrid.setHgap(20);
        reqGrid.setVgap(8);

        Label lblGroup = new Label("Required Group: " + (request != null ? request.getRequiredBloodGroup() : "O+"));
        lblGroup.setStyle("-fx-text-fill: #ef4444; -fx-font-weight: bold; -fx-font-size: 14px;");

        Label lblUnits = new Label("Units Required: " + (request != null ? request.getUnitsRequired() : 2));
        lblUnits.setStyle("-fx-text-fill: #e2e8f0; -fx-font-size: 14px;");

        Label lblLocation = new Label("Location: " + (request != null ? request.getLocation() : "Vijayawada"));
        lblLocation.setStyle("-fx-text-fill: #e2e8f0; -fx-font-size: 14px;");

        Label lblUrgency = new Label("Urgency: " + (request != null ? request.getUrgency() : "CRITICAL"));
        lblUrgency.setStyle("-fx-text-fill: #f87171; -fx-font-weight: bold; -fx-font-size: 14px;");

        reqGrid.add(lblGroup, 0, 0);
        reqGrid.add(lblUnits, 1, 0);
        reqGrid.add(lblLocation, 2, 0);
        reqGrid.add(lblUrgency, 3, 0);

        requestBanner.getChildren().addAll(bannerTitle, reqGrid);

        // 2. Ranked Donor Cards Container
        Label sectionTitle = new Label("Prioritized Donor Matches (Mock Ranking Engine Output)");
        sectionTitle.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 15px; -fx-font-weight: bold;");

        VBox cardsBox = new VBox(15);

        // Priority #1
        VBox card1 = createRankedDonorCard(
            "Priority #1",
            "Rahul",
            "O+",
            "2.1 km",
            "Available",
            "92%",
            "94"
        );

        // Priority #2
        VBox card2 = createRankedDonorCard(
            "Priority #2",
            "Arjun",
            "O-",
            "4.5 km",
            "Available",
            "88%",
            "87"
        );

        // Priority #3
        VBox card3 = createRankedDonorCard(
            "Priority #3",
            "Kiran",
            "O+",
            "7.2 km",
            "Available",
            "80%",
            "79"
        );

        cardsBox.getChildren().addAll(card1, card2, card3);

        ScrollPane scroll = new ScrollPane(cardsBox);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        root.getChildren().addAll(requestBanner, sectionTitle, scroll);
    }

    private VBox createRankedDonorCard(
        String rankLabel,
        String name,
        String bloodGroup,
        String distance,
        String availability,
        String responseRate,
        String priorityScore
    ) {
        VBox card = new VBox(10);
        card.getStyleClass().add("card-panel");

        HBox topRow = new HBox(15);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label lblRank = new Label(rankLabel);
        lblRank.setStyle("-fx-background-color: #dc2626; -fx-text-fill: #ffffff; -fx-padding: 4px 12px; -fx-background-radius: 6px; -fx-font-weight: bold; -fx-font-size: 12px;");

        Label lblName = new Label(name);
        lblName.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 18px; -fx-font-weight: bold;");

        Label lblGroupBadge = new Label("Group: " + bloodGroup);
        lblGroupBadge.setStyle("-fx-text-fill: #38bdf8; -fx-font-weight: bold; -fx-font-size: 14px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblScore = new Label("Priority Score: " + priorityScore);
        lblScore.setStyle("-fx-text-fill: #4ade80; -fx-font-size: 16px; -fx-font-weight: bold;");

        topRow.getChildren().addAll(lblRank, lblName, lblGroupBadge, spacer, lblScore);

        GridPane details = new GridPane();
        details.setHgap(30);
        details.setVgap(6);

        Label d1 = new Label("Distance: " + distance);
        d1.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 13px;");

        Label d2 = new Label("Availability: " + availability);
        d2.setStyle("-fx-text-fill: #4ade80; -fx-font-size: 13px; -fx-font-weight: bold;");

        Label d3 = new Label("Response History Rate: " + responseRate);
        d3.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 13px;");

        details.add(d1, 0, 0);
        details.add(d2, 1, 0);
        details.add(d3, 2, 0);

        card.getChildren().addAll(topRow, new Separator(), details);
        return card;
    }

    public Node getView() {
        return root;
    }
}
