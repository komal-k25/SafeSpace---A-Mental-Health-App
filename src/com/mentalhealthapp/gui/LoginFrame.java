package com.mentalhealthapp.gui;

import com.mentalhealthapp.dao.HelpSeekerDAO;
import com.mentalhealthapp.dao.CounsellorDAO;
import com.mentalhealthapp.model.HelpSeeker;
import com.mentalhealthapp.model.Counsellor;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JComboBox<String> countryCodeCombo;
    private JTextField contactField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton loginButton;
    private JButton registerButton;

    private HelpSeekerDAO helpSeekerDAO = new HelpSeekerDAO();
    private CounsellorDAO counsellorDAO = new CounsellorDAO();

    public LoginFrame() {
        setTitle("SafeSpace - A Mental Health Support Platform");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(70, 130, 180));

        JLabel titleLabel = new JLabel("Mental Health Support");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Anonymous • Safe • Professional");
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(subtitleLabel);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 15));

        formPanel.add(new JLabel("Select Role:"));
        String[] roles = {"Help Seeker", "Counsellor"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(roleComboBox);

        formPanel.add(new JLabel("Contact Number:"));
        JPanel contactPanel = new JPanel(new BorderLayout(5, 0));
        String[] countryCodes = {"+91", "+1", "+44", "+61", "+81"};
        countryCodeCombo = new JComboBox<>(countryCodes);
        countryCodeCombo.setFont(new Font("Arial", Font.PLAIN, 14));

        contactField = new JTextField();
        contactField.setFont(new Font("Arial", Font.PLAIN, 14));

        contactPanel.add(countryCodeCombo, BorderLayout.WEST);
        contactPanel.add(contactField, BorderLayout.CENTER);
        formPanel.add(contactPanel);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(passwordField);

        formPanel.add(new JLabel());
        formPanel.add(new JLabel());

        JPanel buttonPanel = new JPanel(new FlowLayout());

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.addActionListener(e -> handleLogin());

        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.addActionListener(e -> handleRegister());

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void handleLogin() {

        String contact = countryCodeCombo.getSelectedItem().toString()
                + contactField.getText().trim();

        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        if (contact.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields!",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        
        if (role.equals("Help Seeker")) {

            HelpSeeker helpSeeker = helpSeekerDAO.loginHelpSeeker(contact, password);

            if (helpSeeker != null) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                dispose();
                new HelpSeekerDashboard(helpSeeker).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
            }
        }

        
        else if (role.equals("Counsellor")) {

            Counsellor counsellor = counsellorDAO.loginCounsellor(contact, password);

            if (counsellor != null) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                dispose();
                new CounsellorDashboard(counsellor).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
            }
        }
    }

    private void handleRegister() {

        String role = (String) roleComboBox.getSelectedItem();

        if (role.equals("Help Seeker")) {
            new HelpSeekerRegistrationDialog(this).setVisible(true);
        } else if (role.equals("Counsellor")) {
            new CounsellorRegistrationDialog(this).setVisible(true);
        }
    }
}
