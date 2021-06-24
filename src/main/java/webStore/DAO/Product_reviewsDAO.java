package webStore.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import webStore.model.Product_review;
import webStore.utilities.connectionPool;



public class Product_reviewsDAO
{
	private static final String addReview = "INSERT INTO Product_reviews(grade, comment, product_ID, customer) VALUES(?, ?, ?, ?)";
	   
	
	private connectionPool pool;
	
	public Product_reviewsDAO(connectionPool pool)
	{
		this.pool = pool;
	}
	
	public boolean add(Product_review review)
	{
		Connection connection = pool.getConnection();
		try(PreparedStatement addReviewStatement = connection.prepareStatement(addReview))
		{
			addReviewStatement.setByte(1, review.grade);
			addReviewStatement.setString(2, review.comment);
			addReviewStatement.setInt(3, review.product_ID);
			addReviewStatement.setInt(4, review.customer_ID);
			
			addReviewStatement.executeUpdate();
		}
		catch(SQLException e) {return false;}
		finally
		{
			pool.returnConnection(connection);
		}


		return true;
	}
}
