package entity;

public class orderDetails {

	 private int orderDetailID;
	    private order order;       
	    private product product;   
	    private int quantity;

	    
	    public orderDetails(int orderDetailID, order order, product product, int quantity) {
	        this.orderDetailID = orderDetailID;
	        this.order = order;
	        this.product = product;
	        this.quantity = quantity;
	    }

	    
	    public int getOrderDetailID() {
	        return orderDetailID;
	    }

	    public void setOrderDetailID(int orderDetailID) {
	        this.orderDetailID = orderDetailID;
	    }

	    public order getOrder() {
	        return order;
	    }

	    public void setOrder(order order) {
	        this.order = order;
	    }

	    public product getProduct() {
	        return product;
	    }

	    public void setProduct(product product) {
	        this.product = product;
	    }

	    public int getQuantity() {
	        return quantity;
	    }

	    public void setQuantity(int quantity) {
	        this.quantity = quantity;
	    }

	
	    @Override
	    public String toString() {
	        return "OrderDetail{" +
	                "orderDetailID=" + orderDetailID +
	                ", order=" + order +
	                ", product=" + product +
	                ", quantity=" + quantity +
	                '}';
	    }
}
