create database techshop2;
use techshop2;

create table customers(
customerid int primary key auto_increment,
f_name varchar(50) not null,
l_name varchar(50) not null,
email varchar(20) not null,
phone varchar(9) not null,
address varchar(100) not null);
alter table customers modify email varchar(40);

create table products(
productid int primary key auto_increment,
productname varchar(50),
p_description varchar(50),
price int,
category varchar(50)
)auto_increment=100;

create table orders (
orderid int primary key auto_increment,
customerid int,
orderdate date,
totalamount int,
ostatus varchar(20),
foreign key(customerid) references customers(customerid) on delete cascade) auto_increment=200;

create table orderdetails(
orderdetailid int primary key auto_increment,
customerid int,
productid int,
quantity int,
foreign key(customerid) references customers(customerid),
foreign key(productid) references products(productid))auto_increment=300;

create table inventory(
inventoryid int primary key auto_increment,
productid int ,
quantityinstock int,
laststockupdate date,
foreign key(productid) references products(productid))auto_increment=400;
truncate customers;

select*from customers;
select*from orders;
select*from orderdetails;
select*from inventory;
select*from products;

ALTER TABLE customers AUTO_INCREMENT = 1;

INSERT INTO customers (f_name, l_name, email, phone, address) VALUES
("Rokhitha","N","rokhitha@gmail.com","983674561","Pondicherry"),
("Ross","Geller","ross@gmail.com","453678987","Los Angeles"),
("Rachel","Green","rachel@gmail.com","789789789","New York"),
("Monica","Geller","monica@gmail.com","879564536","New york"),
("Chandler","Bing","chandler@gmail.com","78367465","London"),
("Phoebe","Buffale","Ph@gmail.com","348938938","London"),
("Lisa","Kundro","lisa@gmail.com","324654345","Las vegas"),
("Jennifer","Anniston","jeniffer@gmail.com","478930987","Las vegas"),
("Mathew","Perry","mathewperry@gmail.com","839899989","Chicago"),
("Joeshp","Tribiyani","Joe@gmail.com","468987878","New york");

alter table products auto_increment=101;
INSERT INTO products (productname, p_description, price, category) VALUES
('Laptop', 'High-performance laptop', 75000, 'Electronics'),
('Smartphone', 'Latest model with AI camera', 50000, 'Electronics'),
('Headphones', 'Noise-canceling wireless', 7000, 'Accessories'),
('Smartwatch', 'Fitness and health tracking', 15000, 'Wearables'),
('Keyboard', 'Mechanical gaming keyboard', 5000, 'Accessories'),
('Mouse', 'Wireless ergonomic mouse', 3000, 'Accessories'),
('Monitor', '4K Ultra HD Display', 35000, 'Electronics'),
('Tablet', '10-inch display tablet', 25000, 'Electronics'),
('Gaming Console', 'Latest-gen gaming console', 60000, 'Gaming'),
('External Hard Drive', '1TB SSD storage', 12000, 'Storage');

INSERT INTO orders (customerid, orderdate, totalamount, ostatus) VALUES
(1, '2025-03-01', 125000, 'Shipped'),
(2, '2025-03-02', 75000, 'Delivered'),
(3, '2025-03-03', 50000, 'Processing'),
(4, '2025-03-04', 15000, 'Shipped'),
(5, '2025-03-05', 20000, 'Pending'),
(6, '2025-03-06', 5000, 'Cancelled'),
(7, '2025-03-07', 70000, 'Shipped'),
(8, '2025-03-08', 35000, 'Delivered'),
(9, '2025-03-09', 60000, 'Processing'),
(10, '2025-03-10', 12000, 'Pending');

INSERT INTO orderdetails (customerid, productid, quantity) VALUES
(1, 101, 1), 
(2, 102, 1), 
(3, 103, 2), 
(4, 104, 1), 
(5, 105, 1), 
(6, 106, 1), 
(7, 107, 1), 
(8, 108, 1), 
(9, 109, 1), 
(10, 110, 1); 


INSERT INTO inventory (productid, quantityinstock, laststockupdate) VALUES
(101, 50, '2025-02-28'),
(102, 100, '2025-02-27'),
(103, 200, '2025-02-26'),
(104, 30, '2025-02-25'),
(105, 150, '2025-02-24'),
(106, 300, '2025-02-23'),
(107, 80, '2025-02-22'),
(108, 40, '2025-02-21'),
(109, 60, '2025-02-20'),
(110, 20, '2025-02-19');
