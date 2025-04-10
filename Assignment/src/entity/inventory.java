package entity;

import java.time.LocalDate;

public class inventory {
	 private int inventoryID;
	    private product product;          
	    private int quantityInStock;
	    private LocalDate lastStockUpdate;

	    public inventory(int inventoryID, product product, int quantityInStock, LocalDate lastStockUpdate) {
	        this.inventoryID = inventoryID;
	        this.product = product;
	        this.quantityInStock = quantityInStock;
	        this.lastStockUpdate = lastStockUpdate;
	    }

	
	    public int getInventoryID() {
	        return inventoryID;
	    }

	    public void setInventoryID(int inventoryID) {
	        this.inventoryID = inventoryID;
	    }

	    public product getProduct() {
	        return product;
	    }

	    public void setProduct(product product) {
	        this.product = product;
	    }

	    public int getQuantityInStock() {
	        return quantityInStock;
	    }

	    public void setQuantityInStock(int quantityInStock) {
	        this.quantityInStock = quantityInStock;
	    }

	    public LocalDate getLastStockUpdate() {
	        return lastStockUpdate;
	    }

	    public void setLastStockUpdate(LocalDate lastStockUpdate) {
	        this.lastStockUpdate = lastStockUpdate;
	    }

	   
	    @Override
	    public String toString() {
	        return "Inventory{" +
	                "inventoryID=" + inventoryID +
	                ", product=" + product +
	                ", quantityInStock=" + quantityInStock +
	                ", lastStockUpdate=" + lastStockUpdate +
	                '}';
	    }
}
