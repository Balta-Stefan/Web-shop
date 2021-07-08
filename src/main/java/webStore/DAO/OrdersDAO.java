package webStore.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import webStore.model.Order;
import webStore.utilities.connectionPool;



public class OrdersDAO
{
	private static final String addOrder = "INSERT INTO Orders(inventory_ID, amount, order_received_at, order_delivered_at, returned_reason, status, handled_by, ordered_by) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String addReturnedReason = "UPDATE Orders SET returned_reason=? WHERE order_ID=?";
	private static final String get_all_orders = "SELECT * FROM Orders";
	private static final String get_order = "SELECT * FROM Orders WHERE order_ID = ?";

	private connectionPool pool;
	
	public OrdersDAO(connectionPool pool)
	{
		this.pool = pool;
	}
	
	public Order get(int orderID)
	{
		Connection connection = pool.getConnection();
		try(PreparedStatement s = connection.prepareStatement(get_order))
		{
			s.setInt(1, orderID);
			try(ResultSet result = s.executeQuery();)
			{
				result.next();
	    		
	    		return new Order(result.getInt("order_ID"), 
				           result.getInt("inventory_ID"),
				           result.getInt("amount"), 
				           (LocalDateTime)(result.getObject("order_received_at", LocalDateTime.class)),
				           (LocalDateTime)(result.getObject("order_delivered_at", LocalDateTime.class)),
				           result.getString("returned_reason"),
				           result.getInt("status"),
				           result.getInt("handled_by"),
				           result.getInt("ordered_by"));
			}
		}
		catch(SQLException e) {return null;}
		finally
		{
			pool.returnConnection(connection);
		}
	}
	
	public List<Order> getAll()
	{
		Connection connection = pool.getConnection();
		List<Order> list = new ArrayList<>();
		try(PreparedStatement s = connection.prepareStatement(get_all_orders);
			ResultSet results = s.executeQuery())
		{
			while(results.next())
    		{
        		Order tempOrder =  new Order(results.getInt("order_ID"), 
								           results.getInt("inventory_ID"),
								           results.getInt("amount"), 
								           (LocalDateTime)(results.getObject("order_received_at", LocalDateTime.class)),
								           (LocalDateTime)(results.getObject("order_delivered_at", LocalDateTime.class)),
								           results.getString("returned_reason"),
								           results.getInt("status"),
								           results.getInt("handled_by"),
								           results.getInt("ordered_by"));
        		list.add(tempOrder);
    		}
		}
		catch(SQLException e) {return null;}
		finally
		{
			pool.returnConnection(connection);
		}


		return list;
	}
	
	public boolean addReturnedReason(String returned_reason, int order_ID)
	{
		Connection connection = pool.getConnection();
		try(PreparedStatement s = connection.prepareStatement(addReturnedReason))
		{
			s.setString(1, returned_reason);
            s.setInt(2, order_ID);

            s.executeUpdate();
		}
		catch(SQLException e) {return false;}
		finally
		{
			pool.returnConnection(connection);
		}


		return true;
	}
	
	public boolean add(Order order)
	{
		Connection connection = pool.getConnection();
		try(PreparedStatement addOrderStatement = connection.prepareStatement(addOrder))
		{
		    addOrderStatement.setInt(1, order.inventory_ID);
            addOrderStatement.setInt(2, order.amount);
            addOrderStatement.setObject(3, order.order_received_at);
            addOrderStatement.setObject(4, order.order_delivered_at);
            addOrderStatement.setString(5, order.returned_reason);
            addOrderStatement.setInt(6, order.status_ID);
            addOrderStatement.setObject(7, order.handled_by, java.sql.Types.INTEGER);
            
            addOrderStatement.setInt(8, order.ordered_by);
            addOrderStatement.executeUpdate();
		}
		catch(SQLException e) {return false;}
		finally
		{
			pool.returnConnection(connection);
		}


		return true;
	}
}
