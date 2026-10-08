package com.college.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {
    
    public static class Doctor {
        private int id;
        private String name;
        private String email;
        private String specialty;
        
        public Doctor(int id, String name, String email, String specialty) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.specialty = specialty;
        }
        
        public Doctor(String name, String email, String specialty) {
            this(0, name, email, specialty);
        }
        
        public int getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getSpecialty() { return specialty; }
    }
    
    public static List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT id, name, email, specialty FROM doctors ORDER BY id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                doctors.add(new Doctor(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("specialty")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error loading doctors: " + e.getMessage());
        }
        return doctors;
    }
    
    public static boolean addDoctor(Doctor d) {
        String sql = "INSERT INTO doctors (name, email, specialty) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, d.getName());
            ps.setString(2, d.getEmail());
            ps.setString(3, d.getSpecialty());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding doctor: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean updateDoctor(Doctor d) {
        String sql = "UPDATE doctors SET name = ?, email = ?, specialty = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, d.getName());
            ps.setString(2, d.getEmail());
            ps.setString(3, d.getSpecialty());
            ps.setInt(4, d.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating doctor: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean deleteDoctor(int id) {
        String sql = "DELETE FROM doctors WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting doctor: " + e.getMessage());
            return false;
        }
    }
    
    public static List<Doctor> searchDoctors(String query) {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT id, name, email, specialty FROM doctors " +
                     "WHERE name LIKE ? OR specialty LIKE ? ORDER BY id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String pattern = "%" + query + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    doctors.add(new Doctor(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("specialty")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching doctors: " + e.getMessage());
        }
        return doctors;
    }
    
    public static int getDoctorCount() {
        String sql = "SELECT COUNT(*) FROM doctors";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error counting doctors: " + e.getMessage());
        }
        return 0;
    }
    
    public static void main(String[] args) {
        System.out.println("Testing DoctorDAO...\n");
        
        System.out.println("All doctors:");
        for (Doctor d : getAllDoctors()) {
            System.out.println("  " + d.getName() + " - " + d.getSpecialty());
        }
        
        System.out.println("\nTotal: " + getDoctorCount() + " doctors");
    }
}