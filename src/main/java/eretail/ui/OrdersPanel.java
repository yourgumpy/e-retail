
package eretail.ui;

import eretail.dao.OrderDAO;
import eretail.model.Order;
import eretail.model.OrderItem;
import eretail.model.User;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class OrdersPanel extends JPanel {
    private MainFrame mainFrame;
    private User currentUser;
    private JTable ordersTable;
    private DefaultTableModel ordersTableModel;
    private JTable orderItemsTable;
    private DefaultTableModel orderItemsTableModel;
    private JButton viewOrderDetailsButton;
    private JButton backButton;

    public OrdersPanel(MainFrame mainFrame, User user) {
        this.mainFrame = mainFrame;
        this.currentUser = user;
        initComponents();
        loadOrders();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("My Orders");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Split pane for orders and order details
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.5);
        
        // Orders table
        String[] orderColumnNames = {"Order ID", "Date", "Total", "Status"};
        ordersTableModel = new DefaultTableModel(orderColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        ordersTable = new JTable(ordersTableModel);
        ordersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ordersTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane ordersScrollPane = new JScrollPane(ordersTable);
        
        // Order details table
        String[] orderItemsColumnNames = {"Product ID", "Product Name", "Quantity", "Price", "Subtotal"};
        orderItemsTableModel = new DefaultTableModel(orderItemsColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        orderItemsTable = new JTable(orderItemsTableModel);
        orderItemsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        orderItemsTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane orderItemsScrollPane = new JScrollPane(orderItemsTable);
        
        // Order details panel
        JPanel orderDetailsPanel = new JPanel(new BorderLayout());
        JLabel orderDetailsLabel = new JLabel("Order Details");
        orderDetailsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        orderDetailsPanel.add(orderDetailsLabel, BorderLayout.NORTH);
        orderDetailsPanel.add(orderItemsScrollPane, BorderLayout.CENTER);
        
        // Add to split pane
        splitPane.setTopComponent(ordersScrollPane);
        splitPane.setBottomComponent(orderDetailsPanel);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        viewOrderDetailsButton = new JButton("View Order Details");
        backButton = new JButton("Back to Products");
        
        viewOrderDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = ordersTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(OrdersPanel.this,
                        "Please select an order to view details.",
                        "Selection Required",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                int orderId = (int) ordersTableModel.getValueAt(selectedRow, 0);
                loadOrderItems(orderId);
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showProductListPanel();
            }
        });
        
        buttonPanel.add(viewOrderDetailsButton);
        buttonPanel.add(backButton);
        
        // Add components to main panel
        add(titlePanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadOrders() {
        // Clear existing data
        ordersTableModel.setRowCount(0);
        orderItemsTableModel.setRowCount(0);
        
        // Get user's orders
        OrderDAO orderDAO = OrderDAO.getInstance();
        List<Order> orders = orderDAO.getOrdersByUserId(currentUser.getUserID());
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        
        // Populate orders table
        for (Order order : orders) {
            Object[] row = {
                order.getOrderID(),
                dateFormat.format(order.getOrderDate()),
                String.format("$%.2f", order.getTotalAmount()),
                order.getStatus()
            };
            
            ordersTableModel.addRow(row);
        }
    }

    private void loadOrderItems(int orderId) {
        // Clear existing data
        orderItemsTableModel.setRowCount(0);
        
        // Get order items
        OrderDAO orderDAO = OrderDAO.getInstance();
        Order order = orderDAO.getOrderById(orderId);
        
        if (order != null) {
            List<OrderItem> items = order.getOrderItems();
            
            // Populate order items table
            for (OrderItem item : items) {
                String productName = item.getProduct() != null ? 
                    item.getProduct().getName() : "Product #" + item.getProductID();
                    
                double subtotal = item.getPrice() * item.getQuantity();
                
                Object[] row = {
                    item.getProductID(),
                    productName,
                    item.getQuantity(),
                    String.format("$%.2f", item.getPrice()),
                    String.format("$%.2f", subtotal)
                };
                
                orderItemsTableModel.addRow(row);
            }
        }
    }
}
