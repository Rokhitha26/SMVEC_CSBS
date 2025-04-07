use  techshop2;

select f_name as firstname, l_name as secondname, email as customerEmail from customers;

select orderdate,(select f_name from customers where customers.customerid=orders.customerid) as CustomerName,
(select productname from products where products.productid=(select productid from  orderdetails where orders.customerid=orderdetails.customerid)limit 1) as Product_Name from orders;


insert into customers (f_name,l_name,email,phone,address) values
("Matt","Lablack","matt@gmail.com","475637839","WashingtonDC");

update products set price=(price*1.10);

delete from orders where orderid=210;

INSERT INTO orders (customerid, orderdate, totalamount, ostatus) VALUES
(8, '2025-09-01', 125000, 'Shipped');

update customers set email="rossgeller@gmail.com", address="Japan" where customerid=2;

update orders set totalamount=(select sum(quantity* (select price from products where products.productid=orderdetails.productid))
from orderdetails where orderdetails.orderid=orders.orderid);

delete from orders where customerid=3;

insert into products(productname,p_description, price,category) values
("Wireless Charger", "for iphone", 4550, "accesories");

update orders set ostatus="shipped" where orderid=214;

select count(orders.customerid) as total_orderplaced, (select f_name as customerName from customers where customers.customerid=orders.customerid) from orders group by orders.customerid;



