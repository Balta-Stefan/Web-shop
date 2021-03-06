package webStore.DAO;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import webStore.model.Customer;
import webStore.model.Employee;
import webStore.model.Inventory;
import webStore.model.Manufacturer;
import webStore.model.Order;
import webStore.model.Order_status;
import webStore.model.Product;
import webStore.model.Product_category;
import webStore.model.Product_review;
import webStore.model.Supplier;
import webStore.model.Warehouse;
import webStore.responses.FilterValuesOfCategory;
import webStore.responses.ID_string_pair;

public class MySQL_DAO implements DAOInterface
{
    // Format of MySQL datetime: yyy-mm-dd hh:mm:ss

    private final Connection connection;

    // SQL query constants
    private static final String addProductQuery = "INSERT INTO Products(name, manufacturer, category, mass, description, thumbnail, warranty_months) VALUES(?, ?, ?, ?, ?, ?, ?)";// done
    private static final String addProductPicture = "INSERT INTO Product_pictures(product_ID, picture_URI) VALUES(?, ?)"; // done
    //private static final String addProductCategory = "INSERT INTO Product_categories(category_name) VALUES(?)";
   
    private static final String addFilterValues = "INSERT INTO Filter_values(filter_ID, value) VALUES(?, ?)"; // done
    private static final String addFilterToProduct = "INSERT INTO Product_filter_values(product_ID, filter_value_ID) VALUES(?, ?)"; // done
    private static final String getWarehouses = "SELECT * FROM Warehouses"; // done
    private static final String getSuppliers = "SELECT * FROM suppliers"; // done
    private static final String getInventory = "SELECT * FROM Inventory"; // done
    private static final String addToInventory = "INSERT INTO Inventory(amount, price, delivered_at, available_amount, stored_at, suppliers_price, product_ID, supplier_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?)"; // done
    private static final String addOrder = "INSERT INTO Orders(inventory_ID, amount, order_received_at, order_delivered_at, returned_reason, status, handled_by, ordered_by) VALUES(?, ?, ?, ?, ?, ?, ?, ?)"; // done
    private static final String addReturnedReason = "UPDATE Orders SET returned_reason=? WHERE order_ID=?"; // done
    private static final String getEmployee = "SELECT * FROM Employees WHERE username=?"; // done
    private static final String getCustomer = "SELECT * FROM Customers WHERE email=?"; // done
    private static final String getCustomerByID = "SELECT * FROM Customers WHERE customer_ID = ?"; // done
    private static final String registerCustomer = "INSERT INTO Customers(name, last_name, email, password, phone, shipping_address, city, state, ZIP_code) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)"; // done
    private static final String addReview = "INSERT INTO Product_reviews(grade, comment, product_ID, customer) VALUES(?, ?, ?, ?)"; // done
    private static final String getAllProducts = "SELECT * FROM Products"; // done
    private static final String addManufacturer = "INSERT INTO Manufacturers(name) VALUES(?)"; // done
    private static final String getManufacturers = "SELECT * FROM Manufacturers"; // done
    private static final String getAllOrderStatusTypes = "SELECT * FROM Order_statuses"; // done
    private static final String insert_category_call = "CALL insert_category(?, ?)"; // done
    private static final String delete_category_call = "CALL delete_category(?, ?)"; // done
    private static final String get_main_categories = "SELECT category_ID, category_name FROM Product_categories WHERE parent_category_ID IS NULL"; // done
    private static final String get_category = "SELECT * FROM Product_categories WHERE category_ID=?"; // done
    private static final String get_subcategories = "SELECT category_ID, category_name FROM Product_categories WHERE parent_category_ID = ?"; // done
    private static final String addCategoryFilter = "INSERT INTO Category_filters(category_ID, filter) VALUES(?, ?)"; // done
    private static final String get_filters = "select T.filter, T.filter_ID, T.filter_value_ID, T.value from (select * from (product_categories join Category_filters using(category_ID) join filter_values using(filter_ID)) ) AS T where T.category_ID=?"; // done
    private static final String get_product = "SELECT * FROM Products WHERE product_ID = ?"; // done
    private static final String get_manufacturer = "SELECT * FROM Manufacturers WHERE manufacturer_ID = ?"; // done
    private static final String update_shopping_cart = "UPDATE Customers SET shopping_cart = ? WHERE customer_ID = ?"; // done
    private static final String get_all_orders = "SELECT * FROM Orders"; // done
    private static final String get_order = "SELECT * FROM Orders WHERE order_ID = ?"; // done
    private static final String get_product_from_inventory = "SELECT * FROM Inventory WHERE product_ID = ?"; // done
    private static final String update_product_price = "UPDATE Products SET price = ? WHERE product_ID = ?"; // done
    
    
    // prepared statements
    private final PreparedStatement addProductStatement;
    private final PreparedStatement addProductPictureStatement;
    //private final PreparedStatement addProductCategoryStatement;
    private final PreparedStatement addCategoryFilterStatement;
    private final PreparedStatement addFilterValuesStatement;
    private final PreparedStatement addFilterToProductStatement;
    private final PreparedStatement getWarehousesStatement;
    private final PreparedStatement getSuppliersStatement;
    private final PreparedStatement getInventoryStatement;
    private final PreparedStatement addToInventoryStatement;
    private final PreparedStatement addOrderStatement;
    private final PreparedStatement addReturnedReasonStatement;
    private final PreparedStatement getEmployeeStatement;
    private final PreparedStatement getCustomerStatement;
    private final PreparedStatement registerCustomerStatement;
    private final PreparedStatement addReviewStatement;
    private final PreparedStatement getAllProductsStatement;
    private final PreparedStatement addManufacturerStatement;
    private final PreparedStatement getManufacturersStatement;
    private final PreparedStatement getAllOrderStatusTypesStatement;
    private final PreparedStatement getMainCategoriesStatement;
    private final PreparedStatement getCategoryStatement;
    private final PreparedStatement getSubcategoriesStatement;
    private final PreparedStatement getFiltersStatement;
    private final PreparedStatement getProductStatement;
    private final PreparedStatement getManufacturerStatement;
    private final PreparedStatement updateShoppingCartStatement;
    private final PreparedStatement getCustomerByIDStatement;
    private final PreparedStatement getAllOrdersStatement;
    private final PreparedStatement getOrderStatement;
    private final PreparedStatement getProductFromInventoryStatement;
    private final PreparedStatement updateProductPriceStatement;
    
