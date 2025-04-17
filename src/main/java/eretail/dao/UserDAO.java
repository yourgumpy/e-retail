
package eretail.dao;

import eretail.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAO {
    private static UserDAO instance;
    private Map<Integer, User> users;
    private int nextId;

    private UserDAO() {
        users = new HashMap<>();
        nextId = 1;
        
        // Add some sample data
        addUser(new User(nextId, "Admin User", "admin@example.com", "admin123", User.UserRole.ADMIN));
        addUser(new User(nextId, "John Doe", "john@example.com", "password", User.UserRole.CUSTOMER));
        addUser(new User(nextId, "Jane Smith", "jane@example.com", "password", User.UserRole.SELLER));
    }

    public static synchronized UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    public User addUser(User user) {
        if (user.getUserID() == 0) {
            user.setUserID(nextId++);
        }
        users.put(user.getUserID(), user);
        return user;
    }

    public User getUserById(int id) {
        return users.get(id);
    }
    
    public User getUserByEmail(String email) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User updateUser(User user) {
        if (users.containsKey(user.getUserID())) {
            users.put(user.getUserID(), user);
            return user;
        }
        return null;
    }

    public boolean deleteUser(int id) {
        return users.remove(id) != null;
    }
    
    public User authenticateUser(String email, String password) {
        User user = getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
