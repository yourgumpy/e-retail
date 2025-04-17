
package eretail.ui;

import eretail.dao.UserDAO;
import eretail.model.User;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RegisterPanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<User.UserRole> roleComboBox;
    private JButton registerButton;
    private JButton backButton;

    public RegisterPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Name field
        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(nameField, gbc);
        
        // Email field
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(emailField, gbc);
        
        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(passwordField, gbc);
        
        // Confirm Password field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordField = new JPasswordField(20);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(confirmPasswordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(confirmPasswordField, gbc);
        
        // Role selection
        JLabel roleLabel = new JLabel("Account Type:");
        roleComboBox = new JComboBox<>(User.UserRole.values());
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(roleLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(roleComboBox, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel();
        registerButton = new JButton("Create Account");
        backButton = new JButton("Back to Login");
        
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                User.UserRole role = (User.UserRole) roleComboBox.getSelectedItem();
                
                // Basic validation
                if (name.trim().isEmpty() || email.trim().isEmpty() || 
                    password.trim().isEmpty() || confirmPassword.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterPanel.this,
                        "Please fill in all fields.",
                        "Registration Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(RegisterPanel.this,
                        "Passwords do not match.",
                        "Registration Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Check if email already exists
                UserDAO userDAO = UserDAO.getInstance();
                if (userDAO.getUserByEmail(email) != null) {
                    JOptionPane.showMessageDialog(RegisterPanel.this,
                        "Email already registered.",
                        "Registration Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Create new user
                User newUser = new User(0, name, email, password, role);
                userDAO.addUser(newUser);
                
                JOptionPane.showMessageDialog(RegisterPanel.this,
                    "Account created successfully!",
                    "Registration Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                mainFrame.showLoginPanel();
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showLoginPanel();
            }
        });
        
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        // Add panels to main panel
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
    }
}
