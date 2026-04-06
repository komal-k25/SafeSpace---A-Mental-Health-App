package com.mentalhealthapp.gui;

import com.mentalhealthapp.dao.AppointmentDAO;
import com.mentalhealthapp.dao.HelpSeekerDAO;
import com.mentalhealthapp.model.Appointment;
import com.mentalhealthapp.model.Counsellor;
import com.mentalhealthapp.model.HelpSeeker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CounsellorDashboard extends JFrame {
    private Counsellor counsellor;
    private AppointmentDAO appointmentDAO = new AppointmentDAO();
    private HelpSeekerDAO helpSeekerDAO = new HelpSeekerDAO();
    
    public CounsellorDashboard(Counsellor counsellor) {
        this.counsellor = counsellor;
        
        setTitle("Counsellor Dashboard - Dr. " + counsellor.getName());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
    }
    
    private void initComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("My Appointments", createAppointmentsPanel());
        tabbedPane.addTab("Update Status", createUpdateStatusPanel());
        
        add(tabbedPane);
        
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        fileMenu.add(logoutItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }
    
    private JPanel createAppointmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel welcomeLabel = new JLabel("Welcome, Dr. " + counsellor.getName(), JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        
        String[] columns = {"ID", "Date", "Time", "Help Seeker", "Status", "Medicine", "Meeting Code"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(table);
        
        loadAppointments(model);
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setFont(new Font("Arial", Font.BOLD, 14));
        refreshBtn.addActionListener(e -> loadAppointments(model));
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(refreshBtn);
        
        panel.add(welcomeLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void loadAppointments(DefaultTableModel model) {
        model.setRowCount(0);
        List<Appointment> appointments = appointmentDAO.getAppointmentsByCounsellorId(counsellor.getCid());
        
        for (Appointment apt : appointments) {
            HelpSeeker seeker = helpSeekerDAO.getHelpSeekerById(apt.getSeid());
            String seekerName = seeker != null ? "User-" + seeker.getSeid() : "Unknown";
            
            model.addRow(new Object[]{
            	    apt.getAid(),
            	    apt.getDate(),
            	    apt.getTime().format(DateTimeFormatter.ofPattern("HH:mm")),
            	    seekerName,
            	    apt.getStatus(),
            	    apt.getMedicine(),
            	    apt.getMeetingCode()
            	});
        }
    }
    
    private JPanel createUpdateStatusPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        JLabel titleLabel = new JLabel("Update Appointment Status");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 20));
        
        formPanel.add(new JLabel("Appointment ID:"));
        JTextField aidField = new JTextField();
        formPanel.add(aidField);
        
        formPanel.add(new JLabel("New Status:"));
        String[] statuses = {"Scheduled", "In Progress", "Completed", "Cancelled"};
        JComboBox<String> statusCombo = new JComboBox<>(statuses);
        formPanel.add(statusCombo);
        
        // ✅ ONLY ONE medicine field
        formPanel.add(new JLabel("Medicine:"));
        JTextField medicineField = new JTextField();
        formPanel.add(medicineField);
        
        formPanel.add(new JLabel("Meeting Code/Link:"));
        JTextField meetingCodeField = new JTextField();
        formPanel.add(meetingCodeField);
        
        JButton updateBtn = new JButton("Update Status");
        updateBtn.setFont(new Font("Arial", Font.BOLD, 14));

        updateBtn.addActionListener(e -> {
            try {
                int aid = Integer.parseInt(aidField.getText());
                String newStatus = (String) statusCombo.getSelectedItem();
                String medicine = medicineField.getText(); // 
                String meetingCode= meetingCodeField.getText();

                if (appointmentDAO.updateAppointmentStatus(aid, newStatus, medicine,meetingCode)) {
                    JOptionPane.showMessageDialog(this, "Status updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    aidField.setText("");
                    medicineField.setText("");
                    meetingCodeField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid Appointment ID!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(updateBtn);

        formPanel.setMaximumSize(new Dimension(600, 200));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(formPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(buttonPanel);

        return panel;
    }
}
