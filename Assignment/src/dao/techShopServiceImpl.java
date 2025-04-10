package dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import entity.InitialOrderStage;
import entity.Payment;
import entity.customer;
import entity.product;
import util.DBConnUtil;

public class techShopServiceImpl implements techShopService {
	@Override
	public List<customer> getCustomerDetails(int customerID) throws FileNotFoundException, SQLException, IOException {
		List<customer> customerDetails=new ArrayList<>();
		String sql="select customerFirstName, customerLastName,email,address,password from customers where customer_ID=?";
		try(Connection conn=DBConnUtil.getConnection("src/util/application.properties");){
			PreparedStatement ps=conn.prepareStatement(sql);
			
			ps.setInt(1, customerID);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				String firstName=rs.getString("customerFirstName");
				String lastName=rs.getString("customerLastName");
				String email=rs.getString("email");
				String address=rs.getString("address");
				
				customer objInsert=new customer(firstName,lastName,email,address);
				customerDetails.add(objInsert);
			}
		}
		System.out.println();
		
		return customerDetails;
	}
	public void placeOrder(InitialOrderStage ios, Scanner sc) {
	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties")) {

	        int productID = getProduct(ios.getProductName(), conn);
	        int quantityInStock = getQuantityInStock(conn, productID);

	        if (isProductAvailable(conn, ios.getQuantity(), quantityInStock, productID)) {
	            double productPrice = getProductPrice(conn, productID);
	            double totalAmount = calculateTotalAmount(productPrice, ios.getQuantity());

	            int orderID = insertIntoOrders(conn, ios.getCustomerID(), ios.getDate(), totalAmount);
	            insertIntoOrderDetails(conn, orderID, productID, ios.getQuantity());

	            System.out.println("Order placed successfully with Order ID: " + orderID);
	            sc.nextLine();
	            System.out.print("Enter Payment Method (UPI / Card / Cash): ");
	            String paymentMethod = sc.nextLine().trim();
	            Payment payment = new Payment(orderID, paymentMethod, totalAmount, ios.getDate());
	            processPayment(payment);
	        } else {
	            System.out.println("Insufficient stock available.");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	 @Override
	    public void processPayment(Payment payment) {
	        String sql = "insert into payments (orderID, paymentMethod, amountPaid, paymentDate) values (?, ?, ?, ?)";

	        try (Connection conn = DBConnUtil.getConnection("src/util/application.properties");
	             PreparedStatement ps = conn.prepareStatement(sql)) {
	            if (!isValidOrder(payment.getOrderID(), conn)) {
	                System.out.println("Invalid Order ID. Payment not processed.");
	                return;
	            }
	            ps.setInt(1, payment.getOrderID());
	            ps.setString(2, payment.getPaymentMethod());
	            ps.setDouble(3, payment.getAmountPaid());
	            ps.setDate(4, Date.valueOf(payment.getPaymentDate()));

	            int rows = ps.executeUpdate();
	            if (rows > 0) {
	                System.out.println("Payment recorded successfully.");
	            } else {
	                System.out.println("Payment recording failed.");
	            }

	        } catch (Exception e) {
	            System.out.println("Error while processing payment: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }
	    private boolean isValidOrder(int orderID, Connection conn) throws SQLException {
	        String checkSql = "select orderID from orders where orderID = ?";
	        PreparedStatement ps = conn.prepareStatement(checkSql);
	        ps.setInt(1, orderID);
	        ResultSet rs = ps.executeQuery();
	        return rs.next();
	    }

	 @Override
		public int getProduct(String productName, Connection conn) throws SQLException {
			
			 String sql = "select productId from products where lower(productName) = lower(?)";
			    PreparedStatement stmt = conn.prepareStatement(sql);
			    stmt.setString(1, productName);
			    ResultSet rs = stmt.executeQuery();
			    if (rs.next()) return rs.getInt("productId");
			    throw new SQLException("Product not found.");	
		}

		@Override
		public int getQuantityInStock(Connection conn,int productID) throws SQLException {
			
		    String sql = "select quantityInStock from inventory where productID = ?";
	        try (PreparedStatement ps = conn.prepareStatement(sql)) {
	            ps.setInt(1, productID);
	            ResultSet rs = ps.executeQuery();
	            if (rs.next()) return rs.getInt("quantityInStock");
	        }
	        return 0;
		}
		@Override
		public boolean isProductAvailable(Connection conn, int userQty, int stockQty, int productID) throws SQLException {
			
			  if (userQty <= stockQty) {
		            int updatedQty = stockQty - userQty;
		            String updateSql = "update inventory set quantityInStock = ?, lastStockUpdate = ? where productID = ?";
		            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
		                ps.setInt(1, updatedQty);
		                ps.setDate(2, Date.valueOf(LocalDate.now()));
		                ps.setInt(3, productID);
		                ps.executeUpdate();
		            }
		            return true;
		        }
		        return false;
		}
		
	   public double getProductPrice(Connection conn, int productID) throws SQLException {
	        String sql = "select productPrice from products where productId = ?";
	        try (PreparedStatement ps = conn.prepareStatement(sql)) {
	            ps.setInt(1, productID);
	            ResultSet rs = ps.executeQuery();
	            if (rs.next()) return rs.getDouble("productPrice");
	        }
	        throw new SQLException("Price not found for product ID: " + productID);
	    }

	    public double calculateTotalAmount(double price, int qty) {
	        return price * qty;
	    }
	    
	    public int insertIntoOrders(Connection conn, int customerID, LocalDate date, double totalAmount) throws SQLException {
	        String sql = "insert into orders (customer_ID, orderDate, totalAmount, status) values (?, ?, ?, ?)";
	        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	            ps.setInt(1, customerID);
	            ps.setDate(2, Date.valueOf(date));
	            ps.setDouble(3, totalAmount);
	            ps.setString(4, "Processing Order");
	            ps.executeUpdate();
	            ResultSet rs = ps.getGeneratedKeys();
	            if (rs.next()) return rs.getInt(1);
	        }
	        throw new SQLException("Failed to insert into orders.");
	    }
	    
	    public void insertIntoOrderDetails(Connection conn, int orderID, int productID, int qty) throws SQLException {
	        String sql = "insert into orderdetails (orderID, productID, quantity) values (?, ?, ?)";
	        try (PreparedStatement ps = conn.prepareStatement(sql)) {
	            ps.setInt(1, orderID);
	            ps.setInt(2, productID);
	            ps.setInt(3, qty);
	            ps.executeUpdate();
	        }
	    }
	@Override
	public List<product> getProductDetails() throws FileNotFoundException, SQLException, IOException {
		List<product>showProduct=new ArrayList<>();
		String sql="select productname,productDes,productPrice from products";
		try(Connection conn=DBConnUtil.getConnection("src/util/application.properties");){
			PreparedStatement ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				String productName=rs.getString("productName");
				String productDes=rs.getString("productDes");
				double productPrice=rs.getDouble("productPrice");
				
				product showProd=new product(productName, productDes,productPrice);
				showProduct.add(showProd);
				}
		}
		
		return showProduct;
	}
	@Override
	public void getOrderDetails(int customerID) throws FileNotFoundException, SQLException, IOException {
		String sql = "select p.productName, o.status, o.totalAmount from products p " +
	             "join orderdetails od on p.productId = od.productId " +
	             "join orders o on od.orderID = o.orderID " +
	             "where o.customer_id = ?";

		try(Connection conn=DBConnUtil.getConnection("src/util/application.properties");){
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setInt(1, customerID);
			ResultSet rs= ps.executeQuery();
			while(rs.next()) {
				String PurchaseProductName= rs.getString(1);
				String OrderStatus=rs.getString(2);
				int totalAmount=rs.getInt(3);
				
				System.out.println("Purchased Product Name: "+PurchaseProductName);
				System.out.println();
				System.out.println("Order status: "+OrderStatus);
				System.out.println();
				System.out.println("Total Amount:"+ totalAmount);
				System.out.println();
			}
		}	
	}
	
	@Override
	public void getOrderDetailInfo(int orderID) throws FileNotFoundException, SQLException, IOException {
		String sql = "select p.productName, o.status, o.totalAmount FROM products p " +
	             "join orderdetails od on p.productId = od.productId " +
	             "join orders o on od.orderID = o.orderID " +
	             "where o.order_id = ?";

		try(Connection conn=DBConnUtil.getConnection("src/util/application.properties");){
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setInt(1, orderID);
			ResultSet rs= ps.executeQuery();
			while(rs.next()) {
				String PurchaseProductName= rs.getString(1);
				String OrderStatus=rs.getString(2);
				int totalAmount=rs.getInt(3);				
				System.out.println("Purchased Product Name: "+PurchaseProductName);
				System.out.println();
				System.out.println("Order status: "+OrderStatus);
				System.out.println();
				System.out.println("Total Amount:"+ totalAmount);
				System.out.println();
			}
		}	
	}

	@Override
	public void calculateTotalOrders() {
		String sql = "select count(*) from orders where status = 'Processing Order'";

	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties");
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        if (rs.next()) {
	            int count = rs.getInt(1);
	            System.out.println("Total number of orders with status 'processing': " + count);
	        }

	    } catch (Exception e) {
	        System.out.println(" Error while calculating total processing orders: " + e.getMessage());
	        e.printStackTrace();
	    }
	}
	@Override
	public void updateProductInfo(Scanner sc) {
		   

		    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties")) {

		        System.out.print("Enter the Product ID you want to update: ");
		        int productId = sc.nextInt();
		        sc.nextLine(); // clear buffer

	
		        System.out.print("Enter new Product Name: ");
		        String productName = sc.nextLine();

		        System.out.print("Enter new Product Description: ");
		        String productDes = sc.nextLine();

		        System.out.print("Enter new Product Price: ");
		        double productPrice = sc.nextDouble();

		        String sql = "update products set productName = ?, productDes = ?, productPrice = ? where productId = ?";
		        PreparedStatement ps = conn.prepareStatement(sql);

		        ps.setString(1, productName);
		        ps.setString(2, productDes);
		        ps.setDouble(3, productPrice);
		        ps.setInt(4, productId);

		        int rowsUpdated = ps.executeUpdate();

		        if (rowsUpdated > 0) {
		            System.out.println(" Product information updated successfully.");
		        } else {
		            System.out.println("Product with ID " + productId + " not found.");
		        }

		    } catch (Exception e) {
		        System.out.println(" Error updating product: " + e.getMessage());
		        e.printStackTrace();
		    }
		
	}

	@Override
	public void isProductInStock(Scanner sc1) {
	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties");)
	           {

	        System.out.print("Enter the Product Name to check stock: ");
	       String productName = sc1.nextLine().trim();

	        String sql = "select p.productName, i.quantityInStock " +
	                     "from products p join inventory i on p.productId = i.productID " +
	                     "where p.productName = ?";

	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, productName);

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            int quantity = rs.getInt("quantityInStock");
	            if (quantity > 0) {
	                System.out.println(" Product: " + rs.getString("productName") +
	                                   " | Available Quantity: " + quantity);
	            } else {
	                System.out.println(" Product is currently out of stock.");
	            }
	        } else {
	            System.out.println("Product not found in inventory.");
	        }
	        
	        
	    } catch (Exception e) {
	        System.out.println("Error checking product stock: " + e.getMessage());
	        e.printStackTrace();
	    }
	    
	}


	@Override
	public void updateOrderStatus(Scanner sc) {
		System.out.print("Enter the Order ID to update status: ");
	    int orderID = sc.nextInt();
	    sc.nextLine(); // consume newline

	    String checkSql = "select status from orders where orderID = ?";
	    String updateSql = "update orders set status = 'Order Delivered' where orderID = ?";

	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties")) {
	        
	        PreparedStatement checkStmt = conn.prepareStatement(checkSql);
	        checkStmt.setInt(1, orderID);
	        ResultSet rs = checkStmt.executeQuery();

	        if (rs.next()) {
	            String status = rs.getString("status");
	            if (status.equalsIgnoreCase("Processing Order")) {
	              
	                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
	                updateStmt.setInt(1, orderID);
	                int rows = updateStmt.executeUpdate();

	                if (rows > 0) {
	                    System.out.println(" Order ID " + orderID + " status updated to 'Order Delivered'.");
	                }
	            } else {
	                System.out.println(" Order ID " + orderID + " is already marked as: " + status);
	            }
	        } else {
	            System.out.println(" Order ID " + orderID + " not found.");
	        }

	    } catch (Exception e) {
	        System.out.println("Error updating order status: " + e.getMessage());
	        e.printStackTrace();
	    }
		
	}

	@Override
	public void cancelOrder(Scanner sc) {
	    System.out.print("Enter the Order ID to cancel: ");
	    int orderID = sc.nextInt();
	    sc.nextLine(); 

	    String checkSql = "select status from orders where orderID = ?";
	    String updateSql = "update orders set status = 'Cancelled' where orderID = ?";

	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties")) {
	        
	        PreparedStatement checkStmt = conn.prepareStatement(checkSql);
	        checkStmt.setInt(1, orderID);
	        ResultSet rs = checkStmt.executeQuery();

	        if (rs.next()) {
	            String status = rs.getString("status");

	            if (!status.equalsIgnoreCase("Cancelled")) {
	            
	                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
	                updateStmt.setInt(1, orderID);
	                int rows = updateStmt.executeUpdate();

	                if (rows > 0) {
	                    System.out.println(" Order ID " + orderID + " has been cancelled successfully.");
	                }
	            } else {
	                System.out.println("Order ID " + orderID + " is already cancelled.");
	            }
	        } else {
	            System.out.println(" Order ID " + orderID + " not found.");
	        }

	    } catch (Exception e) {
	        System.out.println("❌ Error while cancelling the order: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

	@Override
	public void calculateSubTotal() {
	    String sql = "select sum(totalAmount) as subtotal from orders";

	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties");
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        if (rs.next()) {
	            double subtotal = rs.getDouble("subtotal");
	            System.out.println(" Subtotal of all orders: ₹" + subtotal);
	        } else {
	            System.out.println("No orders found.");
	        }

	    } catch (Exception e) {
	        System.out.println("Error calculating subtotal: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

	@Override
	public void updateQuantity(Scanner scanner) {
	    System.out.print("Enter Product ID to update quantity: ");
	    int productID = scanner.nextInt();
	    scanner.nextLine(); // clear newline

	    System.out.print("Enter new stock quantity: ");
	    int newQuantity = scanner.nextInt();
	    scanner.nextLine(); // clear newline

	    String sql = "update inventory set quantityInStock = ?, lastStockUpdate = ? where productID = ?";

	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties")) {
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setInt(1, newQuantity);
	        ps.setDate(2, Date.valueOf(LocalDate.now())); 
	        ps.setInt(3, productID);

	        int rows = ps.executeUpdate();

	        if (rows > 0) {
	            System.out.println("Inventory updated successfully for product ID: " + productID);
	        } else {
	            System.out.println("No product found with ID: " + productID);
	        }

	    } catch (Exception e) {
	        System.out.println("Error updating inventory: " + e.getMessage());
	        e.printStackTrace();
	    }
	}


	@Override
	public void addDiscount(Scanner scanner) {


	    System.out.print("Enter Order ID to apply discount: ");
	    int orderID = scanner.nextInt();
	    scanner.nextLine(); 

	    System.out.print("Enter discount percentage (e.g., 10 for 10%): ");
	    double discountPercent = scanner.nextDouble();
	    scanner.nextLine(); 

	    String selectSql = "select totalAmount from orders where orderID = ?";
	    String updateSql = "update orders set totalAmount = ? where orderID = ?";

	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties")) {
	        PreparedStatement selectStmt = conn.prepareStatement(selectSql);
	        selectStmt.setInt(1, orderID);
	        ResultSet rs = selectStmt.executeQuery();

	        if (rs.next()) {
	            double currentAmount = rs.getDouble("totalAmount");
	            double discountAmount = currentAmount * (discountPercent / 100);
	            double newTotal = currentAmount - discountAmount;

	            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
	            updateStmt.setDouble(1, newTotal);
	            updateStmt.setInt(2, orderID);

	            int rows = updateStmt.executeUpdate();
	            if (rows > 0) {
	                System.out.println(" Discount applied successfully!");
	                System.out.println(" New Total Amount: ₹" + newTotal);
	            } else {
	                System.out.println(" Failed to apply discount.");
	            }

	        } else {
	            System.out.println("Order ID not found.");
	        }

	    } catch (Exception e) {
	        System.out.println(" Error applying discount: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

    @Override
	public void addtoInventory(Scanner scanner) {
    	 System.out.print("Enter Product ID to add quantity: ");
 	    int productID = scanner.nextInt();
 	    scanner.nextLine(); 

 	    System.out.print("Enter new stock quantity: ");
 	    int newQuantity = scanner.nextInt();
 	    scanner.nextLine(); 

 	    String sql = "update inventory set quantityInStock = ?, lastStockUpdate = ? WHERE productID = ?";

 	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties")) {
 	        PreparedStatement ps = conn.prepareStatement(sql);
 	        ps.setInt(1, newQuantity);
 	        ps.setDate(2, Date.valueOf(LocalDate.now())); 
 	        ps.setInt(3, productID);

 	        int rows = ps.executeUpdate();

 	        if (rows > 0) {
 	            System.out.println("Inventory updated successfully for product ID: " + productID);
 	        } else {
 	            System.out.println("No product found with ID: " + productID);
 	        }

 	    } catch (Exception e) {
 	        System.out.println(" Error updating inventory: " + e.getMessage());
 	        e.printStackTrace();
 	    }
		
	}
    @Override
    public void removeFromInventory(Scanner scanner) {
        System.out.print("Enter Product ID to update inventory: ");
        int productID = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("Enter quantity to remove: ");
        int qtyToRemove = scanner.nextInt();
        scanner.nextLine(); 

        String selectSql = "select quantityInStock from inventory where productID = ?";
        String updateSql = "update inventory set quantityInStock = ?, lastStockUpdate = ? where productID = ?";

        try (Connection conn = DBConnUtil.getConnection("src/util/application.properties")) {
            
            PreparedStatement selectStmt = conn.prepareStatement(selectSql);
            selectStmt.setInt(1, productID);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int currentStock = rs.getInt("quantityInStock");
                int updatedStock = (qtyToRemove >= currentStock) ? 0 : (currentStock - qtyToRemove);

               
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setInt(1, updatedStock);
                updateStmt.setDate(2, Date.valueOf(LocalDate.now()));
                updateStmt.setInt(3, productID);

                int rows = updateStmt.executeUpdate();

                if (rows > 0) {
                    System.out.println(" Inventory updated for Product ID: " + productID +
                            ". New stock: " + updatedStock);
                } else {
                    System.out.println("Update failed. Product ID not found.");
                }
            } else {
                System.out.println("Product ID " + productID + " not found in inventory.");
            }

        } catch (Exception e) {
            System.out.println("Error removing from inventory: " + e.getMessage());
            e.printStackTrace();
        }
    }

	@Override
	public void updateStockQuantity(Scanner scanner) {
		System.out.print("Enter Product ID to update inventory: ");
        int productID = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("Enter quantity to remove: ");
        int qtyToRemove = scanner.nextInt();
        scanner.nextLine(); 

        String selectSql = "select quantityInStock from inventory where productID = ?";
        String updateSql = "update inventory set quantityInStock = ?, lastStockUpdate = ? where productID = ?";

        try (Connection conn = DBConnUtil.getConnection("src/util/application.properties")) {
            
            PreparedStatement selectStmt = conn.prepareStatement(selectSql);
            selectStmt.setInt(1, productID);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int currentStock = rs.getInt("quantityInStock");
                int updatedStock = (qtyToRemove >= currentStock) ? 0 : (currentStock - qtyToRemove);

               
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setInt(1, updatedStock);
                updateStmt.setDate(2, Date.valueOf(LocalDate.now()));
                updateStmt.setInt(3, productID);

                int rows = updateStmt.executeUpdate();

                if (rows > 0) {
                    System.out.println(" Inventory updated for Product ID: " + productID +
                            ". New stock: " + updatedStock);
                } else {
                    System.out.println("Update failed. Product ID not found.");
                }
            } else {
                System.out.println("Product ID " + productID + " not found in inventory.");
            }

        } catch (Exception e) {
            System.out.println("Error removing from inventory: " + e.getMessage());
            e.printStackTrace();
        }
		
	}@Override
	public void getInventoryValue() {
	    String sql = "select sum(p.productPrice * i.quantityInStock) as totalValue " +
	                 "from products p join inventory i on p.productId = i.productID";

	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties");
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        if (rs.next()) {
	            double totalValue = rs.getDouble("totalValue");
	            System.out.println(" Total Inventory Value: ₹" + totalValue);
	        } else {
	            System.out.println("No inventory data found.");
	        }

	    } catch (Exception e) {
	        System.out.println("Error calculating inventory value: " + e.getMessage());
	        e.printStackTrace();
	    }
	}


	@Override
	public void listLowStockProduct() {
	    int threshold = 20; 

	    String sql = "select p.productName, i.quantityInStock " +
	                 "from products p join inventory i on p.productId = i.productID " +
	                 "where i.quantityInStock < ?";

	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties");
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setInt(1, threshold);
	        ResultSet rs = ps.executeQuery();

	        System.out.println(" Products below stock threshold (" + threshold + " units):");

	        boolean found = false;
	        while (rs.next()) {
	            String name = rs.getString("productName");
	            int qty = rs.getInt("quantityInStock");

	            System.out.println( name + " — " + qty + " units");
	            found = true;
	        }

	        if (!found) {
	            System.out.println("All products are above the low stock threshold.");
	        }

	    } catch (Exception e) {
	        System.out.println("Error fetching low stock products: " + e.getMessage());
	        e.printStackTrace();
	    }}
	    @Override
	    public void listOutOfStockProduct() {
	        String sql = "select p.productName " +
	                     "from products p join inventory i on p.productId = i.productID " +
	                     "where i.quantityInStock = 0";

	        try (Connection conn = DBConnUtil.getConnection("src/util/application.properties");
	             PreparedStatement ps = conn.prepareStatement(sql);
	             ResultSet rs = ps.executeQuery()) {

	            System.out.println(" Out of Stock Products:");

	            boolean found = false;
	            while (rs.next()) {
	                String name = rs.getString("productName");
	                System.out.println(name);
	                found = true;
	            }

	            if (!found) {
	                System.out.println(" No products are currently out of stock.");
	            }

	        } catch (Exception e) {
	            System.out.println(" Error listing out-of-stock products: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }

	    @Override
	    public void listAllProduct() {
	        String sql = "select p.productName, p.productDes, p.productPrice, " +
	                     "i.quantityInStock, i.lastStockUpdate " +
	                     "from products p join inventory i on p.productId = i.productID";

	        try (Connection conn = DBConnUtil.getConnection("src/util/application.properties");
	             PreparedStatement ps = conn.prepareStatement(sql);
	             ResultSet rs = ps.executeQuery()) {

	            System.out.println(" All Products in Inventory:");
	            
	            while (rs.next()) {
	                String name = rs.getString("productName");
	                String desc = rs.getString("productDes");
	                double price = rs.getDouble("productPrice");
	                int stock = rs.getInt("quantityInStock");
	                Date lastUpdate = rs.getDate("lastStockUpdate");

	                System.out.println(name + " | " + desc + " | ₹" + price + " | " + stock + " | " + lastUpdate);
	            }

	        } catch (Exception e) {
	            System.out.println("Error listing all products: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }

	public boolean customerDBInsert(customer customer) throws FileNotFoundException, SQLException, IOException {
		String sql="insert into customers (customerFirstName,customerLastName,email,address,password) values (?,?,?,?,?)";
		try(Connection conn=DBConnUtil.getConnection("src/util/application.properties");){
			PreparedStatement ps=conn.prepareStatement(sql);
			
			ps.setString(1,customer.getFirstName());
			ps.setString(2,customer.getLastName());
			ps.setString(3,customer.getEmail());
			ps.setString(4,customer.getAddress());
			ps.setString(5,customer.getPassword());
			
			int rows=ps.executeUpdate();
			return rows>0;
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public int validatePassenger(String email, String password) throws FileNotFoundException, IOException {
	    String sql = "select customer_ID from customers where email = ? and password = ?";
	    
	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties");
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	         
	        ps.setString(1, email.trim());
	        ps.setString(2, password.trim());

	        System.out.println("Trying with email: " + email.trim() + " and password: " + password.trim()); 

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            return rs.getInt("customer_ID");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return -1;
	}
	
	    
	    public void updateCustomerInfo(int customerID,Scanner scanner) throws FileNotFoundException, SQLException, IOException {
	       

	        System.out.println("---- Update Your Profile ----");

	        System.out.print("Enter new First Name: ");
	        String firstName = scanner.nextLine();

	        System.out.print("Enter new Last Name: ");
	        String lastName = scanner.nextLine();

	        System.out.print("Enter new Email: ");
	        String email = scanner.nextLine();

	        System.out.print("Enter new Address: ");
	        String address = scanner.nextLine();

	        System.out.print("Enter new 4-digit Password: ");
	        String password = scanner.nextLine();

	        String sql = "update customers set customerFirstName = ?, customerLastName = ?, email = ?, address = ?, password = ? where customer_ID = ?";

	        try (Connection conn = DBConnUtil.getConnection("src/util/application.properties")) {
	            PreparedStatement ps = conn.prepareStatement(sql);
	            ps.setString(1, firstName);
	            ps.setString(2, lastName);
	            ps.setString(3, email);
	            ps.setString(4, address);
	            ps.setString(5, password);
	            ps.setInt(6, customerID);

	            int rowsUpdated = ps.executeUpdate();

	            if (rowsUpdated > 0) {
	                System.out.println("Your profile has been updated successfully!");
	            } else {
	                System.out.println("Update failed. No customer found with ID: " + customerID);
	            }
	           
	        }
	    }
	    @Override
	    public void generateTotalSalesReport() {
	        String sql = "select sum(totalAmount) as totalSales from orders where status = 'Order Delivered'";

	        try (Connection conn = DBConnUtil.getConnection("src/util/application.properties");
	             PreparedStatement ps = conn.prepareStatement(sql);
	             ResultSet rs = ps.executeQuery()) {

	            if (rs.next()) {
	                double totalSales = rs.getDouble("totalSales");
	                System.out.println(" Total Sales: ₹" + totalSales);
	            } else {
	                System.out.println("No delivered orders found.");
	            }

	        } catch (Exception e) {
	            System.out.println("Error generating sales report: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }
	   
	   
	}

