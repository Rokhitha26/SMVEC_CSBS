create database TechShop;
use TechShop;

create table Customers( CustomerID int primary key,
FirstName varchar(50),
LastName varchar(50),
Email varchar(50),
Phone varchar(9),
Address varchar(50));

create table Products( ProductID varchar(9) primary key,
ProductName varchar(50),
Descriptions varchar(100),
Price decimal(10,2));

create table Orders( OrderID varchar(10) primary key,
CustomerID int,
OrderDate date,
TotalAmount decimal(10,2),
foreign key(CustomerID) references Customers(CustomerID));

create table OrderDetails(
OrderDetailID varchar(10) primary key,
OrderID varchar(10),
ProductID varchar(9),
Quantity int,
foreign key (OrderId) references Orders(OrderId),
foreign key(ProductID) references Products(ProductID));

create table Inventory( 
InventoryID int primary key,
ProductID varchar(9) ,
QuantityInStock int,
LastStockUpdate int,
foreign key(ProductID) references Products(ProductID));

insert into Customers values
(101,"Rokhitha","N","rokhitha@gmail.com","324123452","Puducherry"),
(102,"Riya","N","riya@gmail.com","284913748","Puducherry"),
(103,"Ravi", "S","ravi@gmail.com","184763829","Cuddalure"),
(104,"Meha","S","meha@gmail.com","536718936","Karaikal"),
(105,"John","A","john@gmail.com","463781123","Mahe"),
(106,"Rose","K","rose@gmail.com","367818934","Puducherry"),
(107,"Jack","K","jack@gmail.com","473912345","Cuddalure"),
(108,"Hana","A","hana@gmail.com","478391931","Karaikal"),
(109,"Giraha","N","giraha@gmail.com","472821245","Mahe"),
(110,"Harini","G","harini@gmail.com","738193738","Cuddalure");
select*from Customers;

insert into Products values
("P1","Laptop","14 inch display", 60000),
("P2","Smartphone","6.5 inch screen",13000),
("P3","Headphone","Noise-cancellation",4500),
("P4","Smartwatch","Heart-rate monitor",13000),
("P5","Gaming Mouse","RGB lighting",12000),
("P6","Keyboard","RGB Lighting", 30000),
("P7","Speaker","Bluetooth connection",12000),
("P8","Tablet","10-inch display",14000),
("P9","Webcam","Auto-focus",3000),
("P10","Monitor","Anti-glare",18000);

insert into Orders values
("or1",101,"2024-02-10",60000),
("or2",102,"2024-02-11",13000),
("or3",103,"2024-02-12",4500),
("or4",104,"2024-02-13",13000),
("or5",105,"2024-02-13",12000),
("or6",106,"2024-02-14",30000),
("or7",107,"2024-02-15",12000),
("or8",108,"2024-02-16",14000),
("or9",109,"2024-02-16",3000),
("or10",110,"2024-02-16",18000);

insert into OrderDetails values
("od1","or1","P1",1),
("od2","or2","P2",1),
("od3","or3","P3",1),
("od4","or4","P4",1),
("od5","or5","P5",1),
("od6","or6","P6",1),
("od7","or7","P7",1),
("od8","or8","P8",1),
("od9","or9","P9",1),
("od10","or10","P10",1);

insert into Inventory values
(1,"P1",144, 145),
(2,"P2",123,125),
(3,"P3",132,133),
(4,"P4",145,144),
(5,"P5",156,159),
(6,"P6",90,99),
(7,"P7",87,90),
(8,"P8",32,40),
(9,"P9",55,60),
(10,"P10",60,65);
