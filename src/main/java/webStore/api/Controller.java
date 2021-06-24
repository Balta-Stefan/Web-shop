package webStore.api;



import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.json.JSONArray;
import org.json.JSONObject;

import webStore.DAO.MySQL_DAO;
import webStore.model.Customer;
import webStore.model.Inventory;
import webStore.model.Order;
import webStore.model.Order_status;
import webStore.model.Product;
import webStore.model.Product_category;
import webStore.responses.*;

// URL = http://localhost:8080/Web_store/api/v1

/*
	Format of the customer's shopping cart:
	[
		{
			ID: xxx,
			quantity: xxx
		},...
	]
*/
 

@Path("/v1")
public class Controller
{
	private static MySQL_DAO DBAccessObject;
	
	private static String html_file;
	private static String css_file;
	private static String javascript_file;
	
	private static final String ordered_status_name = "ORDERED";
	private static int ordered_status_ID; // might cause issues
	
	private static String pathPrefix = "D:\\Knjige za fakultet\\3. godina\\6. semestar\\Baze podataka\\Baze podataka - projekat\\Source\\Web-shop\\src\\main\\webapp\\Resources\\";
	
	private static HashMap<String, Customer> cookies = new HashMap<>(); // maps email to a customer
	
	static
	{
		System.out.println("In static block");
		try
		{
		 	Class.forName("com.mysql.cj.jdbc.Driver"); 
		    Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb", "root", "sigurnost");
		    DBAccessObject = new MySQL_DAO(connection);
		    
		    List<Order_status> tmpList = DBAccessObject.getAllOrderStatusTypes();
		    for(Order_status s : tmpList)
		    {
		    	if(s.status_type.equals(ordered_status_name))
		    	{
		    		ordered_status_ID = s.status_ID;
		    		break;
		    	}
		    }
		}
		catch(Exception e){System.out.println("ovo je greska: " + e);}
		
		// read static files
		/*try
		{
			html_file = new String(Files.readAllBytes(Paths.get("D:\\Knjige za fakultet\\3. godina\\6. semestar\\Baze podataka\\Baze podataka - projekat\\Source\\Eclipse project\\src\\main\\webapp\\Resources\\index.html")));
			css_file = new String(Files.readAllBytes(Paths.get("D:\\Knjige za fakultet\\3. godina\\6. semestar\\Baze podataka\\Baze podataka - projekat\\Source\\Eclipse project\\src\\main\\webapp\\Resources\\css\\index.css")));
			javascript_file = new String(Files.readAllBytes(Paths.get("D:\\Knjige za fakultet\\3. godina\\6. semestar\\Baze podataka\\Baze podataka - projekat\\Source\\Eclipse project\\src\\main\\webapp\\Resources\\js\\index.js")));
		}
		catch(Exception e) {System.out.println("Exception happened: " + e);}*/
	}
	
	/*
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Student> getStudents()
	{
		return students;
	}*/
	
	/*
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStudent(@PathParam("id") int id)
	{
		Student toReturn = findStudent(id);

		
		if(toReturn != null)
			return Response.status(200).entity(toReturn).build();
		else return Response.status(404).build();
	}*/
	
	
    @GET
    @Produces("text/html")
    public String getHtml()
    {
    	try
		{
			html_file = new String(Files.readAllBytes(Paths.get(pathPrefix + "index.html")));
			return html_file;
		}
		catch(Exception e) {System.out.println("Exception happened: " + e); return null;}
    }
    
    @GET
    @Path("/css/index.css")
    @Produces("text/css")
    public String getCSS()
    {
    	try
		{
			css_file = new String(Files.readAllBytes(Paths.get(pathPrefix + "css\\index.css")));
			return css_file;
		}
		catch(Exception e) {System.out.println("Exception happened: " + e); return null;}
    }
    
    @GET
    @Path("/js/index.js")
    @Produces("text/javascript")
    public String getJS()
    {
    	try
		{
			javascript_file = new String(Files.readAllBytes(Paths.get(pathPrefix + "js\\index.js")));
			return javascript_file;
		}
		catch(Exception e) {System.out.println("Exception happened: " + e); return null;}
    }
	

	@POST
	@Path("/customers")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerUser(Customer registrationInfo)
	{
		// only emails are unique so there is no need to check if the user already exists
		boolean response = DBAccessObject.customerRegistration(registrationInfo);
		
		if(response == true)
			return Response.status(200).build();
		else
			return Response.status(400).build();
	}
	
