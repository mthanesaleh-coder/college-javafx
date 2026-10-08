package com.college.views;

import com.college.database.StudentDAO;
import com.college.utils.Session;
import javafx.beans.property.SimpleIntegerProperty;
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

public class StudentsView {
    
    private Stage stage;
    private TableView<Student> table;
    private ObservableList<Student> students;
    private TextField searchField;
    
    public StudentsView(Stage stage) {
        this.stage = stage;
        this.students = FXCollections.observableArrayList();
    }
    
    public Scene createScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F5F7FA;");
        root.setLeft(createSidebar());
        root.setTop(createTopBar());
        root.setCenter(createMainContent());
        loadStudents();
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
        st.setStyle("-fx-background-color: #3B82F6; -fx-text-fill: white; -fx-font-size: 15px; -fx-padding: 15 25; -fx-alignment: center-left; -fx-cursor: hand; -fx-max-width: infinity;");
        lo.setStyle("-fx-background-color: transparent; -fx-text-fill: #EF4444; -fx-font-size: 15px; -fx-padding: 15 25; -fx-alignment: center-left; -fx-cursor: hand; -fx-max-width: infinity;");
        db.setOnAction(e -> goToDashboard());
        dr.setOnAction(e -> goToDoctors());
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
        Label title = new Label("Students Management");
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
        searchField.textProperty().addListener((o, a, b) -> filterStudents(b));
        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);
        toolbar.getChildren().addAll(searchField, sp);
        
        if (Session.isAdmin()) {
            Button add = new Button("+ Add Student");
            add.setStyle("-fx-background-color: #3B82F6; -fx-text-fill: white; -fx-padding: 12 20; -fx-background-radius: 10; -fx-cursor: hand; -fx-font-weight: bold;");
            add.setOnAction(e -> showDialog(null));
            toolbar.getChildren().add(add);
        }
        
        table = new TableView<>();
        table.setStyle("-fx-background-color: white;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<Student, String> c1 = new TableColumn<>("Student No.");
        c1.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
        c1.setPrefWidth(120);
        TableColumn<Student, String> c2 = new TableColumn<>("Name");
        c2.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        c2.setPrefWidth(180);
        TableColumn<Student, String> c3 = new TableColumn<>("Email");
        c3.setCellValueFactory(new PropertyValueFactory<>("email"));
        c3.setPrefWidth(220);
        TableColumn<Student, String> c4 = new TableColumn<>("Major");
        c4.setCellValueFactory(new PropertyValueFactory<>("major"));
        c4.setPrefWidth(180);
        TableColumn<Student, Integer> c5 = new TableColumn<>("Level");
        c5.setCellValueFactory(new PropertyValueFactory<>("level"));
        c5.setPrefWidth(80);
        table.getColumns().addAll(c1, c2, c3, c4, c5);
        
        if (Session.isAdmin()) {
            TableColumn<Student, Void> ac = new TableColumn<>("Actions");
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
                        Student s = getTableView().getItems().get(getIndex());
                        Alert c = new Alert(Alert.AlertType.CONFIRMATION);
                        c.setHeaderText("Delete " + s.getFullName() + "?");
                        c.showAndWait().ifPresent(r -> {
                            if (r == ButtonType.OK) {
                                if (StudentDAO.deleteStudent(s.getId())) loadStudents();
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
        
        table.setItems(students);
        VBox.setVgrow(table, Priority.ALWAYS);
        content.getChildren().addAll(toolbar, table);
        return content;
    }
    
    private void loadStudents() {
        students.clear();
        for (StudentDAO.Student s : StudentDAO.getAllStudents()) {
            students.add(new Student(s.getId(), s.getStudentNumber(), s.getFullName(), 
                                     s.getEmail(), s.getMajor(), s.getLevel()));
        }
    }
    
    private void filterStudents(String q) {
        if (q == null || q.isEmpty()) {
            loadStudents();
        } else {
            students.clear();
            for (StudentDAO.Student s : StudentDAO.searchStudents(q)) {
                students.add(new Student(s.getId(), s.getStudentNumber(), s.getFullName(),
                                         s.getEmail(), s.getMajor(), s.getLevel()));
            }
        }
    }
    
    private void showDialog(Student ex) {
        Stage d = new Stage();
        d.initModality(Modality.APPLICATION_MODAL);
        VBox b = new VBox(15);
        b.setPadding(new Insets(25));
        b.setStyle("-fx-background-color: white;");
        TextField n1 = new TextField(); n1.setPromptText("Number"); n1.setPrefHeight(42);
        TextField n2 = new TextField(); n2.setPromptText("Name"); n2.setPrefHeight(42);
        TextField n3 = new TextField(); n3.setPromptText("Email"); n3.setPrefHeight(42);
        TextField n4 = new TextField(); n4.setPromptText("Major"); n4.setPrefHeight(42);
        TextField n5 = new TextField(); n5.setPromptText("Level"); n5.setPrefHeight(42);
        if (ex != null) {
            n1.setText(ex.getStudentNumber()); n2.setText(ex.getFullName());
            n3.setText(ex.getEmail()); n4.setText(ex.getMajor());
            n5.setText(String.valueOf(ex.getLevel()));
        }
        Button sb = new Button(ex == null ? "Add" : "Save");
        sb.setPrefHeight(45);
        sb.setMaxWidth(Double.MAX_VALUE);
        sb.setStyle("-fx-background-color: #3B82F6; -fx-text-fill: white; -fx-background-radius: 10; -fx-cursor: hand; -fx-font-weight: bold;");
        sb.setOnAction(e -> {
            try {
                int lv = Integer.parseInt(n5.getText());
                if (ex == null) {
                    StudentDAO.Student ns = new StudentDAO.Student(n1.getText(), n2.getText(), n3.getText(), n4.getText(), lv);
                    if (StudentDAO.addStudent(ns)) { loadStudents(); d.close(); }
                    else new Alert(Alert.AlertType.ERROR, "Failed to add").showAndWait();
                } else {
                    StudentDAO.Student us = new StudentDAO.Student(ex.getId(), n1.getText(), n2.getText(), n3.getText(), n4.getText(), lv);
                    if (StudentDAO.updateStudent(us)) { loadStudents(); d.close(); }
                    else new Alert(Alert.AlertType.ERROR, "Failed to update").showAndWait();
                }
            } catch (Exception err) {
                new Alert(Alert.AlertType.WARNING, "Invalid level").showAndWait();
            }
        });
        b.getChildren().addAll(n1, n2, n3, n4, n5, sb);
        d.setScene(new Scene(b, 400, 500));
        d.showAndWait();
    }
    
    private void goToDashboard() { try { stage.setScene(new DashboardView(stage).createScene()); } catch (Exception e) {} }
    private void goToDoctors() { try { stage.setScene(new DoctorsView(stage).createScene()); } catch (Exception e) {} }
    private void goToCourses() { try { stage.setScene(new CoursesView(stage).createScene()); } catch (Exception e) {} }
    private void logout() { Session.clear(); stage.setScene(new LoginView(stage).createScene()); stage.setResizable(false); stage.centerOnScreen(); }
    
    public static class Student {
        private final int id;
        private final SimpleStringProperty studentNumber;
        private final SimpleStringProperty fullName;
        private final SimpleStringProperty email;
        private final SimpleStringProperty major;
        private final SimpleIntegerProperty level;
        public Student(int id, String number, String name, String email, String major, int level) {
            this.id = id;
            this.studentNumber = new SimpleStringProperty(number);
            this.fullName = new SimpleStringProperty(name);
            this.email = new SimpleStringProperty(email);
            this.major = new SimpleStringProperty(major);
            this.level = new SimpleIntegerProperty(level);
        }
        public int getId() { return id; }
        public String getStudentNumber() { return studentNumber.get(); }
        public void setStudentNumber(String v) { studentNumber.set(v); }
        public String getFullName() { return fullName.get(); }
        public void setFullName(String v) { fullName.set(v); }
        public String getEmail() { return email.get(); }
        public void setEmail(String v) { email.set(v); }
        public String getMajor() { return major.get(); }
        public void setMajor(String v) { major.set(v); }
        public int getLevel() { return level.get(); }
        public void setLevel(int v) { level.set(v); }
    }
}