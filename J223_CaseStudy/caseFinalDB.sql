create database finalCS;
use finalCS;

CREATE TABLE Vehicles (
    VehicleID INT AUTO_INCREMENT PRIMARY KEY,
    Model VARCHAR(255) NOT NULL,
    Capacity DECIMAL(10, 2) NOT NULL,
    Type VARCHAR(50),
    Status VARCHAR(50)
);
CREATE TABLE Trips (
    TripID INT AUTO_INCREMENT PRIMARY KEY,
    VehicleID INT,
    RouteID INT,
    DepartureDate DATETIME NOT NULL,
    ArrivalDate DATETIME,
    Status VARCHAR(50),
    TripType VARCHAR(50) DEFAULT 'Freight',
    MaxPassengers INT,
    driverID INT,
    FOREIGN KEY (VehicleID) REFERENCES Vehicles(VehicleID),
    FOREIGN KEY (RouteID) REFERENCES Routes(RouteID)
);
CREATE TABLE Bookings (
    BookingID INT AUTO_INCREMENT PRIMARY KEY,
    TripID INT,
    PassengerID INT,
    BookingDate DATETIME NOT NULL,
    Status VARCHAR(50),
    FOREIGN KEY (TripID) REFERENCES Trips(TripID),
    FOREIGN KEY (PassengerID) REFERENCES Passengers(PassengerID)
);
CREATE TABLE Routes (
    RouteID INT AUTO_INCREMENT PRIMARY KEY,
    StartDestination VARCHAR(255) NOT NULL,
    EndDestination VARCHAR(255) NOT NULL,
    Distance DECIMAL(10, 2) NOT NULL
);
CREATE TABLE Passengers (
    PassengerID INT AUTO_INCREMENT PRIMARY KEY,
    FirstName VARCHAR(255) NOT NULL,
    Gender VARCHAR(255) CHECK (Gender IN ('Male', 'Female', 'Other')),
    Age INT CHECK (Age > 0),
    Email VARCHAR(255) UNIQUE NOT NULL,
    PhoneNumber VARCHAR(50)
);
CREATE TABLE Driver (
    driverID INT AUTO_INCREMENT PRIMARY KEY,
    driverName VARCHAR(255) NOT NULL,
    dPhoneNumber VARCHAR(50) NOT NULL,
    dStatus VARCHAR(50) DEFAULT 'Available',
    licenseNumber VARCHAR(100) NOT NULL UNIQUE
);
INSERT INTO vehicles (Model, Capacity, Type, Status) VALUES  
('Tata Winger', 15.00, 'Van', 'Available'),
('Ashok Leyland Bus', 45.00, 'Bus', 'Available'),
('Mahindra Supro', 12.00, 'Van', 'Maintenance'),
('Volvo B9R', 50.00, 'Bus', 'Available'),
('Eicher Skyline', 35.00, 'Bus', 'On Trip'),
('Force Traveller', 20.00, 'Van', 'Available'),
('SML Isuzu', 32.00, 'Bus', 'Available'),
('BharatBenz Coach', 49.00, 'Bus', 'Available'),
('Toyota Hiace', 14.00, 'Van', 'On Trip'),
('Eicher Pro 3015', 30.00, 'Truck', 'Available');

ALTER TABLE routes
ADD COLUMN TravelDate DATE NOT NULL;

ALTER TABLE routes
ADD COLUMN Price DECIMAL(10,2) NOT NULL;

ALTER TABLE routes
ADD COLUMN JourneyLabel VARCHAR(50);

INSERT INTO routes (StartDestination, EndDestination, Distance, TravelDate, Price, JourneyLabel) VALUES
('Chennai', 'Bangalore', 346.00, '2025-04-10', 550.00, 'Journey 1'),
('Hyderabad', 'Pune', 561.00, '2025-04-11', 700.00, 'Journey 2'),
('Mumbai', 'Delhi', 1418.00, '2025-04-12', 1200.00, 'Journey 3'),
('Kolkata', 'Chennai', 1670.00, '2025-04-13', 950.00, 'Journey 4'),
('Bangalore', 'Hyderabad', 569.00, '2025-04-14', 600.00, 'Journey 5'),
('Pune', 'Mumbai', 148.00, '2025-04-15', 400.00, 'Journey 6'),
('Delhi', 'Ahmedabad', 940.00, '2025-04-16', 800.00, 'Journey 7'),
('Ahmedabad', 'Surat', 265.00, '2025-04-17', 300.00, 'Journey 8'),
('Chennai', 'Coimbatore', 505.00, '2025-04-18', 550.00, 'Journey 9'),
('Delhi', 'Chandigarh', 243.00, '2025-04-19', 450.00, 'Journey 10');
alter table trips add column driverID int,
     add constraint foreign key(driverID) references driver(driverID)
    on delete cascade;