    private final CallableStatement insert_category_statement;
    private final CallableStatement delete_category_statement;

    public MySQL_DAO(Connection connection) throws SQLException
    {
        this.connection = connection;

        addProductStatement = connection.prepareStatement(addProductQuery);
        addProductPictureStatement = connection.prepareStatement(addProductPicture);
        //addProductCategoryStatement = connection.prepareStatement(addProductCategory);
        addCategoryFilterStatement = connection.prepareStatement(addCategoryFilter);
        addFilterValuesStatement = connection.prepareStatement(addFilterValues);
        addFilterToProductStatement = connection.prepareStatement(addFilterToProduct);
        getWarehousesStatement = connection.prepareStatement(getWarehouses);
        getSuppliersStatement = connection.prepareStatement(getSuppliers);
        getInventoryStatement = connection.prepareStatement(getInventory);
        addToInventoryStatement = connection.prepareStatement(addToInventory);
        addOrderStatement = connection.prepareStatement(addOrder);
        addReturnedReasonStatement = connection.prepareStatement(addReturnedReason);
        getEmployeeStatement = connection.prepareStatement(getEmployee);
        getCustomerStatement = connection.prepareStatement(getCustomer);
        registerCustomerStatement = connection.prepareStatement(registerCustomer);
        addReviewStatement = connection.prepareStatement(addReview);
        getAllProductsStatement = connection.prepareStatement(getAllProducts);
        addManufacturerStatement = connection.prepareStatement(addManufacturer);
        getManufacturersStatement = connection.prepareStatement(getManufacturers);
        getAllOrderStatusTypesStatement = connection.prepareStatement(getAllOrderStatusTypes);
        getMainCategoriesStatement = connection.prepareStatement(get_main_categories);
        getCategoryStatement = connection.prepareStatement(get_category);
        getSubcategoriesStatement = connection.prepareStatement(get_subcategories);
        getFiltersStatement = connection.prepareStatement(get_filters);
        getProductStatement = connection.prepareStatement(get_product);
        getManufacturerStatement = connection.prepareStatement(get_manufacturer);
        updateShoppingCartStatement = connection.prepareStatement(update_shopping_cart);
        getCustomerByIDStatement = connection.prepareStatement(getCustomerByID);
        getAllOrdersStatement = connection.prepareStatement(get_all_orders);
        getOrderStatement = connection.prepareStatement(get_order);
        getProductFromInventoryStatement = connection.prepareStatement(get_product_from_inventory);
        updateProductPriceStatement = connection.prepareStatement(update_product_price);
        
        insert_category_statement = connection.prepareCall(insert_category_call);
        delete_category_statement = connection.prepareCall(delete_category_call);
    }
    
