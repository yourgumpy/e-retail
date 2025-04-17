
package eretail.ui.components;

import eretail.model.Product;
import eretail.model.User;
import eretail.ui.MainFrame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ProductCard extends JPanel {
    private MainFrame mainFrame;
    private User currentUser;
    private Product product;

    public ProductCard(MainFrame mainFrame, User currentUser, Product product) {
        this.mainFrame = mainFrame;
        this.currentUser = currentUser;
        this.product = product;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240)));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Product image panel (placeholder)
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                // Fill background
                g2d.setColor(new Color(245, 245, 245));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw a placeholder rectangle
                g2d.setColor(new Color(230, 230, 230));
                g2d.fillRect(20, 20, getWidth() - 40, getHeight() - 40);
                
                // Draw product name as a placeholder
                g2d.setColor(Color.GRAY);
                g2d.setFont(new Font("Arial", Font.BOLD, 14));
                String name = product.getName();
                
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(name);
                int textHeight = fm.getHeight();
                
                g2d.drawString(name, (getWidth() - textWidth) / 2, 
                               (getHeight() - textHeight) / 2 + fm.getAscent());
            }
        };
        
        imagePanel.setPreferredSize(new Dimension(0, 300));
        
        // Product info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(10, 15, 15, 15));
        
        // Category and stock info
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        categoryPanel.setBackground(Color.WHITE);
        
        JLabel categoryLabel = new JLabel(
            product.getCategory() != null ? product.getCategory().getName() : "Uncategorized"
        );
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        categoryLabel.setForeground(Color.GRAY);
        
        JLabel stockLabel = new JLabel(" • " + product.getStockQuantity());
        stockLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        stockLabel.setForeground(Color.GRAY);
        
        categoryPanel.add(categoryLabel);
        categoryPanel.add(stockLabel);
        categoryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Product name
        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Price panel
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        pricePanel.setBackground(Color.WHITE);
        
        JLabel priceLabel = new JLabel(String.format("$%.0f", product.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        pricePanel.add(priceLabel);
        pricePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        infoPanel.add(categoryPanel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(pricePanel);
        
        add(imagePanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);
        
        // Add click listener for the entire card
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.showProductDetailPanel(product.getProductID());
            }
        });
    }
}
