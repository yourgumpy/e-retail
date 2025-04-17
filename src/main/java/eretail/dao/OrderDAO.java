
package eretail.dao;

import eretail.model.Order;
import eretail.model.OrderItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderDAO {
    private static OrderDAO instance;
    private Map<Integer, Order> orders;
    private int nextId;
    private Map<Integer, List<OrderItem>> orderItems;
    private int nextItemId;

    private OrderDAO() {
        orders = new HashMap<>();
        orderItems = new HashMap<>();
        nextId = 1;
        nextItemId = 1;
    }

    public static synchronized OrderDAO getInstance() {
        if (instance == null) {
            instance = new OrderDAO();
        }
        return instance;
    }

    public Order createOrder(int userId) {
        Order order = new Order(nextId++, userId, new Date());
        orders.put(order.getOrderID(), order);
        orderItems.put(order.getOrderID(), new ArrayList<>());
        return order;
    }

    public Order getOrderById(int orderId) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.getOrderItems().clear();
            order.getOrderItems().addAll(getOrderItemsByOrderId(orderId));
            order.calculateTotalAmount();
        }
        return order;
    }

    public List<Order> getOrdersByUserId(int userId) {
        List<Order> userOrders = orders.values().stream()
                .filter(o -> o.getUserID() == userId)
                .collect(Collectors.toList());
        
        // Load order items for each order
        for (Order order : userOrders) {
            order.getOrderItems().clear();
            order.getOrderItems().addAll(getOrderItemsByOrderId(order.getOrderID()));
            order.calculateTotalAmount();
        }
        
        return userOrders;
    }

    public OrderItem addOrderItem(OrderItem item) {
        if (item.getOrderItemID() == 0) {
            item.setOrderItemID(nextItemId++);
        }
        
        List<OrderItem> items = orderItems.get(item.getOrderID());
        if (items == null) {
            items = new ArrayList<>();
            orderItems.put(item.getOrderID(), items);
        }
        
        items.add(item);
        
        // Update order total
        Order order = orders.get(item.getOrderID());
        if (order != null) {
            order.addOrderItem(item);
        }
        
        return item;
    }

    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> items = orderItems.get(orderId);
        return items != null ? items : new ArrayList<>();
    }

    public boolean updateOrderStatus(int orderId, Order.OrderStatus status) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.setStatus(status);
            return true;
        }
        return false;
    }

    public List<Order> getAllOrders() {
        List<Order> allOrders = new ArrayList<>(orders.values());
        
        // Load order items for each order
        for (Order order : allOrders) {
            order.getOrderItems().clear();
            order.getOrderItems().addAll(getOrderItemsByOrderId(order.getOrderID()));
            order.calculateTotalAmount();
        }
        
        return allOrders;
    }
}