    public void updatePrice(int product_ID, BigDecimal newPrice)
    {
    	try
    	{
    		updateProductPriceStatement.setBigDecimal(1,  newPrice);
    		updateProductPriceStatement.setInt(2,  product_ID);
    		
    		updateProductPriceStatement.execute();
    	}
    	catch(SQLException e) {return;}
    }
    
    public List<Order> getOrders()
    {
    	try
    	{
    		List<Order> orders = new ArrayList<>();
    		
    		ResultSet results = getAllOrdersStatement.executeQuery();
    		
    		while(results.next())
    		{
        		Order tempOrder =  new Order(results.getInt("order_ID"), 
								           results.getInt("inventory_ID"),
								           results.getInt("amount"), 
								           (LocalDateTime)(results.getObject("order_received_at", LocalDateTime.class)),
								           (LocalDateTime)(results.getObject("order_delivered_at", LocalDateTime.class)),
								           results.getString("returned_reason"),
								           results.getInt("status"),
								           results.getInt("handled_by"),
								           results.getInt("ordered_by"));
        		orders.add(tempOrder);
    		}
    		return orders;
    	}
    	catch(SQLException e) {return null;}
    }
    public Order getOrder(int orderID)
    {
    	try
    	{
    		getOrderStatement.setInt(1, orderID);
    		ResultSet result = getOrderStatement.executeQuery();
    		
    		result.next();
    		
    		return new Order(result.getInt("order_ID"), 
			           result.getInt("inventory_ID"),
			           result.getInt("amount"), 
			           (LocalDateTime)(result.getObject("order_received_at", LocalDateTime.class)),
			           (LocalDateTime)(result.getObject("order_delivered_at", LocalDateTime.class)),
			           result.getString("returned_reason"),
			           result.getInt("status"),
			           result.getInt("handled_by"),
			           result.getInt("ordered_by"));
    	}
    	catch(SQLException e) {return null;}
    }
  
    public boolean updateShoppingCart(Customer customer)
    {
    	try
    	{
    		updateShoppingCartStatement.setString(1, customer.shopping_cart);
    		updateShoppingCartStatement.setInt(2,  customer.customer_ID);
    		updateShoppingCartStatement.execute();
    		
    		return true;
    	}
    	catch(SQLException e) {return false;}
    }
    
    public Manufacturer getManufacturer(int manufacturerID)
    {
    	try
    	{
    		getManufacturerStatement.setInt(1, manufacturerID);
    		ResultSet result = getManufacturerStatement.executeQuery();
    		
    		if(result.next() == false)
    			return null;
    		return new Manufacturer(result.getInt("manufacturer_ID"), result.getString("name"));
    	}
    	catch(SQLException e) {return null;}
    }
    
    public Product getProduct(int productID)
    {
    	try
    	{
    		getProductStatement.setInt(1, productID);
    		ResultSet results = getProductStatement.executeQuery();
    		
    		if(results.next() == false)
    			return null;
    		
    		Manufacturer manufacturer = getManufacturer(results.getInt("manufacturer"));
    		
    		return new Product(results.getInt("product_ID"), 
    				           results.getString("name"),
    				           results.getInt("manufacturer"), 
    				           results.getBigDecimal("price"),
    				           results.getInt("category"),
    				           results.getDouble("mass"),
    				           results.getString("description"),
    				           results.getString("thumbnail"),
    				           results.getByte("warranty_months"),
    				           manufacturer.name);
    	}
    	catch(SQLException e) {return null;}
    }

