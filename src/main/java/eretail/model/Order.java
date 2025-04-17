
package eretail.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private int orderID;
    private int userID;
    private Date orderDate;
    private double totalAmount;
    private OrderStatus status;
    private List<OrderItem> orderItems;
    private Payment payment;

    public enum OrderStatus {
        PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    }

    public Order(int orderID, int userID, Date orderDate) {
        this.orderID = orderID;
        this.userID = userID;
        this.orderDate = orderDate;
        this.status = OrderStatus.PENDING;
        this.orderItems = new ArrayList<>();
        this.totalAmount = 0.0;
    }

    // Getters and Setters
    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void calculateTotalAmount() {
        double total = 0.0;
        for (OrderItem item : orderItems) {
            total += item.getPrice() * item.getQuantity();
        }
        this.totalAmount = total;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        calculateTotalAmount();
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    @Override
    public String toString() {
        return "Order #" + orderID + " - $" + totalAmount + " (" + status + ")";
    }
}
