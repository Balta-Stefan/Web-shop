import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAOtester
{
    /*
        Things to test:
        1)Product related
            -add products - working
            -add product pictures - working
            -add product categories - working
            -add category filters - working
            -add filter values - working
            -add filter values to products
            -get all products
            -get filtered products
        2)Warehouse related
            -get warehouses
        3)Supplier related
            -get all suppliers
        4)Inventory related
            -get all of inventory
            -add things to inventory
        5)Orders related
            -add orders
            -add returned reason
        6)Employees related
            -get all employees
        7)Customer related
            -get a customer with email
            -register a customer
            -add a review to a product
        8)Manufacturer related
            -add a manufacturer
            -get all manufacturers
     */
    public static void main(String[] args)
    {
        try
        {
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb", "root", "sigurnost");
            MySQL_DAO dao = new MySQL_DAO(connection);

            //Product product1 = new Product(4, "banane cokoladne s vanilijom", 1, new BigDecimal("1.31"), 1, 1.0, "to su banane", null, (byte)0);
            //Manufacturer manufacturer1 = new Manufacturer("Bananito");
            //Product_category category1 = new Product_category("Voce");
            //Category_filter category_filter1 = new Category_filter("Vrsta");
            //Filter_value filter_value1 = new Filter_value("Kostunicavo");


            // System.out.println();
            System.out.println(dao.addFilterToProduct(1, 1));
        }
        catch(SQLException e){System.out.println(e);}

    }
}