    public Product_category getCategory(int categoryID)
    {
    	try
    	{
    		getCategoryStatement.setInt(1,  categoryID);
    		ResultSet result = getCategoryStatement.executeQuery();
    		
    		if (result.next() == false)
    		{    
    		    // result set is empty (category doesn't exist
    			return null;
    		} 
    		//Product_category(int category_ID, String category_name, Integer parent_category_ID, int number_of_subcategories)
    		// result.getInt("category_ID");
    		return new Product_category(result.getInt("category_ID"), result.getString("category_name"), result.getInt("parent_category_ID"), result.getInt("number_of_subcategories"));
    	}
    	catch(SQLException e) {return null;}
    }
    
    public List<ID_string_pair> get_subcategories(int parentID)
    {
    	try
    	{
    		List<ID_string_pair> response = new ArrayList<>();
    		getSubcategoriesStatement.setInt(1,  parentID);
    		ResultSet results = getSubcategoriesStatement.executeQuery();
    		
    		while(results.next())
    		{
    			response.add(new ID_string_pair(results.getInt("category_ID"), results.getString("category_name")));
    		}
    		return response;
    	}
    	catch(SQLException e) {return null;}
    }
    
    public FilterValuesOfCategory get_filters(int categoryID)
    {
    	/* 
  			obtains filters and their values of the given category such that the JSON object obtained from results looks like following:
    			{
    				[
    					filter_ID: xxx,
    					filter_name: xxx,
    					filter_values:
    					[
    						{
    							filter_value_ID: xxx,
    							value: xxx
    						},...
    					]
    				],...
    			}
    	*/
    	FilterValuesOfCategory filtersWithValues = new FilterValuesOfCategory();
    	try
    	{
    		//List<ID_string_pair> filters = new ArrayList<>();
    		getFiltersStatement.setInt(1,  categoryID);
    		ResultSet results = getFiltersStatement.executeQuery();
    		
    		while(results.next())
    		{
    			filtersWithValues.addFilterOrValue(results.getInt("filter_value_ID"), results.getInt("filter_ID"), results.getString("filter"), results.getString("value"));
    			//filters.add(new ID_string_pair(results.getInt("filter_value_ID"), results.getString("value")));
    		}
    		return filtersWithValues;
    	}
    	catch(SQLException e) {System.out.println(e);return null;}
    }
    
    
    public List<ID_string_pair> getMainCategories()
    {
    	try
    	{
    		List<ID_string_pair> categories = new ArrayList<>();
    		ResultSet results = getMainCategoriesStatement.executeQuery();
    		
    		while(results.next())
    		{
    			ID_string_pair tmp = new ID_string_pair(results.getInt("category_ID"), results.getString("category_name"));
    			categories.add(tmp);
    		}
    		return categories;
    	}
    	catch(SQLException e) {return null;}
    }
    
    public List<Order_status> getAllOrderStatusTypes()
    {
        try
        {
            List<Order_status> list = new ArrayList<>();
            ResultSet results = getAllOrderStatusTypesStatement.executeQuery();

            while(results.next())
            {
                Order_status temp = new Order_status(results.getInt("status_ID"), results.getString("status_type"));
                list.add(temp);
            }

            return list;
        }
        catch(SQLException e){return null;}
    }

    public List<Manufacturer> getManufacturers()
    {
        try
        {
            List<Manufacturer> list = new ArrayList<>();

            ResultSet results = getManufacturersStatement.executeQuery();

            while(results.next())
            {
                Manufacturer temp = new Manufacturer(results.getInt("manufacturer_ID"), results.getString("name"));
                list.add(temp);
            }


            return list;
        }
        catch(SQLException e){return null;}
    }

    public boolean addManufacturer(Manufacturer manufacturer)
    {
        try
        {
            addManufacturerStatement.setString(1, manufacturer.name);

            addManufacturerStatement.executeUpdate();
        }
        catch(SQLException e){return false;}

        return true;
    }

    @Override
    public boolean addProduct(Product product)
    {
    	// price is set to null.This is because price is automatically set when a product is added to inventory.
        try
        {
            addProductStatement.setString(1, product.name);
            addProductStatement.setInt(2, product.manufacturer_ID);
            //addProductStatement.setBigDecimal(3, product.price);
            addProductStatement.setInt(3, product.category_ID);
            addProductStatement.setDouble(4, product.mass);
            addProductStatement.setString(5, product.description);
            addProductStatement.setString(6, product.thumbnail);
            addProductStatement.setByte(7, product.warranty_months);

            addProductStatement.executeUpdate();
        }
        catch(SQLException e){return false;}

        return true;
    }

