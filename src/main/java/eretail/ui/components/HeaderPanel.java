
package eretail.ui.components;

import eretail.model.User;
import eretail.ui.MainFrame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class HeaderPanel extends JPanel {
    private MainFrame mainFrame;
    private User currentUser;
    private JLabel logoLabel;
    private JButton homeButton;
    private JButton collectionsButton;
    private JButton newButton;
    private JButton cartButton;
    private JButton profileButton;
    
    public HeaderPanel(MainFrame mainFrame, User currentUser) {
        this.mainFrame = mainFrame;
        this.currentUser = currentUser;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Left side navigation
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        navPanel.setOpaque(false);
        
        // Menu button (hamburger)
        JButton menuButton = new JButton("\u2630");
        menuButton.setFont(new Font("Arial", Font.PLAIN, 18));
        menuButton.setBorderPainted(false);
        menuButton.setFocusPainted(false);
        menuButton.setContentAreaFilled(false);
        menuButton.setForeground(Color.BLACK);
        
        homeButton = new JButton("Home");
        homeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        homeButton.setBorderPainted(false);
        homeButton.setFocusPainted(false);
        homeButton.setContentAreaFilled(false);
        homeButton.setForeground(Color.BLACK);
        
        collectionsButton = new JButton("Collections");
        collectionsButton.setFont(new Font("Arial", Font.PLAIN, 14));
        collectionsButton.setBorderPainted(false);
        collectionsButton.setFocusPainted(false);
        collectionsButton.setContentAreaFilled(false);
        collectionsButton.setForeground(Color.BLACK);
        
        newButton = new JButton("New");
        newButton.setFont(new Font("Arial", Font.PLAIN, 14));
        newButton.setBorderPainted(false);
        newButton.setFocusPainted(false);
        newButton.setContentAreaFilled(false);
        newButton.setForeground(Color.BLACK);
        
        navPanel.add(menuButton);
        navPanel.add(homeButton);
        navPanel.add(collectionsButton);
        navPanel.add(newButton);
        
        // Center logo panel
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setOpaque(false);
        
        // Logo with circular emblem
        JPanel logoContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        logoContainer.setOpaque(false);
        
        JPanel circleLogoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.BLACK);
                g2d.fillOval(0, 0, 30, 30);
                g2d.setColor(new Color(245, 245, 245));
                g2d.fillArc(0, 0, 30, 30, 0, 240);
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(30, 30);
            }
        };
        
        logoLabel = new JLabel("Come & Change");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 22));
        logoLabel.setForeground(Color.BLACK);
        
        logoContainer.add(circleLogoPanel);
        logoContainer.add(logoLabel);
        logoPanel.add(logoContainer);
        
        // Right side user actions
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionPanel.setOpaque(false);
        
        JButton wishlistButton = new JButton("♥");
        wishlistButton.setFont(new Font("Arial", Font.PLAIN, 18));
        wishlistButton.setBorderPainted(false);
        wishlistButton.setFocusPainted(false);
        wishlistButton.setContentAreaFilled(false);
        wishlistButton.setForeground(Color.BLACK);
        
        // Cart button with count indicator
        JPanel cartContainer = new JPanel(new BorderLayout());
        cartContainer.setOpaque(false);
        
        cartButton = new JButton("Cart");
        cartButton.setFont(new Font("Arial", Font.BOLD, 14));
        cartButton.setBorderPainted(false);
        cartButton.setFocusPainted(false);
        cartButton.setBackground(new Color(40, 40, 40));
        cartButton.setForeground(Color.WHITE);
        cartButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Create a circular label for cart items count
        JLabel cartCountLabel = new JLabel("0");
        cartCountLabel.setFont(new Font("Arial", Font.BOLD, 10));
        cartCountLabel.setForeground(Color.WHITE);
        cartCountLabel.setBackground(Color.BLACK);
        cartCountLabel.setOpaque(true);
        cartCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cartCountLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        cartCountLabel.setPreferredSize(new Dimension(20, 20));
        
        // Add a small panel for the count icon
        JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        countPanel.setOpaque(false);
        countPanel.add(cartCountLabel);
        
        cartContainer.add(cartButton, BorderLayout.CENTER);
        cartContainer.add(countPanel, BorderLayout.EAST);
        
        profileButton = new JButton("👤");
        profileButton.setFont(new Font("Arial", Font.PLAIN, 18));
        profileButton.setBorderPainted(false);
        profileButton.setFocusPainted(false);
        profileButton.setContentAreaFilled(false);
        profileButton.setForeground(Color.BLACK);
        
        actionPanel.add(wishlistButton);
        actionPanel.add(cartContainer);
        actionPanel.add(profileButton);
        
        // Add components to main panel
        add(navPanel, BorderLayout.WEST);
        add(logoPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.EAST);
        
        // Add event listeners
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showProductListPanel();
            }
        });
        
        collectionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // For now, this just shows all products
                mainFrame.showProductListPanel();
            }
        });
        
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // For now, this just shows all products
                mainFrame.showProductListPanel();
            }
        });
        
        cartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showCartPanel();
            }
        });
        
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a popup menu for user actions
                JPopupMenu popupMenu = new JPopupMenu();
                JMenuItem viewOrdersItem = new JMenuItem("My Orders");
                JMenuItem viewProfileItem = new JMenuItem("My Profile");
                JMenuItem logoutItem = new JMenuItem("Logout");
                
                // Add special menu items for admin and seller roles
                if (currentUser.getRole() == User.UserRole.ADMIN) {
                    JMenuItem manageUsersItem = new JMenuItem("Manage Users");
                    JMenuItem manageCategoriesItem = new JMenuItem("Manage Categories");
                    popupMenu.add(manageUsersItem);
                    popupMenu.add(manageCategoriesItem);
                    popupMenu.addSeparator();
                }
                
                if (currentUser.getRole() == User.UserRole.ADMIN || 
                    currentUser.getRole() == User.UserRole.SELLER) {
                    JMenuItem manageProductsItem = new JMenuItem("Manage Products");
                    JMenuItem viewSalesItem = new JMenuItem("View Sales");
                    popupMenu.add(manageProductsItem);
                    popupMenu.add(viewSalesItem);
                    popupMenu.addSeparator();
                }
                
                popupMenu.add(viewOrdersItem);
                popupMenu.add(viewProfileItem);
                popupMenu.addSeparator();
                popupMenu.add(logoutItem);
                
                viewOrdersItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mainFrame.showOrdersPanel();
                    }
                });
                
                logoutItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mainFrame.logout();
                    }
                });
                
                popupMenu.show(profileButton, 0, profileButton.getHeight());
            }
        });
    }
}
