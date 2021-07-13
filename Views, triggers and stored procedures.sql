USE `mydb` ;

-- PROCEDURES

DELIMITER $$
CREATE PROCEDURE insert_category(in category_name VARCHAR(45), parent_category_ID INT)
	BEGIN 
		-- the problem with this procedure is that it allows insertion of categories that are a subcategory of itself.This is solved by a trigger.
    
        -- check if the parent doesn't exist
        IF (parent_category_ID IS NOT NULL) THEN
			IF (NOT EXISTS(SELECT * FROM mydb.Product_categories WHERE category_ID = parent_category_ID)) THEN
				SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = "Given parent doesn't exist!";
            END IF;
            INSERT INTO mydb.Product_categories(category_name, parent_category_ID) VALUES (category_name, parent_category_ID);
            UPDATE mydb.Product_categories SET number_of_subcategories = number_of_subcategories + 1 WHERE category_ID = parent_category_ID;
        ELSE
			INSERT INTO mydb.Product_categories(category_name, parent_category_ID) VALUES (category_name, NULL);
		END IF;  
	END$$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE delete_category(in category_ID INT, parent_category_ID INT)
	BEGIN 
		IF (parent_category_ID IS NOT NULL) THEN
			IF(NOT EXISTS(SELECT * FROM mydb.Product_categories WHERE mydb.Product_categories.category_ID = parent_category_ID)) THEN
				SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = "Given parent doesn't exist!";
            ELSE
				UPDATE mydb.Product_categories SET number_of_subcategories=number_of_subcategories-1 WHERE mydb.Product_categories.category_ID = parent_category_ID;
			END IF;
		END IF;
        DELETE FROM mydb.Product_categories WHERE Product_categories.category_ID=category_ID;
    END$$
DELIMITER ;

-- TRIGGERS

DELIMITER $$

USE `mydb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mydb`.`Product_categories_BEFORE_INSERT` BEFORE INSERT ON `Product_categories` FOR EACH ROW
BEGIN
	IF (NEW.category_ID=NEW.parent_category_ID) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'You cannot set a category to be its own parent category.';
    END IF;
END$$

USE `mydb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mydb`.`Inventory_BEFORE_INSERT` BEFORE INSERT ON `Inventory` FOR EACH ROW
BEGIN
	UPDATE mydb.Warehouses SET free_capacity = free_capacity - NEW.amount WHERE Warehouses.warehouse_ID = NEW.stored_at;
END$$


USE `mydb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mydb`.`Orders_BEFORE_INSERT` BEFORE INSERT ON `Orders` FOR EACH ROW
BEGIN
	UPDATE mydb.inventory SET available_amount = available_amount - NEW.amount WHERE inventory.inventory_ID=NEW.inventory_ID;
END$$

USE `mydb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mydb`.`Product_filter_values_AFTER_INSERT` AFTER INSERT ON `Product_filter_values` FOR EACH ROW
BEGIN
	UPDATE Filter_values SET total_products=total_products+1
    WHERE Filter_values.filter_value_ID=NEW.filter_value_ID;
END$$

USE `mydb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mydb`.`Product_filter_values_AFTER_DELETE` AFTER DELETE ON `Product_filter_values` FOR EACH ROW
BEGIN
	UPDATE Filter_values SET total_products=total_products-1
	WHERE Filter_values.filter_value_ID=OLD.filter_value_ID;
END$$

USE `mydb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mydb`.`Inventory_AFTER_INSERT` AFTER INSERT ON `Inventory` FOR EACH ROW
BEGIN
	UPDATE Products SET price = NEW.price WHERE Products.product_ID = NEW.product_ID AND Products.price IS NULL; 
END$$

-- VIEWS

-- -----------------------------------------------------
-- Placeholder table for view `mydb`.`employee_Employee_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`employee_Employee_view` (`employee_ID` INT, `username` INT, `password` INT);

-- -----------------------------------------------------
-- Placeholder table for view `mydb`.`customer_Customers_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`customer_Customers_view` (`email` INT, `password` INT, `shopping_cart` INT);

-- -----------------------------------------------------
-- Placeholder table for view `mydb`.`guest_register_customer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`guest_register_customer` (`customer_ID` INT, `name` INT, `last_name` INT, `email` INT, `password` INT, `phone` INT, `shipping_address` INT, `city` INT, `state` INT, `ZIP_code` INT, `shopping_cart` INT);


-- -----------------------------------------------------
-- View `mydb`.`employee_Employee_view`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`employee_Employee_view`;
USE `mydb`;
CREATE  OR REPLACE VIEW `employee_Employee_view` AS
SELECT employee_ID, username, password FROM Employees;

-- -----------------------------------------------------
-- View `mydb`.`customer_Customers_view`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`customer_Customers_view`;
USE `mydb`;
CREATE  OR REPLACE VIEW `customer_Customers_view` AS
SELECT customer_ID, email, password, shopping_cart FROM Customers;

-- -----------------------------------------------------
-- View `mydb`.`guest_register_customer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`guest_register_customer`;
USE `mydb`;
CREATE  OR REPLACE VIEW `guest_register_customer` AS
SELECT * FROM Customers;


DELIMITER ;

CREATE USER 'customer' IDENTIFIED BY 'customerPass';

CREATE USER 'employee' IDENTIFIED BY 'employeePass';

GRANT SELECT ON Products TO customer;
GRANT SELECT ON Product_pictures TO customer;
GRANT SELECT, INSERT, DELETE, UPDATE ON Product_reviews TO customer;
GRANT SELECT ON Product_categories TO customer;
GRANT SELECT ON Category_filters TO customer;
GRANT SELECT ON Filter_values TO customer;
GRANT SELECT ON Product_filter_values TO customer;
GRANT INSERT ON Orders TO customer;
GRANT SELECT ON Manufacturers TO customer;
GRANT SELECT, UPDATE ON customer_Customers_view TO customer;
GRANT SELECT ON Inventory TO customer;
GRANT INSERT ON Orders TO customer;


GRANT SELECT, INSERT, UPDATE ON Products TO employee;
GRANT SELECT, DELETE ON Product_categories TO employee;
GRANT SELECT ON Product_reviews TO employee;
GRANT SELECT ON Product_pictures TO employee;
GRANT SELECT ON Customers TO employee;
GRANT SELECT, INSERT ON Suppliers TO employee;
GRANT SELECT ON Warehouses TO employee;
GRANT SELECT ON Manufacturers TO employee;
GRANT SELECT ON Orders TO employee;
GRANT SELECT, INSERT ON Inventory TO employee;
GRANT SELECT, INSERT, DELETE ON Category_filters TO employee;
GRANT SELECT, INSERT, DELETE ON Filter_values TO employee;
GRANT SELECT, INSERT, DELETE ON Product_filter_values TO employee;
GRANT SELECT ON employee_Employee_view TO employee;


GRANT EXECUTE ON PROCEDURE insert_category TO employee;
GRANT EXECUTE ON PROCEDURE delete_category TO employee;
