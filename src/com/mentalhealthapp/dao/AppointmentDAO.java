package com.mentalhealthapp.dao;

import com.mentalhealthapp.model.Appointment;
import com.mentalhealthapp.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
    
    public boolean bookAppointment(Appointment appointment) {
        String sql = "INSERT INTO APPOINTMENT (SEID, CID, DATE, TIME, STATUS) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, appointment.getSeid());
            if (appointment.getCid() > 0) {
                pstmt.setInt(2, appointment.getCid());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            pstmt.setDate(3, Date.valueOf(appointment.getDate()));
            pstmt.setTime(4, Time.valueOf(appointment.getTime()));
            pstmt.setString(5, appointment.getStatus());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Appointment> getAppointmentsBySeekerId(int seid) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM APPOINTMENT WHERE SEID = ? ORDER BY DATE DESC, TIME DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, seid);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setAid(rs.getInt("AID"));
                appointment.setSeid(rs.getInt("SEID"));
                appointment.setCid(rs.getInt("CID"));
                appointment.setDate(rs.getDate("DATE").toLocalDate());
                appointment.setTime(rs.getTime("TIME").toLocalTime());
                appointment.setStatus(rs.getString("STATUS"));
                
                appointment.setMedicine(rs.getString("MEDICINE")); 
                
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }
    
    public List<Appointment> getAppointmentsByCounsellorId(int cid) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM APPOINTMENT WHERE CID = ? ORDER BY DATE DESC, TIME DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, cid);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setAid(rs.getInt("AID"));
                appointment.setSeid(rs.getInt("SEID"));
                appointment.setCid(rs.getInt("CID"));
                appointment.setDate(rs.getDate("DATE").toLocalDate());
                appointment.setTime(rs.getTime("TIME").toLocalTime());
                appointment.setStatus(rs.getString("STATUS"));
                appointment.setMedicine(rs.getString("MEDICINE"));
                appointments.add(appointment);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return appointments;
    }
    
    public boolean updateAppointmentStatus(int aid, String status, String medicine) {
        String sql = "UPDATE APPOINTMENT SET STATUS = ?, MEDICINE = ? WHERE AID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, medicine);
            ps.setInt(3, aid);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean assignCounsellor(int aid, int cid) {
        String sql = "UPDATE APPOINTMENT SET CID = ?, STATUS = 'Scheduled' WHERE AID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, cid);
            pstmt.setInt(2, aid);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Appointment> getPendingAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM APPOINTMENT WHERE STATUS = 'Pending' ORDER BY DATE, TIME";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setAid(rs.getInt("AID"));
                appointment.setSeid(rs.getInt("SEID"));
                appointment.setCid(rs.getInt("CID"));
                appointment.setDate(rs.getDate("DATE").toLocalDate());
                appointment.setTime(rs.getTime("TIME").toLocalTime());
                appointment.setStatus(rs.getString("STATUS"));
            
                appointment.setMedicine(rs.getString("MEDICINE")); 
                
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }
    
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM APPOINTMENT ORDER BY DATE DESC, TIME DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setAid(rs.getInt("AID"));
                appointment.setSeid(rs.getInt("SEID"));
                appointment.setCid(rs.getInt("CID"));
                appointment.setDate(rs.getDate("DATE").toLocalDate());
                appointment.setTime(rs.getTime("TIME").toLocalTime());
                appointment.setStatus(rs.getString("STATUS"));
                appointments.add(appointment);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return appointments;
    }
}
