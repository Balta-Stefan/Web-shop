package webStore.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import webStore.utilities.connectionPool;



public class Product_filter_valuesDAO
{
	private static final String addFilterToProduct = "INSERT INTO Product_filter_values(product_ID, filter_value_ID) VALUES(?, ?)";

	
	
	private connectionPool pool;
	
	public Product_filter_valuesDAO(connectionPool pool)
	{
		this.pool = pool;
	}
	
	public boolean addFilterToProduct(int productID, int filter_value_id)
	{
		Connection connection = pool.getConnection();
		try(PreparedStatement s = connection.prepareStatement(addFilterToProduct))
		{
			s.setInt(1, productID);
			s.setInt(2, filter_value_id);
			
			s.execute();
		}
		catch(SQLException e) {return false;}
		finally
		{
			pool.returnConnection(connection);
		}


		return true;
	}
}
