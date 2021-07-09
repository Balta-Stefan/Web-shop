package webStore.api;



import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.json.JSONArray;
import org.json.JSONObject;

import webStore.DAO.CustomerDAO;
import webStore.DAO.EmployeesDAO;
import webStore.DAO.Filter_valuesDAO;
import webStore.DAO.InventoryDAO;
import webStore.DAO.Order_statusesDAO;
import webStore.DAO.OrdersDAO;
import webStore.DAO.ProductDAO;
import webStore.DAO.Product_categoriesDAO;
import webStore.model.Customer;
import webStore.model.Employee;
import webStore.model.Inventory;
import webStore.model.Order;
import webStore.model.Order_status;
import webStore.model.Product;
import webStore.model.Product_category;
import webStore.responses.*;
import webStore.utilities.connectionPool;

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
	private static connectionPool pool = new connectionPool();
	
	private static String html_file;
	private static String css_file;
	private static String javascript_file;
	
	private static final String ordered_status_name = "ORDERED";
	private static int ordered_status_ID; // might cause issues along with ordered_status_name
	
	private static String pathPrefix = "D:\\Knjige za fakultet\\3. godina\\6. semestar\\Baze podataka\\Baze podataka - projekat\\Source\\Web-shop\\src\\main\\webapp\\Resources";
	
	private static HashMap<String, Customer> cookies = new HashMap<>(); // maps email to a customer
	
	static
	{
		try
		{
		 	Class.forName("com.mysql.cj.jdbc.Driver"); 
		    //Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb", "root", "sigurnost");
		    //DBAccessObject = new MySQL_DAO(connection);
		    
		    List<Order_status> tmpList = new Order_statusesDAO(pool).getAll();
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
	
	private String readFile(String relativePath)
	{
		try
		{
			return new String(Files.readAllBytes(Paths.get(pathPrefix + relativePath)));
		}
		catch(Exception e) {return null;}
	}
	
	@GET
	@Path("/employees/orders")
	@Produces("text/html")
	public Response getEmployeeOrdersPanel()
	{
		String employeePanel = readFile("\\Employees\\employee orders.html");
		if(employeePanel == null)
			return Response.status(500).build();
		
		return Response.status(200).entity(employeePanel).build();
	}
	@GET
	@Path("/employees/css/employee orders.css")
	@Produces("text/css")
	public Response getEmployeeOrdersCSS()
	{
		String employeePanel = readFile("\\Employees\\css\\employee orders.css");
		if(employeePanel == null)
			return Response.status(500).build();
		
		return Response.status(200).entity(employeePanel).build();
	}
	
	@GET
	@Path("/employees/suppliers")
	@Produces("text/html")
	public Response getEmployeeSuppliersPanel()
	{
		String employeePanel = readFile("\\Employees\\employee suppliers.html");
		if(employeePanel == null)
			return Response.status(500).build();
		
		return Response.status(200).entity(employeePanel).build();
	}
	@GET
	@Path("/employees/css/employee suppliers.css")
	@Produces("text/css")
	public Response getEmployeeSuppliersCSS()
	{
		String employeePanel = readFile("\\Employees\\css\\employee suppliers.css");
		if(employeePanel == null)
			return Response.status(500).build();
		
		return Response.status(200).entity(employeePanel).build();
	}
	
	@GET
	@Path("/employees/warehouses")
	@Produces("text/html")
	public Response getEmployeeWarehousesPanel()
	{
		String employeePanel = readFile("\\Employees\\employee warehouses.html");
		if(employeePanel == null)
			return Response.status(500).build();
		
		return Response.status(200).entity(employeePanel).build();
	}
	@GET
	@Path("/employees/css/employee warehouses.css")
	@Produces("text/css")
	public Response getEmployeeWarehousesCSS()
	{
		String employeePanel = readFile("\\Employees\\css\\employee warehouses.css");
		if(employeePanel == null)
			return Response.status(500).build();
		
		return Response.status(200).entity(employeePanel).build();
	}
	
	@GET
	@Path("/employees/products")
	@Produces("text/html")
	public Response getEmployeeProductsPanel()
	{
		String employeePanel = readFile("\\Employees\\employee products.html");
		if(employeePanel == null)
			return Response.status(500).build();
		
		return Response.status(200).entity(employeePanel).build();
	}
	@GET
	@Path("/employees/css/employee products.css")
	@Produces("text/css")
	public Response getEmployeeProductsCSS()
	{
		String employeePanel = readFile("\\Employees\\css\\employee products.css");
		if(employeePanel == null)
			return Response.status(500).build();
		
		return Response.status(200).entity(employeePanel).build();
	}

	@GET
	@Path("/employees/inventory")
	@Produces("text/html")
	public Response getEmployeesInventory()
	{
		String employeePanel = readFile("\\Employees\\employee inventory.html");
		if(employeePanel == null)
			return Response.status(500).build();
		
		return Response.status(200).entity(employeePanel).build();
	}
	@GET
	@Path("/employees/css/employee inventory.css")
	@Produces("text/css")
	public Response getEmployeeInventoryCSS()
	{
		String employeePanel = readFile("\\Employees\\css\\employee inventory.css");
		if(employeePanel == null)
			return Response.status(500).build();
		
		return Response.status(200).entity(employeePanel).build();
	}
	
	@GET
	@Path("/employees/panel")
	@Produces("text/html")
	public Response getEmployeeIndex()
	{
		String employeePanel = readFile("\\Employees\\employee index.html");
		if(employeePanel == null)
			return Response.status(500).build();
		
		return Response.status(200).entity(employeePanel).build();
	}
	@GET
	@Path("/employees/css/employee index.css")
	@Produces("text/css")
	public Response getEmployeeIndexCSS()
	{
		String employeePanel = readFile("\\Employees\\css\\employee index.css");
		if(employeePanel == null)
			return Response.status(500).build();
		
		return Response.status(200).entity(employeePanel).build();
	}
	
	@POST
	@Path("/employees/login")
	public Response employeeLogin(@FormParam("username") String username, @FormParam("password") String password)
	{
		Employee employee = new EmployeesDAO(pool).get(username);
		if(employee == null || password.equals(employee.password) == false)
			return Response.status(403).build();
		
		// handle sessions - to do
		
		URI getEmployeePanelURL = URI.create("v1/employees/panel");
		return Response.seeOther(getEmployeePanelURL).build();
	}
	
	@GET
	@Path("/employees")
	@Produces("text/html")
	public Response getEmployeeLoginPanel()
	{
		String file = readFile("\\Employees\\employee login.html");
    	if(file == null)
    		return Response.status(500).build();
    	return Response.status(200).entity(file).build();
	}
    @GET
    @Path("/css/employee login.css")
    @Produces("text/css")
    public Response getEmployeeLoginCSS()
    {
     	String file = readFile("\\Employees\\css\\employee login.css");
    	if(file == null)
    		return Response.status(500).build();
    	return Response.status(200).entity(file).build();
    }

    
    @GET
    @Produces("text/html")
    public Response getCustomerIndex()
    {
     	String file = readFile("\\Customers\\index.html");
    	if(file == null)
    		return Response.status(500).build();
    	return Response.status(200).entity(file).build();
    }
    @GET
    @Path("/css/index.css")
    @Produces("text/css")
    public Response getCustomerCSS()
    {
     	String file = readFile("\\Customers\\css\\index.css");
    	if(file == null)
    		return Response.status(500).build();
    	return Response.status(200).entity(file).build();
    }
    @GET
    @Path("/js/index.js")
    @Produces("text/javascript")
    public Response getCustomerJS()
    {
    	String file = readFile("\\Customers\\js\\index.js");
    	if(file == null)
    		return Response.status(500).build();
    	return Response.status(200).entity(file).build();
    	/*try
		{
			javascript_file = new String(Files.readAllBytes(Paths.get(pathPrefix + "js\\index.js")));
			return javascript_file;
		}
		catch(Exception e) {System.out.println("Exception happened: " + e); return null;}*/
    }
	
    
    
    private void handleEmptyInventory(List<Inventory> inventories_containing_product)
    {
    	// called when a customer empties the stock of an inventory whose price is used in the table Products.The price of the product might need to be changed.
    	
    	// for now, the oldest stock will be used
    	inventories_containing_product.sort((a, b) -> a.delivered_at.compareTo(b.delivered_at));
    	Inventory target_inventory = inventories_containing_product.get(0);
    	
    	ProductDAO prodDAO = new ProductDAO(pool);
    	prodDAO.updateProductPrice(target_inventory.product_ID, target_inventory.price);
    	//DBAccessObject.updatePrice(target_inventory.product_ID, target_inventory.price);
    }

	@POST
	@Path("/customers")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerUser(Customer registrationInfo)
	{
		// only emails are unique so there is no need to check if the user already exists
		boolean response = new CustomerDAO(pool).add(registrationInfo);
		//boolean response = DBAccessObject.customerRegistration(registrationInfo);
		
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
			if(exists != null && exists.password.equals(customer.password))
				return Response.status(200).entity(new TempClass(exists.customer_ID)).build();
		}
		
		// getCustomer(String email)
		//Customer customerData = DBAccessObject.getCustomer(customer.email);
		Customer customerData = new CustomerDAO(pool).get(customer.email);
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
		List<ID_string_pair> categories = new Product_categoriesDAO(pool).getMainCategories();//DBAccessObject.getMainCategories();
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
		
		//Product_category category = DBAccessObject.getCategory(categoryID);
		Product_categoriesDAO productCatsDAO = new Product_categoriesDAO(pool);
		Product_category category = productCatsDAO.get(categoryID);
		if(category.number_of_subcategories == 0)
		{
			// get the category's filters
			Filters filters = new Filters();
			//filters.contents = DBAccessObject.get_filters(categoryID);
			filters.contents = new Filter_valuesDAO(pool).get_filters(categoryID);
			return Response.status(200).entity(filters).build();
		}
		
		// get subcategories
		List<ID_string_pair> subcategories = productCatsDAO.getSubcategories(categoryID);//DBAccessObject.get_subcategories(categoryID);
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
		
		//List<Product> products = DBAccessObject.getFilteredProducts(filterIdentifiers);
		List<Product> products = new ProductDAO(pool).getFilteredProducts(filterIdentifiers);
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
		//Product product = DBAccessObject.getProduct(productID);
		Product product = new ProductDAO(pool).getProduct(productID);
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
		
		//Customer customer = DBAccessObject.getCustomer(purchaser.customer_ID);
		CustomerDAO custDAO = new CustomerDAO(pool);
		Customer customer = custDAO.get(purchaser.customer_ID);
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
		//boolean result = DBAccessObject.updateShoppingCart(customer);
		boolean result = custDAO.updateShoppingCart(customer);
	
		if(result == false)
			return Response.status(404).build();
	
		
		return Response.status(200).build();
	}
	
	private Customer get_customer_shopping_cart(int customerID)
	{
		ProductDAO prodDAO = new ProductDAO(pool);
		
		List<Product> cart = new ArrayList<>();
		
		//Customer customer = DBAccessObject.getCustomer(customerID);
		Customer customer = new CustomerDAO(pool).get(customerID);
		if(customer == null)
			return null;
		
		String shoppingCart = customer.shopping_cart;
		JSONArray jarray = new JSONArray(shoppingCart);
		
		for(int i = 0; i < jarray.length(); i++)
		{
			JSONObject tmp = jarray.getJSONObject(i);
			//Product product = DBAccessObject.getProduct(tmp.getInt("ID"));
			Product product = prodDAO.getProduct(tmp.getInt("ID"));
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
		CustomerDAO custDAO = new CustomerDAO(pool);
		//Customer customer = DBAccessObject.getCustomer(customerID);
		Customer customer = custDAO.get(customerID);
		
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
				
				if(custDAO.updateShoppingCart(customer) == false)
					return Response.status(400).build();
				//if(DBAccessObject.updateShoppingCart(customer) == false)
					//return Response.status(400).build();
				
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
		
		InventoryDAO invDAO = new InventoryDAO(pool);
		OrdersDAO ordersDAO = new OrdersDAO(pool);
		for(Product p : products)
		{	//int inventory_ID, int amount, LocalDateTime order_received_at, int status_ID, int ordered_by
			//List<Inventory> inventory_status = DBAccessObject.getProductFromInventory(p.product_ID, true);
			List<Inventory> inventory_status = invDAO.getProductFromInventory(p.product_ID, true);
			if(inventory_status == null || inventory_status.size() == 0)
			{
				order_statuses.add(new ID_string_pair(p.product_ID, "false"));
				continue;
			}
			// inventory_status list could be sorted by some criteria.A static Filter could be used.
			// Application should decide what to do if inventory gets empty for this product.A new price has to be set in the table Products to reflect this change.
			
			// find the inventory that has enough stock
			Inventory inventory_to_take = null;
			for(Inventory i : inventory_status)
			{
				if(i.available_amount >= p.quantity)
				{
					inventory_to_take = i;
					break;
				}
			}
		
			if(inventory_to_take == null)
			{
				// no inventory has enough stock
				order_statuses.add(new ID_string_pair(p.product_ID, "false"));
				continue;
			}
			
			inventory_to_take.available_amount -= p.quantity;
			Order order = new Order(inventory_status.get(0).inventory_ID, p.quantity, LocalDateTime.now(), ordered_status_ID, customerID);
			//Boolean orderStatus = DBAccessObject.addOrder(order);
			Boolean orderStatus = ordersDAO.add(order);
			
			
			if(inventory_to_take.available_amount == 0)
			{
				// this customer has emptied the stock in this inventory.Decide what to do with the price in the table Products.
				inventory_status.remove(inventory_to_take);
				handleEmptyInventory(inventory_status);
			}
			
			order_statuses.add(new ID_string_pair(p.product_ID, orderStatus.toString()));
		}
		
		// modify the customer's cart so that it contains only the non successful orders
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
		CustomerDAO custDAO = new CustomerDAO(pool);
		if(custDAO.updateShoppingCart(purchaser) == false)
			return Response.status(400).build();
		
		return Response.status(200).entity(order_statuses).build();
	}
	
	
	// employee related methods
	
	
	
	public static void main(String[] args)
	{
		
	}
}
