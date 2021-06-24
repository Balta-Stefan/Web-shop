package webStore.utilities;

import java.sql.Connection;

public class connectionPool
{
	// private Queue<Connection> availableConnections;
	// private Queue<Connection> usedConnections;
	
	public synchronized Connection getConnection()
	{
		return null;
	}
	
	public synchronized void returnConnection(Connection c)
	{
		
	}
}
