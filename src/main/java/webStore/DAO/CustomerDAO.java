package webStore.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import webStore.model.Customer;
import webStore.utilities.connectionPool;

public class CustomerDAO
{
	private static final String getCustomer = "SELECT * FROM Customers WHERE email=?";
	private static final String getCustomerByID = "SELECT * FROM Customers WHERE customer_ID = ?";
	private static final String registerCustomer = "INSERT INTO Customers(name, last_name, email, password, phone, shipping_address, city, state, ZIP_code) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String update_shopping_cart = "UPDATE Customers SET shopping_cart = ? WHERE customer_ID = ?";
	
	
	private connectionPool pool;
	
	public CustomerDAO(connectionPool pool)
	{
		this.pool = pool;
	}
	
	public boolean updateShoppingCart(String shopping_cart, int customer_ID)
	{
		Connection connection = pool.getConnection();
		try(PreparedStatement s = connection.prepareStatement(update_shopping_cart))
		{
    		s.setString(1, shopping_cart);
    		s.setInt(2,  customer_ID);
    		s.execute();
		}
		catch(SQLException e) {return false;}
		finally
		{
			pool.returnConnection(connection);
		}


		return true;
	}
	
	public boolean add(Customer customer)
	{
		Connection connection = pool.getConnection();
		try(PreparedStatement registerCustomerStatement = connection.prepareStatement(registerCustomer))
		{
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
		catch(SQLException e) {return false;}
		finally
		{
			pool.returnConnection(connection);
		}


		return true;
	}
	
	public Customer get(int ID)
	{
		Connection connection = pool.getConnection();
		try(PreparedStatement s = connection.prepareStatement(getCustomerByID))
		{
            s.setInt(1, ID);
            
            try(ResultSet result = s.executeQuery())
            {
            	  result.next();
                  return new Customer(result.getInt("customer_ID"),
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
            }
		}
		catch(SQLException e) {return null;}
		finally
		{
			pool.returnConnection(connection);
		}
	}
	
	public Customer get(String email)
	{
		Connection connection = pool.getConnection();
		try(PreparedStatement s = connection.prepareStatement(getCustomer))
		{
            s.setString(1, email);
            
            try(ResultSet result = s.executeQuery())
            {
            	  result.next();
                  return new Customer(result.getInt("customer_ID"),
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
		}
		catch(SQLException e) {return null;}
		finally
		{
			pool.returnConnection(connection);
		}
	}
}
