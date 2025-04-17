
package eretail.dao;

import eretail.model.CartItem;
import eretail.model.Product;
import eretail.model.ShoppingCart;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCartDAO {
    private static ShoppingCartDAO instance;
    private Map<Integer, ShoppingCart> carts;
    private int nextCartId;
    private Map<Integer, List<CartItem>> cartItems;
    private int nextItemId;

    private ShoppingCartDAO() {
        carts = new HashMap<>();
        cartItems = new HashMap<>();
        nextCartId = 1;
        nextItemId = 1;
    }

    public static synchronized ShoppingCartDAO getInstance() {
        if (instance == null) {
            instance = new ShoppingCartDAO();
        }
        return instance;
    }

    public ShoppingCart getCartByUserId(int userId) {
        // Find existing cart for user
        for (ShoppingCart cart : carts.values()) {
            if (cart.getUserID() == userId) {
                // Load cart items
                List<CartItem> items = cartItems.get(cart.getCartID());
                if (items != null) {
                    cart.getCartItems().clear();
                    for (CartItem item : items) {
                        // Load product details for the cart item
                        Product product = ProductDAO.getInstance().getProductById(item.getProductID());
                        item.setProduct(product);
                        cart.addCartItem(item);
                    }
                }
                return cart;
            }
        }
        
        // Create new cart if not found
        ShoppingCart newCart = new ShoppingCart(nextCartId++, userId, new Date());
        carts.put(newCart.getCartID(), newCart);
        return newCart;
    }

    public CartItem addCartItem(int cartId, int productId, int quantity) {
        // Check if product exists
        Product product = ProductDAO.getInstance().getProductById(productId);
        if (product == null) {
            return null;
        }
        
        ShoppingCart cart = carts.get(cartId);
        if (cart == null) {
            return null;
        }
        
        // Check if item already exists in cart
        List<CartItem> items = cartItems.get(cartId);
        if (items == null) {
            items = new ArrayList<>();
            cartItems.put(cartId, items);
        }
        
        for (CartItem item : items) {
            if (item.getProductID() == productId) {
                // Update quantity
                item.setQuantity(item.getQuantity() + quantity);
                return item;
            }
        }
        
        // Add new item
        CartItem newItem = new CartItem(nextItemId++, cartId, productId, quantity);
        newItem.setProduct(product);
        items.add(newItem);
        
        return newItem;
    }

    public boolean removeCartItem(int cartId, int productId) {
        List<CartItem> items = cartItems.get(cartId);
        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getProductID() == productId) {
                    items.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean updateCartItemQuantity(int cartId, int productId, int quantity) {
        List<CartItem> items = cartItems.get(cartId);
        if (items != null) {
            for (CartItem item : items) {
                if (item.getProductID() == productId) {
                    item.setQuantity(quantity);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean clearCart(int cartId) {
        ShoppingCart cart = carts.get(cartId);
        if (cart != null) {
            cartItems.put(cartId, new ArrayList<>());
            return true;
        }
        return false;
    }
}
