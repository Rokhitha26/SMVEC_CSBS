package entity;

import java.time.LocalDate;

public class order {
	 private int orderID;
	    private customer customer; 
	    private LocalDate orderDate;
	    private Double totalAmount;
	    private product product;

	  
	    public order(int orderID, customer customer, LocalDate orderDate, Double totalAmount) {
	        this.orderID = orderID;
	        this.customer = customer;
	        this.orderDate = orderDate;
	        this.totalAmount = totalAmount;
	    }
	    

	  
	    public int getOrderID() {
	        return orderID;
	    }

	    public void setOrderID(int orderID) {
	        this.orderID = orderID;
	    }

	    public customer getCustomer() {
	        return customer;
	    }

	    public void setCustomer(customer customer) {
	        this.customer = customer;
	    }

	    public LocalDate getOrderDate() {
	        return orderDate;
	    }

	    public void setOrderDate(LocalDate orderDate) {
	        this.orderDate = orderDate;
	    }

	    public Double getTotalAmount() {
	        return totalAmount;
	    }

	    public void setTotalAmount(Double totalAmount) {
	        this.totalAmount = totalAmount;
	    }

	   
	    @Override
	    public String toString() {
	        return "Order{" +
	                "orderID=" + orderID +
	                ", customer=" + customer +
	                ", orderDate=" + orderDate +
	                ", totalAmount=" + totalAmount +
	                '}';
	    }
}
