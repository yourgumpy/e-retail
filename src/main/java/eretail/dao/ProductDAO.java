
package eretail.dao;

import eretail.model.Product;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductDAO {
    private static ProductDAO instance;
    private Map<Integer, Product> products;
    private int nextId;

    private ProductDAO() {
        products = new HashMap<>();
        nextId = 1;
        
        // Add some sample products
        addProduct(new Product(nextId, "Laptop", "High-performance laptop", 999.99, 50, 1));
        addProduct(new Product(nextId, "Smartphone", "Latest smartphone model", 699.99, 100, 1));
        addProduct(new Product(nextId, "Headphones", "Noise-cancelling headphones", 199.99, 75, 1));
        addProduct(new Product(nextId, "T-shirt", "Cotton t-shirt", 19.99, 200, 2));
        addProduct(new Product(nextId, "Jeans", "Slim fit jeans", 49.99, 150, 2));
    }

    public static synchronized ProductDAO getInstance() {
        if (instance == null) {
            instance = new ProductDAO();
        }
        return instance;
    }

    public Product addProduct(Product product) {
        if (product.getProductID() == 0) {
            product.setProductID(nextId++);
        }
        products.put(product.getProductID(), product);
        
        // Link product with its category
        CategoryDAO categoryDAO = CategoryDAO.getInstance();
        product.setCategory(categoryDAO.getCategoryById(product.getCategoryID()));
        
        return product;
    }

    public Product getProductById(int id) {
        return products.get(id);
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }
    
    public List<Product> getProductsByCategory(int categoryId) {
        return products.values().stream()
                .filter(p -> p.getCategoryID() == categoryId)
                .collect(Collectors.toList());
    }

    public List<Product> searchProducts(String query) {
        String searchQuery = query.toLowerCase();
        return products.values().stream()
                .filter(p -> p.getName().toLowerCase().contains(searchQuery) || 
                             p.getDescription().toLowerCase().contains(searchQuery))
                .collect(Collectors.toList());
    }

    public Product updateProduct(Product product) {
        if (products.containsKey(product.getProductID())) {
            products.put(product.getProductID(), product);
            return product;
        }
        return null;
    }

    public boolean deleteProduct(int id) {
        return products.remove(id) != null;
    }
}
