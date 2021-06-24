package webStore.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import webStore.responses.FilterValuesOfCategory;
import webStore.utilities.connectionPool;



public class Filter_valuesDAO
{
	private static final String addFilterValues = "INSERT INTO Filter_values(filter_ID, value) VALUES(?, ?)";
	private static final String get_filters = "select T.filter, T.filter_ID, T.filter_value_ID, T.value from (select * from (product_categories join Category_filters using(category_ID) join filter_values using(filter_ID)) ) AS T where T.category_ID=?"; // done
    
	
	
	private connectionPool pool;
	
	public Filter_valuesDAO(connectionPool pool)
	{
		this.pool = pool;
	}
	
	public FilterValuesOfCategory get_filters(int categoryID)
	{
    	/* 
  			obtains filters and their values of the given category such that the JSON object obtained from results looks like following:
    			{
    				[
    					filter_ID: xxx,
    					filter_name: xxx,
    					filter_values:
    					[
    						{
    							filter_value_ID: xxx,
    							value: xxx
    						},...
    					]
    				],...
    			}
    	*/
		FilterValuesOfCategory filtersWithValues = new FilterValuesOfCategory();
		Connection connection = pool.getConnection();
		try(PreparedStatement s = connection.prepareStatement(get_filters))
		{
			s.setInt(1, categoryID);
			
			try(ResultSet results = s.executeQuery())
			{
				while(results.next())
	    		{
	    			filtersWithValues.addFilterOrValue(results.getInt("filter_value_ID"), results.getInt("filter_ID"), results.getString("filter"), results.getString("value"));
	    			//filters.add(new ID_string_pair(results.getInt("filter_value_ID"), results.getString("value")));
	    		}
			}
		}
		catch(SQLException e) {return null;}
		finally
		{
			pool.returnConnection(connection);
		}


		return filtersWithValues;
	}
	

	public boolean addFilterValue(int filter_ID, String value)
	{
		Connection connection = pool.getConnection();
		try(PreparedStatement s = connection.prepareStatement(addFilterValues))
		{
			s.setInt(1, filter_ID);
			s.setString(2, value);
			
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
