package webStore.DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import webStore.model.Product_category;
import webStore.responses.ID_string_pair;
import webStore.utilities.connectionPool;
/*
Connection connection = pool.getConnection();
try(PreparedStatement s = connection.prepareStatement(addProductQuery))
{
			
	s.execute();
}
catch(SQLException e) {return false;}
finally
{
	pool.returnConnection(connection);
}


return true;
*/
public class Product_categoriesDAO
{
	private static final String get_main_categories = "SELECT category_ID, category_name FROM Product_categories WHERE parent_category_ID IS NULL";
	private static final String get_category = "SELECT * FROM Product_categories WHERE category_ID=?";
	private static final String get_subcategories = "SELECT category_ID, category_name FROM Product_categories WHERE parent_category_ID = ?";
	private static final String delete_category = "CALL delete_category(?, ?)";
	
	private static final String insert_category_call = "CALL insert_category(?, ?)";
	
	private connectionPool pool;
	
	public Product_categoriesDAO(connectionPool pool)
	{
		this.pool = pool;
	}
	
	public boolean deleteCategory(int categoryID, Integer parentCategoryID)
	{
		Connection connection = pool.getConnection();
	  	try(CallableStatement ps = connection.prepareCall(delete_category))
    	{
    		ps.setInt(1, categoryID);
    		if(parentCategoryID == null)
    			ps.setNull(2, java.sql.Types.INTEGER);
        	else 
        		ps.setInt(2, categoryID);
    		
    		ps.executeUpdate();
    	}
    	catch(SQLException e) {return false;}
    	
    	return true;
	}
	
	public List<ID_string_pair> getSubcategories(int parentID)
	{
		Connection connection = pool.getConnection();
		List<ID_string_pair> list = new ArrayList<>();
		try(PreparedStatement s = connection.prepareStatement(get_subcategories))
		{
			s.setInt(1,  parentID);
    		try(ResultSet results = s.executeQuery())
    		{
    			while(results.next())
        		{
        			list.add(new ID_string_pair(results.getInt("category_ID"), results.getString("category_name")));
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

	public boolean addCategory(Integer parentCategoryID, String subcategoryName)
	{
		Connection connection = pool.getConnection();
		try(PreparedStatement ps = connection.prepareCall(insert_category_call))
		{
			if(parentCategoryID == null)
        		ps.setNull(2, java.sql.Types.INTEGER);
        	else 
        		ps.setInt(2, parentCategoryID);
			ps.setString(1, subcategoryName);
			
        	ps.executeUpdate();
        	
        	return true;
		}
		catch(SQLException e) {return false;}
		finally
		{
			pool.returnConnection(connection);
		}
	}
		
	
	public Product_category get(int ID)
	{
		Connection connection = pool.getConnection();
		try(PreparedStatement s = connection.prepareStatement(get_category))
		{
			s.setInt(1,  ID);
    		try(ResultSet result = s.executeQuery())
    		{
    			result.next();
    			return new Product_category(result.getInt("category_ID"), result.getString("category_name"), result.getInt("parent_category_ID"), result.getInt("number_of_subcategories"));
    		}
		}
		catch(SQLException e) {return null;}
		finally
		{
			pool.returnConnection(connection);
		}
	}
	
	public List<ID_string_pair> getMainCategories()
	{
		Connection connection = pool.getConnection();
		List<ID_string_pair> list = new ArrayList<>();
		try(PreparedStatement s = connection.prepareStatement(get_main_categories))
		{
			try(ResultSet results = s.executeQuery())
			{
				while(results.next())
	    		{
	    			ID_string_pair tmp = new ID_string_pair(results.getInt("category_ID"), results.getString("category_name"));
	    			list.add(tmp);
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
