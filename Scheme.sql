-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`Manufacturers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Manufacturers` (
  `manufacturer_ID` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`manufacturer_ID`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Product_categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Product_categories` (
  `category_ID` INT NOT NULL AUTO_INCREMENT,
  `category_name` VARCHAR(45) NOT NULL,
  `parent_category_ID` INT NULL DEFAULT NULL,
  `number_of_subcategories` INT NULL DEFAULT 0,
  PRIMARY KEY (`category_ID`),
  UNIQUE INDEX `category_name_UNIQUE` (`category_name` ASC) VISIBLE,
  INDEX `parent_category_ID_idx` (`parent_category_ID` ASC) VISIBLE,
  CONSTRAINT `parent_category_ID`
    FOREIGN KEY (`parent_category_ID`)
    REFERENCES `mydb`.`Product_categories` (`category_ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Products` (
  `product_ID` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `manufacturer` INT NOT NULL,
  `price` DECIMAL(10,4) NULL,
  `category` INT NOT NULL,
  `mass` DOUBLE NULL,
  `description` VARCHAR(2000) NULL,
  `thumbnail` VARCHAR(255) NULL,
  `warranty_months` TINYINT UNSIGNED NULL DEFAULT 0,
  PRIMARY KEY (`product_ID`),
  INDEX `manufacturer_idx` (`manufacturer` ASC) VISIBLE,
  INDEX `category_idx` (`category` ASC) VISIBLE,
  CONSTRAINT `manufacturer`
    FOREIGN KEY (`manufacturer`)
    REFERENCES `mydb`.`Manufacturers` (`manufacturer_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `category`
    FOREIGN KEY (`category`)
    REFERENCES `mydb`.`Product_categories` (`category_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Suppliers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Suppliers` (
  `supplier_ID` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `phone` VARCHAR(55) NULL,
  `email` VARCHAR(55) NULL,
  `website` VARCHAR(55) NULL,
  PRIMARY KEY (`supplier_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Warehouses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Warehouses` (
  `warehouse_ID` INT NOT NULL AUTO_INCREMENT,
  `capacity` INT NULL,
  `free_capacity` INT NULL,
  `address` VARCHAR(45) NULL,
  `country` VARCHAR(45) NULL,
  `state` VARCHAR(45) NULL,
  PRIMARY KEY (`warehouse_ID`),
  CONSTRAINT `nonnegative_free_capacity`
   CHECK(free_capacity >= 0) 
 )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Inventory`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Inventory` (
  `inventory_ID` INT NOT NULL AUTO_INCREMENT,
  `amount` INT NOT NULL,
  `price` DECIMAL(10,4),
  `delivered_at` DATETIME NOT NULL,
  `available_amount` INT NULL,
  `stored_at` INT NULL,
  `suppliers_price` DECIMAL(10,4) NOT NULL,
  `product_ID` INT NOT NULL,
  `supplier_ID` INT NOT NULL,
  `expiration_date` DATE NULL,
  PRIMARY KEY (`inventory_ID`),
  INDEX `stored_at_idx` (`stored_at` ASC) VISIBLE,
  INDEX `Inventory_product_ID_idx` (`product_ID` ASC) VISIBLE,
  INDEX `supplier_ID_idx` (`supplier_ID` ASC) VISIBLE,
  CONSTRAINT `stored_at`
    FOREIGN KEY (`stored_at`)
    REFERENCES `mydb`.`Warehouses` (`warehouse_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_Inventory_product_ID`
    FOREIGN KEY (`product_ID`)
    REFERENCES `mydb`.`Products` (`product_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `supplier_ID`
    FOREIGN KEY (`supplier_ID`)
    REFERENCES `mydb`.`Suppliers` (`supplier_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `nonnegative_available_amount`
    CHECK(available_amount >= 0))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Employees`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Employees` (
  `employee_ID` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `phone` VARCHAR(45) NULL,
  `address` VARCHAR(45) NOT NULL,
  `city` VARCHAR(45) NOT NULL,
  `state` VARCHAR(45) NULL,
  `ZIP` VARCHAR(45) NOT NULL,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`employee_ID`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Customers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Customers` (
  `customer_ID` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `email` VARCHAR(55) NOT NULL,
  `password` VARCHAR(64) NOT NULL,
  `phone` VARCHAR(45) NULL,
  `shipping_address` VARCHAR(45) NOT NULL,
  `city` VARCHAR(45) NOT NULL,
  `state` VARCHAR(45) NOT NULL,
  `ZIP_code` VARCHAR(45) NOT NULL,
  `shopping_cart` TEXT NULL,
  PRIMARY KEY (`customer_ID`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Order_statuses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Order_statuses` (
  `status_ID` INT NOT NULL AUTO_INCREMENT,
  `status_type` VARCHAR(55) NOT NULL,
  PRIMARY KEY (`status_ID`),
  UNIQUE INDEX `status_type_UNIQUE` (`status_type` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Orders` (
  `order_ID` INT NOT NULL AUTO_INCREMENT,
  `inventory_ID` INT NOT NULL,
  `amount` INT NOT NULL,
  `order_received_at` DATETIME NULL,
  `order_delivered_at` DATETIME NULL,
  `returned_reason` VARCHAR(2000) NULL,
  `status` INT NOT NULL,
  `handled_by` INT,
  `ordered_by` INT NOT NULL,
  PRIMARY KEY (`order_ID`),
  INDEX `inventory_ID_idx` (`inventory_ID` ASC) VISIBLE,
  INDEX `handled_by_idx` (`handled_by` ASC) VISIBLE,
  INDEX `ordered_by_idx` (`ordered_by` ASC) VISIBLE,
  INDEX `status_idx` (`status` ASC) VISIBLE,
  CONSTRAINT `inventory_ID`
    FOREIGN KEY (`inventory_ID`)
    REFERENCES `mydb`.`Inventory` (`inventory_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `handled_by`
    FOREIGN KEY (`handled_by`)
    REFERENCES `mydb`.`Employees` (`employee_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `ordered_by`
    FOREIGN KEY (`ordered_by`)
    REFERENCES `mydb`.`Customers` (`customer_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `status`
    FOREIGN KEY (`status`)
    REFERENCES `mydb`.`Order_statuses` (`status_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Category_filters`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Category_filters` (
  `filter_ID` INT NOT NULL AUTO_INCREMENT,
  `category_ID` INT NOT NULL,
  `filter` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`filter_ID`),
  INDEX `category_ID_idx` (`category_ID` ASC) VISIBLE,
  CONSTRAINT `category_ID`
    FOREIGN KEY (`category_ID`)
    REFERENCES `mydb`.`Product_categories` (`category_ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Product_pictures`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Product_pictures` (
  `picture_ID` INT NOT NULL AUTO_INCREMENT,
  `product_ID` INT NOT NULL,
  `picture_URI` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`picture_ID`),
  INDEX `Product_pictures_product_ID_idx` (`product_ID` ASC) VISIBLE,
  UNIQUE INDEX `picture_URI_UNIQUE` (`picture_URI` ASC) VISIBLE,
  CONSTRAINT `FK_Product_pictures_product_ID`
    FOREIGN KEY (`product_ID`)
    REFERENCES `mydb`.`Products` (`product_ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Product_reviews`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Product_reviews` (
  `grade` TINYINT UNSIGNED NOT NULL,
  `comment` VARCHAR(2500) NOT NULL,
  `product_ID` INT NOT NULL,
  `customer` INT NOT NULL,
  PRIMARY KEY (`product_ID`, `customer`),
  INDEX `Product_reviews_product_ID_idx` (`product_ID` ASC) VISIBLE,
  INDEX `customer_idx` (`customer` ASC) VISIBLE,
  CONSTRAINT `FK_Product_reviews_product_ID`
    FOREIGN KEY (`product_ID`)
    REFERENCES `mydb`.`Products` (`product_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `customer`
    FOREIGN KEY (`customer`)
    REFERENCES `mydb`.`Customers` (`customer_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Filter_values`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Filter_values` (
  `filter_value_ID` INT NOT NULL AUTO_INCREMENT,
  `filter_ID` INT NOT NULL,
  `value` VARCHAR(45) NOT NULL,
  `total_products` INT NULL DEFAULT 0,
  PRIMARY KEY (`filter_value_ID`),
  INDEX `filter_ID_idx` (`filter_ID` ASC) VISIBLE,
  INDEX `filter_value_index` (`value` ASC) VISIBLE,
  UNIQUE INDEX `value_UNIQUE` (`value` ASC) VISIBLE,
  CONSTRAINT `filter_ID`
    FOREIGN KEY (`filter_ID`)
    REFERENCES `mydb`.`Category_filters` (`filter_ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Product_filter_values`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Product_filter_values` (
  `product_ID` INT NOT NULL,
  `filter_value_ID` INT NOT NULL,
  PRIMARY KEY (`product_ID`, `filter_value_ID`),
  INDEX `filter_value_ID_idx` (`filter_value_ID` ASC) VISIBLE,
  CONSTRAINT `FK_Product_filter_values_product_ID`
    FOREIGN KEY (`product_ID`)
    REFERENCES `mydb`.`Products` (`product_ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `filter_value_ID`
    FOREIGN KEY (`filter_value_ID`)
    REFERENCES `mydb`.`Filter_values` (`filter_value_ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `mydb` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
