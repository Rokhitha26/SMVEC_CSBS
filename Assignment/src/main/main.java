package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import entity.InitialOrderStage;
import entity.customer;
import entity.product;

import dao.techShopService;
import dao.techShopServiceImpl;

public class main {
public static void main(String[]args) throws FileNotFoundException, IOException, SQLException  {
	Scanner get=new Scanner(System.in);
	
	techShopService service=new techShopServiceImpl();
	int userType;
	do {
		System.out.println("-----TechShop Application-----");
		System.out.println("1.Customer");
		System.out.println("2.Employee");
		System.out.println("3.Exit TechShop");
		System.out.println();
		System.out.println("Enter your type: ");
		userType=get.nextInt();
		get.nextLine();
		int customerID = 0;
		switch(userType) {
		case 1:
			System.out.println("-----Customer DashBoard-----");
			System.out.println();
			int authChoice;
			boolean loggedIn=false;
			
			do {
				System.out.println("1.create Account");
				System.out.println("2.Login");
				System.out.println("Choose Option: ");
				authChoice=get.nextInt();
				get.nextLine();
				
				switch(authChoice) {
				case 1:
					System.out.println("Enter FirstName: ");
					String firstName=get.nextLine();
					System.out.println("Enter LastName");
					String lastName=get.nextLine();
					System.out.println("Enter Email: ");
					String email=get.nextLine().trim();
					System.out.println("Enter Password: ");
					String password= get.nextLine().trim();
					
					System.out.println("Enter Address: ");
					String address= get.nextLine();
					
					boolean customerInsert=service.customerDBInsert(new customer(firstName,lastName,email,password,address));
					if(customerInsert) {
					  System.out.println("Account Created Successfully, now login");
					}
					else {
						System.out.println("Error Occured");
					}
					break;
				case 2:
					System.out.print("Enter your email: ");
					String loginEmail = get.nextLine().trim();

					System.out.print("Enter your password (4 digits): ");
					String loginPassword = get.nextLine().trim();


					 customerID = service.validatePassenger(loginEmail, loginPassword);

					while (customerID == -1) {
					    System.out.println("Invalid login. Please register or check your credentials.");
						System.out.print("Enter your email: ");
						 loginEmail = get.nextLine().trim();

						System.out.print("Enter your password (4 digits): ");
						 loginPassword = get.nextLine().trim();
						 customerID=service.validatePassenger(loginEmail, loginPassword);
						 
					} 
					System.out.println("ID: "+customerID);
					loggedIn=true;
					break;	
				}
			}while(!loggedIn);
			int customerOption;
			do {
				System.out.println("-----Customer DashBoard-----");
				System.out.println();
				System.out.println("1.View Product Catlog");
				System.out.println("2.Place Order");
				System.out.println("3.View Order Status");
				System.out.println("4.View My profile");
				System.out.println("5.Update My profile");
				System.out.println("6.Exit DashBoard");
				System.out.println();
				System.out.println("Enter your choice: ");
				customerOption=get.nextInt();
				get.nextLine();
				
			    switch(customerOption) {
			    case 1:
			    	System.out.println("-----Viewing Customer Option-----");
			    	System.out.println();
			    	List<product>showProducts=service.getProductDetails();
			    	for(product showProd:showProducts) {
			    		System.out.println(showProd);
			    	}
			    	System.out.println();
			    	break;
			    case 2:
			    	System.out.println("-----Place Order-----");
			    	System.out.println();
			    	List<product>inPlaceOrder=service.getProductDetails();
			    	for(product showProd:inPlaceOrder) {
			    		System.out.println(showProd);
			    	}
			    	System.out.println();
			    	System.out.println("enter the product name to be purchased: ");
			    	String productName=get.nextLine();
			    	System.out.println("Enter the quantity of product: ");
			    	int productQuantity=get.nextInt();
			    	LocalDate currentDate=LocalDate.now();
			    	 service.placeOrder(new InitialOrderStage(productName,productQuantity,currentDate,customerID),get);
			    	System.out.println();
			    	break;
			    	
			    case 3:
			    	System.out.println("------Viewing Order Status-----");
			    	service.getOrderDetails(customerID);
			    	break;
			    	
			    case 4:
			    	System.out.println("-----Viewing My Profile-----");
			    	System.out.println();
			    	
			    	List<customer>customerProfile=service.getCustomerDetails(customerID);
			    	for(customer customerData: customerProfile ) {
			    		System.out.println(customerData);
			    	}
			    	
			    	System.out.println();
			    	break;
			    case 5:
			    	System.out.println();
			    	service.updateCustomerInfo(customerID,get);
			    	break;
			    case 6:
			    	System.out.println("Exiting DashBoard...");
			    	break;
			    	default:
			    		System.out.println("Enter a valid choice!");
			    }			
			}while(customerOption!=6);
			
			break;
			
			
		case 2:
		
		    int adminChoice = 0; 
		    do {
		        System.out.println("-----Employee Management DashBoard-----");
		        System.out.println("1.Calculate total Orders");
		        System.out.println("2.Update Product Information");
		        System.out.println("3.Check Product in Stock Availability");
		        System.out.println("4.Update OrderStatus");
		        System.out.println("5.Cancel Orders");
		        System.out.println("6.Calculate Sub Total");
		        System.out.println("7.UpdateQuantity of product");
		        System.out.println("8.Add to inventory");
		        System.out.println("9.Remove from Inventory");
		        System.out.println("10.updateStockQuantity");
		        System.out.println("11.get Inventory value");
		        System.out.println("12.List low stock Inventory");
		        System.out.println("13.List out of stock inventory");
		        System.out.println("14.List all products");
		        System.out.println("15.Apply discount");
		        System.out.println("16.Get Sales Report");
		        System.out.println("17. Exit Admin Dashboard.");
		        System.out.println();
		        System.out.println("Enter your choice: ");
		        
		       adminChoice=get.nextInt();
		       get.nextLine();
		            switch(adminChoice) {
		                case 1:
		                    System.out.println();
		                    System.out.println("Calculated total pending orders: ");
		                    service.calculateTotalOrders();
		                    System.out.println();
		                    break;
		                case 2:
		                    System.out.println();
		                    service.updateProductInfo(get);
		                    System.out.println();
		                    break;
		                case 3:
		                    System.out.println();
		                    service.isProductInStock(get);
		                    System.out.println();
		                    break;
		                case 4:
		                    System.out.println();
		                    System.out.println("Update Order Status");
		                    service.updateOrderStatus(get);
		                    System.out.println();
		                    break;
		                case 5:
		                    System.out.println();
		                    System.out.println("Cancel Order");
		                    service.cancelOrder(get);
		                    System.out.println();
		                    break;
		                case 6:
		                    System.out.println();
		                    System.out.println("Calculate SubTotal");
		                    service.calculateSubTotal();
		                    System.out.println();
		                    break;
		                case 7:
		                    System.out.println();
		                   System.out.println("UpdateQuantity");
		                   service.updateQuantity(get);
		                    System.out.println();
		                    break;
		                case 8:
		                    System.out.println();
		                    System.out.println("Add to inventory");
		                    service.addtoInventory(get);
		                    System.out.println();
		                    break;
		                case 9:
		                    System.out.println();
		                    System.out.println("Remove from Inventory");
		                    service.removeFromInventory(get);
		                    System.out.println();
		                    break;
		                case 10:
		                    System.out.println();
		                    System.out.println("Update Stock Quantity");
		                    service.updateStockQuantity(get);
		                    System.out.println();
		                    break;
		                case 11:
		                    System.out.println();
		                    System.out.println("Get Inventory value");
		                    service.getInventoryValue();
		                    System.out.println();
		                    break;
		                case 12:
		                    System.out.println();
		                    System.out.println("List low stock Inventory");
		                    service.listLowStockProduct();
		                    System.out.println();
		                    break;
		                case 13:
		                    System.out.println();
		                    service.listOutOfStockProduct();
		                    System.out.println();
		                    break;
		                case 14:
		                    System.out.println();
		                    service.listAllProduct();
		                    System.out.println();
		                    break;
		                case 15:
		                	System.out.println();
		                	service.addDiscount(get);
		                	System.out.println();
		                	break;
		                case 16:
		                	System.out.println();
		                	System.out.println("------Sales Report------");
		                	service.generateTotalSalesReport();
		                	System.out.println();
		                case 17:
		                    System.out.println("Exiting Admin Dashboard..");
		                    break;
		                    
		                    
		                default:
		                    System.out.println("Enter valid choice!");
		            }
		        
		    } while(adminChoice != 17);
		    break;
			
		case 3:
			System.out.println("Exiting TechShop...");
			break;
			default:
				System.out.println("Enter a valid choice");
		}
		
	}while(userType!=3);
	get.close();
}

}