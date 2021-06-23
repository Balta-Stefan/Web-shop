package webStore.DAO;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import webStore.model.Product_category;


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
            -add filter values to products - working
            -get all products - working
            -get filtered products - working
        2)Warehouse related
            -get warehouses - working
        3)Supplier related
            -get all suppliers - working
        4)Inventory related
            -get all of inventory - working
            -add things to inventory - working
        5)Orders related
            -add orders - working
            -add returned reason - working
        6)Employees related
            -get all employees - not implemented
            -get an employee by username - working
        7)Customer related
            -get a customer with email - working
            -register a customer - working
            -add a review to a product - working
        8)Manufacturer related
            -add a manufacturer - working
            -get all manufacturers - working
        9)Order statuses related
            -get all order status types - working
     */
    public static void main(String[] args)
    {
        try
        {
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb", "root", "sigurnost");
            MySQL_DAO dao = new MySQL_DAO(connection);

            // (int category_ID, String category_name, Integer parent_category_ID, int number_of_subcategories)
            // String category_name, Integer parent_category_ID
            Product_category category = new Product_category(9, "dijete 1", 912, 0);
            System.out.println(dao.deleteProductCategory(category));
        }
        catch(SQLException e){System.out.println(e);}

    }
}
