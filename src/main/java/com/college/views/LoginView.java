package com.college.views;

import com.college.utils.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginView {
    
    private Stage stage;
    private TextField usernameField;
    private PasswordField passwordField;
    private Label errorLabel;
    
    public LoginView(Stage stage) {
        this.stage = stage;
    }
    
    public Scene createScene() {
        LinearGradient gradient = new LinearGradient(
            0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#667eea")),
            new Stop(1, Color.web("#764ba2"))
        );
        BackgroundFill bgFill = new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(bgFill);
        
        StackPane root = new StackPane();
        root.setBackground(background);
        
        VBox loginBox = new VBox(18);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(50, 40, 50, 40));
        loginBox.setMaxWidth(400);
        loginBox.setMaxHeight(Region.USE_PREF_SIZE);
        loginBox.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 30, 0, 0, 10);"
        );
        
        Label logo = new Label("🎓");
        logo.setStyle("-fx-font-size: 60px;");
        
        Label title = new Label("College Management");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        title.setStyle("-fx-text-fill: #1E293B;");
        
        Label subtitle = new Label("Sign in to continue");
        subtitle.setStyle("-fx-text-fill: #64748B; -fx-font-size: 14px;");
        VBox.setMargin(subtitle, new Insets(0, 0, 15, 0));
        
        VBox usernameGroup = new VBox(8);
        usernameGroup.setAlignment(Pos.CENTER_LEFT);
        Label usernameLabel = new Label("USERNAME");
        usernameLabel.setStyle("-fx-text-fill: #475569; -fx-font-size: 13px; -fx-font-weight: bold;");
        
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setPrefHeight(45);
        usernameField.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #E2E8F0;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 10 15;" +
            "-fx-font-size: 15px;"
        );
        usernameGroup.getChildren().addAll(usernameLabel, usernameField);
        
        VBox passwordGroup = new VBox(8);
        passwordGroup.setAlignment(Pos.CENTER_LEFT);
        Label passwordLabel = new Label("PASSWORD");
        passwordLabel.setStyle("-fx-text-fill: #475569; -fx-font-size: 13px; -fx-font-weight: bold;");
        
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefHeight(45);
        passwordField.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #E2E8F0;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 10 15;" +
            "-fx-font-size: 15px;"
        );
        passwordGroup.getChildren().addAll(passwordLabel, passwordField);
        
        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #EF4444; -fx-font-size: 13px;");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
        
        Button loginButton = new Button("Sign In");
        loginButton.setPrefHeight(50);
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        loginButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #667eea, #764ba2);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;"
        );
        loginButton.setOnAction(e -> handleLogin());
        
        Label hint = new Label("Default: admin / admin123");
        hint.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 12px;");
        VBox.setMargin(hint, new Insets(15, 0, 0, 0));
        
        loginBox.getChildren().addAll(
            logo, title, subtitle,
            usernameGroup, passwordGroup,
            errorLabel, loginButton, hint
        );
        
        root.getChildren().add(loginBox);
        
        usernameField.setOnAction(e -> passwordField.requestFocus());
        passwordField.setOnAction(e -> handleLogin());
        
        return new Scene(root, 900, 700);
    }
    
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter username and password");
            return;
        }
        
        String role = validateCredentials(username, password);
        if (role != null) {
            Session.setUser(username, role);
            openDashboard();
        } else {
            showError("Invalid username or password");
            passwordField.clear();
            passwordField.requestFocus();
        }
    }
    
    private String validateCredentials(String username, String password) {
        if (username.equals("admin") && password.equals("admin123")) return "ADMIN";
        if (username.equals("doctor") && password.equals("doctor123")) return "DOCTOR";
        if (username.equals("student") && password.equals("student123")) return "STUDENT";
        return null;
    }
    
    private void showError(String message) {
        errorLabel.setText("⚠ " + message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
    
    private void openDashboard() {
        try {
            DashboardView dashboard = new DashboardView(stage);
            Scene scene = dashboard.createScene();
            stage.setScene(scene);
            stage.setResizable(true);
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to open dashboard");
        }
    }
}
