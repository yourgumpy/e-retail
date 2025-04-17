
package eretail.model;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private int categoryID;
    private String name;
    private String description;
    private List<Product> products;

    public Category(int categoryID, String name, String description) {
        this.categoryID = categoryID;
        this.name = name;
        this.description = description;
        this.products = new ArrayList<>();
    }

    // Getters and Setters
    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
        product.setCategory(this);
    }

    @Override
    public String toString() {
        return name;
    }
}
