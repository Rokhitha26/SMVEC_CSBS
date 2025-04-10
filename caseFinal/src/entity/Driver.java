package entity;

public class Driver {

	int driverID;
	String driverName;
	String dPhoneNumber;
	String dStatus;
	String licenseNumber;
	
	public Driver(String driverName, String dPhoneNumber,String licenseNumber) {
		this.driverName=driverName;
		this.dPhoneNumber=dPhoneNumber;
		this.licenseNumber=licenseNumber;
		this.dStatus="Available";
	}
	
	public Driver(int driverID,String driverName, String dPhoneNumber, String dStatus,String licenseNumber) {
		this.driverID=driverID;
		this.driverName=driverName;
		this.dPhoneNumber=dPhoneNumber;
		this.dStatus=dStatus;
		this.licenseNumber=licenseNumber;
	}
public Driver() {
	
}
	public int getDriverID() {
		return driverID;
	}
	public String getDriverName() {
		return driverName;
	}
	public String getDPhoneNumber() {
		return dPhoneNumber;
	}
	public String getDStatus() {
		return dStatus;
	}
	public String getlicenseNumber() {
		return licenseNumber;
	}
	
	public void setDStatus(String dStatus) {
		this.dStatus=dStatus;
	}
	public void setDriverID(int driverID) {
		this.driverID=driverID;
	}
	public void setDstatus(String dStatus) {
		this.dStatus=dStatus;
	}
	
	public String toString() {
		return "dID"+driverID+", dName: "+driverName+",dPhone: "+dPhoneNumber+", dStatus: "+dStatus+" ,License: "+licenseNumber;
	}
	
}
