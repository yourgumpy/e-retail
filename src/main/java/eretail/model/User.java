
package eretail.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int userID;
    private String name;
    private String email;
    private String password;
    private UserRole role;
    private List<Order> orders;
    private List<ShoppingCart> shoppingCarts;

    public enum UserRole {
        CUSTOMER, ADMIN, SELLER
    }

    public User(int userID, String name, String email, String password, UserRole role) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.orders = new ArrayList<>();
        this.shoppingCarts = new ArrayList<>();
    }

    // Getters and Setters
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public List<ShoppingCart> getShoppingCarts() {
        return shoppingCarts;
    }

    public void addShoppingCart(ShoppingCart cart) {
        this.shoppingCarts.add(cart);
    }

    @Override
    public String toString() {
        return name + " (" + email + ")";
    }
}
