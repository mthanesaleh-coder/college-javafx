package com.college;

import com.college.views.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage stage) {
        LoginView loginView = new LoginView(stage);
        Scene scene = loginView.createScene();
        
        stage.setTitle("College Management System");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
