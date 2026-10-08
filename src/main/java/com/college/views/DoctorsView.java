package com.college.views;

import com.college.database.DoctorDAO;
import com.college.utils.Session;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DoctorsView {
    
    private Stage stage;
    private TableView<Doctor> table;
    private ObservableList<Doctor> doctors;
    private TextField searchField;
    
    public DoctorsView(Stage stage) {
        this.stage = stage;
        this.doctors = FXCollections.observableArrayList();
    }
    
    public Scene createScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F5F7FA;");
        root.setLeft(createSidebar());
        root.setTop(createTopBar());
        root.setCenter(createMainContent());
        loadDoctors();
        return new Scene(root, 1200, 700);
    }
    
    private VBox createSidebar() {
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(250);
        sidebar.setStyle("-fx-background-color: #1E293B;");
        Label logo = new Label("College System");
        logo.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 25;");
        Button db = createBtn("Dashboard");
        Button st = createBtn("Students");
        Button dr = createBtn("Doctors");
        Button co = createBtn("Courses");
        Button lo = createBtn("Logout");
        dr.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-font-size: 15px; -fx-padding: 15 25; -fx-alignment: center-left; -fx-cursor: hand; -fx-max-width: infinity;");
        lo.setStyle("-fx-background-color: transparent; -fx-text-fill: #EF4444; -fx-font-size: 15px; -fx-padding: 15 25; -fx-alignment: center-left; -fx-cursor: hand; -fx-max-width: infinity;");
        db.setOnAction(e -> goToDashboard());
        st.setOnAction(e -> goToStudents());
        co.setOnAction(e -> goToCourses());
        lo.setOnAction(e -> logout());
        Region sp = new Region();
        VBox.setVgrow(sp, Priority.ALWAYS);
        sidebar.getChildren().addAll(logo, db, st, dr, co, sp, lo);
        return sidebar;
    }
    
    private Button createBtn(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 15px; -fx-padding: 15 25; -fx-alignment: center-left; -fx-cursor: hand; -fx-max-width: infinity;");
        return b;
    }
    
    private HBox createTopBar() {
        HBox t = new HBox();
        t.setPadding(new Insets(20, 30, 20, 30));
        t.setAlignment(Pos.CENTER_LEFT);
        t.setStyle("-fx-background-color: white; -fx-border-color: #E2E8F0; -fx-border-width: 0 0 1 0;");
        Label title = new Label("Doctors Management");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);
        Label w = new Label("Welcome, " + Session.getUsername() + " (" + Session.getRole() + ")");
        w.setStyle("-fx-text-fill: #64748B; -fx-font-size: 14px;");
        t.getChildren().addAll(title, sp, w);
        return t;
    }
    
    private VBox createMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        HBox toolbar = new HBox(12);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.setPrefHeight(45);
        searchField.setPrefWidth(400);
        searchField.setStyle("-fx-background-color: white; -fx-border-color: #E2E8F0; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 10 15;");
        searchField.textProperty().addListener((o, a, b) -> filter(b));
        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);
        toolbar.getChildren().addAll(searchField, sp);
        
        if (Session.isAdmin() || Session.isDoctor()) {
            Button add = new Button("+ Add Doctor");
            add.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-padding: 12 20; -fx-background-radius: 10; -fx-cursor: hand; -fx-font-weight: bold;");
            add.setOnAction(e -> showDialog(null));
            toolbar.getChildren().add(add);
        }
        
        table = new TableView<>();
        table.setStyle("-fx-background-color: white;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<Doctor, String> c1 = new TableColumn<>("Name");
        c1.setCellValueFactory(new PropertyValueFactory<>("name"));
        c1.setPrefWidth(250);
        TableColumn<Doctor, String> c2 = new TableColumn<>("Email");
        c2.setCellValueFactory(new PropertyValueFactory<>("email"));
        c2.setPrefWidth(250);
        TableColumn<Doctor, String> c3 = new TableColumn<>("Specialty");
        c3.setCellValueFactory(new PropertyValueFactory<>("specialty"));
        c3.setPrefWidth(250);
        table.getColumns().addAll(c1, c2, c3);
        
        if (Session.isAdmin()) {
            TableColumn<Doctor, Void> ac = new TableColumn<>("Actions");
            ac.setPrefWidth(140);
            ac.setCellFactory(col -> new TableCell<>() {
                private final Button eb = new Button("Edit");
                private final Button db = new Button("Del");
                private final HBox hb = new HBox(8, eb, db);
                {
                    eb.setStyle("-fx-background-color: #FEF3C7; -fx-text-fill: #92400E; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 6 10;");
                    db.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 6 10;");
                    eb.setOnAction(e -> showDialog(getTableView().getItems().get(getIndex())));
                    db.setOnAction(e -> {
                        Doctor d = getTableView().getItems().get(getIndex());
                        Alert c = new Alert(Alert.AlertType.CONFIRMATION);
                        c.setHeaderText("Delete " + d.getName() + "?");
                        c.showAndWait().ifPresent(r -> {
                            if (r == ButtonType.OK) {
                                if (DoctorDAO.deleteDoctor(d.getId())) loadDoctors();
                            }
                        });
                    });
                }
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : hb);
                }
            });
            table.getColumns().add(ac);
        }
        
        table.setItems(doctors);
        VBox.setVgrow(table, Priority.ALWAYS);
        content.getChildren().addAll(toolbar, table);
        return content;
    }
    
    private void loadDoctors() {
        doctors.clear();
        for (DoctorDAO.Doctor d : DoctorDAO.getAllDoctors()) {
            doctors.add(new Doctor(d.getId(), d.getName(), d.getEmail(), d.getSpecialty()));
        }
    }
    
    private void filter(String q) {
        if (q == null || q.isEmpty()) {
            loadDoctors();
        } else {
            doctors.clear();
            for (DoctorDAO.Doctor d : DoctorDAO.searchDoctors(q)) {
                doctors.add(new Doctor(d.getId(), d.getName(), d.getEmail(), d.getSpecialty()));
            }
        }
    }
    
    private void showDialog(Doctor ex) {
        Stage d = new Stage();
        d.initModality(Modality.APPLICATION_MODAL);
        VBox b = new VBox(15);
        b.setPadding(new Insets(25));
        b.setStyle("-fx-background-color: white;");
        TextField n1 = new TextField(); n1.setPromptText("Name"); n1.setPrefHeight(42);
        TextField n2 = new TextField(); n2.setPromptText("Email"); n2.setPrefHeight(42);
        TextField n3 = new TextField(); n3.setPromptText("Specialty"); n3.setPrefHeight(42);
        if (ex != null) {
            n1.setText(ex.getName());
            n2.setText(ex.getEmail());
            n3.setText(ex.getSpecialty());
        }
        if (Session.isDoctor() && ex == null) {
            n1.setText(Session.getUsername());
            n1.setEditable(false);
            n1.setStyle("-fx-background-color: #F1F5F9; -fx-border-color: #E2E8F0; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10 15;");
        }
        Button sb = new Button(ex == null ? "Add" : "Save");
        sb.setPrefHeight(45);
        sb.setMaxWidth(Double.MAX_VALUE);
        sb.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-background-radius: 10; -fx-cursor: hand; -fx-font-weight: bold;");
        sb.setOnAction(e -> {
            if (n1.getText().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please enter name").showAndWait();
                return;
            }
            if (ex == null) {
                DoctorDAO.Doctor nd = new DoctorDAO.Doctor(n1.getText().trim(), n2.getText().trim(), n3.getText().trim());
                if (DoctorDAO.addDoctor(nd)) { loadDoctors(); d.close(); }
                else new Alert(Alert.AlertType.ERROR, "Failed to add").showAndWait();
            } else {
                DoctorDAO.Doctor ud = new DoctorDAO.Doctor(ex.getId(), n1.getText().trim(), n2.getText().trim(), n3.getText().trim());
                if (DoctorDAO.updateDoctor(ud)) { loadDoctors(); d.close(); }
                else new Alert(Alert.AlertType.ERROR, "Failed to update").showAndWait();
            }
        });
        b.getChildren().addAll(n1, n2, n3, sb);
        d.setScene(new Scene(b, 400, 350));
        d.showAndWait();
    }
    
    private void goToDashboard() { try { stage.setScene(new DashboardView(stage).createScene()); } catch (Exception e) {} }
    private void goToStudents() { try { stage.setScene(new StudentsView(stage).createScene()); } catch (Exception e) {} }
    private void goToCourses() { try { stage.setScene(new CoursesView(stage).createScene()); } catch (Exception e) {} }
    private void logout() { Session.clear(); stage.setScene(new LoginView(stage).createScene()); stage.setResizable(false); stage.centerOnScreen(); }
    
    public static class Doctor {
        private final int id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty email;
        private final SimpleStringProperty specialty;
        public Doctor(int id, String name, String email, String specialty) {
            this.id = id;
            this.name = new SimpleStringProperty(name);
            this.email = new SimpleStringProperty(email);
            this.specialty = new SimpleStringProperty(specialty);
        }
        public Doctor(String name, String email, String specialty) {
            this(0, name, email, specialty);
        }
        public int getId() { return id; }
        public String getName() { return name.get(); }
        public void setName(String v) { name.set(v); }
        public String getEmail() { return email.get(); }
        public void setEmail(String v) { email.set(v); }
        public String getSpecialty() { return specialty.get(); }
        public void setSpecialty(String v) { specialty.set(v); }
    }
}