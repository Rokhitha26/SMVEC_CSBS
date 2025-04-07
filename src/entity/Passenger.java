package entity;

public class Passenger {

	int passengerID;
	String firstName;
	String gender;
	int age;
	String email;
	String phone;
	
	Passenger(String firstName,String gender,int age,String email,String phone){
	//	this.passengerID=(int)(System.currentTimeMillis() % 1000000000);
		this.firstName=firstName;
		this.gender=gender;
		this.age=age;
		this.email=email;
		this.phone=phone;
	}
	
	public int getPassengerID() {
		return passengerID;
	}
	public String firstName() {
		return firstName;
	}
	public String getGender() {
		return gender;
	}
	public int getAge() {
		return age;
	}
	public String getEmail() {
		return email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPassengerID(int passengerID) {
		this.passengerID=passengerID;
	}
	public void setFirstName(String firstName) {
		this.firstName=firstName;
	}
	public void setGender(String gender) {
		this.gender=gender;
	}
	public void setAge(int age) {
		this.age=age;
	}
	public void setEmail(String email) {
		this.email=email;
	}
	public void setPhone(String phone) {
		this.phone=phone;
	}
}
