package webStore.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import webStore.model.Category_filter;
import webStore.utilities.connectionPool;



public class Category_filtersDAO
{

	private static final String get_subcategories = "SELECT category_ID, category_name FROM Product_categories WHERE parent_category_ID = ?";
    private static final String addCategoryFilter = "INSERT INTO Category_filters(category_ID, filter) VALUES(?, ?)";
    private static final String delete_filter = "DELETE FROM Category_filters WHERE filter_ID = ?";
    
	
	private connectionPool pool;
	
	public Category_filtersDAO(connectionPool pool)
	{
		this.pool = pool;
	}
	
	public boolean deleteFilter(int filterID)
	{
		Connection connection = pool.getConnection();
		try(PreparedStatement s = connection.prepareStatement(delete_filter))
		{
			s.setInt(1,  filterID);
			s.execute();
			
			return true;
		}
		catch(SQLException e) {return false;}
		finally
		{
			pool.returnConnection(connection);
		}
	}
	
	public List<Category_filter> getSubcategories(int parent_category_ID)
	{
		List<Category_filter> response = new ArrayList<>();
		
		Connection connection = pool.getConnection();
		try(PreparedStatement s = connection.prepareStatement(get_subcategories))
		{
			s.setInt(1, parent_category_ID);
			
			try(ResultSet results = s.executeQuery())
			{
		  		while(results.next())
	    		{
	    			response.add(new Category_filter(results.getInt("filter_ID"), results.getInt("category_ID"), results.getString("category_name")));
	    		}
			}
		}
		catch(SQLException e) {return null;}
		finally
		{
			pool.returnConnection(connection);
		}


		return response;
	}
	
	public boolean addCategoryFilter(int categoryID, String filter)
	{
		Connection connection = pool.getConnection();
		try(PreparedStatement s = connection.prepareStatement(addCategoryFilter))
		{
			s.setInt(1, categoryID);
			s.setString(2, filter);
		
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