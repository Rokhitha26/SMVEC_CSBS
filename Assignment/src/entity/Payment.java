package entity;

import java.time.LocalDate;

public class Payment {
    private int orderID;
    private String paymentMethod;
    private double amountPaid;
    private LocalDate paymentDate;

    public Payment(int orderID, String paymentMethod, double amountPaid, LocalDate paymentDate) {
        this.orderID = orderID;
        this.paymentMethod = paymentMethod;
        this.amountPaid = amountPaid;
        this.paymentDate = paymentDate;
    }

    

    public int getOrderID() { return orderID; }
    public void setOrderID(int orderID) { this.orderID = orderID; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    @Override
    public String toString() {
        return "Order ID: " + orderID +
               " | Payment Method: " + paymentMethod +
               " | Amount Paid: ₹" + amountPaid +
               " | Payment Date: " + paymentDate;
    }
}
