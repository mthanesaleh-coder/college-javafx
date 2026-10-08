package com.college.database;

import java.sql.*;

public class UserDAO {
    
    public static class User {
        private final int id;
        private final String username;
        private final String role;
        
        public User(int id, String username, String role) {
            this.id = id;
            this.username = username;
            this.role = role;
        }
        
        public int getId() { return id; }
        public String getUsername() { return username; }
        public String getRole() { return role; }
    }
    
    /**
     * التحقق من المستخدم
     * @return User إذا نجح، null إذا فشل
     */
    public static User authenticate(String username, String password) {
        String sql = "SELECT id, username, role FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("role")
                    );
                    System.out.println("Login: " + username + " (" + user.getRole() + ")");
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
        }
        return null;
    }
    
    // اختبار
    public static void main(String[] args) {
        System.out.println("Testing login...");
        
        User u1 = authenticate("admin", "admin123");
        System.out.println("admin: " + (u1 != null ? "SUCCESS - " + u1.getRole() : "FAILED"));
        
        User u2 = authenticate("doctor", "doctor123");
        System.out.println("doctor: " + (u2 != null ? "SUCCESS - " + u2.getRole() : "FAILED"));
        
        User u3 = authenticate("student", "student123");
        System.out.println("student: " + (u3 != null ? "SUCCESS - " + u3.getRole() : "FAILED"));
        
        User u4 = authenticate("wrong", "wrong");
        System.out.println("wrong: " + (u4 != null ? "SUCCESS" : "FAILED (expected)"));
    }
}