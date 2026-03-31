package com.mentalhealthapp.dao;

import com.mentalhealthapp.model.Alert;
import com.mentalhealthapp.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlertDAO {
    
    public List<Alert> getActiveAlerts() {
        List<Alert> alerts = new ArrayList<>();
        String sql = "SELECT * FROM ALERT WHERE STATUS IN ('Active', 'Critical') ORDER BY CREATED_AT DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Alert alert = new Alert();
                alert.setAlid(rs.getInt("ALID"));
                alert.setLogId(rs.getInt("LOGID"));
                alert.setEmergencyContact(rs.getString("ECONTACT"));
                alert.setStatus(rs.getString("STATUS"));
                alert.setCreatedAt(rs.getTimestamp("CREATED_AT").toLocalDateTime());
                alerts.add(alert);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return alerts;
    }
    
    public List<Alert> getAlertsBySeekerId(int seid) {
        List<Alert> alerts = new ArrayList<>();
        String sql = "SELECT A.* FROM ALERT A " +
                     "INNER JOIN MOODLOG M ON A.LOGID = M.LOGID " +
                     "WHERE M.SEID = ? " +
                     "ORDER BY A.CREATED_AT DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, seid);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Alert alert = new Alert();
                alert.setAlid(rs.getInt("ALID"));
                alert.setLogId(rs.getInt("LOGID"));
                alert.setEmergencyContact(rs.getString("ECONTACT"));
                alert.setStatus(rs.getString("STATUS"));
                alert.setCreatedAt(rs.getTimestamp("CREATED_AT").toLocalDateTime());
                alerts.add(alert);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return alerts;
    }
    
    public boolean updateAlertStatus(int alid, String newStatus) {
        String sql = "UPDATE ALERT SET STATUS = ? WHERE ALID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, alid);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean resolveAlert(int alid) {
        return updateAlertStatus(alid, "Resolved");
    }
    
    public List<Alert> getAllAlerts() {
        List<Alert> alerts = new ArrayList<>();
        String sql = "SELECT * FROM ALERT ORDER BY CREATED_AT DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Alert alert = new Alert();
                alert.setAlid(rs.getInt("ALID"));
                alert.setLogId(rs.getInt("LOGID"));
                alert.setEmergencyContact(rs.getString("ECONTACT"));
                alert.setStatus(rs.getString("STATUS"));
                alert.setCreatedAt(rs.getTimestamp("CREATED_AT").toLocalDateTime());
                alerts.add(alert);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return alerts;
    }
}
