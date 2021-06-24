package webStore.DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import webStore.model.Product_category;
import webStore.utilities.connectionPool;

public class StoredProceduresDAO
{
	private static final String insert_category_call = "CALL insert_category(?, ?)";
	private static final String delete_category_call = "CALL delete_category(?, ?)";
	
	private connectionPool pool;
	
	public StoredProceduresDAO(connectionPool pool)
	{
		this.pool = pool;
	}
	
	public boolean deleteCategory(Product_category category)
	{
		Connection connection = pool.getConnection();
		try(CallableStatement delete_category_statement = connection.prepareCall(delete_category_call))
		{
			delete_category_statement.setInt(1, category.category_ID);
    		if(category.parent_category_ID == null)
    			delete_category_statement.setNull(2, java.sql.Types.INTEGER);
        	else 
        		delete_category_statement.setInt(2, category.parent_category_ID);
    		
    		delete_category_statement.executeUpdate();
		}
		catch(SQLException e) {return false;}
		finally
		{
			pool.returnConnection(connection);
		}


		return true;
	}
	
	public boolean addProductCategory(Product_category category)
	{
		Connection connection = pool.getConnection();
		try(CallableStatement insert_category_statement = connection.prepareCall(insert_category_call))
		{
			insert_category_statement.setString(1, category.category_name);
        	if(category.parent_category_ID == null)
        		insert_category_statement.setNull(2, java.sql.Types.INTEGER);
        	else 
        		insert_category_statement.setInt(2, category.parent_category_ID);
        	insert_category_statement.executeUpdate();
		}
		catch(SQLException e) {return false;}
		finally
		{
			pool.returnConnection(connection);
		}


		return true;
	}
}
