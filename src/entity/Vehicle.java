package entity;

public class Vehicle {

	int vehicleID;
	String model;
	double capacity;
	String type;
	String status;
	
	
	public Vehicle( String model, double capacity,String type,String status){
		
		this.model=model;
		this.capacity=capacity;
		this.type=type;
		this.status=status;
	}
	

	public int getVehicleID() {
		return vehicleID;
	}
	public String getModel() {
		return model;
	}
	public double getCapacity() {
		return capacity;
	}
	public String getType() {
		return type;
	}
	public String getStatus() {
		return status;
	}
	
	public void setVehicleID(int newVehicleID){
	this.vehicleID=newVehicleID;}
	
	public void setModel(String newModel) {
		this.model=newModel;
	}
	
	public void setCapacity(double newCapacity) {
		this.capacity=newCapacity;
	}
	
	public void setType(String newType) {
		this.type=newType;
	}
	
	public void setStatus(String newStatus) {
		this.status=newStatus;
	}
	
	
}
