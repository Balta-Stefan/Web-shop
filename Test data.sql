USE `mydb` ;

INSERT INTO Manufacturers(name) VALUES("Chiquita");
INSERT INTO Manufacturers(name) VALUES("Mondelez");
INSERT INTO Manufacturers(name) VALUES("Nivea");
INSERT INTO Manufacturers(name) VALUES("Ferrero");
INSERT INTO Manufacturers(name) VALUES("GSK");

CALL insert_category("Food items", NULL);
CALL insert_category("Fruit", 1);
CALL insert_category("Confectionery", 1);
CALL insert_category("Body care", NULL);

INSERT INTO Category_filters(category_ID, filter) VALUES(2, "Type");
INSERT INTO Category_filters(category_ID, filter) VALUES(4, "Type");
INSERT INTO Category_filters(category_ID, filter) VALUES(3, "Chocolate");

INSERT INTO Filter_values(filter_ID, value) VALUES(1, "Berries");
INSERT INTO Filter_values(filter_ID, value) VALUES(1, "Exotic");
INSERT INTO Filter_values(filter_ID, value) VALUES(1, "Melons");
INSERT INTO Filter_values(filter_ID, value) VALUES(2, "Creams");
INSERT INTO Filter_values(filter_ID, value) VALUES(2, "Toothpastes");
INSERT INTO Filter_values(filter_ID, value) VALUES(3, "White");
INSERT INTO Filter_values(filter_ID, value) VALUES(3, "Milky");
INSERT INTO Filter_values(filter_ID, value) VALUES(3, "Dark");
INSERT INTO Filter_values(filter_ID, value) VALUES(3, "Spreads");


INSERT INTO Products(name, manufacturer, price, category, mass, description, thumbnail, warranty_months) VALUES("Bananas", 1, 1.15, 2, 1.0, "Bananas are a fruit that is good only when unripe and horrible when ripe.", "https://cdn.mos.cms.futurecdn.net/42E9as7NaTaAi4A6JcuFwG-970-80.jpg.webp", 0);
INSERT INTO Products(name, manufacturer, price, category, mass, description, thumbnail, warranty_months) VALUES("Watermelon", 1, 0.7, 2, 1.0, "Good luck finding one that won't go straight into the trash.", "https://www.gardeningknowhow.com/wp-content/uploads/2021/05/whole-and-slices-watermelon.jpg", 0);
INSERT INTO Products(name, manufacturer, price, category, mass, description, thumbnail, warranty_months) VALUES("Milka white", 2, 3.5, 3, 0.1, "Chocolate as white as Africa after some tea drinkers discovered it.", "https://www.fresh-store.eu/9210/milka-white-chocolate-100-g-34-oz.jpg", 0);
INSERT INTO Products(name, manufacturer, price, category, mass, description, thumbnail, warranty_months) VALUES("Milka dark", 2, 4.5, 3, 0.09, "By adding 10% more cocoa, price can apparently be raised by 28.5%.Still, nowhere as dark as your web history.", "https://austriansupermarket-live.imgix.net/85/b8/c8/1601134030/9065_mil4073542.jpg?auto=compress%2Cformat&q=75", 0);
INSERT INTO Products(name, manufacturer, price, category, mass, description, thumbnail, warranty_months) VALUES("Nivea creme", 3, 8.5, 4, 0.1, "A 110 year old cream.At least few wars have been waged since then.Certainly a battle tested product.", "https://images-na.ssl-images-amazon.com/images/I/81CGBGpbNqL._SX679_.jpg", 0);
INSERT INTO Products(name, manufacturer, price, category, mass, description, thumbnail, warranty_months) VALUES("Nutella", 4, 10.5, 3, 1, "50% sugar, 45% of nobodyknows, 5% of hazelnuts.", "https://images-na.ssl-images-amazon.com/images/I/714A58g3xWS.jpg", 0);
INSERT INTO Products(name, manufacturer, price, category, mass, description, thumbnail, warranty_months) VALUES("Sensodyne extra sensitive", 5, 10.5, 4, 0.14, "A toothpaste that can't even take some criticism.", "https://images.heb.com/is/image/HEBGrocery/001857226?fit=constrain,1&wid=800&hei=800&fmt=jpg&qlt=85,0&resMode=sharp2&op_usm=1.75,0.3,2,0", 0);


