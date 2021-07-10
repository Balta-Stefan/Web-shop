package webStore.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connectionPool
{
	// private Queue<Connection> availableConnections;
	// private Queue<Connection> usedConnections;
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/mydb";
	private String username;
	private String pass;

	public connectionPool(String username, String pass)
	{
		this.username = username;
		this.pass = pass;
	}
	
	public synchronized Connection getConnection()
	{
		try
		{
			return DriverManager.getConnection(URL, username, pass);
		}
		catch(SQLException e) {return null;}
	}
	
	public synchronized void returnConnection(Connection c)
	{
		try
		{
			c.close();
		} catch (SQLException e) {}
	}
}
