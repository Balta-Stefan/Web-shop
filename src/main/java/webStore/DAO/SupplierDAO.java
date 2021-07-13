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
	private static final String getSupplier = "SELECT * FROM Suppliers WHERE supplier_ID = ?";
	private static final String insertSupplier = "INSERT INTO Suppliers(name, phone, email, website) VALUES(?, ?, ?, ?)";
	

	private connectionPool pool;
	
	public SupplierDAO(connectionPool pool)
	{
		this.pool = pool;
	}
	
	public Supplier getSupplier(int ID)
	{
		Connection connection = pool.getConnection();
		
		try(PreparedStatement s = connection.prepareStatement(getSupplier))
		{
			s.setInt(1, ID);
			
			try(ResultSet result = s.executeQuery())
			{
				result.next();
				
				return new Supplier(result.getInt("supplier_ID"),
                        result.getString("name"),
                        result.getString("phone"),
                        result.getString("email"),
                        result.getString("website"));
			}
		}
		catch(SQLException e) {return null;}
	}
	
	public boolean addSupplier(Supplier supplier)
	{
		Connection connection = pool.getConnection();
		try(PreparedStatement s = connection.prepareStatement(insertSupplier))
		{
			s.setString(1,  supplier.name);
			s.setString(2, supplier.phone);
			s.setString(3, supplier.email);
			s.setString(4, supplier.website);
		
			s.execute();
		}
		catch(SQLException e) {return false;}
		finally
		{
			pool.returnConnection(connection);
		}

		
		return true;
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
