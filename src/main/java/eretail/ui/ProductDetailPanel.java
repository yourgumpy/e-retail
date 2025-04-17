
package eretail.ui;

import eretail.dao.ProductDAO;
import eretail.dao.ShoppingCartDAO;
import eretail.model.Product;
import eretail.model.User;
import eretail.ui.components.HeaderPanel;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ProductDetailPanel extends JPanel {
    private MainFrame mainFrame;
    private User currentUser;
    private Product product;
    private JLabel productNameLabel;
    private JLabel priceLabel;
    private JLabel descriptionLabel;
    private JComboBox<String> sizeComboBox;
    private JComboBox<String> colorComboBox;
    private JSpinner quantitySpinner;
    private JButton addToCartButton;
    private JPanel thumbnailsPanel;
    private JPanel mainImagePanel;

    public ProductDetailPanel(MainFrame mainFrame, User currentUser, int productId) {
        this.mainFrame = mainFrame;
        this.currentUser = currentUser;
        this.product = ProductDAO.getInstance().getProductById(productId);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        
        // Add header
        HeaderPanel headerPanel = new HeaderPanel(mainFrame, currentUser);
        add(headerPanel, BorderLayout.NORTH);
        
        if (product == null) {
            JLabel errorLabel = new JLabel("Product not found");
            errorLabel.setFont(new Font("Arial", Font.BOLD, 18));
            errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(errorLabel, BorderLayout.CENTER);
            return;
        }
        
        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout(30, 0));
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.setBorder(new EmptyBorder(20, 30, 30, 30));
        
        // Left side - product images
        JPanel imagesPanel = new JPanel(new BorderLayout(0, 15));
        imagesPanel.setBackground(new Color(245, 245, 245));
        
        // Main product image
        mainImagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                // Fill background
                g2d.setColor(new Color(245, 245, 245));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw a placeholder rectangle
                g2d.setColor(new Color(230, 230, 230));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw product name as a placeholder
                g2d.setColor(Color.GRAY);
                g2d.setFont(new Font("Arial", Font.BOLD, 18));
                String name = product.getName();
                
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(name);
                int textHeight = fm.getHeight();
                
                g2d.drawString(name, (getWidth() - textWidth) / 2, 
                               (getHeight() - textHeight) / 2 + fm.getAscent());
            }
        };
        
        mainImagePanel.setPreferredSize(new Dimension(400, 500));
        
        // Thumbnail images
        thumbnailsPanel = new JPanel(new GridLayout(1, 6, 10, 0));
        thumbnailsPanel.setBackground(new Color(245, 245, 245));
        
        // Create 6 thumbnail placeholders
        for (int i = 0; i < 6; i++) {
            JPanel thumbnail = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    
                    // Fill background
                    g2d.setColor(new Color(220, 220, 220));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            
            thumbnail.setPreferredSize(new Dimension(60, 60));
            thumbnail.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            thumbnailsPanel.add(thumbnail);
            
            // Add click listener for thumbnails
            final int index = i;
            thumbnail.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Select this thumbnail (add a border)
                    for (Component comp : thumbnailsPanel.getComponents()) {
                        comp.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                    }
                    thumbnail.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                    
                    // Update main image (in a real app, would load different image)
                    mainImagePanel.repaint();
                }
            });
        }
        
        imagesPanel.add(mainImagePanel, BorderLayout.CENTER);
        imagesPanel.add(thumbnailsPanel, BorderLayout.SOUTH);
        
        // Right side - product details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Product name
        productNameLabel = new JLabel(product.getName().toUpperCase());
        productNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        productNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Price
        priceLabel = new JLabel(String.format("$%.0f", product.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Tax info
        JLabel taxLabel = new JLabel("MRP incl. of all taxes");
        taxLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        taxLabel.setForeground(Color.GRAY);
        taxLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Description
        descriptionLabel = new JLabel("<html>" + product.getDescription() + "</html>");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Color selection
        JLabel colorLabel = new JLabel("Color");
        colorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        colorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Color swatches
        JPanel colorSwatchesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        colorSwatchesPanel.setBackground(Color.WHITE);
        colorSwatchesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create color swatches
        Color[] colors = {
            Color.LIGHT_GRAY, 
            new Color(150, 150, 150), 
            Color.BLACK, 
            new Color(100, 200, 180), 
            Color.WHITE, 
            new Color(180, 200, 250)
        };
        
        for (Color color : colors) {
            JPanel swatch = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    
                    // Fill with color
                    g2d.setColor(color);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    
                    // Add border for white color
                    if (color.equals(Color.WHITE)) {
                        g2d.setColor(Color.LIGHT_GRAY);
                        g2d.drawRect(0, 0, getWidth()-1, getHeight()-1);
                    }
                }
            };
            
            swatch.setPreferredSize(new Dimension(30, 30));
            swatch.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            colorSwatchesPanel.add(swatch);
            
            // Add click listener for swatches
            swatch.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Select this color (add a border)
                    for (Component comp : colorSwatchesPanel.getComponents()) {
                        comp.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
                    }
                    swatch.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                }
            });
        }
        
        // Size selection
        JLabel sizeLabel = new JLabel("Size");
        sizeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        sizeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Size buttons
        JPanel sizeButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        sizeButtonsPanel.setBackground(Color.WHITE);
        sizeButtonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String[] sizes = {"XS", "S", "M", "L", "XL", "2X"};
        ButtonGroup sizeGroup = new ButtonGroup();
        
        for (String size : sizes) {
            JToggleButton sizeBtn = new JToggleButton(size);
            sizeBtn.setFont(new Font("Arial", Font.PLAIN, 12));
            sizeBtn.setFocusPainted(false);
            sizeBtn.setPreferredSize(new Dimension(35, 35));
            
            sizeGroup.add(sizeBtn);
            sizeButtonsPanel.add(sizeBtn);
        }
        
        // Size guide link
        JPanel sizeGuidePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        sizeGuidePanel.setBackground(Color.WHITE);
        sizeGuidePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel findSizeLabel = new JLabel("FIND YOUR SIZE | ");
        findSizeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JLabel measurementGuideLabel = new JLabel("MEASUREMENT GUIDE");
        measurementGuideLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        measurementGuideLabel.setForeground(Color.BLUE);
        
        sizeGuidePanel.add(findSizeLabel);
        sizeGuidePanel.add(measurementGuideLabel);
        
        // Add to cart button
        addToCartButton = new JButton("ADD");
        addToCartButton.setFont(new Font("Arial", Font.BOLD, 14));
        addToCartButton.setBackground(new Color(220, 220, 220));
        addToCartButton.setForeground(Color.BLACK);
        addToCartButton.setFocusPainted(false);
        addToCartButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addToCartButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add to cart with quantity 1
                ShoppingCartDAO cartDAO = ShoppingCartDAO.getInstance();
                int cartId = cartDAO.getCartByUserId(currentUser.getUserID()).getCartID();
                cartDAO.addCartItem(cartId, product.getProductID(), 1);
                
                JOptionPane.showMessageDialog(ProductDetailPanel.this,
                    "Product added to cart!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Add components to details panel
        detailsPanel.add(productNameLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        detailsPanel.add(priceLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detailsPanel.add(taxLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        detailsPanel.add(descriptionLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        detailsPanel.add(colorLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        detailsPanel.add(colorSwatchesPanel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        detailsPanel.add(sizeLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        detailsPanel.add(sizeButtonsPanel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        detailsPanel.add(sizeGuidePanel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        detailsPanel.add(addToCartButton);
        
        // Add panels to content panel
        contentPanel.add(imagesPanel, BorderLayout.CENTER);
        contentPanel.add(detailsPanel, BorderLayout.EAST);
        
        // Add back button
        JButton backButton = new JButton("←");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBackground(new Color(245, 245, 245));
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showProductListPanel();
            }
        });
        
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setBackground(new Color(245, 245, 245));
        backButtonPanel.add(backButton);
        
        // Add components to main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.add(backButtonPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
}