    @Override
    public boolean addProductPictures(Product product, String[] picturePaths)
    {
        try
        {
            addProductPictureStatement.setInt(1, product.product_ID);

            for(String s : picturePaths)
            {
                addProductPictureStatement.setString(2, s);
                addProductPictureStatement.executeUpdate();
            }
        }
        catch(SQLException e){return false;}

        return true;
    }
    
    @Override
    public boolean deleteProductCategory(Product_category category)
    {
    	try
    	{
    		delete_category_statement.setInt(1, category.category_ID);
    		if(category.parent_category_ID == null)
    			delete_category_statement.setNull(2, java.sql.Types.INTEGER);
        	else 
        		delete_category_statement.setInt(2, category.parent_category_ID);
    		
    		delete_category_statement.executeUpdate();
    	}
    	catch(SQLException e) {return false;}
    	
    	return true;
    }

    @Override
    public boolean addProductCategory(Product_category category)
    {
        try
        {
            insert_category_statement.setString(1, category.category_name);
        	if(category.parent_category_ID == null)
        		insert_category_statement.setNull(2, java.sql.Types.INTEGER);
        	else 
        		insert_category_statement.setInt(2, category.parent_category_ID);
        	insert_category_statement.executeUpdate();
        }
        catch(SQLException e){return false;}

        return true;
    }

    @Override
    public boolean addCategoryFilters(int category_ID, String filter)
    {
        try
        {
            addCategoryFilterStatement.setInt(1, category_ID);
            addCategoryFilterStatement.setString(2, filter);
            addCategoryFilterStatement.executeUpdate();
        }
        catch(SQLException e){return false;}

        return true;
    }

    @Override
    public boolean addFilterValues(int filter_ID, String filter_value)
    {
        try
        {
            addFilterValuesStatement.setInt(1, filter_ID);
            addFilterValuesStatement.setString(2, filter_value);
            addFilterValuesStatement.executeUpdate();
        }
        catch(SQLException e){return false;}

        return true;
    }

    @Override
    public boolean addFilterToProduct(int product_ID, int filter_value_ID)
    {
        try
        {
            addFilterToProductStatement.setInt(1, product_ID);
            addFilterToProductStatement.setInt(2, filter_value_ID);
            addFilterToProductStatement.executeUpdate();
        }
        catch(SQLException e){return false;}

        return true;
    }

    @Override
    public List<Product> getFilteredProducts(List<Integer> list_value_IDs)
    {
        if(list_value_IDs.size() == 0)
            return null;

        List<Product> foundProducts = new ArrayList<>();

        try
        {
            // using preparedStatement to avoid SQL injection
            //String variableList = null;
            StringBuilder variableList = new StringBuilder();

            for(int i = 0; i < list_value_IDs.size()-1; i++)
                variableList.append("?,");
            variableList.append("?");

            String query = "WITH temp(product_ID) AS (SELECT product_ID FROM product_filter_values WHERE filter_value_ID IN(" + variableList.toString() + ")) SELECT product_ID, name, manufacturer, price, category, mass, description, thumbnail, warranty_months FROM (temp INNER JOIN products USING(product_ID));";

            PreparedStatement statement = connection.prepareStatement(query);
            for(int i = 0; i < list_value_IDs.size(); i++)
                statement.setInt(i+1, list_value_IDs.get(i));
            ResultSet results = statement.executeQuery();

            while(results.next())
            {
                Product temp = new Product(results.getInt("product_ID"), results.getString("name"), results.getInt("manufacturer"), results.getBigDecimal("price"), results.getInt("category"), results.getDouble("mass"), results.getString("description"), results.getString("thumbnail"), results.getByte("warranty_months"));
                foundProducts.add(temp);
            }

        }
        catch(SQLException e){return null;}

        return foundProducts;
    }

    @Override
    public List<Warehouse> getWarehouses()
    {
        try
        {
            ResultSet results = getWarehousesStatement.executeQuery();
            List<Warehouse> list = new ArrayList<>();

            while(results.next())
            {
                Warehouse temp = new Warehouse(results.getInt("warehouse_ID"),
                                               results.getInt("capacity"),
                                               results.getInt("free_capacity"),
                                               results.getString("address"),
                                               results.getString("country"),
                                               results.getString("state"));
                list.add(temp);
            }

            return list;
        }
        catch(SQLException e){return null;}
    }

