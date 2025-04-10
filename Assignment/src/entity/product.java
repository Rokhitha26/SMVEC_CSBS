package entity;

public class product {

	 private int productID;
	    private String productName;
	    private String description;
	    private Double price;

	   
	    public product(int productID, String productName, String description, Double price) {
	        this.productID = productID;
	        this.productName = productName;
	        this.description = description;
	        this.price = price;
	    }
	    
	    public product(String productName,String productDes, Double productPrice) {
	    	this.productName=productName;
	    	this.description=productDes;
	    	this.price=productPrice;
	    }
	    
	    public product(String productName, Double productPrice) {
	    	this.productName=productName;
	    	this.price=productPrice;
	    }


	    public int getProductID() {
	        return productID;
	    }

	    public void setProductID(int productID) {
	        this.productID = productID;
	    }

	    public String getProductName() {
	        return productName;
	    }

	    public void setProductName(String productName) {
	        this.productName = productName;
	    }

	    public String getDescription() {
	        return description;
	    }

	    public void setDescription(String description) {
	        this.description = description;
	    }

	    public Double getPrice() {
	        return price;
	    }

	    public void setPrice(Double price) {
	        this.price = price;
	    }

	   
	    @Override
	    public String toString() {
	        return "Product{" +
	                " productName='" + productName + '\'' +
	                ", description='" + description + '\'' +
	                ", price=" + price +
	                '}';
	    }
	}