INSERT INTO driver (driverName, dPhoneNumber, dStatus, licenseNumber)
VALUES 
('Ross Geller', '9876543210', 'Available', 'DL-RG1001'),
('Rachel Green', '9876543211', 'Available', 'DL-RG1002'),
('Monica Geller', '9876543212', 'Available', 'DL-MG1003'),
('Chandler Bing', '9876543213', 'Available', 'DL-CB1004'),
('Joey Tribbiani', '9876543214', 'Available', 'DL-JT1005'),
('Phoebe Buffay', '9876543215', 'Available', 'DL-PB1006'),
('Janice Litman', '9876543216', 'Available', 'DL-JL1007'),
('Gunther Central', '9876543217', 'Available', 'DL-GC1008'),
('Mike Hannigan', '9876543218', 'Available', 'DL-MH1009'),
('Carol Willick', '9876543219', 'Available', 'DL-CW1010');

ALTER TABLE Vehicles 
MODIFY COLUMN VehicleID INT NOT NULL AUTO_INCREMENT;

INSERT INTO Vehicles (Model, Capacity, Type, Status) VALUES 
('Toyota Hiace', 15.00, 'Van', 'Available'),
('Ford Transit', 18.00, 'Van', 'Available'),
('Volvo FMX', 40.00, 'Truck', 'Available'),
('Ashok Leyland', 42.00, 'Bus', 'Available'),
('Tata Ace', 12.00, 'Truck', 'Available'),
('Mercedes Sprinter', 20.00, 'Van', 'Available'),
('Scania Touring', 50.00, 'Bus', 'Available'),
('Isuzu NPR', 25.00, 'Truck', 'Available'),
('Eicher Pro', 28.00, 'Truck', 'Available'),
('Mahindra Supro', 10.00, 'Van', 'Available');

INSERT INTO driver (driverName, dPhoneNumber, dStatus, licenseNumber) VALUES
('Ross Geller', '1234567890', 'Available', 'NY1234R'),
('Rachel Green', '2345678901', 'Available', 'NY1234G'),
('Monica Geller', '3456789012', 'Available', 'NY1234M'),
('Chandler Bing', '4567890123', 'Available', 'NY1234C'),
('Joey Tribbiani', '5678901234', 'Available', 'NY1234J'),
('Phoebe Buffay', '6789012345', 'Available', 'NY1234P');

INSERT INTO driver (driverName, dPhoneNumber, dStatus, licenseNumber) VALUES
('Gunther Centralperk', '7890123456', 'Available', 'NY1235G'),
('Janice Hosenstein', '8901234567', 'Available', 'NY1235J'),
('Mike Hannigan', '9012345678', 'Available', 'NY1235M'),
('Carol Willick', '9123456789', 'Available', 'NY1235C'),
('Susan Bunch', '9234567890', 'Available', 'NY1235S'),
('Emily Waltham', '9345678901', 'Available', 'NY1235E'),
('David TheScientist', '9456789012', 'Available', 'NY1235D'),
('Frank Buffay Jr.', '9567890123', 'Available', 'NY1235F'),
('Estelle Leonard', '9678901234', 'Available', 'NY1235L'),
('Richard Burke', '9789012345', 'Available', 'NY1235R');


ALTER TABLE trips
MODIFY COLUMN DepartureDate DATE ;

ALTER TABLE trips
MODIFY COLUMN ArrivalDate DATE ;

INSERT INTO routes (StartDestination, EndDestination, Distance, TravelDate, Price, JourneyLabel)
VALUES 
('Chennai', 'Madurai', 462.00, '2025-04-20', 500.00, 'Journey 11'),
('Bangalore', 'Mysore', 145.00, '2025-04-21', 300.00, 'Journey 12'),
('Hyderabad', 'Vizag', 620.00, '2025-04-22', 650.00, 'Journey 13'),
('Delhi', 'Jaipur', 281.00, '2025-04-23', 400.00, 'Journey 14'),
('Pune', 'Nagpur', 715.00, '2025-04-24', 750.00, 'Journey 15'),
('Mumbai', 'Goa', 589.00, '2025-04-25', 800.00, 'Journey 16'),
('Ahmedabad', 'Udaipur', 262.00, '2025-04-26', 350.00, 'Journey 17'),
('Kolkata', 'Bhubaneswar', 441.00, '2025-04-27', 600.00, 'Journey 18'),
('Lucknow', 'Kanpur', 82.00, '2025-04-28', 200.00, 'Journey 19'),
('Surat', 'Nashik', 234.00, '2025-04-29', 320.00, 'Journey 20');

