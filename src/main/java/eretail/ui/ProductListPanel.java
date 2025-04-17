
package eretail.ui;

import eretail.dao.CategoryDAO;
import eretail.dao.ProductDAO;
import eretail.dao.ShoppingCartDAO;
import eretail.model.Category;
import eretail.model.Product;
import eretail.model.User;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class ProductListPanel extends JPanel {
    private MainFrame mainFrame;
    private User currentUser;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JComboBox<Category> categoryComboBox;
    private JTextField searchField;
    private JButton addToCartButton;
    private JButton viewCartButton;
    private JButton viewOrdersButton;
    private JButton viewProfileButton;
    private JButton logoutButton;
    
    // Admin/Seller specific buttons
    private JButton addProductButton;
    private JButton editProductButton;
    private JButton deleteProductButton;
    private JButton manageCategoriesButton;

    public ProductListPanel(MainFrame mainFrame, User user) {
        this.mainFrame = mainFrame;
        this.currentUser = user;
        initComponents();
        loadProducts();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Top panel with user info and navigation buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        
        // User info panel
        JPanel userInfoPanel = new JPanel();
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getName() + " (" + currentUser.getRole() + ")");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        userInfoPanel.add(welcomeLabel);
        
        // Navigation buttons
        JPanel navButtonPanel = new JPanel();
        viewCartButton = new JButton("View Cart");
        viewOrdersButton = new JButton("My Orders");
        viewProfileButton = new JButton("My Profile");
        logoutButton = new JButton("Logout");
        
        viewCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showCartPanel();
            }
        });
        
        viewOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showOrdersPanel();
            }
        });
        
        viewProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Implement profile panel
                JOptionPane.showMessageDialog(ProductListPanel.this, 
                    "Profile view not implemented yet.", 
                    "Information", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.logout();
            }
        });
        
        navButtonPanel.add(viewCartButton);
        navButtonPanel.add(viewOrdersButton);
        navButtonPanel.add(viewProfileButton);
        navButtonPanel.add(logoutButton);
        
        topPanel.add(userInfoPanel, BorderLayout.WEST);
        topPanel.add(navButtonPanel, BorderLayout.EAST);
        
        // Filter panel
        JPanel filterPanel = new JPanel();
        JLabel categoryLabel = new JLabel("Category:");
        categoryComboBox = new JComboBox<>();
        
        // Add "All Categories" option
        categoryComboBox.addItem(new Category(0, "All Categories", ""));
        
        // Load categories
        List<Category> categories = CategoryDAO.getInstance().getAllCategories();
        for (Category category : categories) {
            categoryComboBox.addItem(category);
        }
        
        categoryComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProducts();
            }
        });
        
        // Search field
        JLabel searchLabel = new JLabel("Search:");
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProducts();
            }
        });
        
        filterPanel.add(categoryLabel);
        filterPanel.add(categoryComboBox);
        filterPanel.add(searchLabel);
        filterPanel.add(searchField);
        filterPanel.add(searchButton);
        
        // Product table
        String[] columnNames = {"ID", "Name", "Description", "Price", "Stock", "Category"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.getTableHeader().setReorderingAllowed(false);
        
        // Set column widths
        productTable.getColumnModel().getColumn(0).setPreferredWidth(30);  // ID
        productTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Name
        productTable.getColumnModel().getColumn(2).setPreferredWidth(300); // Description
        productTable.getColumnModel().getColumn(3).setPreferredWidth(60);  // Price
        productTable.getColumnModel().getColumn(4).setPreferredWidth(50);  // Stock
        productTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Category
        
        JScrollPane scrollPane = new JScrollPane(productTable);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        addToCartButton = new JButton("Add to Cart");
        
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(ProductListPanel.this,
                        "Please select a product first.",
                        "Selection Required",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                int productId = (int) tableModel.getValueAt(selectedRow, 0);
                
                // Show quantity dialog
                String input = JOptionPane.showInputDialog(ProductListPanel.this, 
                    "Enter quantity:", "1");
                    
                if (input != null) {
                    try {
                        int quantity = Integer.parseInt(input);
                        if (quantity <= 0) {
                            JOptionPane.showMessageDialog(ProductListPanel.this,
                                "Quantity must be greater than zero.",
                                "Invalid Quantity",
                                JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        
                        // Add to cart
                        ShoppingCartDAO cartDAO = ShoppingCartDAO.getInstance();
                        cartDAO.getCartByUserId(currentUser.getUserID());
                        int cartId = cartDAO.getCartByUserId(currentUser.getUserID()).getCartID();
                        cartDAO.addCartItem(cartId, productId, quantity);
                        
                        JOptionPane.showMessageDialog(ProductListPanel.this,
                            "Product added to cart successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(ProductListPanel.this,
                            "Please enter a valid number.",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        buttonPanel.add(addToCartButton);
        
        // Admin/Seller specific buttons
        if (currentUser.getRole() == User.UserRole.ADMIN || 
            currentUser.getRole() == User.UserRole.SELLER) {
            
            addProductButton = new JButton("Add Product");
            editProductButton = new JButton("Edit Product");
            deleteProductButton = new JButton("Delete Product");
            
            if (currentUser.getRole() == User.UserRole.ADMIN) {
                manageCategoriesButton = new JButton("Manage Categories");
                buttonPanel.add(manageCategoriesButton);
                
                manageCategoriesButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // TODO: Implement category management
                        JOptionPane.showMessageDialog(ProductListPanel.this, 
                            "Category management not implemented yet.", 
                            "Information", 
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                });
            }
            
            addProductButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // TODO: Implement add product functionality
                    JOptionPane.showMessageDialog(ProductListPanel.this, 
                        "Add product not implemented yet.", 
                        "Information", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
            });
            
            editProductButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = productTable.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(ProductListPanel.this,
                            "Please select a product to edit.",
                            "Selection Required",
                            JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    // TODO: Implement edit product functionality
                    JOptionPane.showMessageDialog(ProductListPanel.this, 
                        "Edit product not implemented yet.", 
                        "Information", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
            });
            
            deleteProductButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = productTable.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(ProductListPanel.this,
                            "Please select a product to delete.",
                            "Selection Required",
                            JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    int productId = (int) tableModel.getValueAt(selectedRow, 0);
                    int confirm = JOptionPane.showConfirmDialog(ProductListPanel.this,
                        "Are you sure you want to delete this product?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION);
                        
                    if (confirm == JOptionPane.YES_OPTION) {
                        ProductDAO productDAO = ProductDAO.getInstance();
                        boolean success = productDAO.deleteProduct(productId);
                        
                        if (success) {
                            JOptionPane.showMessageDialog(ProductListPanel.this,
                                "Product deleted successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                            loadProducts();
                        } else {
                            JOptionPane.showMessageDialog(ProductListPanel.this,
                                "Failed to delete product.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
            
            buttonPanel.add(addProductButton);
            buttonPanel.add(editProductButton);
            buttonPanel.add(deleteProductButton);
        }
        
        // Add components to main panel
        add(topPanel, BorderLayout.NORTH);
        add(filterPanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadProducts() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get selected category
        Category selectedCategory = (Category) categoryComboBox.getSelectedItem();
        String searchQuery = searchField.getText().trim();
        
        List<Product> products;
        
        if (selectedCategory != null && selectedCategory.getCategoryID() > 0) {
            // Filter by category
            products = ProductDAO.getInstance().getProductsByCategory(selectedCategory.getCategoryID());
        } else if (!searchQuery.isEmpty()) {
            // Search by query
            products = ProductDAO.getInstance().searchProducts(searchQuery);
        } else {
            // Get all products
            products = ProductDAO.getInstance().getAllProducts();
        }
        
        // Populate table
        for (Product product : products) {
            String categoryName = product.getCategory() != null ? 
                product.getCategory().getName() : "Unknown";
                
            Object[] row = {
                product.getProductID(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                categoryName
            };
            
            tableModel.addRow(row);
        }
    }
}
