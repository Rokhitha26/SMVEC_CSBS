package dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import entity.InitialOrderStage;
import entity.Payment;
import entity.customer;
import entity.product;
public interface techShopService {
	
	void calculateTotalOrders();
	List<customer> getCustomerDetails(int customerID) throws FileNotFoundException, SQLException, IOException;
	 public void updateCustomerInfo(int customerID,Scanner scanner) throws FileNotFoundException, SQLException, IOException;
	List<product> getProductDetails() throws FileNotFoundException, SQLException, IOException;
	void getOrderDetailInfo(int orderID) throws FileNotFoundException, SQLException, IOException;
	void updateProductInfo(Scanner sc);
	void isProductInStock(Scanner sc);
	void updateOrderStatus(Scanner scanner);
	void cancelOrder(Scanner get);
	void getOrderDetails(int customerId) throws FileNotFoundException, SQLException, IOException;
	void calculateSubTotal();
	void updateQuantity(Scanner scanner);
	void addDiscount(Scanner scanner);
	void generateTotalSalesReport();
	void processPayment(Payment payment) ;
	int getProduct(String productName,Connection conn) throws SQLException;
	int getQuantityInStock(Connection conn,int productID) throws SQLException;
	void addtoInventory(Scanner scanner);
	void removeFromInventory(Scanner scanner);
	void updateStockQuantity(Scanner scanner);
	boolean isProductAvailable(Connection conn, int userQty, int stockQty, int productID) throws SQLException;
	void getInventoryValue();
	void listLowStockProduct();
	void listOutOfStockProduct();
	void listAllProduct();
	boolean customerDBInsert(customer customer) throws FileNotFoundException, SQLException, IOException;
	int validatePassenger(String email, String password) throws FileNotFoundException, IOException;
	void placeOrder(InitialOrderStage IOS,Scanner scanner);
	double getProductPrice(Connection conn, int productID) throws SQLException;
	double calculateTotalAmount(double price, int qty);
	int insertIntoOrders(Connection conn, int customerID, LocalDate date, double totalAmount) throws SQLException;
    void insertIntoOrderDetails(Connection conn, int orderID, int productID, int qty) throws SQLException;
	
    
}
