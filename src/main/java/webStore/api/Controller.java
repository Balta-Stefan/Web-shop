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

import webStore.DAO.Category_filtersDAO;
import webStore.DAO.CustomerDAO;
import webStore.DAO.EmployeesDAO;
import webStore.DAO.Filter_valuesDAO;
import webStore.DAO.InventoryDAO;
import webStore.DAO.ManufacturersDAO;
import webStore.DAO.Order_statusesDAO;
import webStore.DAO.OrdersDAO;
import webStore.DAO.ProductDAO;
import webStore.DAO.Product_categoriesDAO;
import webStore.DAO.SupplierDAO;
import webStore.DAO.WarehouseDAO;
import webStore.model.Customer;
import webStore.model.Employee;
import webStore.model.Inventory;
import webStore.model.Manufacturer;
import webStore.model.Order;
import webStore.model.Order_status;
import webStore.model.Product;
import webStore.model.Product_category;
import webStore.model.Supplier;
import webStore.model.Warehouse;
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
	private static final String DB_rootUsername = "root";
	private static final String DB_rootPassword = "sigurnost";
	private static final String DB_customerUsername = "customer";
	private static final String DB_customerPassword = "customerPass";
	private static final String DB_employeeUsername = "employee";
	private static final String DB_employeePassword = "employeePass";
	
	private static connectionPool customerPool = new connectionPool(DB_customerUsername, DB_customerPassword);
	private static connectionPool employeePool = new connectionPool(DB_employeeUsername, DB_employeePassword);
	private static connectionPool rootPool = new connectionPool(DB_rootUsername, DB_rootPassword);
	
	
	private static final String ordered_status_name = "Ordered";
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
		    
		    List<Order_status> tmpList = new Order_statusesDAO(rootPool).getAll();
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
	
	// employee related
	@GET
	@Path("/employees/js/utilities.js")
	@Produces("text/javascript")
	public Response getEmployeeUtilitiesJS()
	{
		String employeePanel = readFile("\\Employees\\js\\utilities.js");
		if(employeePanel == null)
			return Response.status(500).build();
		
		return Response.status(200).entity(employeePanel).build();
	}
	
	
	@GET
	@Path("/employees/orders-panel")
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
	@Path("/employees/js/employee orders.js")
	@Produces("text/javascript")
	public Response getEmployeeOrdersJS()
	{
		String employeePanel = readFile("\\Employees\\js\\employee orders.js");
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
	@Path("/employees/js/employee suppliers.js")
	@Produces("text/javascript")
	public Response getEmployeeSuppliersJS()
	{
		String employeePanel = readFile("\\Employees\\js\\employee suppliers.js");
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
	@Path("/employees/js/employee warehouses.js")
	@Produces("text/javascript")
	public Response getEmployeeWarehousesJS()
	{
		String employeePanel = readFile("\\Employees\\js\\employee warehouses.js");
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
	@Path("/employees/js/employee products.js")
	@Produces("text/javascript")
	public Response getEmployeeProductsJS()
	{
		String employeePanel = readFile("\\Employees\\js\\employee products.js");
		if(employeePanel == null)
			return Response.status(500).build();
		
		return Response.status(200).entity(employeePanel).build();
	}
	
	@GET
	@Path("/employees/inventory.html")
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
	@Path("/employees/js/employee inventory.js")
	@Produces("text/javascript")
	public Response getEmployeeInventoryJS()
	{
		String employeePanel = readFile("\\Employees\\js\\employee inventory.js");
		if(employeePanel == null)
			return Response.status(500).build();
		
		return Response.status(200).entity(employeePanel).build();
	}
	
	@GET
	@Path("/employees/add-inventory")
	@Produces("text/html")
	public Response getEmployeesAddInventory()
	{
		String employeePanel = readFile("\\Employees\\employee add inventory.html");
		if(employeePanel == null)
			return Response.status(500).build();
		
		return Response.status(200).entity(employeePanel).build();
	}
	@GET
	@Path("/employees/css/employee add inventory.css")
	@Produces("text/css")
	public Response getEmployeeAddInventoryCSS()
	{
		String employeePanel = readFile("\\Employees\\css\\employee add inventory.css");
		if(employeePanel == null)
			return Response.status(500).build();
		
		return Response.status(200).entity(employeePanel).build();
	}
	@GET
	@Path("/employees/js/employee add inventory.js")
	@Produces("text/javascript")
	public Response getEmployeeAddInventoryJS()
	{
		String employeePanel = readFile("\\Employees\\js\\employee add inventory.js");
		if(employeePanel == null)
			return Response.status(500).build();
		
		return Response.status(200).entity(employeePanel).build();
	}
	
	
	@GET
	@Path("/employees/categories_and_filters")
	@Produces("text/html")
	public Response getEmployeesCategoriesAndFilters()
	{
		String employeePanel = readFile("\\Employees\\employee categories and filters.html");
		if(employeePanel == null)
			return Response.status(500).build();
		
		return Response.status(200).entity(employeePanel).build();
	}
	@GET
	@Path("/employees/css/employee categories and filters.css")
	@Produces("text/css")
	public Response getEmployeesCategoriesAndFiltersCSS()
	{
		String employeePanel = readFile("\\Employees\\css\\employee categories and filters.css");
		if(employeePanel == null)
			return Response.status(500).build();
		
		return Response.status(200).entity(employeePanel).build();
	}
	@GET
	@Path("/employees/js/employee categories and filters.js")
	@Produces("text/javascript")
	public Response getEmployeesCategoriesAndFiltersJS()
	{
		String employeePanel = readFile("\\Employees\\js\\employee categories and filters.js");
		if(employeePanel == null)
			return Response.status(500).build();
		
		return Response.status(200).entity(employeePanel).build();
	}
	
	@GET
	@Path("/employees/add-product")
	@Produces("text/html")
	public Response getEmployeesAddProduct()
	{
		String employeePanel = readFile("\\Employees\\employee add product.html");
		if(employeePanel == null)
			return Response.status(500).build();
		
		return Response.status(200).entity(employeePanel).build();
	}
	@GET
	@Path("/employees/css/employee add product.css")
	@Produces("text/css")
	public Response getEmployeesAddProductCSS()
	{
		String employeePanel = readFile("\\Employees\\css\\employee add product.css");
		if(employeePanel == null)
			return Response.status(500).build();
		
		return Response.status(200).entity(employeePanel).build();
	}
	@GET
	@Path("/employees/js/employee add product.js")
	@Produces("text/javascript")
	public Response getEmployeesAddProductJS()
	{
		String employeePanel = readFile("\\Employees\\js\\employee add product.js");
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
		Employee employee = new EmployeesDAO(employeePool).get(username);
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
    @Path("/employees/orders")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrders()
    {
    	List<Order> orders = new OrdersDAO(employeePool).getAll();
    	
    	if(orders == null)
    		return Response.status(500).build();
    	
    	CacheControl cache = new CacheControl();
    	cache.setMaxAge(60);
    	cache.setPrivate(true);
    	
    	return Response.status(200).cacheControl(cache).entity(orders).build();
    }
    @GET
    @Path("/employees/orders/{order_ID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrder(@PathParam("order_ID") int order_ID)
    {
    	Order order = new OrdersDAO(employeePool).get(order_ID);
    	
    	if(order == null)
    		return Response.status(500).build();
    	
    	return Response.status(200).entity(order).build();
    }

    @GET
    @Path("/employees/warehouse")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWarehouses()
    {
    	List<Warehouse> warehouses = new WarehouseDAO(employeePool).getWarehouses();
    	
    	if(warehouses == null)
    		return Response.status(500).build();
    	
    	return Response.status(200).entity(warehouses).build();
    }
    @GET
    @Path("/employees/warehouse/{warehouse_ID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWarehouse(@PathParam("warehouse_ID") int warehouse_ID)
    {
    	Warehouse warehouse = new WarehouseDAO(employeePool).getWarehouse(warehouse_ID);
    	
    	if(warehouse == null)
    		return Response.status(500).build();
    	
    	return Response.status(200).entity(warehouse).build();
    }
    
    @GET
    @Path("/employees/product")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts()
    {
    	List<Product> products = new ProductDAO(employeePool).getProducts();
    	
    	if(products == null)
    		return Response.status(500).build();
    	
    	CacheControl cache = new CacheControl();
    	cache.setMaxAge(60);
    	cache.setPrivate(true);
    	
    	return Response.status(200).cacheControl(cache).entity(products).build();
    }
    @GET
    @Path("/employees/products/{product_ID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductEmployee(@PathParam("product_ID") int product_ID)
    {
    	Product product = new ProductDAO(employeePool).getProduct(product_ID);
    	
    	if(product == null)
    		return Response.status(500).build();
    	
    	return Response.status(200).entity(product).build();
    }
    
    @GET
    @Path("/employees/inventory")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInventories()
    {
    	List<Inventory> inventories = new InventoryDAO(employeePool).getInventory();
    	
    	if(inventories == null)
    		return Response.status(500).build();
    	
    	CacheControl cache = new CacheControl();
    	cache.setMaxAge(60);
    	cache.setPrivate(true);
    	
    	return Response.status(200).cacheControl(cache).entity(inventories).build();
    }
    @GET
    @Path("/employees/inventory/{inventory_ID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInventory(@PathParam("inventory_ID") int inventory_ID)
    {
    	Inventory inventory = new InventoryDAO(employeePool).getInventory(inventory_ID);
    	
    	if(inventory == null)
    		return Response.status(500).build();
    	
    	return Response.status(200).entity(inventory).build();
    }
    @PUT
    @Path("/employees/inventory")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addInventory(Inventory inventory)
    {
    	boolean status = new InventoryDAO(employeePool).insert(inventory);
    	
    	if(status == false)
    		return Response.status(400).build();
    	
    	return Response.status(200).build();
    }
    
    @PUT
    @Path("/product")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addProduct(Product product)
    {
    	boolean status = new ProductDAO(employeePool).insert(product);
    	
    	if(status == false)
    		return Response.status(400).build();
    				
    	return Response.status(200).build();
    }
    
    @PUT
    @Path("/supplier")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addSupplier(Supplier supplier)
    {
    	if(supplier.name.isEmpty())
    		return Response.status(400).build();

    	boolean status = new SupplierDAO(employeePool).addSupplier(supplier);
    	
    	if(status == false)
    		return Response.status(400).build();
    	
    	return Response.status(200).build();
    }
    @GET
    @Path("/supplier")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSuppliers()
    {
    	List<Supplier> suppliers = new SupplierDAO(employeePool).getSuppliers();
    	if(suppliers == null)
    		return Response.status(400).build();
    	
    	List<ID_string_pair> pairs = new ArrayList<>();
    	for(Supplier s : suppliers)
    	{
    		pairs.add(new ID_string_pair(s.supplier_ID, s.name));
    	}
    	
    	CacheControl cache = new CacheControl();
    	cache.setMaxAge(60);
    	cache.setPrivate(true);
    	
    	return Response.status(200).cacheControl(cache).entity(pairs).build();
    }
    @GET
    @Path("/supplier/{ID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSupplier(@PathParam("ID") int supplier_ID)
    {
    	Supplier supplier = new SupplierDAO(employeePool).getSupplier(supplier_ID);
    	
    	if(supplier == null)
    		return Response.status(400).build();
    	
    	return Response.status(200).entity(supplier).build();
    }
    
    @GET
    @Path("/manufacturer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getManufacturers()
    {
    	List<Manufacturer> manufacturers = new ManufacturersDAO(employeePool).getAll();
    	
    	if(manufacturers == null)
    		return Response.status(500).build();
    	
    	CacheControl cache = new CacheControl();
    	cache.setMaxAge(60);
    	cache.setPrivate(true);
    	
    	return Response.status(200).cacheControl(cache).entity(manufacturers).build();
    }
    
    // customer related
    
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
    	
    	ProductDAO prodDAO = new ProductDAO(customerPool);
    	prodDAO.updateProductPrice(target_inventory.product_ID, target_inventory.price);
    	//DBAccessObject.updatePrice(target_inventory.product_ID, target_inventory.price);
    }

	@POST
	@Path("/customers")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerUser(Customer registrationInfo)
	{
		// only emails are unique so there is no need to check if the user already exists
		boolean response = new CustomerDAO(customerPool).add(registrationInfo);
		//boolean response = DBAccessObject.customerRegistration(registrationInfo);
		
		if(response == true)
			return Response.status(200).build();
		else
			return Response.status(400).build();
	}
	
	@POST
	@Path("/customer/login")
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
		
		/*synchronized(cookies)
		{
			// check if the user is already logged in
			Customer exists = cookies.get(customer.email);
			if(exists != null && exists.password.equals(customer.password))
				return Response.status(200).entity(new TempClass(exists.customer_ID)).build();
		}*/
		
		// getCustomer(String email)
		//Customer customerData = DBAccessObject.getCustomer(customer.email);
		Customer customerData = new CustomerDAO(customerPool).get(customer.email);
		if(customerData == null || customer.password.equals(customerData.password) == false)
			return Response.status(404).build();
		
		//Cookie tmp = new Cookie("user-cookie", customer.email);
		//NewCookie cookie = new NewCookie("user-cookie", customer.email);
		//NewCookie cookie = new NewCookie(tmp, null, 1200, true);
		/*synchronized(cookies)
		{
			cookies.put(customerData.email, customerData);
		}*/
		

		
		TempClass tmp = new TempClass(customerData.customer_ID);
		
		//return Response.status(200).cookie(cookie).build();
		return Response.status(200).entity(tmp).build();
	}
	
	@GET
	@Path("/category")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMainCategories()
	{
		List<ID_string_pair> categories = new Product_categoriesDAO(customerPool).getMainCategories();//DBAccessObject.getMainCategories();
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
		Product_categoriesDAO productCatsDAO = new Product_categoriesDAO(customerPool);
		Product_category category = productCatsDAO.get(categoryID);
		if(category.number_of_subcategories == 0)
		{
			// get the category's filters
			Filters filters = new Filters();
			//filters.contents = DBAccessObject.get_filters(categoryID);
			filters.contents = new Filter_valuesDAO(customerPool).get_filters(categoryID);
			return Response.status(200).entity(filters).build();
		}
		
		// get subcategories
		List<ID_string_pair> subcategories = productCatsDAO.getSubcategories(categoryID);//DBAccessObject.get_subcategories(categoryID);
		Subcategories subcategoriesWrapper = new Subcategories();
		subcategoriesWrapper.contents = subcategories;
		
		return Response.status(200).entity(subcategoriesWrapper).build();
	}
	
	@PUT
	@Path("/category")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addSubCategory(ID_string_pair value)
	{
		// authentication and authorization have to be handled - to do
		
		Integer parentID = (value.ID == -1) ? null : value.ID;
		boolean response = new Product_categoriesDAO(employeePool).addCategory(parentID, value.name);
		
		if(response == false)
			return Response.status(400).build();
		
		return Response.status(200).build();
	}
	@PUT
	@Path("/filter")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addFilter(ID_string_pair value)
	{
		// authentication and authorization have to be handled - to do
		Product_category category = new Product_categoriesDAO(employeePool).get(value.ID);
		System.out.println(category);
		if(category == null)
			return Response.status(400).build();
		
		if(category.number_of_subcategories != 0)
			return Response.status(400).build(); // cannot add filters to non-leaf categories
		
		boolean status = new Category_filtersDAO(employeePool).addCategoryFilter(value.ID, value.name);
		if(status == false)
			return Response.status(400).build();
		
		return Response.status(200).build();
	}
	@PUT
	@Path("/filter-value")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addFilterValue(ID_string_pair value)
	{
		// authentication and authorization have to be handled - to do
		boolean status = new Filter_valuesDAO(employeePool).addFilterValue(value.ID, value.name);
		if(status == false)
			return Response.status(400).build();
		
		
		return Response.status(200).build();
	}
	
	@DELETE
	@Path("/category/{categoryID}")
	public Response deleteCategory(@PathParam("categoryID") int categoryID)
	{
		// authentication and authorization have to be handled - to do
		Product_categoriesDAO dao = new Product_categoriesDAO(employeePool);
		Product_category category = dao.get(categoryID);
		boolean status = dao.deleteCategory(categoryID, category.parent_category_ID);
		
		if(status == false)
			return Response.status(400).build();
		
		
		return Response.status(200).build();
	}
	@DELETE
	@Path("/filter/{filterID}")
	public Response deleteFilter(@PathParam("filterID") int filterID)
	{
		// authentication and authorization have to be handled - to do
		boolean status = new Category_filtersDAO(employeePool).deleteFilter(filterID);
		if(status == false)
			return Response.status(400).build();
		
		
		return Response.status(200).build();
	}
	@DELETE
	@Path("/filter-value/{filter_value_ID}")
	public Response deleteFilterValue(@PathParam("filter_value_ID") int filter_value_ID)
	{
		// authentication and authorization have to be handled - to do
		boolean status = new Filter_valuesDAO(employeePool).deleteFilterValue(filter_value_ID);
		if(status == false)
			return Response.status(400).build();
		
		
		return Response.status(200).build();
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
		List<Product> products = new ProductDAO(customerPool).getFilteredProducts(filterIdentifiers);
		List<TrimmedProduct> responseProducts = new ArrayList<>();
		
		for(Product p : products)
		{
			if(p.price == null)
				continue; // ignore products that have no price
			responseProducts.add(new TrimmedProduct(p.product_ID, p.name, p.thumbnail, p.price.toString()));
		}
		
		return Response.status(200).entity(responseProducts).build();
	}
	
	@GET
	@Path("/product/{productID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProduct(@PathParam("productID") int productID)
	{
		//Product product = DBAccessObject.getProduct(productID);
		Product product = new ProductDAO(customerPool).getProduct(productID);
		if(product == null)
			return Response.status(404).build();
		
		return Response.status(200).entity(product).build();
	}

	@PUT
	@Path("/customer/{customerID}/shopping-cart")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add_product_to_cart(@PathParam("customerID") int customerID, BuyProduct purchaseInfo)
	{
		// old path: @Path("/buy/{ID}")
		
		/*Customer purchaser = cookies.get(quantity.customer_email);
		if(purchaser == null)
			return Response.status(404).build();*/
		
		//Customer customer = DBAccessObject.getCustomer(purchaser.customer_ID);
		CustomerDAO custDAO = new CustomerDAO(customerPool);
		Customer customer = custDAO.get(customerID);
		String customerCart = customer.shopping_cart;
		if(customerCart == null)
			customerCart = "[]";
		
		JSONArray jarray = new JSONArray(customerCart);
		System.out.println("Customer's cart: " + jarray);
		
		String newItem = "{ID: " + purchaseInfo.productID + ", quantity: " + purchaseInfo.quantity + "}";
		JSONObject obj = new JSONObject(newItem);
		
		
		boolean productAlreadyBought = false;
		for(int i = 0; i < jarray.length(); i++)
		{
			JSONObject tmp = jarray.getJSONObject(i);
			Integer tmpProductID = tmp.getInt("ID");
			Integer oldQuantity = tmp.getInt("quantity");
			oldQuantity += purchaseInfo.quantity;
			
			if(tmpProductID == purchaseInfo.productID)
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
		ProductDAO prodDAO = new ProductDAO(customerPool);

		//Customer customer = DBAccessObject.getCustomer(customerID);
		Customer customer = new CustomerDAO(customerPool).get(customerID);
		if(customer == null)
			return null;
		
		String shoppingCart = customer.shopping_cart;
		JSONArray jarray = new JSONArray(shoppingCart);
		
		List<Product> cart = new ArrayList<>();
		
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
	@Path("/customer/{ID}/shopping-cart")
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
		
		
		/*List<Product> items_in_cart = get_customer_shopping_cart(customerID).shopping_cart_list;
		if(items_in_cart == null) 
			return Response.status(404).build();
		
		return Response.status(200).entity(items_in_cart).build();*/
		
		
		ProductDAO prodDAO = new ProductDAO(customerPool);

		//Customer customer = DBAccessObject.getCustomer(customerID);
		Customer customer = new CustomerDAO(customerPool).get(customerID);
		if(customer == null)
			return Response.status(404).build();
		
		String shoppingCart = customer.shopping_cart;
		if(shoppingCart == null)
			return Response.status(200).entity(new JSONArray("[]")).build();
		
		JSONArray jarray = new JSONArray(shoppingCart);
		
		List<Product> cart = new ArrayList<>();
		
		for(int i = 0; i < jarray.length(); i++)
		{
			JSONObject tmp = jarray.getJSONObject(i);
			//Product product = DBAccessObject.getProduct(tmp.getInt("ID"));
			Product product = prodDAO.getProduct(tmp.getInt("ID"));
			product.quantity = tmp.getInt("quantity");
			cart.add(product);
		}
		
		//customer.shopping_cart_list = cart;
		
		return Response.status(200).entity(cart).build();
	}
	@DELETE
	@Path("/customer/{ID}/shopping-cart/{product_ID}")
	public Response removeProductFromShoppingCart(@PathParam("ID") int customerID, @PathParam("product_ID") int product_ID)
	{
		// authentication isn't possible because of the lack of cookies
		CustomerDAO custDAO = new CustomerDAO(customerPool);
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
	@Path("/customer/{ID}/shopping-cart/buy")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buyShoppingCart(@PathParam("ID") int customerID)
	{
		System.out.println("Purchaser: " + customerID);
		List<ID_string_pair> order_statuses = new ArrayList<>();
		
		Customer purchaser = get_customer_shopping_cart(customerID);
		List<Product> products = purchaser.shopping_cart_list;

		if(products == null)
			return Response.status(404).build();
		
		InventoryDAO invDAO = new InventoryDAO(customerPool);
		OrdersDAO ordersDAO = new OrdersDAO(customerPool);
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
		CustomerDAO custDAO = new CustomerDAO(customerPool);
		if(custDAO.updateShoppingCart(purchaser) == false)
			return Response.status(400).build();
		
		return Response.status(200).entity(order_statuses).build();
	}
	
	
	// employee related methods
	
	
	
	public static void main(String[] args)
	{
		
	}
}
