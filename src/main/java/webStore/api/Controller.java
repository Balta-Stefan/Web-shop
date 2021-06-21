package webStore.api;



import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import webStore.*;
import webStore.DAO.MySQL_DAO;
import webStore.model.Customer;
import webStore.model.Product_category;
import webStore.responses.*;


@Path("/v1")
public class Controller
{
	private static MySQL_DAO customerAccessObject;
	private static MySQL_DAO employeeAccessObject;
	
	private static String html_file;
	private static String css_file;
	private static String javascript_file;
	
	private static HashMap<Customer, NewCookie> cookies = new HashMap<>();
	
	static
	{
		System.out.println("In static block");
		try
		{
		 	Class.forName("com.mysql.cj.jdbc.Driver"); 
		    Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb", "root", "sigurnost");
		    customerAccessObject = new MySQL_DAO(connection);
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
			html_file = new String(Files.readAllBytes(Paths.get("D:\\Knjige za fakultet\\3. godina\\6. semestar\\Baze podataka\\Baze podataka - projekat\\Source\\Eclipse project\\src\\main\\webapp\\Resources\\index.html")));
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
			css_file = new String(Files.readAllBytes(Paths.get("D:\\Knjige za fakultet\\3. godina\\6. semestar\\Baze podataka\\Baze podataka - projekat\\Source\\Eclipse project\\src\\main\\webapp\\Resources\\css\\index.css")));
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
			javascript_file = new String(Files.readAllBytes(Paths.get("D:\\Knjige za fakultet\\3. godina\\6. semestar\\Baze podataka\\Baze podataka - projekat\\Source\\Eclipse project\\src\\main\\webapp\\Resources\\js\\index.js")));
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
		boolean response = customerAccessObject.customerRegistration(registrationInfo);
		
		if(response == true)
			return Response.status(200).build();
		else
			return Response.status(400).build();
	}
	
	@POST
	@Path("/customers/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response loginCustomer(Customer customer)
	{
		synchronized(cookies)
		{
			// check if the user is already logged in
			NewCookie exists = cookies.get(customer);
			if(exists != null)
				return Response.status(200).cookie(exists).build();
		}
		
		// getCustomer(String email)
		Customer customerData = customerAccessObject.getCustomer(customer.email);
		if(customerData == null || customer.password.equals(customerData.password) == false)
			return Response.status(404).build();
		
		Cookie tmp = new Cookie("user-cookie", customer.email);
		NewCookie cookie = new NewCookie(tmp, null, 1200, true);
		synchronized(cookies)
		{
			cookies.put(customerData, cookie);
		}
		
		return Response.status(200).cookie(cookie).build();
	}
	
	@GET
	@Path("/category")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMainCategories()
	{
		List<ID_string_pair> categories = customerAccessObject.getMainCategories();
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
		
		Product_category category = customerAccessObject.getCategory(categoryID);
		if(category.number_of_subcategories == 0)
		{
			// get the category's filters
			Filters filters = new Filters();
			filters.contents = customerAccessObject.get_filters(categoryID);
			return Response.status(200).entity(filters).build();
		}
		
		// get subcategories
		List<ID_string_pair> subcategories = customerAccessObject.get_subcategories(categoryID);
		Subcategories subcategoriesWrapper = new Subcategories();
		subcategoriesWrapper.contents = subcategories;
		
		return Response.status(200).entity(subcategoriesWrapper).build();
	}

}
