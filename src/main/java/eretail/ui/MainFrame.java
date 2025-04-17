
package eretail.ui;

import eretail.model.User;
import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    private User currentUser;
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private ProductListPanel productListPanel;
    private CartPanel cartPanel;
    private OrdersPanel ordersPanel;
    
    private JPanel contentPanel;
    
    public MainFrame() {
        initComponents();
        showLoginPanel();
    }
    
    private void initComponents() {
        setTitle("E-Retail Platform");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        contentPanel = new JPanel(new CardLayout());
        add(contentPanel);
        
        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        
        contentPanel.add(loginPanel, "login");
        contentPanel.add(registerPanel, "register");
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public void showLoginPanel() {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "login");
    }
    
    public void showRegisterPanel() {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "register");
    }
    
    public void showProductListPanel() {
        if (productListPanel == null) {
            productListPanel = new ProductListPanel(this, currentUser);
            contentPanel.add(productListPanel, "products");
        } else {
            // Refresh product panel
            contentPanel.remove(productListPanel);
            productListPanel = new ProductListPanel(this, currentUser);
            contentPanel.add(productListPanel, "products");
        }
        
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "products");
    }
    
    public void showCartPanel() {
        // Always recreate to refresh data
        cartPanel = new CartPanel(this, currentUser);
        contentPanel.add(cartPanel, "cart");
        
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "cart");
    }
    
    public void showOrdersPanel() {
        // Always recreate to refresh data
        ordersPanel = new OrdersPanel(this, currentUser);
        contentPanel.add(ordersPanel, "orders");
        
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "orders");
    }
    
    public void logout() {
        currentUser = null;
        
        // Clear panels
        if (productListPanel != null) {
            contentPanel.remove(productListPanel);
            productListPanel = null;
        }
        
        if (cartPanel != null) {
            contentPanel.remove(cartPanel);
            cartPanel = null;
        }
        
        if (ordersPanel != null) {
            contentPanel.remove(ordersPanel);
            ordersPanel = null;
        }
        
        showLoginPanel();
    }
}