    @Override
    public List<Supplier> getSuppliers()
    {
        try
        {
            ResultSet results = getSuppliersStatement.executeQuery();
            List<Supplier> list = new ArrayList<>();

            while(results.next())
            {
                Supplier temp = new Supplier(results.getInt("supplier_ID"),
                                            results.getString("name"),
                                            results.getString("phone"),
                                            results.getString("email"),
                                            results.getString("website"));
                list.add(temp);
            }

            return list;
        }
        catch(SQLException e){return null;}
    }

    @Override
    public List<Inventory> getInventory()
    {
        try
        {
            ResultSet results = getInventoryStatement.executeQuery();
            List<Inventory> list = new ArrayList<>();

            while(results.next())
            {
                //String name, int manufacturer_ID, String description, String thumbnailPath, int category_ID,
                //                   BigDecimal price, double mass, byte warranty_months, Integer product_ID
                Inventory temp = new Inventory(results.getInt("inventory_ID"),
                                               results.getInt("amount"),
                                               results.getBigDecimal("price"),
                                               results.getObject("delivered_at", LocalDateTime.class),
                                               results.getInt("available_amount"),
                                               results.getInt("stored_at"),
                                               results.getBigDecimal("suppliers_price"),
                                               results.getInt("product_ID"),
                                               results.getInt("supplier_ID"),
                                               results.getDate("expiration_date"));

                list.add(temp);
            }

            return list;
        }
        catch(SQLException e){return null;}
    }

    @Override
    public boolean addToInventory(Product product, int amount, LocalDateTime delivered_at, Warehouse warehouse, BigDecimal suppliers_price, Supplier supplier)
    {
        try
        {// amount, price, delivered_at, available_amount, stored_at, suppliers_price, product_ID, supplier_ID
            addToInventoryStatement.setInt(1, amount);
            addToInventoryStatement.setBigDecimal(2, product.price);
            addToInventoryStatement.setObject(3, delivered_at);
            addToInventoryStatement.setInt(4, amount);
            addToInventoryStatement.setInt(5, warehouse.warehouse_ID);
            addToInventoryStatement.setBigDecimal(6, suppliers_price);
            addToInventoryStatement.setInt(7, product.product_ID);
            addToInventoryStatement.setInt(8, supplier.supplier_ID);

            addToInventoryStatement.executeUpdate();
        }
        catch(SQLException e){return false;}

        return true;
    }

    public  List<Inventory> getProductFromInventory(int productID, boolean only_non_zero_available_amount)
    {
    	List<Inventory> products = new ArrayList<>();
    	
    	try
    	{
    		getProductFromInventoryStatement.setInt(1, productID);
    		ResultSet results = getProductFromInventoryStatement.executeQuery();
    		
    		while(results.next())
    		{
    			Inventory temp = new Inventory(results.getInt("inventory_ID"),
    										   results.getInt("amount"),
    										   results.getBigDecimal("price"),
    										   (LocalDateTime)(results.getObject("delivered_at")),
    										   results.getInt("available_amount"),
    										   results.getInt("stored_at"),
    										   results.getBigDecimal("suppliers_price"),
    										   results.getInt("product_ID"),
    										   results.getInt("supplier_ID"),
    										   results.getDate("expiration_date"));
    			
    			if(only_non_zero_available_amount == true && temp.available_amount == 0)
    				continue;
    			
    			products.add(temp);
    		}
    		return products;
    	}
    	catch(SQLException e) {return null;}
    }
    
    @Override
    public boolean addOrder(Order order)
    {
        try
        {//inventory_ID, amount, order_received_at, order_delivered_at, returned_reason, status, handled_by, ordered_by
            addOrderStatement.setInt(1, order.inventory_ID);
            addOrderStatement.setInt(2, order.amount);
            addOrderStatement.setObject(3, order.order_received_at);
            addOrderStatement.setObject(4, order.order_delivered_at);
            addOrderStatement.setString(5, order.returned_reason);
            addOrderStatement.setInt(6, order.status_ID);
            addOrderStatement.setObject(7, order.handled_by, java.sql.Types.INTEGER);
            
            addOrderStatement.setInt(8, order.ordered_by);
            addOrderStatement.executeUpdate();
        }
        catch(SQLException e) {return false;}

        return true;
    }

