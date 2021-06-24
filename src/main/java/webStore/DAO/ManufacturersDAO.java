package webStore.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import webStore.model.Manufacturer;
import webStore.utilities.connectionPool;


public class ManufacturersDAO
{
	private static final String addManufacturer = "INSERT INTO Manufacturers(name) VALUES(?)";
	private static final String getManufacturers = "SELECT * FROM Manufacturers";
	private static final String get_manufacturer = "SELECT * FROM Manufacturers WHERE manufacturer_ID = ?";
	
	private connectionPool pool;
	
	public ManufacturersDAO(connectionPool pool)
	{
		this.pool = pool;
	}
	
	public Manufacturer get(int ID)
	{
		Connection connection = pool.getConnection();
		try(PreparedStatement s = connection.prepareStatement(get_manufacturer))
		{
			s.setInt(1, ID);
    		try(ResultSet result = s.executeQuery())
    		{
    			result.next();
    			return new Manufacturer(result.getInt("manufacturer_ID"), result.getString("name"));
    		}
		}
		catch(SQLException e) {return null;}
		finally
		{
			pool.returnConnection(connection);
		}
	}
	
	public List<Manufacturer> getAll()
	{
		Connection connection = pool.getConnection();
		List<Manufacturer> list = new ArrayList<>();
		try(PreparedStatement s = connection.prepareStatement(getManufacturers))
		{
            try( ResultSet results = s.executeQuery();)
            {

                while(results.next())
                {
                    Manufacturer temp = new Manufacturer(results.getInt("manufacturer_ID"), results.getString("name"));
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
	
	public boolean add(Manufacturer manufacturer)
	{
		Connection connection = pool.getConnection();
		try(PreparedStatement s = connection.prepareStatement(addManufacturer))
		{
			s.setString(1, manufacturer.name);

            s.executeUpdate();
		}
		catch(SQLException e) {return false;}
		finally
		{
			pool.returnConnection(connection);
		}


		return true;
	}
}
