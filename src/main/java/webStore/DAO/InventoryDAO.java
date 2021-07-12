package webStore.DAO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import webStore.model.Inventory;
import webStore.utilities.connectionPool;



public class InventoryDAO
{
	private static final String getInventories = "SELECT * FROM Inventory";
	private static final String addToInventory = "INSERT INTO Inventory(amount, price, delivered_at, available_amount, stored_at, suppliers_price, product_ID, supplier_ID, expiration_date) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String get_product_from_inventory = "SELECT * FROM Inventory WHERE product_ID = ?";
	private static final String getInventory = "SELECT * FROM Inventory WHERE inventory_ID = ?";
	
	private connectionPool pool;
	
	public InventoryDAO(connectionPool pool)
	{
		this.pool = pool;
	}
	
	
	public List<Inventory> getProductFromInventory(int productID, boolean only_non_zero_available_amount)
	{
		Connection connection = pool.getConnection();
		List<Inventory> list = new ArrayList<>();
		try(PreparedStatement s = connection.prepareStatement(get_product_from_inventory))
		{
			s.setInt(1, productID);
    		try(ResultSet results = s.executeQuery())
    		{
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
        			
        			list.add(temp);
        		}
    		}
		}
		catch(SQLException e) {return null;}
		finally
		{
			pool.returnConnection(connection);
		}


		return list;
	}
	
	public boolean insert(Inventory inventory)
	{
		Connection connection = pool.getConnection();
		try(PreparedStatement s = connection.prepareStatement(addToInventory))
		{
			s.setInt(1, inventory.amount);
			s.setBigDecimal(2, inventory.price);
			s.setObject(3, inventory.delivered_at);
			s.setInt(4, inventory.amount);
			s.setInt(5, inventory.stored_at);
			s.setBigDecimal(6, inventory.suppliers_price);
			s.setInt(7, inventory.product_ID);
			s.setInt(8, inventory.supplier_ID);
			s.setObject(9, inventory.expiration_date);
			
			s.executeUpdate();
		}
		catch(SQLException e) {return false;}
		finally
		{
			pool.returnConnection(connection);
		}


		return true;
	}
	
	public List<Inventory> getInventory()
	{
		
		Connection connection = pool.getConnection();
		List<Inventory> list = new ArrayList<>();
		try(PreparedStatement s = connection.prepareStatement(getInventories))
		{
			try(ResultSet results = s.executeQuery())
			{
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
			}
		}
		catch(SQLException e) {return null;}
		finally
		{
			pool.returnConnection(connection);
		}


		return list;
	}

	public Inventory getInventory(int inventoryID)
	{
		Connection connection = pool.getConnection();
		try(PreparedStatement s = connection.prepareStatement(getInventory))
		{
			s.setInt(1, inventoryID);
			try(ResultSet results = s.executeQuery();)
			{
				results.next();
	    		
	    		return  new Inventory(results.getInt("inventory_ID"),
                        results.getInt("amount"),
                        results.getBigDecimal("price"),
                        results.getObject("delivered_at", LocalDateTime.class),
                        results.getInt("available_amount"),
                        results.getInt("stored_at"),
                        results.getBigDecimal("suppliers_price"),
                        results.getInt("product_ID"),
                        results.getInt("supplier_ID"),
                        results.getDate("expiration_date"));
			}
		}
		catch(SQLException e) {return null;}
		finally
		{
			pool.returnConnection(connection);
		}
	}
}
