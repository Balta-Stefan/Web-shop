package webStore.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import webStore.model.Warehouse;
import webStore.utilities.connectionPool;

public class WarehouseDAO
{
	private static final String getWarehouses = "SELECT * FROM Warehouses";
	private static final String getWarehouse = "SELECT * FROM Warehouses WHERE warehouse_ID = ?";

	private connectionPool pool;
	
	public WarehouseDAO(connectionPool pool)
	{
		this.pool = pool;
	}
	
	public Warehouse getWarehouse(int ID)
	{
		Connection connection = pool.getConnection();
		
		
		try(PreparedStatement s = connection.prepareStatement(getWarehouse))
		{
			s.setInt(1, ID);
			
			try(ResultSet results = s.executeQuery())
			{
				results.next();
				return new Warehouse(results.getInt("warehouse_ID"),
	                    results.getInt("capacity"),
	                    results.getInt("free_capacity"),
	                    results.getString("address"),
	                    results.getString("country"),
	                    results.getString("state"));
			}
		}
		catch(SQLException e) {return null;}
		finally
		{
			pool.returnConnection(connection);
		}
	}

	public List<Warehouse> getWarehouses()
	{
		Connection connection = pool.getConnection();
		List<Warehouse> list = new ArrayList<>();
		
		try(PreparedStatement s = connection.prepareStatement(getWarehouses);
			ResultSet results = s.executeQuery())
		{
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
		}
		catch(SQLException e) {return null;}
		finally
		{
			pool.returnConnection(connection);
		}


		return list;
	}
}
