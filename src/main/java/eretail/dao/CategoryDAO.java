
package eretail.dao;

import eretail.model.Category;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryDAO {
    private static CategoryDAO instance;
    private Map<Integer, Category> categories;
    private int nextId;

    private CategoryDAO() {
        categories = new HashMap<>();
        nextId = 1;
        
        // Add some sample categories
        addCategory(new Category(nextId, "Electronics", "Electronic devices and accessories"));
        addCategory(new Category(nextId, "Clothing", "Men's and women's apparel"));
        addCategory(new Category(nextId, "Books", "Fiction and non-fiction books"));
        addCategory(new Category(nextId, "Home & Kitchen", "Home appliances and kitchenware"));
    }

    public static synchronized CategoryDAO getInstance() {
        if (instance == null) {
            instance = new CategoryDAO();
        }
        return instance;
    }

    public Category addCategory(Category category) {
        if (category.getCategoryID() == 0) {
            category.setCategoryID(nextId++);
        }
        categories.put(category.getCategoryID(), category);
        return category;
    }

    public Category getCategoryById(int id) {
        return categories.get(id);
    }

    public List<Category> getAllCategories() {
        return new ArrayList<>(categories.values());
    }

    public Category updateCategory(Category category) {
        if (categories.containsKey(category.getCategoryID())) {
            categories.put(category.getCategoryID(), category);
            return category;
        }
        return null;
    }

    public boolean deleteCategory(int id) {
        return categories.remove(id) != null;
    }
}
