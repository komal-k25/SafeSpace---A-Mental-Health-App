package com.mentalhealthapp.gui;
import com.mentalhealthapp.dao.*;
import com.mentalhealthapp.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HelpSeekerDashboard extends JFrame {
    private HelpSeeker helpSeeker;
    private MoodLogDAO moodLogDAO = new MoodLogDAO();
    private AppointmentDAO appointmentDAO = new AppointmentDAO();
    private CounsellorDAO counsellorDAO = new CounsellorDAO();
    private AlertDAO alertDAO = new AlertDAO();
    private JTabbedPane tabbedPane;
    
    public HelpSeekerDashboard(HelpSeeker helpSeeker) {
        this.helpSeeker = helpSeeker;
        
        setTitle("Help Seeker Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
    }
    
    private void initComponents() {
        tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("Dashboard", createDashboardPanel());
        tabbedPane.addTab("Mood Logs", createMoodLogPanel());
        tabbedPane.addTab("Appointments", createAppointmentPanel());
        tabbedPane.addTab("Alerts", createAlertPanel());
        
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
    
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + helpSeeker.getName() + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        double avgMood = moodLogDAO.getAverageMoodScore(helpSeeker.getSeid());
        int criticalCount = moodLogDAO.getCriticalMoodCount(helpSeeker.getSeid());
        List<Appointment> appointments = appointmentDAO.getAppointmentsBySeekerId(helpSeeker.getSeid());
        List<Alert> alerts = alertDAO.getAlertsBySeekerId(helpSeeker.getSeid());
        
        statsPanel.add(createStatCard("Average Mood", String.format("%.1f/10", avgMood), new Color(70, 130, 180)));
        statsPanel.add(createStatCard("Total Mood Logs", String.valueOf(moodLogDAO.getMoodLogsBySeekerId(helpSeeker.getSeid()).size()), new Color(100, 149, 237)));
        statsPanel.add(createStatCard("Appointments", String.valueOf(appointments.size()), new Color(65, 105, 225)));
        statsPanel.add(createStatCard("Critical Days", String.valueOf(criticalCount), criticalCount > 0 ? Color.RED : new Color(50, 205, 50)));
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton logMoodBtn = new JButton("Log Mood Today");
        JButton bookApptBtn = new JButton("Book Appointment");
        JButton refreshBtn = new JButton("Refresh");
        
        logMoodBtn.setFont(new Font("Arial", Font.BOLD, 14));
        bookApptBtn.setFont(new Font("Arial", Font.BOLD, 14));
        refreshBtn.setFont(new Font("Arial", Font.BOLD, 14));
        
        logMoodBtn.addActionListener(e -> showLogMoodDialog());
        bookApptBtn.addActionListener(e -> showBookAppointmentDialog());
        refreshBtn.addActionListener(e -> {
            tabbedPane.setComponentAt(0, createDashboardPanel());
        });
        
        buttonPanel.add(logMoodBtn);
        buttonPanel.add(bookApptBtn);
        buttonPanel.add(refreshBtn);
        
        panel.add(welcomeLabel, BorderLayout.NORTH);
        panel.add(statsPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(color);
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 36));
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(titleLabel);
        card.add(valueLabel);
        card.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        return card;
    }
    
    private JPanel createMoodLogPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columns = {"Date", "Time", "Score"};
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
        
        loadMoodLogs(model);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton logMoodBtn = new JButton("Log New Mood");
        JButton refreshBtn = new JButton("Refresh");
        
        logMoodBtn.addActionListener(e -> {
            showLogMoodDialog();
            loadMoodLogs(model);
        });
        refreshBtn.addActionListener(e -> loadMoodLogs(model));
        
        buttonPanel.add(logMoodBtn);
        buttonPanel.add(refreshBtn);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void loadMoodLogs(DefaultTableModel model) {
        model.setRowCount(0);
        List<MoodLog> logs = moodLogDAO.getMoodLogsBySeekerId(helpSeeker.getSeid());
        
        for (MoodLog log : logs) {
            String status = log.getScore() <= 3 ? "Critical" : (log.getScore() <= 6 ? "Moderate" : "Good");
            model.addRow(new Object[]{
                log.getDate(),
                log.getTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                log.getScore(),
                status
            });
        }
    }
    
    private void showLogMoodDialog() {
        JDialog dialog = new JDialog(this, "Log Your Mood", true);
        dialog.setSize(350, 250);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("How are you feeling today?");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel scoreLabel = new JLabel("Rate your mood (1-10):");
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JSpinner scoreSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        ((JSpinner.DefaultEditor) scoreSpinner.getEditor()).getTextField().setFont(new Font("Arial", Font.BOLD, 20));
        scoreSpinner.setMaximumSize(new Dimension(100, 40));
        scoreSpinner.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel infoLabel = new JLabel("<html><center>1-3: Critical | 4-6: Moderate | 7-10: Good</center></html>");
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton submitBtn = new JButton("Submit");
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitBtn.addActionListener(e -> {
            int score = (Integer) scoreSpinner.getValue();
            MoodLog log = new MoodLog(helpSeeker.getSeid(), score, LocalDate.now(), LocalTime.now());
            
            if (moodLogDAO.addMoodLog(log)) {
                if (score <= 3) {
                    JOptionPane.showMessageDialog(dialog,
                        "Critical mood score!",
                        "An alert has been sent to your emergency contact.",
                        JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Mood logged.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
                dialog.dispose();
                tabbedPane.setSelectedIndex(1);
            }
        });
        
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(scoreLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(scoreSpinner);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(infoLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(submitBtn);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private JPanel createAppointmentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columns = {"ID", "Date", "Time", "Counsellor", "Status", "Medicine", "Meeting Code"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        
        loadAppointments(model);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton bookBtn = new JButton("Book Appointment");
        JButton refreshBtn = new JButton("Refresh");
        
        bookBtn.addActionListener(e -> {
            showBookAppointmentDialog();
            loadAppointments(model);
        });
        refreshBtn.addActionListener(e -> loadAppointments(model));
        
        buttonPanel.add(bookBtn);
        buttonPanel.add(refreshBtn);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void loadAppointments(DefaultTableModel model) {
        model.setRowCount(0);
        List<Appointment> appointments = appointmentDAO.getAppointmentsBySeekerId(helpSeeker.getSeid());
        
        for (Appointment apt : appointments) {
            String counsellorName = "Unassigned";
            if (apt.getCid() > 0) {
                Counsellor counsellor = counsellorDAO.getCounsellorById(apt.getCid());
                if (counsellor != null) {
                    counsellorName = counsellor.getName();
                }
            }
            
            String medicine = (apt.getMedicine() == null || apt.getMedicine().isEmpty()) 
                              ? "-" : apt.getMedicine();
            String meetingCode = (apt.getMeetingCode() == null || apt.getMeetingCode().isEmpty())
                    ? "-" : apt.getMeetingCode();
            
            model.addRow(new Object[]{
                apt.getAid(),
                apt.getDate(),
                apt.getTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                counsellorName,
                apt.getStatus(),
                medicine,
                meetingCode
            });
        }
    }
    
    private void showBookAppointmentDialog() {
        JDialog dialog = new JDialog(this, "Book Appointment", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(new JLabel("Select Counsellor:"));
        List<Counsellor> counsellors = counsellorDAO.getAllCounsellors();
        JComboBox<String> counsellorCombo = new JComboBox<>();
        counsellorCombo.addItem("Any Available");
        for (Counsellor c : counsellors) {
            counsellorCombo.addItem(c.getCid() + " - " + c.getName() + " (" + c.getSpecialization() + ")");
        }
        panel.add(counsellorCombo);
        
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        JTextField dateField = new JTextField(LocalDate.now().plusDays(1).toString());
        panel.add(dateField);
        
        panel.add(new JLabel("Time (HH:MM):"));
        JTextField timeField = new JTextField("10:00");
        panel.add(timeField);
        
        JButton bookBtn = new JButton("Book");
        JButton cancelBtn = new JButton("Cancel");
        
        bookBtn.addActionListener(e -> {
            try {
                LocalDate date = LocalDate.parse(dateField.getText());
                LocalTime time = LocalTime.parse(timeField.getText());
                
                LocalDate tomorrow = LocalDate.now().plusDays(1);
                if (date.isBefore(tomorrow)) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Professional Policy: Appointments must be scheduled at least 24 hours in advance.\n" +
                        "Please select a date from " + tomorrow + " onwards.", 
                        "Scheduling Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return; 
                }
                
                int selectedIndex = counsellorCombo.getSelectedIndex();
                int cid = selectedIndex > 0 ? counsellors.get(selectedIndex - 1).getCid() : 0;
                
                Appointment apt = new Appointment(helpSeeker.getSeid(), cid, date, time, "Pending");
                
                if (appointmentDAO.bookAppointment(apt)) {
                    JOptionPane.showMessageDialog(dialog, "Appointment booked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    
                    tabbedPane.setSelectedIndex(2);

                    JPanel apptPanel = (JPanel) tabbedPane.getComponentAt(2);
                    for (Component c : apptPanel.getComponents()) {
                        if (c instanceof JScrollPane) {
                            JViewport viewport = ((JScrollPane) c).getViewport();
                            JTable apptTable = (JTable) viewport.getView();
                            loadAppointments((DefaultTableModel) apptTable.getModel());
                        }
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "Format Error: Use YYYY-MM-DD for Date (e.g., 2026-04-01)\n" +
                    "and HH:MM for Time (e.g., 14:30).", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        panel.add(bookBtn);
        panel.add(cancelBtn);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private JPanel createAlertPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columns = {"Alert ID", "Created At", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        
        loadAlerts(model);
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadAlerts(model));
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(refreshBtn);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void loadAlerts(DefaultTableModel model) {
        model.setRowCount(0);
        List<Alert> alerts = alertDAO.getAlertsBySeekerId(helpSeeker.getSeid());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Alert alert : alerts) {
            model.addRow(new Object[]{
                alert.getAlid(),
                alert.getCreatedAt().format(formatter),
                alert.getStatus()
            });
        }
    }
}
