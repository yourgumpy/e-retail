
package eretail.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShoppingCart {
    private int cartID;
    private int userID;
    private Date creationDate;
    private List<CartItem> cartItems;

    public ShoppingCart(int cartID, int userID, Date creationDate) {
        this.cartID = cartID;
        this.userID = userID;
        this.creationDate = creationDate;
        this.cartItems = new ArrayList<>();
    }

    // Getters and Setters
    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void addCartItem(CartItem item) {
        // Check if product already exists in cart
        for (CartItem existingItem : cartItems) {
            if (existingItem.getProductID() == item.getProductID()) {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                return;
            }
        }
        // If not found, add new item
        cartItems.add(item);
    }

    public void removeCartItem(int productID) {
        cartItems.removeIf(item -> item.getProductID() == productID);
    }

    public void updateCartItemQuantity(int productID, int newQuantity) {
        for (CartItem item : cartItems) {
            if (item.getProductID() == productID) {
                item.setQuantity(newQuantity);
                return;
            }
        }
    }

    public double calculateTotal() {
        double total = 0.0;
        for (CartItem item : cartItems) {
            if (item.getProduct() != null) {
                total += item.getProduct().getPrice() * item.getQuantity();
            }
        }
        return total;
    }

    public void clear() {
        cartItems.clear();
    }

    @Override
    public String toString() {
        return "Cart #" + cartID + " - Items: " + cartItems.size() + " - Total: $" + calculateTotal();
    }
}
