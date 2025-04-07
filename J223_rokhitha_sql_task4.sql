use techshop2;

-- SQL query to find out which customers have not placed any orders.

select f_name as customer from customers where
customerid not in (select distinct customerid from orders);

select c.f_name as customerName, o.orderid as ordersid from customers c 
left join orders o on c.customerid=o.customerid where o.customerid is null;

-- n SQL query to find the total number of products available for sale.  
select sum(quantityinstock) as availableProd from inventory;

-- SQL query to calculate the total revenue generated by TechShop.  
select sum(totalamount) from orders where ostatus not in ("Canceled");

--  SQL query to calculate the average quantity ordered for products in a specific category. Allow users to input the category name as a parameter
select avg(od.quantity) as AvgQuantity , p.category as category from orderdetails od join
products p on od.productid=p.productid where p.category="Electronics" group by p.category ;

-- SQL query to calculate the total revenue generated by a specific customer. Allow users to input the customer ID as a parameter. 
select c.customerid as customer , sum(o.totalamount) as totalrevC from orders o
join customers c on c.customerid=o.customerid  where c.customerid=8 group by c.customerid;

-- SQL query to find the customers who have placed the most orders. List their names and the number of orders they've placed.
select c.f_name as customer,  count(orderid) as orderP from orders o join
customers c on c.customerid=o.customerid group by c.f_name order by count(orderid) desc limit 1;

--  SQL query to find the most popular product category, which is the one with the highest total quantity ordered across all orders. 
select p.category as Pcategory, sum(od.quantity) as PQuantity from orderdetails od join
products p on od.productid=p.productid group by p.category order by sum(od.quantity) desc limit 1;

-- SQL query to find the customer who has spent the most money (highest total revenue) on electronic gadgets. List their name and total spending. 
select c.f_name as customer, sum(o.totalamount)as totalRev from orders o
join customers c on c.customerid=o.customerid group by c.f_name  order by sum(o.totalamount) desc limit 1;

-- SQL query to calculate the average order value (total revenue divided by the number of orders) for all customers. 
select sum(totalamount)/count(orderid) as avg_order_Val from orders where ostatus <> "canceled";

--  SQL query to find the total number of orders placed by each customer and list their names along with the order count. 
select c.f_name as customer, count(o.orderid) as Corders from orders o join
customers c on c.customerid=o.customerid group by c.f_name;
