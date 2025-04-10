package entity;

public class customer {
	 private int customerID;
	    private String firstName;
	    private String lastName;
	    private String email;
	    private String phone;
	    private String address;
       private String password;
	  
	    public customer( String firstName, String lastName, String email,String password,  String address) {

	        this.firstName = firstName;
	        this.lastName = lastName;
	        this.email = email;
	        this.password=password;
	        this.address = address;
	    }
	    
	    public customer(String firstName,String lastName,String email,String address) {
	    	this.firstName=firstName;
	    	this.lastName=lastName;
	    	this.email=email;
	    	this.address=address;
	    }


	    public int getCustomerID() {
	        return customerID;
	    }
	    public String getPassword() {
	    	return password;
	    }
	    public void setPassword(String password) {
	    	this.password=password;
	    }
	    public void setCustomerID(int customerID) {
	        this.customerID = customerID;
	    }

	    public String getFirstName() {
	        return firstName;
	    }

	    public void setFirstName(String firstName) {
	        this.firstName = firstName;
	    }

	    public String getLastName() {
	        return lastName;
	    }

	    public void setLastName(String lastName) {
	        this.lastName = lastName;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getPhone() {
	        return phone;
	    }

	    public void setPhone(String phone) {
	        this.phone = phone;
	    }

	    public String getAddress() {
	        return address;
	    }

	    public void setAddress(String address) {
	        this.address = address;
	    }
	    public String toString() {
	        return "Customer{" +
	                " firstName='" + firstName + '\'' +
	                ", lastName='" + lastName + '\'' +
	                ", email='" + email + '\'' +
	                ", address='" + address + '\'' +
	                '}';
	    }
	}


