package com.mentalhealthapp.gui;

import com.mentalhealthapp.dao.CounsellorDAO;
import com.mentalhealthapp.model.Counsellor;

import javax.swing.*;
import java.awt.*;

public class CounsellorRegistrationDialog extends JDialog {
    private JTextField nameField;
    private JTextField contactField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    private JTextField specializationField;
    private JTextField licenseField;
    private JComboBox<String> countryCodeCombo;
    private CounsellorDAO counsellorDAO = new CounsellorDAO();
    
    public CounsellorRegistrationDialog(JFrame parent) {
        super(parent, "Counsellor Registration", true);
        setSize(400, 500);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Register as Counsellor", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 15));
        
        formPanel.add(new JLabel("Full Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);
        
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
        
        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);
        
        formPanel.add(new JLabel("Specialization:"));
        specializationField = new JTextField();
        formPanel.add(specializationField);
        
        formPanel.add(new JLabel("License Number:"));
        licenseField = new JTextField();
        formPanel.add(licenseField);
        
        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);
        
        formPanel.add(new JLabel("Confirm Password:"));
        confirmPasswordField = new JPasswordField();
        formPanel.add(confirmPasswordField);
        
        JPanel buttonPanel = new JPanel();
        JButton registerButton = new JButton("Register");
        JButton cancelButton = new JButton("Cancel");
        
        registerButton.addActionListener(e -> handleRegistration());
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void handleRegistration() {
        String name = nameField.getText().trim();
        String countryCode = (String) countryCodeCombo.getSelectedItem();
        String number = contactField.getText().trim();

        
        if (!number.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Contact number must be exactly 10 digits!");
            return;
        }

        String contact = countryCode + number;
        String email = emailField.getText().trim();
        String spec = specializationField.getText().trim();
        String license = licenseField.getText().trim();
        String password = new String(passwordField.getPassword());
        String passwordPattern =
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";

        if (!password.matches(passwordPattern)) {
            JOptionPane.showMessageDialog(this,
                    "Password must contain:\n" +
                    "- At least 8 characters\n" +
                    "- One uppercase letter\n" +
                    "- One lowercase letter\n" +
                    "- One digit\n" +
                    "- One special character (@#$%^&+=!)"
            );
            return;
        }
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (name.isEmpty() || contact.isEmpty() || email.isEmpty() || spec.isEmpty() || license.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!");
            return;
        }
        
        Counsellor counsellor = new Counsellor(name, contact, password, email, spec, license);
        
        if (counsellorDAO.registerCounsellor(counsellor)) {
            JOptionPane.showMessageDialog(this, "Registration successful!\nYou can now login.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed!\nContact or email may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
