
package eretail.ui;

import eretail.dao.OrderDAO;
import eretail.dao.ShoppingCartDAO;
import eretail.model.CartItem;
import eretail.model.Order;
import eretail.model.OrderItem;
import eretail.model.ShoppingCart;
import eretail.model.User;
import eretail.ui.components.HeaderPanel;

import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CartPanel extends JPanel {
    private MainFrame mainFrame;
    private User currentUser;
    private JPanel cartItemsPanel;
    private JLabel subtotalLabel;
    private JLabel shippingLabel;
    private JLabel taxLabel;
    private JLabel totalLabel;
    private JButton checkoutButton;
    private JButton continueShoppingButton;

    public CartPanel(MainFrame mainFrame, User user) {
        this.mainFrame = mainFrame;
        this.currentUser = user;
        initComponents();
        loadCartItems();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        
        // Add header
        HeaderPanel headerPanel = new HeaderPanel(mainFrame, currentUser);
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout(30, 0));
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.setBorder(new EmptyBorder(20, 30, 30, 30));
        
        // Cart title
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(245, 245, 245));
        titlePanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JLabel cartTitleLabel = new JLabel("SHOPPING CART");
        cartTitleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titlePanel.add(cartTitleLabel, BorderLayout.WEST);
        
        // Left side - cart items
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBackground(new Color(245, 245, 245));
        
        // Cart items scroll panel
        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        cartItemsPanel.setBackground(new Color(245, 245, 245));
        
        JScrollPane scrollPane = new JScrollPane(cartItemsPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(new Color(245, 245, 245));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Continue shopping button
        continueShoppingButton = new JButton("← Continue Shopping");
        continueShoppingButton.setFont(new Font("Arial", Font.BOLD, 14));
        continueShoppingButton.setBackground(new Color(245, 245, 245));
        continueShoppingButton.setForeground(Color.BLACK);
        continueShoppingButton.setBorderPainted(false);
        continueShoppingButton.setFocusPainted(false);
        continueShoppingButton.setContentAreaFilled(false);
        
        continueShoppingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showProductListPanel();
            }
        });
        
        JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomButtonPanel.setBackground(new Color(245, 245, 245));
        bottomButtonPanel.add(continueShoppingButton);
        
        cartPanel.add(scrollPane, BorderLayout.CENTER);
        cartPanel.add(bottomButtonPanel, BorderLayout.SOUTH);
        
        // Right side - order summary
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        summaryPanel.setPreferredSize(new Dimension(300, 0));
        
        // Order summary title
        JLabel summaryTitleLabel = new JLabel("ORDER SUMMARY");
        summaryTitleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        summaryTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Subtotal
        JPanel subtotalPanel = new JPanel(new BorderLayout());
        subtotalPanel.setBackground(Color.WHITE);
        subtotalPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        subtotalPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtotalTextLabel = new JLabel("Subtotal");
        subtotalTextLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        subtotalLabel = new JLabel("$0.00");
        subtotalLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtotalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        subtotalPanel.add(subtotalTextLabel, BorderLayout.WEST);
        subtotalPanel.add(subtotalLabel, BorderLayout.EAST);
        
        // Shipping
        JPanel shippingPanel = new JPanel(new BorderLayout());
        shippingPanel.setBackground(Color.WHITE);
        shippingPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        shippingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel shippingTextLabel = new JLabel("Shipping");
        shippingTextLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        shippingLabel = new JLabel("Calculated at checkout");
        shippingLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        shippingLabel.setForeground(Color.GRAY);
        shippingLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        shippingPanel.add(shippingTextLabel, BorderLayout.WEST);
        shippingPanel.add(shippingLabel, BorderLayout.EAST);
        
        // Tax
        JPanel taxPanel = new JPanel(new BorderLayout());
        taxPanel.setBackground(Color.WHITE);
        taxPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        taxPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel taxTextLabel = new JLabel("Tax");
        taxTextLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        taxLabel = new JLabel("Calculated at checkout");
        taxLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        taxLabel.setForeground(Color.GRAY);
        taxLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        taxPanel.add(taxTextLabel, BorderLayout.WEST);
        taxPanel.add(taxLabel, BorderLayout.EAST);
        
        // Total
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(Color.WHITE);
        totalPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        totalPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel totalTextLabel = new JLabel("Total");
        totalTextLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        totalLabel = new JLabel("$0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        totalPanel.add(totalTextLabel, BorderLayout.WEST);
        totalPanel.add(totalLabel, BorderLayout.EAST);
        
        // Checkout button
        checkoutButton = new JButton("CHECKOUT");
        checkoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        checkoutButton.setBackground(new Color(40, 40, 40));
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setFocusPainted(false);
        checkoutButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkoutButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cartItemsPanel.getComponentCount() == 0) {
                    JOptionPane.showMessageDialog(CartPanel.this,
                        "Your cart is empty.",
                        "Empty Cart",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Proceed to checkout
                mainFrame.showCheckoutPanel();
            }
        });
        
        // Add components to summary panel
        summaryPanel.add(summaryTitleLabel);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        summaryPanel.add(subtotalPanel);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        summaryPanel.add(shippingPanel);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        summaryPanel.add(taxPanel);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        summaryPanel.add(new JSeparator());
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        summaryPanel.add(totalPanel);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        summaryPanel.add(checkoutButton);
        
        // Add components to main panel
        contentPanel.add(titlePanel, BorderLayout.NORTH);
        contentPanel.add(cartPanel, BorderLayout.CENTER);
        contentPanel.add(summaryPanel, BorderLayout.EAST);
        
        add(contentPanel, BorderLayout.CENTER);
    }

    private void loadCartItems() {
        // Clear existing data
        cartItemsPanel.removeAll();
        
        // Get user's cart
        ShoppingCartDAO cartDAO = ShoppingCartDAO.getInstance();
        ShoppingCart cart = cartDAO.getCartByUserId(currentUser.getUserID());
        List<CartItem> items = cart.getCartItems();
        
        double total = 0.0;
        
        if (items.isEmpty()) {
            // Show empty cart message
            JPanel emptyCartPanel = new JPanel(new BorderLayout());
            emptyCartPanel.setBackground(Color.WHITE);
            emptyCartPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
            emptyCartPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel emptyCartLabel = new JLabel("Your cart is empty");
            emptyCartLabel.setFont(new Font("Arial", Font.BOLD, 18));
            emptyCartLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            emptyCartPanel.add(emptyCartLabel, BorderLayout.CENTER);
            cartItemsPanel.add(emptyCartPanel);
        } else {
            // Add cart items
            for (CartItem item : items) {
                if (item.getProduct() != null) {
                    JPanel itemPanel = new JPanel(new BorderLayout(20, 0));
                    itemPanel.setBackground(Color.WHITE);
                    itemPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                    ));
                    itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
                    itemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    
                    // Item image (placeholder)
                    JPanel imagePanel = new JPanel() {
                        @Override
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            Graphics2D g2d = (Graphics2D) g;
                            g2d.setColor(new Color(230, 230, 230));
                            g2d.fillRect(0, 0, getWidth(), getHeight());
                        }
                        
                        @Override
                        public Dimension getPreferredSize() {
                            return new Dimension(80, 80);
                        }
                    };
                    
                    // Item info
                    JPanel infoPanel = new JPanel();
                    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                    infoPanel.setBackground(Color.WHITE);
                    
                    // Item name and remove button
                    JPanel namePanel = new JPanel(new BorderLayout());
                    namePanel.setBackground(Color.WHITE);
                    
                    JLabel nameLabel = new JLabel(item.getProduct().getName());
                    nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
                    
                    JButton removeButton = new JButton("✕");
                    removeButton.setFont(new Font("Arial", Font.PLAIN, 14));
                    removeButton.setBorderPainted(false);
                    removeButton.setFocusPainted(false);
                    removeButton.setContentAreaFilled(false);
                    removeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    
                    final int productId = item.getProductID();
                    
                    removeButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Remove cart item
                            ShoppingCartDAO cartDAO = ShoppingCartDAO.getInstance();
                            ShoppingCart cart = cartDAO.getCartByUserId(currentUser.getUserID());
                            cartDAO.removeCartItem(cart.getCartID(), productId);
                            
                            loadCartItems();
                        }
                    });
                    
                    namePanel.add(nameLabel, BorderLayout.WEST);
                    namePanel.add(removeButton, BorderLayout.EAST);
                    
                    // Color and size
                    JLabel colorSizeLabel = new JLabel("Black / L");
                    colorSizeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                    colorSizeLabel.setForeground(Color.GRAY);
                    
                    // Item price
                    JLabel priceLabel = new JLabel(String.format("$%.0f", item.getProduct().getPrice()));
                    priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
                    
                    // Quantity selector
                    JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                    quantityPanel.setBackground(Color.WHITE);
                    
                    JButton decreaseButton = new JButton("-");
                    decreaseButton.setFont(new Font("Arial", Font.PLAIN, 14));
                    decreaseButton.setPreferredSize(new Dimension(30, 30));
                    decreaseButton.setFocusPainted(false);
                    
                    JTextField quantityField = new JTextField(String.valueOf(item.getQuantity()));
                    quantityField.setFont(new Font("Arial", Font.PLAIN, 14));
                    quantityField.setHorizontalAlignment(JTextField.CENTER);
                    quantityField.setPreferredSize(new Dimension(40, 30));
                    
                    JButton increaseButton = new JButton("+");
                    increaseButton.setFont(new Font("Arial", Font.PLAIN, 14));
                    increaseButton.setPreferredSize(new Dimension(30, 30));
                    increaseButton.setFocusPainted(false);
                    
                    final int initialQuantity = item.getQuantity();
                    
                    decreaseButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int quantity = Integer.parseInt(quantityField.getText());
                            if (quantity > 1) {
                                quantity--;
                                quantityField.setText(String.valueOf(quantity));
                                updateCartItemQuantity(productId, quantity);
                            }
                        }
                    });
                    
                    increaseButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int quantity = Integer.parseInt(quantityField.getText());
                            quantity++;
                            quantityField.setText(String.valueOf(quantity));
                            updateCartItemQuantity(productId, quantity);
                        }
                    });
                    
                    quantityField.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                            try {
                                int quantity = Integer.parseInt(quantityField.getText());
                                if (quantity > 0 && quantity != initialQuantity) {
                                    updateCartItemQuantity(productId, quantity);
                                } else if (quantity <= 0) {
                                    quantityField.setText("1");
                                    updateCartItemQuantity(productId, 1);
                                }
                            } catch (NumberFormatException ex) {
                                quantityField.setText(String.valueOf(initialQuantity));
                            }
                        }
                    });
                    
                    quantityPanel.add(decreaseButton);
                    quantityPanel.add(quantityField);
                    quantityPanel.add(increaseButton);
                    
                    // Subtotal for this item
                    double price = item.getProduct().getPrice();
                    int quantity = item.getQuantity();
                    double subtotal = price * quantity;
                    total += subtotal;
                    
                    JLabel subtotalLabel = new JLabel(String.format("$%.0f", subtotal));
                    subtotalLabel.setFont(new Font("Arial", Font.BOLD, 14));
                    subtotalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                    
                    // Add components to info panel
                    infoPanel.add(namePanel);
                    infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                    infoPanel.add(colorSizeLabel);
                    infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                    infoPanel.add(priceLabel);
                    infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                    infoPanel.add(quantityPanel);
                    
                    // Add components to item panel
                    itemPanel.add(imagePanel, BorderLayout.WEST);
                    itemPanel.add(infoPanel, BorderLayout.CENTER);
                    itemPanel.add(subtotalLabel, BorderLayout.EAST);
                    
                    cartItemsPanel.add(itemPanel);
                }
            }
        }
        
        // Update summary amounts
        subtotalLabel.setText(String.format("$%.0f", total));
        totalLabel.setText(String.format("$%.0f", total));
        
        // Enable/disable checkout button
        checkoutButton.setEnabled(total > 0);
        
        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
    }
    
    private void updateCartItemQuantity(int productId, int quantity) {
        ShoppingCartDAO cartDAO = ShoppingCartDAO.getInstance();
        ShoppingCart cart = cartDAO.getCartByUserId(currentUser.getUserID());
        cartDAO.updateCartItemQuantity(cart.getCartID(), productId, quantity);
        
        loadCartItems();
    }
}