	@POST
	@Path("/customers/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response loginCustomer(Customer customer)
	{
		class TempClass
		{
			public int ID;
			
			public TempClass() {}
			
			public TempClass(int ID){this.ID = ID;}
		}
		
		synchronized(cookies)
		{
			// check if the user is already logged in
			Customer exists = cookies.get(customer.email);
			if(exists != null)
				return Response.status(200).entity(new TempClass(exists.customer_ID)).build();
		}
		
		// getCustomer(String email)
		Customer customerData = DBAccessObject.getCustomer(customer.email);
		if(customerData == null || customer.password.equals(customerData.password) == false)
			return Response.status(404).build();
		
		//Cookie tmp = new Cookie("user-cookie", customer.email);
		//NewCookie cookie = new NewCookie("user-cookie", customer.email);
		//NewCookie cookie = new NewCookie(tmp, null, 1200, true);
		synchronized(cookies)
		{
			cookies.put(customerData.email, customerData);
		}
		

		
		TempClass tmp = new TempClass(customerData.customer_ID);
		
		//return Response.status(200).cookie(cookie).build();
		return Response.status(200).entity(tmp).build();
	}
	
	@GET
	@Path("/category")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMainCategories()
	{
		List<ID_string_pair> categories = DBAccessObject.getMainCategories();
		if(categories == null)
			return Response.status(400).build();
		for(ID_string_pair m : categories)
			System.out.println(m.name);
		
		return Response.status(200).entity(categories).build();
	}
	
	@GET
	@Path("/category/{ID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFiltersOrSubcategories(@PathParam("ID") int categoryID)
	{
		// first determine whether a category contains any subcategories.If it does, get its subcategories.If it doesn't, get its filters.
		
		Product_category category = DBAccessObject.getCategory(categoryID);
		if(category.number_of_subcategories == 0)
		{
			// get the category's filters
			Filters filters = new Filters();
			filters.contents = DBAccessObject.get_filters(categoryID);
			return Response.status(200).entity(filters).build();
		}
		
		// get subcategories
		List<ID_string_pair> subcategories = DBAccessObject.get_subcategories(categoryID);
		Subcategories subcategoriesWrapper = new Subcategories();
		subcategoriesWrapper.contents = subcategories;
		
		return Response.status(200).entity(subcategoriesWrapper).build();
	}
	
	@PUT
	@Path("/filter-product")
	@Produces(MediaType.APPLICATION_JSON)
	public Response filterProducts(List<ID_string_pair> filter_value_ID_string_pairs)
	{
		List<Integer> filterIdentifiers = new ArrayList<>();
		for(ID_string_pair pair : filter_value_ID_string_pairs)
			filterIdentifiers.add(pair.ID);
		
		List<Product> products = DBAccessObject.getFilteredProducts(filterIdentifiers);
		List<TrimmedProduct> responseProducts = new ArrayList<>();
		
		for(Product p : products)
			responseProducts.add(new TrimmedProduct(p.product_ID, p.name, p.thumbnail, p.price.toString()));
		
		return Response.status(200).entity(responseProducts).build();
	}
	
	@GET
	@Path("/product/{productID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProduct(@PathParam("productID") int productID)
	{
		Product product = DBAccessObject.getProduct(productID);
		if(product == null)
			return Response.status(404).build();
		
		return Response.status(200).entity(product).build();
	}

	@PUT
	@Path("/buy/{ID}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add_product_to_cart(@PathParam("ID") int productID, BuyProduct quantity)
	{
		Customer purchaser = cookies.get(quantity.customer_email);
		if(purchaser == null)
			return Response.status(404).build();
		
		Customer customer = DBAccessObject.getCustomer(purchaser.customer_ID);
		String customerCart = customer.shopping_cart;
		if(customerCart == null)
			customerCart = "[]";
		
		JSONArray jarray = new JSONArray(customerCart);
		System.out.println("Customer's cart: " + jarray);
		
		String newItem = "{ID: " + productID + ", quantity: " + quantity.quantity + "}";
		JSONObject obj = new JSONObject(newItem);
		
		
		boolean productAlreadyBought = false;
		for(int i = 0; i < jarray.length(); i++)
		{
			JSONObject tmp = jarray.getJSONObject(i);
			Integer tmpProductID = tmp.getInt("ID");
			Integer oldQuantity = tmp.getInt("quantity");
			oldQuantity += quantity.quantity;
			
			if(tmpProductID == productID)
			{
				productAlreadyBought = true;
				tmp.put("quantity", oldQuantity.toString());
				tmp.put("ID", tmpProductID.toString());
				break;
			}
		}
		
		if(productAlreadyBought == false)
			jarray.put(obj);
		
		customer.shopping_cart = jarray.toString();
		boolean result = DBAccessObject.updateShoppingCart(customer);
	
		if(result == false)
			return Response.status(404).build();
	
		
		return Response.status(200).build();
	}
	
	private Customer get_customer_shopping_cart(int customerID)
	{
		List<Product> cart = new ArrayList<>();
		
		Customer customer = DBAccessObject.getCustomer(customerID);
		if(customer == null)
			return null;
		
		String shoppingCart = customer.shopping_cart;
		JSONArray jarray = new JSONArray(shoppingCart);
		
		for(int i = 0; i < jarray.length(); i++)
		{
			JSONObject tmp = jarray.getJSONObject(i);
			Product product = DBAccessObject.getProduct(tmp.getInt("ID"));
			product.quantity = tmp.getInt("quantity");
			cart.add(product);
		}
		
		customer.shopping_cart_list = cart;
		
		return customer;
	}
	
	@GET
	@Path("/customers/{ID}/shopping-cart")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getShoppingCart(@PathParam("ID") int customerID)
	{
		// authentication isn't possible because of the lack of cookies
		/*Customer customer = customerAccessObject.getCustomer(customerID);
		
		if(customer == null)
			return Response.status(404).build();
		
		List<Product> items_in_cart = new ArrayList<>();
		String shoppingCart = customer.shopping_cart;
		JSONArray jarray = new JSONArray(shoppingCart);
		
		
		for(int i = 0; i < jarray.length(); i++)
		{
			JSONObject tmp = jarray.getJSONObject(i);
			Product product = customerAccessObject.getProduct(tmp.getInt("ID"));
			product.quantity = tmp.getInt("quantity");
			items_in_cart.add(product);
		}*/
		List<Product> items_in_cart = get_customer_shopping_cart(customerID).shopping_cart_list;
		if(items_in_cart == null) 
			return Response.status(404).build();
		
		return Response.status(200).entity(items_in_cart).build();
	}

	@DELETE
	@Path("/customers/{ID}/shopping-cart/{product_ID}")
	public Response removeProductFromShoppingCart(@PathParam("ID") int customerID, @PathParam("product_ID") int product_ID)
	{
		// authentication isn't possible because of the lack of cookies
		Customer customer = DBAccessObject.getCustomer(customerID);
		
		if(customer == null)
			return Response.status(404).build();
		
		String shoppingCart = customer.shopping_cart;
		JSONArray jarray = new JSONArray(shoppingCart);
		
		
		for(int i = 0; i < jarray.length(); i++)
		{
			JSONObject tmp = jarray.getJSONObject(i);
			if(tmp.getInt("ID") == product_ID)
			{
				jarray.remove(i);
				customer.shopping_cart = jarray.toString();
				
				if(DBAccessObject.updateShoppingCart(customer) == false)
					return Response.status(400).build();
				
				return Response.status(200).build();
			}
		}
		
		return Response.status(400).build();
	}

	@PUT
	@Path("/customers/{ID}/shopping-cart/buy")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buyShoppingCart(@PathParam("ID") int customerID)
	{
		System.out.println("Purchaser: " + customerID);
		List<ID_string_pair> order_statuses = new ArrayList<>();
		
		Customer purchaser = get_customer_shopping_cart(customerID);
		List<Product> products = purchaser.shopping_cart_list;

		if(products == null)
			return Response.status(404).build();
		
		for(Product p : products)
		{	//int inventory_ID, int amount, LocalDateTime order_received_at, int status_ID, int ordered_by
			List<Inventory> inventory_status = DBAccessObject.getProductFromInventory(p.product_ID, true);
			if(inventory_status == null || inventory_status.size() == 0)
			{
				order_statuses.add(new ID_string_pair(p.product_ID, "false"));
				continue;
			}
			// inventory_status list could be sorted by some criteria.A static Filter could be used.
			
			Order order = new Order(inventory_status.get(0).inventory_ID, p.quantity, LocalDateTime.now(), ordered_status_ID, customerID);
			Boolean orderStatus = DBAccessObject.addOrder(order);
			
			order_statuses.add(new ID_string_pair(p.product_ID, orderStatus.toString()));
		}
		
		// modify the customer's cart so that it contains only the non successful orders
		JSONArray newShoppingCart = new JSONArray();
		JSONArray oldShoppingCart = new JSONArray(purchaser.shopping_cart);
		
		for(ID_string_pair pair : order_statuses)
		{
			if(pair.name.equals("true"))
			{
				// remove successfully purchased products from the customer's cart
				
				for(int i = 0; i < oldShoppingCart.length(); i++)
				{
					JSONObject tempObject = oldShoppingCart.getJSONObject(i);
					if(tempObject.getInt("ID") == pair.ID)
					{
						oldShoppingCart.remove(i);
						break;
					}
				}
				continue;
			}
		}
		
		purchaser.shopping_cart = oldShoppingCart.toString();
		if(DBAccessObject.updateShoppingCart(purchaser) == false)
			return Response.status(400).build();
		
		return Response.status(200).entity(order_statuses).build();
	}
	
	
	// employee related methods
	
	
	
	public static void main(String[] args)
	{
		
	}
}
