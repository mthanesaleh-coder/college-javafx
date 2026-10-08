package com.college.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    
    public static class Student {
        private int id;
        private String studentNumber;
        private String fullName;
        private String email;
        private String major;
        private int level;
        
        public Student(int id, String number, String name, String email, String major, int level) {
            this.id = id;
            this.studentNumber = number;
            this.fullName = name;
            this.email = email;
            this.major = major;
            this.level = level;
        }
        
        public Student(String number, String name, String email, String major, int level) {
            this(0, number, name, email, major, level);
        }
        
        public int getId() { return id; }
        public String getStudentNumber() { return studentNumber; }
        public String getFullName() { return fullName; }
        public String getEmail() { return email; }
        public String getMajor() { return major; }
        public int getLevel() { return level; }
    }
    
    /**
     * جلب كل الطلاب من قاعدة البيانات
     */
    public static List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT id, student_number, full_name, email, major, level FROM students ORDER BY student_number";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                students.add(new Student(
                    rs.getInt("id"),
                    rs.getString("student_number"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("major"),
                    rs.getInt("level")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error loading students: " + e.getMessage());
        }
        return students;
    }
    
    /**
     * إضافة طالب جديد
     */
    public static boolean addStudent(Student s) {
        String sql = "INSERT INTO students (student_number, full_name, email, major, level) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, s.getStudentNumber());
            ps.setString(2, s.getFullName());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getMajor());
            ps.setInt(5, s.getLevel());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding student: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * تعديل طالب
     */
    public static boolean updateStudent(Student s) {
        String sql = "UPDATE students SET student_number = ?, full_name = ?, email = ?, major = ?, level = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, s.getStudentNumber());
            ps.setString(2, s.getFullName());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getMajor());
            ps.setInt(5, s.getLevel());
            ps.setInt(6, s.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * حذف طالب
     */
    public static boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * بحث عن طلاب
     */
    public static List<Student> searchStudents(String query) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT id, student_number, full_name, email, major, level FROM students " +
                     "WHERE student_number LIKE ? OR full_name LIKE ? ORDER BY student_number";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String pattern = "%" + query + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("student_number"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("major"),
                        rs.getInt("level")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching students: " + e.getMessage());
        }
        return students;
    }
    
    /**
     * جلب عدد الطلاب
     */
    public static int getStudentCount() {
        String sql = "SELECT COUNT(*) FROM students";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error counting students: " + e.getMessage());
        }
        return 0;
    }
    
    public static void main(String[] args) {
        System.out.println("Testing StudentDAO...\n");
        
        System.out.println("All students:");
        for (Student s : getAllStudents()) {
            System.out.println("  " + s.getStudentNumber() + " - " + s.getFullName());
        }
        
        System.out.println("\nTotal: " + getStudentCount() + " students");
    }
}