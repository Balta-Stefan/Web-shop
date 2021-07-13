package webStore.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import webStore.model.Employee;
import webStore.utilities.connectionPool;

public class EmployeesDAO
{
	private static final String getEmployee = "SELECT * FROM employee_Employee_view WHERE username=?";
	
	
	private connectionPool pool;
	
	public EmployeesDAO(connectionPool pool)
	{
		this.pool = pool;
	}
	
	public Employee get(String username)
	{
		Connection connection = pool.getConnection();
		try(PreparedStatement s = connection.prepareStatement(getEmployee))
		{
			s.setString(1, username);
			try(ResultSet results = s.executeQuery();)
			{
				 results.next();
				 
				 Employee employee = new Employee();
				 employee.employee_ID = results.getInt("employee_ID");
				 employee.username = results.getString("username");
				 employee.password = results.getString("password");
				 
				 return employee;
		         /*return new Employee(results.getInt("employee_ID"),
		                    results.getString("name"),
		                    results.getString("last_name"),
		                    results.getString("phone"),
		                    results.getString("address"),
		                    results.getString("city"),
		                    results.getString("state"),
		                    results.getString("ZIP"),
		                    results.getString("username"),
		                    results.getString("password"));*/
			}
		}
		catch(SQLException e) {return null;}
		finally
		{
			pool.returnConnection(connection);
		}
	}
}
