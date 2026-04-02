package com.mentalhealthapp.gui;

import com.mentalhealthapp.dao.HelpSeekerDAO;
import com.mentalhealthapp.model.HelpSeeker;

import javax.swing.*;
import java.awt.*;

public class HelpSeekerRegistrationDialog extends JDialog {
    private JTextField nameField;
    private JTextField contactField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emergencyContactField;
    private JComboBox<String> countryCodeCombo;
    private HelpSeekerDAO helpSeekerDAO = new HelpSeekerDAO();
    
    public HelpSeekerRegistrationDialog(JFrame parent) {
        super(parent, "Help Seeker Registration", true);
        setSize(400, 450);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Register as Help Seeker", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 15));
        
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
        
        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);
        
        formPanel.add(new JLabel("Confirm Password:"));
        confirmPasswordField = new JPasswordField();
        formPanel.add(confirmPasswordField);
        
        formPanel.add(new JLabel("Emergency Contact:"));
        emergencyContactField = new JTextField();
        formPanel.add(emergencyContactField);
        
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
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String emergencyContact = emergencyContactField.getText().trim();
        
        if (name.isEmpty() || contact.isEmpty() || password.isEmpty() || emergencyContact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!");
            return;
        }
        
        HelpSeeker helpSeeker = new HelpSeeker(name, contact, password, emergencyContact);
        
        if (helpSeekerDAO.registerHelpSeeker(helpSeeker)) {
            JOptionPane.showMessageDialog(this, "Registration successful!\nYou can now login.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed!\nContact number may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
