package webStore.DAO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import webStore.model.Inventory;
import webStore.utilities.connectionPool;



public class InventoryDAO
{
	private static final String getInventory = "SELECT * FROM Inventory";
	private static final String addToInventory = "INSERT INTO Inventory(amount, price, delivered_at, available_amount, stored_at, suppliers_price, product_ID, supplier_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String get_product_from_inventory = "SELECT * FROM Inventory WHERE product_ID = ?";

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
	
	public boolean addToInventory(int amount, BigDecimal product_price, LocalDateTime delivered_at, int warehouse_ID, BigDecimal suppliers_price, int product_ID, int supplier_ID)
	{
		Connection connection = pool.getConnection();
		try(PreparedStatement s = connection.prepareStatement(addToInventory))
		{
			s.setInt(1, amount);
			s.setBigDecimal(2, product_price);
			s.setObject(3, delivered_at);
			s.setInt(4, amount);
			s.setInt(5, warehouse_ID);
			s.setBigDecimal(6, suppliers_price);
			s.setInt(7, product_ID);
			s.setInt(8, supplier_ID);
			
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
		try(PreparedStatement s = connection.prepareStatement(getInventory))
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
}
