package webStore.DAO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import webStore.model.Manufacturer;
import webStore.model.Product;
import webStore.utilities.connectionPool;



public class ProductDAO
{
	private static final String addProductQuery = "INSERT INTO Products(name, manufacturer, category, mass, description, thumbnail, warranty_months) VALUES(?, ?, ?, ?, ?, ?, ?)";
	private static final String addProductPicture = "INSERT INTO Product_pictures(product_ID, picture_URI) VALUES(?, ?)";
	private static final String getAllProducts = "SELECT * FROM Products";
	private static final String get_product = "SELECT * FROM Products WHERE product_ID = ?";
	private static final String update_product_price = "UPDATE Products SET price = ? WHERE product_ID = ?";
	
	private connectionPool pool;
	
	public ProductDAO(connectionPool pool)
	{
		this.pool = pool;
	}
    public List<Product> getFilteredProducts(List<Integer> list_value_IDs)
    {
        if(list_value_IDs.size() == 0)
            return null;

        List<Product> foundProducts = new ArrayList<>();
        
        Connection connection = pool.getConnection();
        try
        {
            // using preparedStatement to avoid SQL injection
            //String variableList = null;
            StringBuilder variableList = new StringBuilder();

            for(int i = 0; i < list_value_IDs.size()-1; i++)
                variableList.append("?,");
            variableList.append("?");

            String query = "WITH temp(product_ID) AS (SELECT product_ID FROM product_filter_values WHERE filter_value_ID IN(" + variableList.toString() + ")) SELECT product_ID, name, manufacturer, price, category, mass, description, thumbnail, warranty_months FROM (temp INNER JOIN products USING(product_ID));";

            PreparedStatement statement = connection.prepareStatement(query);
            for(int i = 0; i < list_value_IDs.size(); i++)
                statement.setInt(i+1, list_value_IDs.get(i));
            ResultSet results = statement.executeQuery();

            while(results.next())
            {
                Product temp = new Product(results.getInt("product_ID"), results.getString("name"), results.getInt("manufacturer"), results.getBigDecimal("price"), results.getInt("category"), results.getDouble("mass"), results.getString("description"), results.getString("thumbnail"), results.getByte("warranty_months"));
                foundProducts.add(temp);
            }

        }
        catch(SQLException e){return null;}
        finally
        {
        	pool.returnConnection(connection);
        }

        return foundProducts;
    }
	
	public boolean updateProductPrice(int productID, BigDecimal newPrice)
	{
		Connection connection = pool.getConnection();
		try(PreparedStatement s = connection.prepareStatement(update_product_price))
		{
			s.setInt(1, productID);
			s.setBigDecimal(2, newPrice);
			
			s.execute();
		}
		catch(SQLException e) {return false;}
		finally
		{
			pool.returnConnection(connection);
		}

		
		return true;
	}
	
	public Product getProduct(int productID)
	{
		Connection connection = pool.getConnection();
		Product product = null;
		try(PreparedStatement s = connection.prepareStatement(get_product))
		{
			s.setInt(1, productID);	
			try(ResultSet result = s.executeQuery())
			{
				result.next();
				ManufacturersDAO manDAO = new ManufacturersDAO(pool);
				Manufacturer manufacturer = manDAO.get(result.getInt("manufacturer"));
				//int product_ID, String name, int manufacturer_ID, BigDecimal price, int category_ID, double mass, String description, String thumbnail, byte warranty_months, String manufacturer)
				
				product = new Product(result.getInt("product_ID"),
						 result.getString("name"),
						 manufacturer.manufacturer_ID,
						 result.getBigDecimal("price"),
						 result.getInt("category"),
						 result.getDouble("mass"),
						 result.getString("description"),
						 result.getString("thumbnail"),
						 result.getByte("warranty_months"),
						 manufacturer.name);
			}
		}
		catch(SQLException e) {return null;}
		finally
		{
			pool.returnConnection(connection);
		}

		
		return product;
	}
	
	public List<Product> getProducts()
	{
 		Connection connection = pool.getConnection();
 		List<Product> products = new ArrayList<>();
 		
		try(PreparedStatement s = connection.prepareStatement(getAllProducts);
			ResultSet results = s.executeQuery())
		{		
            while(results.next())
            {
                Product temp = new Product(results.getInt("product_ID"),
                        results.getString("name"),
                        results.getInt("manufacturer"),
                        results.getBigDecimal("price"),
                        results.getInt("category"),
                        results.getDouble("mass"),
                        results.getString("description"),
                        results.getString("thumbnail"),
                        results.getByte("warranty_months"));
                products.add(temp);
            }
		}
		catch(SQLException e) {return null;}
		finally
		{
			pool.returnConnection(connection);
		}

		
		return products;
	}
	
	public boolean insert(Product product)
	{
		Connection connection = pool.getConnection();
		try(PreparedStatement s = connection.prepareStatement(addProductQuery))
		{
			s.setString(1,  product.name);
			s.setInt(2, product.manufacturer_ID);
			s.setInt(3, product.category_ID);
			s.setDouble(4, product.mass);
			s.setString(5, product.description);
			s.setString(6, product.thumbnail);
			s.setByte(7, product.warranty_months);
			
			s.execute();
		}
		catch(SQLException e) {return false;}
		finally
		{
			pool.returnConnection(connection);
		}

		
		return true;
	}

	public boolean insertPicture(int productID, String pictureURI)
	{
 		Connection connection = pool.getConnection();
		try(PreparedStatement s = connection.prepareStatement(addProductPicture))
		{
			s.setInt(1, productID);
			s.setString(2, pictureURI);
			
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
