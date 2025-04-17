
package eretail.ui;

import eretail.dao.UserDAO;
import eretail.model.User;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginPanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        
        // Logo panel
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(new Color(245, 245, 245));
        
        JLabel logoLabel = new JLabel("Come & Change");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 32));
        logoLabel.setForeground(new Color(40, 40, 40));
        
        // Add a circular logo marker
        JPanel circleLogoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.BLACK);
                g2d.fillOval(0, 0, 40, 40);
                g2d.setColor(new Color(245, 245, 245));
                g2d.fillArc(0, 0, 40, 40, 0, 240);
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(40, 40);
            }
        };
        
        logoPanel.add(circleLogoPanel);
        logoPanel.add(logoLabel);
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(245, 245, 245));
        formPanel.setBorder(new EmptyBorder(50, 100, 50, 100));
        
        JLabel titleLabel = new JLabel("Sign In");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel spacer = new JPanel();
        spacer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        spacer.setOpaque(false);
        
        // Email field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField = new JTextField(20);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        
        loginButton = new JButton("SIGN IN");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loginButton.setBackground(new Color(40, 40, 40));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        
        registerButton = new JButton("CREATE ACCOUNT");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        registerButton.setBackground(new Color(200, 200, 200));
        registerButton.setForeground(Color.BLACK);
        registerButton.setFocusPainted(false);
        
        // Sample credentials label
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Sample Credentials"));
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.setMaximumSize(new Dimension(300, 100));
        
        JLabel adminLabel = new JLabel("Admin: admin@example.com / admin123");
        JLabel customerLabel = new JLabel("Customer: john@example.com / password");
        JLabel sellerLabel = new JLabel("Seller: jane@example.com / password");
        
        infoPanel.add(adminLabel);
        infoPanel.add(customerLabel);
        infoPanel.add(sellerLabel);
        
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                
                if (email.trim().isEmpty() || password.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(LoginPanel.this,
                        "Please enter email and password.",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                UserDAO userDAO = UserDAO.getInstance();
                User user = userDAO.authenticateUser(email, password);
                
                if (user != null) {
                    mainFrame.setCurrentUser(user);
                    mainFrame.showProductListPanel();
                } else {
                    JOptionPane.showMessageDialog(LoginPanel.this,
                        "Invalid email or password.",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showRegisterPanel();
            }
        });
        
        // Add components to form panel
        formPanel.add(titleLabel);
        formPanel.add(spacer);
        formPanel.add(emailLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(emailField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(passwordField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        formPanel.add(loginButton);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(registerButton);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        formPanel.add(infoPanel);
        
        // Add panels to main panel
        add(logoPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
    }
}
