
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
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CartPanel extends JPanel {
    private MainFrame mainFrame;
    private User currentUser;
    private JTable cartTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    private JButton updateQuantityButton;
    private JButton removeItemButton;
    private JButton clearCartButton;
    private JButton checkoutButton;
    private JButton backButton;

    public CartPanel(MainFrame mainFrame, User user) {
        this.mainFrame = mainFrame;
        this.currentUser = user;
        initComponents();
        loadCartItems();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("Shopping Cart");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Cart table
        String[] columnNames = {"Product ID", "Product Name", "Price", "Quantity", "Subtotal"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        cartTable = new JTable(tableModel);
        cartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cartTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(cartTable);
        
        // Bottom panel with total and buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        // Total panel
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalPanel.add(totalLabel);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel();
        updateQuantityButton = new JButton("Update Quantity");
        removeItemButton = new JButton("Remove Item");
        clearCartButton = new JButton("Clear Cart");
        checkoutButton = new JButton("Checkout");
        backButton = new JButton("Back to Products");
        
        updateQuantityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = cartTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(CartPanel.this,
                        "Please select an item to update.",
                        "Selection Required",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                int productId = (int) tableModel.getValueAt(selectedRow, 0);
                int currentQuantity = (int) tableModel.getValueAt(selectedRow, 3);
                
                String input = JOptionPane.showInputDialog(CartPanel.this, 
                    "Enter new quantity:", String.valueOf(currentQuantity));
                    
                if (input != null) {
                    try {
                        int newQuantity = Integer.parseInt(input);
                        if (newQuantity <= 0) {
                            JOptionPane.showMessageDialog(CartPanel.this,
                                "Quantity must be greater than zero.",
                                "Invalid Quantity",
                                JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        
                        // Update cart item quantity
                        ShoppingCartDAO cartDAO = ShoppingCartDAO.getInstance();
                        ShoppingCart cart = cartDAO.getCartByUserId(currentUser.getUserID());
                        cartDAO.updateCartItemQuantity(cart.getCartID(), productId, newQuantity);
                        
                        loadCartItems();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(CartPanel.this,
                            "Please enter a valid number.",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        removeItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = cartTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(CartPanel.this,
                        "Please select an item to remove.",
                        "Selection Required",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                int productId = (int) tableModel.getValueAt(selectedRow, 0);
                
                // Remove cart item
                ShoppingCartDAO cartDAO = ShoppingCartDAO.getInstance();
                ShoppingCart cart = cartDAO.getCartByUserId(currentUser.getUserID());
                cartDAO.removeCartItem(cart.getCartID(), productId);
                
                loadCartItems();
            }
        });
        
        clearCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(CartPanel.this,
                    "Are you sure you want to clear your cart?",
                    "Confirm Clear Cart",
                    JOptionPane.YES_NO_OPTION);
                    
                if (confirm == JOptionPane.YES_OPTION) {
                    // Clear cart
                    ShoppingCartDAO cartDAO = ShoppingCartDAO.getInstance();
                    ShoppingCart cart = cartDAO.getCartByUserId(currentUser.getUserID());
                    cartDAO.clearCart(cart.getCartID());
                    
                    loadCartItems();
                }
            }
        });
        
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tableModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(CartPanel.this,
                        "Your cart is empty.",
                        "Empty Cart",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                int confirm = JOptionPane.showConfirmDialog(CartPanel.this,
                    "Proceed to checkout?",
                    "Confirm Checkout",
                    JOptionPane.YES_NO_OPTION);
                    
                if (confirm == JOptionPane.YES_OPTION) {
                    // Create order
                    OrderDAO orderDAO = OrderDAO.getInstance();
                    Order order = orderDAO.createOrder(currentUser.getUserID());
                    
                    // Add order items
                    ShoppingCartDAO cartDAO = ShoppingCartDAO.getInstance();
                    ShoppingCart cart = cartDAO.getCartByUserId(currentUser.getUserID());
                    
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
                    cartDAO.clearCart(cart.getCartID());
                    
                    JOptionPane.showMessageDialog(CartPanel.this,
                        "Order placed successfully! Order ID: " + order.getOrderID(),
                        "Order Confirmation",
                        JOptionPane.INFORMATION_MESSAGE);
                        
                    // Refresh cart
                    loadCartItems();
                    
                    // Redirect to order page
                    mainFrame.showOrdersPanel();
                }
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showProductListPanel();
            }
        });
        
        buttonPanel.add(updateQuantityButton);
        buttonPanel.add(removeItemButton);
        buttonPanel.add(clearCartButton);
        buttonPanel.add(checkoutButton);
        buttonPanel.add(backButton);
        
        bottomPanel.add(totalPanel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add components to main panel
        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadCartItems() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get user's cart
        ShoppingCartDAO cartDAO = ShoppingCartDAO.getInstance();
        ShoppingCart cart = cartDAO.getCartByUserId(currentUser.getUserID());
        List<CartItem> items = cart.getCartItems();
        
        double total = 0.0;
        
        // Populate table
        for (CartItem item : items) {
            if (item.getProduct() != null) {
                double price = item.getProduct().getPrice();
                int quantity = item.getQuantity();
                double subtotal = price * quantity;
                
                Object[] row = {
                    item.getProductID(),
                    item.getProduct().getName(),
                    price,
                    quantity,
                    subtotal
                };
                
                tableModel.addRow(row);
                total += subtotal;
            }
        }
        
        // Update total
        totalLabel.setText(String.format("Total: $%.2f", total));
        
        // Enable/disable checkout button
        checkoutButton.setEnabled(tableModel.getRowCount() > 0);
    }
}
