
package eretail.ui;

import eretail.dao.CategoryDAO;
import eretail.dao.ProductDAO;
import eretail.dao.ShoppingCartDAO;
import eretail.model.Category;
import eretail.model.Product;
import eretail.model.User;
import eretail.ui.components.HeaderPanel;
import eretail.ui.components.ProductCard;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ProductListPanel extends JPanel {
    private MainFrame mainFrame;
    private User currentUser;
    private JTextField searchField;
    private JComboBox<Category> categoryComboBox;
    private JPanel productsPanel;
    private JPanel filterPanel;
    private JPanel sizeFilterPanel;
    private JPanel categoryFilterPanel;
    private JPanel priceFilterPanel;
    
    // Size checkboxes
    private JCheckBox xsCheckbox;
    private JCheckBox sCheckbox;
    private JCheckBox mCheckbox;
    private JCheckBox lCheckbox;
    private JCheckBox xlCheckbox;
    private JCheckBox xxlCheckbox;

    public ProductListPanel(MainFrame mainFrame, User user) {
        this.mainFrame = mainFrame;
        this.currentUser = user;
        initComponents();
        loadProducts();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        
        // Add header
        HeaderPanel headerPanel = new HeaderPanel(mainFrame, currentUser);
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content panel with search and filters on left, products on right
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.setBorder(new EmptyBorder(0, 20, 20, 20));
        
        // Page title and breadcrumbs
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(245, 245, 245));
        titlePanel.setBorder(new EmptyBorder(10, 0, 20, 0));
        
        JLabel breadcrumbLabel = new JLabel("Home / Products");
        breadcrumbLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JLabel titleLabel = new JLabel("PRODUCTS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        titlePanel.add(breadcrumbLabel, BorderLayout.NORTH);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        // Search panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(new Color(245, 245, 245));
        searchPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        searchField = new JTextField();
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(220, 220, 220));
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        
        // Filter panel on left
        filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setBackground(new Color(245, 245, 245));
        filterPanel.setBorder(new EmptyBorder(0, 0, 0, 20));
        filterPanel.setPreferredSize(new Dimension(200, 0));
        
        JLabel filtersLabel = new JLabel("Filters");
        filtersLabel.setFont(new Font("Arial", Font.BOLD, 16));
        filtersLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Size filter
        JLabel sizeLabel = new JLabel("Size");
        sizeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        sizeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        sizeFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sizeFilterPanel.setBackground(new Color(245, 245, 245));
        sizeFilterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sizeFilterPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        xsCheckbox = createSizeButton("XS");
        sCheckbox = createSizeButton("S");
        mCheckbox = createSizeButton("M");
        lCheckbox = createSizeButton("L");
        xlCheckbox = createSizeButton("XL");
        xxlCheckbox = createSizeButton("2X");
        
        sizeFilterPanel.add(xsCheckbox);
        sizeFilterPanel.add(sCheckbox);
        sizeFilterPanel.add(mCheckbox);
        sizeFilterPanel.add(lCheckbox);
        sizeFilterPanel.add(xlCheckbox);
        sizeFilterPanel.add(xxlCheckbox);
        
        // Availability filter
        JLabel availabilityLabel = new JLabel("Availability");
        availabilityLabel.setFont(new Font("Arial", Font.BOLD, 14));
        availabilityLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel availabilityPanel = new JPanel();
        availabilityPanel.setLayout(new BoxLayout(availabilityPanel, BoxLayout.Y_AXIS));
        availabilityPanel.setBackground(new Color(245, 245, 245));
        availabilityPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JCheckBox inStockCheckbox = new JCheckBox("Availability (450)");
        inStockCheckbox.setBackground(new Color(245, 245, 245));
        inStockCheckbox.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JCheckBox outOfStockCheckbox = new JCheckBox("Out Of Stock (18)");
        outOfStockCheckbox.setBackground(new Color(245, 245, 245));
        outOfStockCheckbox.setFont(new Font("Arial", Font.PLAIN, 12));
        
        availabilityPanel.add(inStockCheckbox);
        availabilityPanel.add(outOfStockCheckbox);
        
        // Category filter
        JLabel categoryLabel = new JLabel("Category");
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 14));
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        categoryFilterPanel = new JPanel();
        categoryFilterPanel.setLayout(new BoxLayout(categoryFilterPanel, BoxLayout.Y_AXIS));
        categoryFilterPanel.setBackground(new Color(245, 245, 245));
        categoryFilterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Load categories
        List<Category> categories = CategoryDAO.getInstance().getAllCategories();
        for (Category category : categories) {
            JCheckBox categoryCheckbox = new JCheckBox(category.getName());
            categoryCheckbox.setBackground(new Color(245, 245, 245));
            categoryCheckbox.setFont(new Font("Arial", Font.PLAIN, 12));
            categoryFilterPanel.add(categoryCheckbox);
        }
        
        // Price range filter
        JLabel priceRangeLabel = new JLabel("Price Range");
        priceRangeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        priceRangeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        priceFilterPanel = new JPanel();
        priceFilterPanel.setLayout(new BoxLayout(priceFilterPanel, BoxLayout.Y_AXIS));
        priceFilterPanel.setBackground(new Color(245, 245, 245));
        priceFilterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JCheckBox price0to50 = new JCheckBox("$0 - $50");
        price0to50.setBackground(new Color(245, 245, 245));
        price0to50.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JCheckBox price50to100 = new JCheckBox("$50 - $100");
        price50to100.setBackground(new Color(245, 245, 245));
        price50to100.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JCheckBox price100to200 = new JCheckBox("$100 - $200");
        price100to200.setBackground(new Color(245, 245, 245));
        price100to200.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JCheckBox price200plus = new JCheckBox("$200+");
        price200plus.setBackground(new Color(245, 245, 245));
        price200plus.setFont(new Font("Arial", Font.PLAIN, 12));
        
        priceFilterPanel.add(price0to50);
        priceFilterPanel.add(price50to100);
        priceFilterPanel.add(price100to200);
        priceFilterPanel.add(price200plus);
        
        // Add all filter components
        filterPanel.add(filtersLabel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        filterPanel.add(sizeLabel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        filterPanel.add(sizeFilterPanel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        filterPanel.add(availabilityLabel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        filterPanel.add(availabilityPanel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        filterPanel.add(categoryLabel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        filterPanel.add(categoryFilterPanel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        filterPanel.add(priceRangeLabel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        filterPanel.add(priceFilterPanel);
        
        // Product category tabs
        JPanel categoryTabsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        categoryTabsPanel.setBackground(new Color(245, 245, 245));
        
        String[] categoryTabs = {"NEW", "SHIRTS", "POLO SHIRTS", "SHORTS", "SUMMER", "BEST SELLERS", "T-SHIRTS", "JEANS", "JACKETS"};
        
        for (String tabName : categoryTabs) {
            JButton tabButton = new JButton(tabName);
            tabButton.setFont(new Font("Arial", Font.PLAIN, 12));
            tabButton.setBackground(Color.WHITE);
            tabButton.setForeground(Color.BLACK);
            tabButton.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
            tabButton.setFocusPainted(false);
            categoryTabsPanel.add(tabButton);
            
            tabButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Filter by category name
                    loadProductsByCategory(tabName);
                }
            });
        }
        
        // Products grid panel
        productsPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        productsPanel.setBackground(new Color(245, 245, 245));
        
        JScrollPane scrollPane = new JScrollPane(productsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Add components to main panel
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(new Color(245, 245, 245));
        
        mainContentPanel.add(titlePanel, BorderLayout.NORTH);
        mainContentPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(245, 245, 245));
        rightPanel.add(searchPanel, BorderLayout.NORTH);
        rightPanel.add(categoryTabsPanel, BorderLayout.NORTH);
        rightPanel.add(mainContentPanel, BorderLayout.CENTER);
        
        contentPanel.add(filterPanel, BorderLayout.WEST);
        contentPanel.add(rightPanel, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Add event listeners
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadProducts();
            }
        });
    }
    
    private JCheckBox createSizeButton(String size) {
        JCheckBox checkbox = new JCheckBox(size);
        checkbox.setFont(new Font("Arial", Font.PLAIN, 12));
        checkbox.setBackground(Color.WHITE);
        checkbox.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        checkbox.setPreferredSize(new Dimension(40, 35));
        
        checkbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update product list based on filter
                loadProducts();
            }
        });
        
        return checkbox;
    }

    private void loadProducts() {
        // Clear existing data
        productsPanel.removeAll();
        
        String searchQuery = searchField.getText().trim();
        
        List<Product> products;
        
        if (!searchQuery.isEmpty()) {
            // Search by query
            products = ProductDAO.getInstance().searchProducts(searchQuery);
        } else {
            // Get all products
            products = ProductDAO.getInstance().getAllProducts();
        }
        
        // Add product cards
        for (Product product : products) {
            ProductCard productCard = new ProductCard(mainFrame, currentUser, product);
            productsPanel.add(productCard);
        }
        
        productsPanel.revalidate();
        productsPanel.repaint();
    }
    
    private void loadProductsByCategory(String categoryName) {
        // Clear existing data
        productsPanel.removeAll();
        
        List<Product> products = ProductDAO.getInstance().getAllProducts();
        
        // Filter products by category name (case insensitive partial match)
        for (Product product : products) {
            if (product.getCategory() != null && 
                product.getCategory().getName().toUpperCase().contains(categoryName.toUpperCase())) {
                ProductCard productCard = new ProductCard(mainFrame, currentUser, product);
                productsPanel.add(productCard);
            }
        }
        
        productsPanel.revalidate();
        productsPanel.repaint();
    }
}
