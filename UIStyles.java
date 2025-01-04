package com.mycompany.ecommerceproject;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class UIStyles {
    // Colors
    public static final String PRIMARY_COLOR = "#2196F3";
    public static final String SECONDARY_COLOR = "#FFC107";
    public static final String BACKGROUND_COLOR = "#f5f5f5";
    public static final String TEXT_COLOR = "#333333";
    public static final String ERROR_COLOR = "#f44336";
    public static final String SUCCESS_COLOR = "#4CAF50";
    public static final double MIN_WIDTH = 800;
    public static final double MIN_HEIGHT = 600;
    public static final double MARGIN = 20;
    
    // Common styles
    public static final String BUTTON_STYLE = """
        -fx-background-color: %s;
        -fx-text-fill: white;
        -fx-padding: 10 20;
        -fx-min-width: 150;
        -fx-cursor: hand;
        -fx-background-radius: 5;
        -fx-font-weight: bold;
        """.formatted(PRIMARY_COLOR);

    public static final String SECONDARY_BUTTON_STYLE = """
        -fx-background-color: white;
        -fx-text-fill: %s;
        -fx-padding: 10 20;
        -fx-min-width: 150;
        -fx-cursor: hand;
        -fx-background-radius: 5;
        -fx-border-color: %s;
        -fx-border-radius: 5;
        -fx-font-weight: bold;
        """.formatted(PRIMARY_COLOR, PRIMARY_COLOR);

    public static final String TEXT_FIELD_STYLE = """
        -fx-padding: 8;
        -fx-background-radius: 5;
        -fx-min-width: 250;
        -fx-background-color: white;
        -fx-border-color: #E0E0E0;
        -fx-border-radius: 5;
        """;

    public static final String HEADER_LABEL_STYLE = """
        -fx-font-size: 24;
        -fx-font-weight: bold;
        -fx-text-fill: %s;
        """.formatted(TEXT_COLOR);

    public static final String SUBHEADER_LABEL_STYLE = """
        -fx-font-size: 18;
        -fx-font-weight: bold;
        -fx-text-fill: %s;
        """.formatted(TEXT_COLOR);

    public static final String LIST_VIEW_STYLE = """
        -fx-background-radius: 5;
        -fx-border-radius: 5;
        -fx-border-color: #E0E0E0;
        """;
        
    public static final String TABLE_VIEW_STYLE = """
        -fx-background-radius: 5;
        -fx-border-radius: 5;
        -fx-border-color: #E0E0E0;
        """;

    public static VBox createStyledVBox() {
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(MARGIN));
        vbox.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        
        // Make VBox expand to fill available space
        VBox.setVgrow(vbox, Priority.ALWAYS);
        return vbox;
    }

    public static Button createStyledButton(String text, boolean isPrimary) {
        Button button = new Button(text);
        button.setStyle(
            String.format("""
                -fx-background-color: %s;
                -fx-text-fill: %s;
                -fx-padding: 10 20;
                -fx-cursor: hand;
                """,
                isPrimary ? PRIMARY_COLOR : SECONDARY_COLOR,
                isPrimary ? "#ffffff" : "#000000"
            )
        );
        
        // Make button expand horizontally but with max width
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefWidth(Region.USE_COMPUTED_SIZE);
        
        return button;
    }

    public static TextField createStyledTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setStyle(TEXT_FIELD_STYLE);
        
        // Make text field expand horizontally
        textField.setMaxWidth(Double.MAX_VALUE);
        
        return textField;
    }

    public static Label createHeaderLabel(String text) {
        Label label = new Label(text);
        label.setStyle(HEADER_LABEL_STYLE);
        return label;
    }

    public static ListView<?> createStyledListView() {
        ListView<?> listView = new ListView<>();
        listView.setStyle(LIST_VIEW_STYLE);
        return listView;
    }
    
    public static TableView<?> createStyledTableView() {
        TableView<?> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setStyle("""
            -fx-background-color: white;
            -fx-border-color: #e0e0e0;
            -fx-border-width: 1px;
            """);
        
        // Make table expand to fill available space
        VBox.setVgrow(tableView, Priority.ALWAYS);
        tableView.setPrefHeight(Region.USE_COMPUTED_SIZE);
        
        return tableView;
    }
}