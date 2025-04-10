package entity;

import java.time.LocalDate;

public class InitialOrderStage {

    private String productName;
    private int quantity;
    private LocalDate date;
    private int customerID;

  

    public InitialOrderStage(String productName, int quantity, LocalDate date, int customerID) {
        this.productName = productName;
        this.quantity = quantity;
        this.date = date;
        this.customerID = customerID;
    }

    // Getters and Setters
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    // toString method
    @Override
    public String toString() {
        return "InitialOrderStage{" +
                "productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", date=" + date +
                ", customerID=" + customerID +
                '}';
    }
}