INSERT INTO Product_filter_values(product_ID, filter_value_ID) VALUES(1, 2);
INSERT INTO Product_filter_values(product_ID, filter_value_ID) VALUES(2, 3);
INSERT INTO Product_filter_values(product_ID, filter_value_ID) VALUES(3, 6);
INSERT INTO Product_filter_values(product_ID, filter_value_ID) VALUES(4, 8);
INSERT INTO Product_filter_values(product_ID, filter_value_ID) VALUES(5, 4);
INSERT INTO Product_filter_values(product_ID, filter_value_ID) VALUES(6, 9);
INSERT INTO Product_filter_values(product_ID, filter_value_ID) VALUES(7, 5);

INSERT INTO Suppliers(name, phone, email, website) VALUES("Honduras", null, null, null);
INSERT INTO Suppliers(name, phone, email, website) VALUES("Silk road Inc.", null, null, null);
INSERT INTO Suppliers(name, phone, email, website) VALUES("Foodman and sons", "672-345-7928", "logistics@foodman.com", "foodman.com");

INSERT INTO Warehouses(capacity, free_capacity, address, country, state) VALUES(1000, 1000, "Warehouse st. 1", null ,null);
INSERT INTO Warehouses(capacity, free_capacity, address, country, state) VALUES(100, 5, null, null, null);

INSERT INTO Inventory(amount, price, delivered_at, available_amount, stored_at, suppliers_price, product_ID, supplier_ID, expiration_date) VALUES(, , , , , , , , ,);
INSERT INTO Inventory(amount, price, delivered_at, available_amount, stored_at, suppliers_price, product_ID, supplier_ID, expiration_date) VALUES(, , , , , , , , ,);
INSERT INTO Inventory(amount, price, delivered_at, available_amount, stored_at, suppliers_price, product_ID, supplier_ID, expiration_date) VALUES(, , , , , , , , ,);
INSERT INTO Inventory(amount, price, delivered_at, available_amount, stored_at, suppliers_price, product_ID, supplier_ID, expiration_date) VALUES(, , , , , , , , ,);


INSERT INTO Employees(name, last_name, phone, address, city, state, ZIP, username, password) VALUES("", "", "", "", "", "", "", "", "");
INSERT INTO Employees(name, last_name, phone, address, city, state, ZIP, username, password) VALUES("", "", "", "", "", "", "", "", "");
INSERT INTO Employees(name, last_name, phone, address, city, state, ZIP, username, password) VALUES("", "", "", "", "", "", "", "", "");


INSERT INTO Order_statuses(status_type) VALUES("Ordered");
INSERT INTO Order_statuses(status_type) VALUES("Delivered");
INSERT INTO Order_statuses(status_type) VALUES("Returned");
INSERT INTO Order_statuses(status_type) VALUES("On hold");

INSERT INTO Customers(name, last_name, email, password, phone, shipping_address, city, state, ZIP_code, shopping_cart) VALUES("", "", "", "", "", "", "", "", "", "");
INSERT INTO Customers(name, last_name, email, password, phone, shipping_address, city, state, ZIP_code, shopping_cart) VALUES("", "", "", "", "", "", "", "", "", "");
INSERT INTO Customers(name, last_name, email, password, phone, shipping_address, city, state, ZIP_code, shopping_cart) VALUES("", "", "", "", "", "", "", "", "", "");
INSERT INTO Customers(name, last_name, email, password, phone, shipping_address, city, state, ZIP_code, shopping_cart) VALUES("", "", "", "", "", "", "", "", "", "");

INSERT INTO Product_reviews(product_ID, customer, grade, comment) VALUES(, , , "");
INSERT INTO Product_reviews(product_ID, customer, grade, comment) VALUES(, , , "");
INSERT INTO Product_reviews(product_ID, customer, grade, comment) VALUES(, , , "");
INSERT INTO Product_reviews(product_ID, customer, grade, comment) VALUES(, , , "");


INSERT INTO Orders(inventory_ID, amount, ordered_received_at, order_delivered_at, returned_reason, status, handled_by, ordered_by) VALUES(, , , , "", , , );
INSERT INTO Orders(inventory_ID, amount, ordered_received_at, order_delivered_at, returned_reason, status, handled_by, ordered_by) VALUES(, , , , "", , , );
INSERT INTO Orders(inventory_ID, amount, ordered_received_at, order_delivered_at, returned_reason, status, handled_by, ordered_by) VALUES(, , , , "", , , );
INSERT INTO Orders(inventory_ID, amount, ordered_received_at, order_delivered_at, returned_reason, status, handled_by, ordered_by) VALUES(, , , , "", , , );










