package webStore.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connectionPool
{
	// private Queue<Connection> availableConnections;
	// private Queue<Connection> usedConnections;
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/mydb";
	private static final String userName = "root";
	private static final String pass = "sigurnost";
	
	public synchronized Connection getConnection()
	{
		try
		{
			return DriverManager.getConnection(URL, userName, pass);
		}
		catch(SQLException e) {return null;}
	}
	
	public synchronized void returnConnection(Connection c)
	{
		
	}
}
