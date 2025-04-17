
package eretail.dao;

import eretail.model.Review;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReviewDAO {
    private static ReviewDAO instance;
    private Map<Integer, Review> reviews;
    private int nextId;

    private ReviewDAO() {
        reviews = new HashMap<>();
        nextId = 1;
        
        // Add some sample reviews
        addReview(new Review(nextId, 2, 1, 5, "Great laptop, very fast!", new Date()));
        addReview(new Review(nextId, 2, 2, 4, "Good phone, but battery life could be better", new Date()));
    }

    public static synchronized ReviewDAO getInstance() {
        if (instance == null) {
            instance = new ReviewDAO();
        }
        return instance;
    }

    public Review addReview(Review review) {
        if (review.getReviewID() == 0) {
            review.setReviewID(nextId++);
        }
        
        // Set review date if not provided
        if (review.getReviewDate() == null) {
            review.setReviewDate(new Date());
        }
        
        reviews.put(review.getReviewID(), review);
        
        // Link review to product
        ProductDAO productDAO = ProductDAO.getInstance();
        productDAO.getProductById(review.getProductID()).addReview(review);
        
        return review;
    }

    public Review getReviewById(int id) {
        return reviews.get(id);
    }

    public List<Review> getReviewsByProductId(int productId) {
        return reviews.values().stream()
                .filter(r -> r.getProductID() == productId)
                .collect(Collectors.toList());
    }

    public List<Review> getReviewsByUserId(int userId) {
        return reviews.values().stream()
                .filter(r -> r.getUserID() == userId)
                .collect(Collectors.toList());
    }

    public double getAverageRatingForProduct(int productId) {
        List<Review> productReviews = getReviewsByProductId(productId);
        if (productReviews.isEmpty()) {
            return 0.0;
        }
        
        double sum = productReviews.stream().mapToInt(Review::getRating).sum();
        return sum / productReviews.size();
    }

    public Review updateReview(Review review) {
        if (reviews.containsKey(review.getReviewID())) {
            reviews.put(review.getReviewID(), review);
            return review;
        }
        return null;
    }

    public boolean deleteReview(int id) {
        return reviews.remove(id) != null;
    }

    public List<Review> getAllReviews() {
        return new ArrayList<>(reviews.values());
    }
}
