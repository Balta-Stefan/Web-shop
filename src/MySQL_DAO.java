import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class MySQL_DAO implements DAOInterface
{
    // Format of MySQL datetime: yyy-mm-dd hh:mm:ss

    private final Connection connection;

    // SQL query constants
    private static final String addProductQuery = "INSERT INTO Products(name, manufacturer, price, category, mass, description, thumbnail, warranty_months) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String addProductPicture = "INSERT INTO Product_pictures(product_ID, picture_URI) VALUES(?, ?)";
    private static final String addProductCategory = "INSERT INTO Product_categories(category_name) VALUES(?)";
    private static final String addCategoryFilter = "INSERT INTO Category_filters(category_ID, filter) VALUES(?, ?)";
    private static final String addFilterValues = "INSERT INTO Filter_values(filter_ID, value) VALUES(?, ?)";
    private static final String addFilterToProduct = "INSERT INTO Product_filter_values(product_ID, filter_value_ID) VALUES(?, ?)";
    private static final String getWarehouses = "SELECT * FROM Warehouses";
    private static final String getSuppliers = "SELECT * FROM suppliers";
    private static final String getInventory = "SELECT * FROM Inventory";
    private static final String addToInventory = "INSERT INTO Inventory(amount, price, delivered_at, available_amount, stored_at, suppliers_price, product_ID, supplier_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String addOrder = "INSERT INTO Orders(inventory_ID, amount, order_received_at, order_delivered_at, returned_reason, status, handled_by, ordered_by) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String addReturnedReason = "UPDATE Orders SET returned_reason=? WHERE order_ID=?";
    private static final String getEmployee = "SELECT * FROM Employees WHERE username=?";
    private static final String getCustomer = "SELECT * FROM Customers WHERE email=?";
    private static final String registerCustomer = "INSERT INTO Customers(name, last_name, email, password, phone, shipping_address, city, state, ZIP_code) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String addReview = "INSERT INTO Product_reviews(grade, comment, product_ID, customer) VALUES(?, ?, ?, ?)";
    private static final String getAllProducts = "SELECT * FROM Products";
    private static final String addManufacturer = "INSERT INTO Manufacturers(name) VALUES(?)";
    private static final String getManufacturers = "SELECT * FROM Manufacturers";
    private static final String getAllOrderStatusTypes = "SELECT * FROM Order_statuses";

    // prepared statements
    private final PreparedStatement addProductStatement;
    private final PreparedStatement addProductPictureStatement;
    private final PreparedStatement addProductCategoryStatement;
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


    public MySQL_DAO(Connection connection) throws SQLException
    {
        this.connection = connection;

        addProductStatement = connection.prepareStatement(addProductQuery);
        addProductPictureStatement = connection.prepareStatement(addProductPicture);
        addProductCategoryStatement = connection.prepareStatement(addProductCategory);
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
        try
        {
            addProductStatement.setString(1, product.name);
            addProductStatement.setInt(2, product.manufacturer_ID);
            addProductStatement.setBigDecimal(3, product.price);
            addProductStatement.setInt(4, product.category_ID);
            addProductStatement.setDouble(5, product.mass);
            addProductStatement.setString(6, product.description);
            addProductStatement.setString(7, product.thumbnail);
            addProductStatement.setByte(8, product.warranty_months);

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
    public boolean addProductCategory(String category_name)
    {
        try
        {
            addProductCategoryStatement.setString(1, category_name);
            addProductCategoryStatement.executeUpdate();
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
                                               results.getInt("supplier_ID"));

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
            addOrderStatement.setInt(7, order.handled_by);
            addOrderStatement.setInt(8, order.ordered_by);

            addOrderStatement.executeUpdate();
        }
        catch(SQLException e){return false;}

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
        Customer found;
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
                    result.getString("ZIP_code"));
        }
        catch(SQLException e){return null;}

        return found;
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
            registerCustomerStatement.setString(6, customer.shipping_address);
            registerCustomerStatement.setString(7, customer.city);
            registerCustomerStatement.setString(8, customer.state);
            registerCustomerStatement.setString(9, customer.ZIP_code);

            registerCustomerStatement.executeUpdate();
        }
        catch(SQLException e){return false;}
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
