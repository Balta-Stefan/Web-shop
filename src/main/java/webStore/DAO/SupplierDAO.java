package webStore.DAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import webStore.model.Supplier;

import webStore.utilities.connectionPool;



public class SupplierDAO
{
	private static final String getSuppliers = "SELECT * FROM Suppliers";

	private connectionPool pool;
	
	public SupplierDAO(connectionPool pool)
	{
		this.pool = pool;
	}

	public List<Supplier> getSuppliers()
	{
		Connection connection = pool.getConnection();
		List<Supplier> list = new ArrayList<>();
		
		try(PreparedStatement s = connection.prepareStatement(getSuppliers);
			ResultSet results = s.executeQuery())
		{
            while(results.next())
            {
                Supplier temp = new Supplier(results.getInt("supplier_ID"),
                                            results.getString("name"),
                                            results.getString("phone"),
                                            results.getString("email"),
                                            results.getString("website"));
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
