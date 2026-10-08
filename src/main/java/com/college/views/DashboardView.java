package com.college.views;

import com.college.database.CourseDAO;
import com.college.database.DoctorDAO;
import com.college.database.StudentDAO;
import com.college.utils.Session;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class DashboardView {
    
    private Stage stage;
    private BorderPane root;
    private VBox contentArea;
    
    public DashboardView(Stage stage) {
        this.stage = stage;
    }
    
    public Scene createScene() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #F5F7FA;");
        
        root.setLeft(createSidebar());
        root.setTop(createTopBar());
        
        contentArea = new VBox(20);
        contentArea.setPadding(new Insets(30));
        contentArea.setStyle("-fx-background-color: #F5F7FA;");
        
        showHome();
        
        root.setCenter(contentArea);
        
        return new Scene(root, 1200, 700);
    }
    
    private VBox createSidebar() {
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(250);
        sidebar.setStyle("-fx-background-color: #1E293B;");
        
        Label logo = new Label("College System");
        logo.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 25 25 25 25;");
        
        Button dashboardBtn = createMenuButton("Dashboard");
        Button studentsBtn = createMenuButton("Students");
        Button doctorsBtn = createMenuButton("Doctors");
        Button coursesBtn = createMenuButton("Courses");
        Button logoutBtn = createMenuButton("Logout");
        
        dashboardBtn.setStyle("-fx-background-color: #3B82F6; -fx-text-fill: white; -fx-font-size: 15px; -fx-padding: 15 25; -fx-alignment: center-left; -fx-cursor: hand; -fx-max-width: infinity;");
        logoutBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #EF4444; -fx-font-size: 15px; -fx-padding: 15 25; -fx-alignment: center-left; -fx-cursor: hand; -fx-max-width: infinity;");
        
        dashboardBtn.setOnAction(e -> {
            DashboardView dv = new DashboardView(stage);
            stage.setScene(dv.createScene());
        });
        
        studentsBtn.setOnAction(e -> {
            try { stage.setScene(new StudentsView(stage).createScene()); } catch (Exception ex) { ex.printStackTrace(); }
        });
        
        doctorsBtn.setOnAction(e -> {
            try { stage.setScene(new DoctorsView(stage).createScene()); } catch (Exception ex) { ex.printStackTrace(); }
        });
        
        coursesBtn.setOnAction(e -> {
            try { stage.setScene(new CoursesView(stage).createScene()); } catch (Exception ex) { ex.printStackTrace(); }
        });
        
        logoutBtn.setOnAction(e -> {
            Session.clear();
            stage.setScene(new LoginView(stage).createScene());
            stage.setResizable(false);
            stage.centerOnScreen();
        });
        
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        
        sidebar.getChildren().addAll(logo, dashboardBtn, studentsBtn, doctorsBtn, coursesBtn, spacer, logoutBtn);
        return sidebar;
    }
    
    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 15px; -fx-padding: 15 25; -fx-alignment: center-left; -fx-cursor: hand; -fx-max-width: infinity;");
        return btn;
    }
    
    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(20, 30, 20, 30));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #E2E8F0; -fx-border-width: 0 0 1 0;");
        
        Label title = new Label("Dashboard");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        title.setStyle("-fx-text-fill: #1E293B;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label welcome = new Label("Welcome, " + Session.getUsername() + " (" + Session.getRole() + ")");
        welcome.setStyle("-fx-text-fill: #64748B; -fx-font-size: 14px;");
        
        topBar.getChildren().addAll(title, spacer, welcome);
        return topBar;
    }
    
    private void showHome() {
        contentArea.getChildren().clear();
        
        // ═══════ جلب الأرقام من قاعدة البيانات ═══════
        int studentCount = StudentDAO.getStudentCount();
        int doctorCount = DoctorDAO.getDoctorCount();
        int courseCount = CourseDAO.getCourseCount();
        
        // Welcome Banner
        VBox welcomeBox = new VBox(8);
        welcomeBox.setPadding(new Insets(30));
        welcomeBox.setStyle(
            "-fx-background-color: linear-gradient(to right, #667eea, #764ba2);" +
            "-fx-background-radius: 15;"
        );
        
        Label welcomeTitle = new Label("Welcome back, " + Session.getUsername() + "!");
        welcomeTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        welcomeTitle.setStyle("-fx-text-fill: white;");
        
        Label welcomeSub = new Label("Here's what's happening in your college today.");
        welcomeSub.setStyle("-fx-text-fill: white; -fx-opacity: 0.9; -fx-font-size: 14px;");
        
        welcomeBox.getChildren().addAll(welcomeTitle, welcomeSub);
        
        // ═══════ Stats Cards (من SQL) ═══════
        HBox stats = new HBox(20);
        stats.getChildren().addAll(
            createStatCard("Students", String.valueOf(studentCount), "Total registered", "#3B82F6"),
            createStatCard("Doctors", String.valueOf(doctorCount), "Active faculty", "#10B981"),
            createStatCard("Courses", String.valueOf(courseCount), "Available courses", "#F59E0B"),
            createStatCard("Departments", "8", "Across college", "#8B5CF6")
        );
        
        // Quick Actions
        Label actionsTitle = new Label("Quick Actions");
        actionsTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        actionsTitle.setStyle("-fx-text-fill: #1E293B;");
        
        HBox actions = new HBox(15);
        actions.getChildren().addAll(
            createActionCard("Add Student", "Register new student", "#3B82F6", e -> {
                try { stage.setScene(new StudentsView(stage).createScene()); } catch (Exception ex) {}
            }),
            createActionCard("Add Doctor", "Register new doctor", "#10B981", e -> {
                try { stage.setScene(new DoctorsView(stage).createScene()); } catch (Exception ex) {}
            }),
            createActionCard("Add Course", "Create new course", "#F59E0B", e -> {
                try { stage.setScene(new CoursesView(stage).createScene()); } catch (Exception ex) {}
            }),
            createActionCard("Refresh", "Update dashboard", "#8B5CF6", e -> showHome())
        );
        
        // ═══════ Two Columns ═══════
        HBox columns = new HBox(20);
        
        VBox activityBox = createActivityBox();
        VBox topStudentsBox = createTopStudentsBox();
        
        HBox.setHgrow(activityBox, Priority.ALWAYS);
        HBox.setHgrow(topStudentsBox, Priority.ALWAYS);
        
        columns.getChildren().addAll(activityBox, topStudentsBox);
        
        contentArea.getChildren().addAll(
            welcomeBox, stats,
            actionsTitle, actions,
            columns
        );
    }
    
    private VBox createStatCard(String label, String value, String change, String color) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(20));
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: " + color + ";" +
            "-fx-border-width: 0 0 0 4;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);"
        );
        HBox.setHgrow(card, Priority.ALWAYS);
        card.setMaxWidth(Double.MAX_VALUE);
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        valueLabel.setStyle("-fx-text-fill: #1E293B;");
        
        Label labelLabel = new Label(label);
        labelLabel.setStyle("-fx-text-fill: #64748B; -fx-font-size: 13px;");
        
        Label changeLabel = new Label(change);
        changeLabel.setStyle("-fx-text-fill: #10B981; -fx-font-size: 11px;");
        
        card.getChildren().addAll(valueLabel, labelLabel, changeLabel);
        return card;
    }
    
    private VBox createActionCard(String title, String subtitle, String color, 
                                   EventHandler<ActionEvent> action) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(20));
        card.setPrefWidth(260);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: #E2E8F0;" +
            "-fx-border-width: 1;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);"
        );
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        titleLabel.setStyle("-fx-text-fill: " + color + ";");
        
        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setStyle("-fx-text-fill: #64748B; -fx-font-size: 12px;");
        
        card.getChildren().addAll(titleLabel, subtitleLabel);
        
        card.setOnMouseEntered(e -> card.setStyle(
            "-fx-background-color: #F8FAFC;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: " + color + ";" +
            "-fx-border-width: 2;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 4);"
        ));
        
        card.setOnMouseExited(e -> card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: #E2E8F0;" +
            "-fx-border-width: 1;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);"
        ));
        
        card.setOnMouseClicked(e -> action.handle(new ActionEvent()));
        
        return card;
    }
    
    private VBox createActivityBox() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(25));
        box.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);"
        );
        
        Label title = new Label("Recent Activity");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setStyle("-fx-text-fill: #1E293B;");
        
        VBox activities = new VBox(12);
        activities.getChildren().addAll(
            createActivityItem("Student logged in", Session.getUsername(), "now", "#3B82F6"),
            createActivityItem("Total students", StudentDAO.getStudentCount() + " registered", "-", "#10B981"),
            createActivityItem("Total doctors", DoctorDAO.getDoctorCount() + " active", "-", "#F59E0B"),
            createActivityItem("Total courses", CourseDAO.getCourseCount() + " available", "-", "#8B5CF6")
        );
        
        box.getChildren().addAll(title, activities);
        return box;
    }
    
    private HBox createActivityItem(String action, String who, String time, String color) {
        HBox item = new HBox(12);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10));
        item.setStyle("-fx-background-color: #F8FAFC; -fx-background-radius: 8;");
        
        Region dot = new Region();
        dot.setPrefSize(10, 10);
        dot.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 50;");
        
        VBox info = new VBox(2);
        Label actionLabel = new Label(action);
        actionLabel.setStyle("-fx-text-fill: #1E293B; -fx-font-size: 13px; -fx-font-weight: bold;");
        Label whoLabel = new Label(who);
        whoLabel.setStyle("-fx-text-fill: #64748B; -fx-font-size: 11px;");
        info.getChildren().addAll(actionLabel, whoLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label timeLabel = new Label(time);
        timeLabel.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 11px;");
        
        item.getChildren().addAll(dot, info, spacer, timeLabel);
        return item;
    }
    
    private VBox createTopStudentsBox() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(25));
        box.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);"
        );
        
        Label title = new Label("Recent Students");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setStyle("-fx-text-fill: #1E293B;");
        
        VBox students = new VBox(12);
        
        // ═══════ جلب الطلاب من SQL ═══════
        int rank = 1;
        for (StudentDAO.Student s : StudentDAO.getAllStudents()) {
            if (rank > 5) break;
            
            String color;
            if (rank == 1) color = "#F59E0B";
            else if (rank == 2) color = "#94A3B8";
            else if (rank == 3) color = "#B45309";
            else color = "#64748B";
            
            students.getChildren().add(
                createTopStudentItem(String.valueOf(rank), s.getFullName(), s.getMajor(), 
                                     "Lvl " + s.getLevel(), color)
            );
            rank++;
        }
        
        // إذا ما فيه طلاب
        if (students.getChildren().isEmpty()) {
            Label noData = new Label("No students yet");
            noData.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 12px;");
            students.getChildren().add(noData);
        }
        
        box.getChildren().addAll(title, students);
        return box;
    }
    
    private HBox createTopStudentItem(String rank, String name, String major, String gpa, String color) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10));
        item.setStyle("-fx-background-color: #F8FAFC; -fx-background-radius: 8;");
        
        Label rankLabel = new Label(rank);
        rankLabel.setPrefSize(32, 32);
        rankLabel.setAlignment(Pos.CENTER);
        rankLabel.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 14px;" +
            "-fx-background-radius: 50;"
        );
        
        VBox info = new VBox(2);
        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-text-fill: #1E293B; -fx-font-size: 13px; -fx-font-weight: bold;");
        Label majorLabel = new Label(major);
        majorLabel.setStyle("-fx-text-fill: #64748B; -fx-font-size: 11px;");
        info.getChildren().addAll(nameLabel, majorLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label gpaLabel = new Label(gpa);
        gpaLabel.setStyle("-fx-text-fill: #10B981; -fx-font-weight: bold; -fx-font-size: 12px;");
        
        item.getChildren().addAll(rankLabel, info, spacer, gpaLabel);
        return item;
    }
}