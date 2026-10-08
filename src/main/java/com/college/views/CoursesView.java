package com.college.views;

import com.college.database.CourseDAO;
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

public class CoursesView {
    
    private Stage stage;
    private TableView<Course> table;
    private ObservableList<Course> courses;
    private TextField searchField;
    
    public CoursesView(Stage stage) {
        this.stage = stage;
        this.courses = FXCollections.observableArrayList();
    }
    
    public Scene createScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F5F7FA;");
        root.setLeft(createSidebar());
        root.setTop(createTopBar());
        root.setCenter(createMainContent());
        loadCourses();
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
        co.setStyle("-fx-background-color: #F59E0B; -fx-text-fill: white; -fx-font-size: 15px; -fx-padding: 15 25; -fx-alignment: center-left; -fx-cursor: hand; -fx-max-width: infinity;");
        lo.setStyle("-fx-background-color: transparent; -fx-text-fill: #EF4444; -fx-font-size: 15px; -fx-padding: 15 25; -fx-alignment: center-left; -fx-cursor: hand; -fx-max-width: infinity;");
        db.setOnAction(e -> goToDashboard());
        st.setOnAction(e -> goToStudents());
        dr.setOnAction(e -> goToDoctors());
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
        Label title = new Label("Courses Management");
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
        
        if (Session.isAdmin()) {
            Button add = new Button("+ Add Course");
            add.setStyle("-fx-background-color: #F59E0B; -fx-text-fill: white; -fx-padding: 12 20; -fx-background-radius: 10; -fx-cursor: hand; -fx-font-weight: bold;");
            add.setOnAction(e -> showDialog(null));
            toolbar.getChildren().add(add);
        }
        