    @Override
    public boolean addReturnedReason(Order order)
    {
        try
        {
            addReturnedReasonStatement.setString(1, order.returned_reason);
            addReturnedReasonStatement.setInt(2, order.order_ID);

            addReturnedReasonStatement.executeUpdate();
        }
        catch(SQLException e){return false;}

        return true;
    }

    @Override
    public Employee getEmployee(String username)
    {
        Employee found;
        try
        {
            getEmployeeStatement.setString(1, username);
            ResultSet results = getEmployeeStatement.executeQuery();
            results.next();

            found = new Employee(results.getInt("employee_ID"),
                    results.getString("name"),
                    results.getString("last_name"),
                    results.getString("phone"),
                    results.getString("address"),
                    results.getString("city"),
                    results.getString("state"),
                    results.getString("ZIP"),
                    results.getString("username"),
                    results.getString("password"));
        }
        catch(SQLException e){return null;}

        return found;
    }

    @Override
    public Customer getCustomer(String email)
    {
        Customer found = null;
        try
        {
            getCustomerStatement.setString(1, email);
            ResultSet result = getCustomerStatement.executeQuery();
            result.next();
            found = new Customer(result.getInt("customer_ID"),
                    result.getString("name"),
                    result.getString("last_name"),
                    email,
                    result.getString("password"),
                    result.getString("phone"),
                    result.getString("shipping_address"),
                    result.getString("city"),
                    result.getString("state"),
                    result.getString("ZIP_code"),
                    result.getString("shopping_cart"));
        }
        catch(SQLException e){return null;}

        return found;
    }

    public Customer getCustomer(int ID)
    {
    	try
    	{
    		getCustomerByIDStatement.setInt(1, ID);
    		ResultSet result = getCustomerByIDStatement.executeQuery();
    		result.next();
    		
            Customer found = new Customer(result.getInt("customer_ID"),
                    result.getString("name"),
                    result.getString("last_name"),
                    result.getString("email"),
                    result.getString("password"),
                    result.getString("phone"),
                    result.getString("shipping_address"),
                    result.getString("city"),
                    result.getString("state"),
                    result.getString("ZIP_code"),
                    result.getString("shopping_cart"));
            return found;
    	}
    	catch(SQLException e) {return null;}
    }
    
    @Override
    public boolean customerRegistration(Customer customer)
    {
        if(getCustomer(customer.email) != null)
            return false;

        try
        {
            // name, last_name, email, password, phone, shipping_address, city, state, ZIP_code
            registerCustomerStatement.setString(1, customer.name);
            registerCustomerStatement.setString(2, customer.last_name);
            registerCustomerStatement.setString(3, customer.email);
            registerCustomerStatement.setString(4, customer.password);
            registerCustomerStatement.setString(5, customer.phone);
            registerCustomerStatement.setString(6, customer.address);
            registerCustomerStatement.setString(7, customer.city);
            registerCustomerStatement.setString(8, customer.state);
            registerCustomerStatement.setString(9, customer.zip);

            registerCustomerStatement.executeUpdate();
        }
        catch(SQLException e){System.out.println(e); return false;}
        return true;
    }

    @Override
    public boolean addReview(Product_review review)
    {
        try
        {
            //grade, comment, product_ID, customer
            addReviewStatement.setByte(1, review.grade);
            addReviewStatement.setString(2, review.comment);
            addReviewStatement.setInt(3, review.product_ID);
            addReviewStatement.setInt(4, review.customer_ID);

            addReviewStatement.executeUpdate();
        }
        catch(SQLException e){return false;}

        return true;
    }

    public List<Product> getAllProducts()
    {
        try
        {
            ResultSet results = getAllProductsStatement.executeQuery();
            List<Product> list = new ArrayList<>();

            while(results.next())
            {
                Product temp = new Product(results.getInt("product_ID"),
                        results.getString("name"),
                        results.getInt("manufacturer"),
                        results.getBigDecimal("price"),
                        results.getInt("category"),
                        results.getDouble("mass"),
                        results.getString("description"),
                        results.getString("thumbnail"),
                        results.getByte("warranty_months"));
                list.add(temp);
            }

            return list;
        }
        catch(SQLException e){return null;}
    }
}
