
package eretail.ui;

import eretail.dao.OrderDAO;
import eretail.dao.ShoppingCartDAO;
import eretail.model.CartItem;
import eretail.model.Order;
import eretail.model.OrderItem;
import eretail.model.ShoppingCart;
import eretail.model.User;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CheckoutPanel extends JPanel {
    private MainFrame mainFrame;
    private User currentUser;
    private ShoppingCart cart;
    private JPanel cartItemsPanel;
    private JLabel subtotalLabel;
    private JLabel shippingLabel;
    private JLabel totalLabel;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JComboBox<String> countryComboBox;
    private JTextField stateField;
    private JTextField addressField;
    private JTextField cityField;
    private JTextField postalCodeField;
    private JButton continueButton;
    private JButton placeOrderButton;
    
    private CardLayout checkoutStepsLayout;
    private JPanel checkoutStepsPanel;
    private JPanel informationPanel;
    private JPanel shippingPanel;
    private JPanel paymentPanel;
    
    public CheckoutPanel(MainFrame mainFrame, User currentUser) {
        this.mainFrame = mainFrame;
        this.currentUser = currentUser;
        this.cart = ShoppingCartDAO.getInstance().getCartByUserId(currentUser.getUserID());
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        
        // Store logo
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(new Color(245, 245, 245));
        
        JLabel logoLabel = new JLabel("Come & Change");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(new Color(40, 40, 40));
        
        // Add a circular logo marker
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
        
        logoPanel.add(circleLogoPanel);
        logoPanel.add(logoLabel);
        
        // Back button
        JButton backButton = new JButton("←");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showCartPanel();
            }
        });
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(logoPanel, BorderLayout.CENTER);
        
        // Checkout title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(245, 245, 245));
        
        JLabel checkoutLabel = new JLabel("CHECKOUT");
        checkoutLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titlePanel.add(checkoutLabel);
        
        // Checkout steps
        JPanel stepsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        stepsPanel.setBackground(new Color(245, 245, 245));
        
        JToggleButton infoButton = new JToggleButton("INFORMATION");
        JToggleButton shippingButton = new JToggleButton("SHIPPING");
        JToggleButton paymentButton = new JToggleButton("PAYMENT");
        
        infoButton.setFont(new Font("Arial", Font.PLAIN, 14));
        shippingButton.setFont(new Font("Arial", Font.PLAIN, 14));
        paymentButton.setFont(new Font("Arial", Font.PLAIN, 14));
        
        infoButton.setFocusPainted(false);
        shippingButton.setFocusPainted(false);
        paymentButton.setFocusPainted(false);
        
        infoButton.setBorderPainted(false);
        shippingButton.setBorderPainted(false);
        paymentButton.setBorderPainted(false);
        
        infoButton.setContentAreaFilled(false);
        shippingButton.setContentAreaFilled(false);
        paymentButton.setContentAreaFilled(false);
        
        infoButton.setSelected(true);
        
        ButtonGroup stepsGroup = new ButtonGroup();
        stepsGroup.add(infoButton);
        stepsGroup.add(shippingButton);
        stepsGroup.add(paymentButton);
        
        stepsPanel.add(infoButton);
        stepsPanel.add(shippingButton);
        stepsPanel.add(paymentButton);
        
        // Main container for checkout steps
        checkoutStepsLayout = new CardLayout();
        checkoutStepsPanel = new JPanel(checkoutStepsLayout);
        checkoutStepsPanel.setBackground(new Color(245, 245, 245));
        
        // Create the three step panels
        informationPanel = createInformationPanel();
        shippingPanel = createShippingPanel();
        paymentPanel = createPaymentPanel();
        
        checkoutStepsPanel.add(informationPanel, "information");
        checkoutStepsPanel.add(shippingPanel, "shipping");
        checkoutStepsPanel.add(paymentPanel, "payment");
        
        // Add event listeners for step buttons
        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkoutStepsLayout.show(checkoutStepsPanel, "information");
            }
        });
        
        shippingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkoutStepsLayout.show(checkoutStepsPanel, "shipping");
            }
        });
        
        paymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkoutStepsLayout.show(checkoutStepsPanel, "payment");
            }
        });
        
        // Order summary panel (right side)
        JPanel orderSummaryPanel = createOrderSummaryPanel();
        
        // Main layout
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 245, 245));
        headerPanel.add(topPanel, BorderLayout.NORTH);
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        headerPanel.add(stepsPanel, BorderLayout.SOUTH);
        
        JPanel mainPanel = new JPanel(new BorderLayout(30, 0));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(new EmptyBorder(20, 30, 30, 30));
        mainPanel.add(checkoutStepsPanel, BorderLayout.CENTER);
        mainPanel.add(orderSummaryPanel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createInformationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        // Contact info section
        JLabel contactInfoLabel = new JLabel("CONTACT INFO");
        contactInfoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        contactInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        emailField = new JTextField(20);
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailField.setText(currentUser.getEmail());
        
        phoneField = new JTextField(20);
        phoneField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        phoneField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        phoneField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Shipping address section
        JLabel shippingAddressLabel = new JLabel("SHIPPING ADDRESS");
        shippingAddressLabel.setFont(new Font("Arial", Font.BOLD, 16));
        shippingAddressLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Name fields (first name, last name)
        JPanel namePanel = new JPanel(new GridLayout(1, 2, 10, 0));
        namePanel.setBackground(new Color(245, 245, 245));
        namePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        namePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        firstNameField = new JTextField(10);
        firstNameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        lastNameField = new JTextField(10);
        lastNameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        namePanel.add(firstNameField);
        namePanel.add(lastNameField);
        
        // Country dropdown
        String[] countries = {"United States", "Canada", "United Kingdom", "Australia", "Germany", "France", "Japan"};
        countryComboBox = new JComboBox<>(countries);
        countryComboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        countryComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        countryComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // State/Region field
        stateField = new JTextField(20);
        stateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        stateField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        stateField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Address field
        addressField = new JTextField(20);
        addressField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        addressField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        addressField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // City and Postal Code
        JPanel cityPostalPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        cityPostalPanel.setBackground(new Color(245, 245, 245));
        cityPostalPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cityPostalPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        cityField = new JTextField(10);
        cityField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        postalCodeField = new JTextField(10);
        postalCodeField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        cityPostalPanel.add(cityField);
        cityPostalPanel.add(postalCodeField);
        
        // Continue button
        continueButton = new JButton("Shipping →");
        continueButton.setFont(new Font("Arial", Font.BOLD, 14));
        continueButton.setBackground(new Color(220, 220, 220));
        continueButton.setForeground(Color.BLACK);
        continueButton.setFocusPainted(false);
        continueButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        continueButton.setMaximumSize(new Dimension(200, 40));
        
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validate fields
                if (emailField.getText().trim().isEmpty() || 
                    firstNameField.getText().trim().isEmpty() || 
                    lastNameField.getText().trim().isEmpty() || 
                    addressField.getText().trim().isEmpty() || 
                    cityField.getText().trim().isEmpty() || 
                    postalCodeField.getText().trim().isEmpty()) {
                    
                    JOptionPane.showMessageDialog(CheckoutPanel.this,
                        "Please fill in all required fields.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Proceed to shipping step
                checkoutStepsLayout.show(checkoutStepsPanel, "shipping");
            }
        });
        
        // Add components to panel
        panel.add(contactInfoLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(emailField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(phoneField);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(shippingAddressLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(namePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(countryComboBox);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(stateField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(addressField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(cityPostalPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(continueButton);
        
        return panel;
    }
    
    private JPanel createShippingPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        // Shipping options section
        JLabel shippingOptionsLabel = new JLabel("SHIPPING METHOD");
        shippingOptionsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        shippingOptionsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create shipping options as radio buttons
        ButtonGroup shippingGroup = new ButtonGroup();
        
        JPanel standardShippingPanel = createShippingOptionPanel("Standard Shipping", "$5.00", "5-7 business days", true);
        JPanel expressShippingPanel = createShippingOptionPanel("Express Shipping", "$15.00", "2-3 business days", false);
        JPanel overnightShippingPanel = createShippingOptionPanel("Overnight Shipping", "$25.00", "Next business day", false);
        
        // Continue button
        JButton continueToPaymentButton = new JButton("Payment →");
        continueToPaymentButton.setFont(new Font("Arial", Font.BOLD, 14));
        continueToPaymentButton.setBackground(new Color(220, 220, 220));
        continueToPaymentButton.setForeground(Color.BLACK);
        continueToPaymentButton.setFocusPainted(false);
        continueToPaymentButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        continueToPaymentButton.setMaximumSize(new Dimension(200, 40));
        
        continueToPaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Proceed to payment step
                checkoutStepsLayout.show(checkoutStepsPanel, "payment");
            }
        });
        
        // Add components to panel
        panel.add(shippingOptionsLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(standardShippingPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(expressShippingPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(overnightShippingPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(continueToPaymentButton);
        
        return panel;
    }
    
    private JPanel createShippingOptionPanel(String title, String price, String description, boolean isSelected) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JRadioButton radioButton = new JRadioButton(title);
        radioButton.setFont(new Font("Arial", Font.BOLD, 14));
        radioButton.setBackground(Color.WHITE);
        radioButton.setSelected(isSelected);
        
        JLabel priceLabel = new JLabel(price);
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        descriptionLabel.setForeground(Color.GRAY);
        
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.add(radioButton, BorderLayout.NORTH);
        leftPanel.add(descriptionLabel, BorderLayout.SOUTH);
        
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(priceLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createPaymentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        // Payment options section
        JLabel paymentOptionsLabel = new JLabel("PAYMENT METHOD");
        paymentOptionsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        paymentOptionsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create payment options
        ButtonGroup paymentGroup = new ButtonGroup();
        
        JPanel creditCardPanel = createPaymentOptionPanel("Credit Card", "Pay with Visa, Mastercard, etc.", true);
        JPanel paypalPanel = createPaymentOptionPanel("PayPal", "Pay with your PayPal account", false);
        JPanel applePanelPanel = createPaymentOptionPanel("Apple Pay", "Pay with Apple Pay", false);
        
        // Payment form (credit card)
        JPanel cardFormPanel = new JPanel();
        cardFormPanel.setLayout(new BoxLayout(cardFormPanel, BoxLayout.Y_AXIS));
        cardFormPanel.setBackground(Color.WHITE);
        cardFormPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        cardFormPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        cardFormPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField cardNumberField = new JTextField("Card Number");
        cardNumberField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        cardNumberField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cardNumberField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField nameOnCardField = new JTextField("Name on Card");
        nameOnCardField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        nameOnCardField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        nameOnCardField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel expirySecurityPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        expirySecurityPanel.setBackground(Color.WHITE);
        expirySecurityPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        expirySecurityPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField expiryField = new JTextField("MM/YY");
        expiryField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JTextField securityCodeField = new JTextField("CVV");
        securityCodeField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        expirySecurityPanel.add(expiryField);
        expirySecurityPanel.add(securityCodeField);
        
        cardFormPanel.add(cardNumberField);
        cardFormPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardFormPanel.add(nameOnCardField);
        cardFormPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardFormPanel.add(expirySecurityPanel);
        
        // Place order button
        placeOrderButton = new JButton("Place Order");
        placeOrderButton.setFont(new Font("Arial", Font.BOLD, 14));
        placeOrderButton.setBackground(new Color(220, 220, 220));
        placeOrderButton.setForeground(Color.BLACK);
        placeOrderButton.setFocusPainted(false);
        placeOrderButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        placeOrderButton.setMaximumSize(new Dimension(200, 40));
        
        placeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create order
                OrderDAO orderDAO = OrderDAO.getInstance();
                Order order = orderDAO.createOrder(currentUser.getUserID());
                
                // Add order items
                for (CartItem item : cart.getCartItems()) {
                    OrderItem orderItem = new OrderItem(
                        0, 
                        order.getOrderID(), 
                        item.getProductID(), 
                        item.getQuantity(), 
                        item.getProduct().getPrice()
                    );
                    orderDAO.addOrderItem(orderItem);
                }
                
                // Clear cart
                ShoppingCartDAO cartDAO = ShoppingCartDAO.getInstance();
                cartDAO.clearCart(cart.getCartID());
                
                JOptionPane.showMessageDialog(CheckoutPanel.this,
                    "Order placed successfully! Order ID: " + order.getOrderID(),
                    "Order Confirmation",
                    JOptionPane.INFORMATION_MESSAGE);
                    
                // Redirect to order page
                mainFrame.showOrdersPanel();
            }
        });
        
        // Add components to panel
        panel.add(paymentOptionsLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(creditCardPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(paypalPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(applePanelPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(cardFormPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(placeOrderButton);
        
        return panel;
    }
    
    private JPanel createPaymentOptionPanel(String title, String description, boolean isSelected) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JRadioButton radioButton = new JRadioButton(title);
        radioButton.setFont(new Font("Arial", Font.BOLD, 14));
        radioButton.setBackground(Color.WHITE);
        radioButton.setSelected(isSelected);
        
        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        descriptionLabel.setForeground(Color.GRAY);
        
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.add(radioButton, BorderLayout.NORTH);
        leftPanel.add(descriptionLabel, BorderLayout.SOUTH);
        
        panel.add(leftPanel, BorderLayout.WEST);
        
        return panel;
    }
    
    private JPanel createOrderSummaryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panel.setPreferredSize(new Dimension(300, 0));
        
        // Order title
        JLabel orderTitleLabel = new JLabel("YOUR ORDER");
        orderTitleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        orderTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Cart items count
        JLabel itemCountLabel = new JLabel("(" + cart.getCartItems().size() + ")");
        itemCountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        itemCountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(orderTitleLabel, BorderLayout.WEST);
        titlePanel.add(itemCountLabel, BorderLayout.EAST);
        titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Cart items
        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        cartItemsPanel.setBackground(Color.WHITE);
        cartItemsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Add cart items
        double totalAmount = 0.0;
        
        for (CartItem item : cart.getCartItems()) {
            if (item.getProduct() != null) {
                JPanel itemPanel = new JPanel(new BorderLayout(10, 0));
                itemPanel.setBackground(Color.WHITE);
                itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
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
                        return new Dimension(60, 60);
                    }
                };
                
                // Item details
                JPanel itemDetailsPanel = new JPanel();
                itemDetailsPanel.setLayout(new BoxLayout(itemDetailsPanel, BoxLayout.Y_AXIS));
                itemDetailsPanel.setBackground(Color.WHITE);
                
                JLabel itemNameLabel = new JLabel(item.getProduct().getName());
                itemNameLabel.setFont(new Font("Arial", Font.BOLD, 12));
                
                JLabel itemColorSizeLabel = new JLabel("Black/L");
                itemColorSizeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                itemColorSizeLabel.setForeground(Color.GRAY);
                
                JLabel quantityLabel = new JLabel("(" + item.getQuantity() + ")");
                quantityLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                
                itemDetailsPanel.add(itemNameLabel);
                itemDetailsPanel.add(itemColorSizeLabel);
                itemDetailsPanel.add(quantityLabel);
                
                // Item price
                double itemTotal = item.getProduct().getPrice() * item.getQuantity();
                totalAmount += itemTotal;
                
                JLabel priceLabel = new JLabel("$" + (int)itemTotal);
                priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
                priceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                
                // Change link
                JLabel changeLink = new JLabel("Change");
                changeLink.setFont(new Font("Arial", Font.PLAIN, 12));
                changeLink.setForeground(Color.BLUE);
                changeLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
                changeLink.setHorizontalAlignment(SwingConstants.RIGHT);
                
                // Add components to item panel
                itemPanel.add(imagePanel, BorderLayout.WEST);
                itemPanel.add(itemDetailsPanel, BorderLayout.CENTER);
                
                JPanel rightPanel = new JPanel(new BorderLayout());
                rightPanel.setBackground(Color.WHITE);
                rightPanel.add(priceLabel, BorderLayout.NORTH);
                rightPanel.add(changeLink, BorderLayout.SOUTH);
                
                itemPanel.add(rightPanel, BorderLayout.EAST);
                
                cartItemsPanel.add(itemPanel);
                cartItemsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        
        // Order summary (subtotal, shipping, total)
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Subtotal
        JPanel subtotalPanel = new JPanel(new BorderLayout());
        subtotalPanel.setBackground(Color.WHITE);
        subtotalPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        
        JLabel subtotalTextLabel = new JLabel("Subtotal");
        subtotalTextLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        subtotalLabel = new JLabel("$" + (int)totalAmount);
        subtotalLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtotalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        subtotalPanel.add(subtotalTextLabel, BorderLayout.WEST);
        subtotalPanel.add(subtotalLabel, BorderLayout.EAST);
        
        // Shipping
        JPanel shippingPanel = new JPanel(new BorderLayout());
        shippingPanel.setBackground(Color.WHITE);
        shippingPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        
        JLabel shippingTextLabel = new JLabel("Shipping");
        shippingTextLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        shippingLabel = new JLabel("Calculated at next step");
        shippingLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        shippingLabel.setForeground(Color.GRAY);
        shippingLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        shippingPanel.add(shippingTextLabel, BorderLayout.WEST);
        shippingPanel.add(shippingLabel, BorderLayout.EAST);
        
        // Total
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(Color.WHITE);
        totalPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        JLabel totalTextLabel = new JLabel("Total");
        totalTextLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        totalLabel = new JLabel("$" + (int)totalAmount);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        totalPanel.add(totalTextLabel, BorderLayout.WEST);
        totalPanel.add(totalLabel, BorderLayout.EAST);
        
        // Add components to summary panel
        summaryPanel.add(subtotalPanel);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        summaryPanel.add(shippingPanel);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        summaryPanel.add(new JSeparator());
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        summaryPanel.add(totalPanel);
        
        // Add all components to main panel
        panel.add(titlePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(cartItemsPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(summaryPanel);
        
        return panel;
    }
}
