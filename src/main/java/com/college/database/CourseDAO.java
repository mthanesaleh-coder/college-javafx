package com.college.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    
    public static class Course {
        private int id;
        private String code;
        private String name;
        private int creditHours;
        private String doctorName;
        
        public Course(int id, String code, String name, int creditHours, String doctorName) {
            this.id = id;
            this.code = code;
            this.name = name;
            this.creditHours = creditHours;
            this.doctorName = doctorName;
        }
        
        public Course(String code, String name, int creditHours, String doctorName) {
            this(0, code, name, creditHours, doctorName);
        }
        
        public int getId() { return id; }
        public String getCode() { return code; }
        public String getName() { return name; }
        public int getCreditHours() { return creditHours; }
        public String getDoctorName() { return doctorName; }
    }
    
    /**
     * جلب كل المواد من قاعدة البيانات
     */
    public static List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT id, code, name, credit_hours, doctor_name FROM courses ORDER BY code";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                courses.add(new Course(
                    rs.getInt("id"),
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getInt("credit_hours"),
                    rs.getString("doctor_name")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error loading courses: " + e.getMessage());
        }
        return courses;
    }
    
    /**
     * إضافة مادة جديدة
     */
    public static boolean addCourse(Course c) {
        String sql = "INSERT INTO courses (code, name, credit_hours, doctor_name) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, c.getCode());
            ps.setString(2, c.getName());
            ps.setInt(3, c.getCreditHours());
            ps.setString(4, c.getDoctorName());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding course: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * تعديل مادة
     */
    public static boolean updateCourse(Course c) {
        String sql = "UPDATE courses SET code = ?, name = ?, credit_hours = ?, doctor_name = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, c.getCode());
            ps.setString(2, c.getName());
            ps.setInt(3, c.getCreditHours());
            ps.setString(4, c.getDoctorName());
            ps.setInt(5, c.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating course: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * حذف مادة
     */
    public static boolean deleteCourse(int id) {
        String sql = "DELETE FROM courses WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting course: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * بحث عن مواد
     */
    public static List<Course> searchCourses(String query) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT id, code, name, credit_hours, doctor_name FROM courses " +
                     "WHERE code LIKE ? OR name LIKE ? ORDER BY code";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String pattern = "%" + query + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    courses.add(new Course(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getInt("credit_hours"),
                        rs.getString("doctor_name")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching courses: " + e.getMessage());
        }
        return courses;
    }
    
    /**
     * جلب عدد المواد
     */
    public static int getCourseCount() {
        String sql = "SELECT COUNT(*) FROM courses";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error counting courses: " + e.getMessage());
        }
        return 0;
    }
    
    public static void main(String[] args) {
        System.out.println("Testing CourseDAO...\n");
        
        System.out.println("All courses:");
        for (Course c : getAllCourses()) {
            System.out.println("  " + c.getCode() + " - " + c.getName() + " (" + c.getCreditHours() + " hrs)");
        }
        
        System.out.println("\nTotal: " + getCourseCount() + " courses");
    }
}