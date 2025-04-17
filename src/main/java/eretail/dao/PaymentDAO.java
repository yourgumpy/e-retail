
package eretail.dao;

import eretail.model.Payment;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentDAO {
    private static PaymentDAO instance;
    private Map<Integer, Payment> payments;
    private int nextId;

    private PaymentDAO() {
        payments = new HashMap<>();
        nextId = 1;
    }

    public static synchronized PaymentDAO getInstance() {
        if (instance == null) {
            instance = new PaymentDAO();
        }
        return instance;
    }

    public Payment createPayment(int orderId, double amount, Payment.PaymentMethod method) {
        Payment payment = new Payment(nextId++, orderId, new Date(), amount, method);
        payments.put(payment.getPaymentID(), payment);
        
        // Link payment to order
        OrderDAO orderDAO = OrderDAO.getInstance();
        orderDAO.getOrderById(orderId).setPayment(payment);
        
        return payment;
    }

    public Payment getPaymentById(int id) {
        return payments.get(id);
    }

    public Payment getPaymentByOrderId(int orderId) {
        for (Payment payment : payments.values()) {
            if (payment.getOrderID() == orderId) {
                return payment;
            }
        }
        return null;
    }

    public boolean updatePaymentStatus(int paymentId, Payment.PaymentStatus status) {
        Payment payment = payments.get(paymentId);
        if (payment != null) {
            payment.setStatus(status);
            return true;
        }
        return false;
    }

    public List<Payment> getAllPayments() {
        return new ArrayList<>(payments.values());
    }
}
