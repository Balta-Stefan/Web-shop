package webStore.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import webStore.model.Order_status;
import webStore.utilities.connectionPool;

public class Order_statusesDAO
{
	private static final String getAllOrderStatusTypes = "SELECT * FROM Order_statuses";
	
	private connectionPool pool;
	
	public Order_statusesDAO(connectionPool pool)
	{
		this.pool = pool;
	}
	
	public List<Order_status> getAll()
	{
		Connection connection = pool.getConnection();
		List<Order_status> list = new ArrayList<>();
		try(PreparedStatement s = connection.prepareStatement(getAllOrderStatusTypes))
		{
		    try(ResultSet results = s.executeQuery())
		    {
		        while(results.next())
	            {
	                Order_status temp = new Order_status(results.getInt("status_ID"), results.getString("status_type"));
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