        table = new TableView<>();
        table.setStyle("-fx-background-color: white;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<Course, String> c1 = new TableColumn<>("Code");
        c1.setCellValueFactory(new PropertyValueFactory<>("code"));
        c1.setPrefWidth(100);
        TableColumn<Course, String> c2 = new TableColumn<>("Course Name");
        c2.setCellValueFactory(new PropertyValueFactory<>("name"));
        c2.setPrefWidth(300);
        TableColumn<Course, Integer> c3 = new TableColumn<>("Credit Hours");
        c3.setCellValueFactory(new PropertyValueFactory<>("creditHours"));
        c3.setPrefWidth(120);
        TableColumn<Course, String> c4 = new TableColumn<>("Doctor");
        c4.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        c4.setPrefWidth(200);
        table.getColumns().addAll(c1, c2, c3, c4);
        
        if (Session.isAdmin()) {
            TableColumn<Course, Void> ac = new TableColumn<>("Actions");
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
                        Course c = getTableView().getItems().get(getIndex());
                        Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
                        conf.setHeaderText("Delete " + c.getCode() + "?");
                        conf.showAndWait().ifPresent(r -> {
                            if (r == ButtonType.OK) {
                                if (CourseDAO.deleteCourse(c.getId())) loadCourses();
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
        
        table.setItems(courses);
        VBox.setVgrow(table, Priority.ALWAYS);
        content.getChildren().addAll(toolbar, table);
        return content;
    }
    
    private void loadCourses() {
        courses.clear();
        for (CourseDAO.Course c : CourseDAO.getAllCourses()) {
            courses.add(new Course(c.getId(), c.getCode(), c.getName(), c.getCreditHours(), c.getDoctorName()));
        }
    }
    
    private void filter(String q) {
        if (q == null || q.isEmpty()) {
            loadCourses();
        } else {
            courses.clear();
            for (CourseDAO.Course c : CourseDAO.searchCourses(q)) {
                courses.add(new Course(c.getId(), c.getCode(), c.getName(), c.getCreditHours(), c.getDoctorName()));
            }
        }
    }
    
    private void showDialog(Course ex) {
        Stage d = new Stage();
        d.initModality(Modality.APPLICATION_MODAL);
        VBox b = new VBox(15);
        b.setPadding(new Insets(25));
        b.setStyle("-fx-background-color: white;");
        TextField n1 = new TextField(); n1.setPromptText("Course Code"); n1.setPrefHeight(42);
        TextField n2 = new TextField(); n2.setPromptText("Course Name"); n2.setPrefHeight(42);
        TextField n3 = new TextField(); n3.setPromptText("Credit Hours"); n3.setPrefHeight(42);
        TextField n4 = new TextField(); n4.setPromptText("Doctor Name"); n4.setPrefHeight(42);
        if (ex != null) {
            n1.setText(ex.getCode());
            n2.setText(ex.getName());
            n3.setText(String.valueOf(ex.getCreditHours()));
            n4.setText(ex.getDoctorName());
        }
        Button sb = new Button(ex == null ? "Add" : "Save");
        sb.setPrefHeight(45);
        sb.setMaxWidth(Double.MAX_VALUE);
        sb.setStyle("-fx-background-color: #F59E0B; -fx-text-fill: white; -fx-background-radius: 10; -fx-cursor: hand; -fx-font-weight: bold;");
        sb.setOnAction(e -> {
            try {
                int hours = Integer.parseInt(n3.getText().trim());
                if (ex == null) {
                    CourseDAO.Course nc = new CourseDAO.Course(n1.getText().trim(), n2.getText().trim(), hours, n4.getText().trim());
                    if (CourseDAO.addCourse(nc)) { loadCourses(); d.close(); }
                    else new Alert(Alert.AlertType.ERROR, "Failed to add (maybe duplicate code)").showAndWait();
                } else {
                    CourseDAO.Course uc = new CourseDAO.Course(ex.getId(), n1.getText().trim(), n2.getText().trim(), hours, n4.getText().trim());
                    if (CourseDAO.updateCourse(uc)) { loadCourses(); d.close(); }
                    else new Alert(Alert.AlertType.ERROR, "Failed to update").showAndWait();
                }
            } catch (Exception err) {
                new Alert(Alert.AlertType.WARNING, "Credit hours must be a number").showAndWait();
            }
        });
        b.getChildren().addAll(n1, n2, n3, n4, sb);
        d.setScene(new Scene(b, 400, 420));
        d.showAndWait();
    }
    
    private void goToDashboard() { try { stage.setScene(new DashboardView(stage).createScene()); } catch (Exception e) {} }
    private void goToStudents() { try { stage.setScene(new StudentsView(stage).createScene()); } catch (Exception e) {} }
    private void goToDoctors() { try { stage.setScene(new DoctorsView(stage).createScene()); } catch (Exception e) {} }
    private void logout() { Session.clear(); stage.setScene(new LoginView(stage).createScene()); stage.setResizable(false); stage.centerOnScreen(); }
    
    public static class Course {
        private final int id;
        private final SimpleStringProperty code;
        private final SimpleStringProperty name;
        private final SimpleIntegerProperty creditHours;
        private final SimpleStringProperty doctorName;
        public Course(int id, String code, String name, int creditHours, String doctorName) {
            this.id = id;
            this.code = new SimpleStringProperty(code);
            this.name = new SimpleStringProperty(name);
            this.creditHours = new SimpleIntegerProperty(creditHours);
            this.doctorName = new SimpleStringProperty(doctorName);
        }
        public Course(String code, String name, int creditHours, String doctorName) {
            this(0, code, name, creditHours, doctorName);
        }
        public int getId() { return id; }
        public String getCode() { return code.get(); }
        public void setCode(String v) { code.set(v); }
        public String getName() { return name.get(); }
        public void setName(String v) { name.set(v); }
        public int getCreditHours() { return creditHours.get(); }
        public void setCreditHours(int v) { creditHours.set(v); }
        public String getDoctorName() { return doctorName.get(); }
        public void setDoctorName(String v) { doctorName.set(v); }
    }
}