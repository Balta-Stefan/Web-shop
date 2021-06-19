package webStore;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface DAOInterface
{
    // all methods that return lists can return null.When they return null, an SQLException has occurred while servicing the query.

    // product related methods
    boolean addProduct(Product product);
    boolean addProductPictures(Product product, String[] picturePaths);
    boolean addProductCategory(Product_category category);
    boolean deleteProductCategory(Product_category category);
    boolean addCategoryFilters(int filter_ID, String filter);
    boolean addFilterValues(int filter_ID, String filter_value);
    boolean addFilterToProduct(int filter_value_ID, int product_ID);
    List<Product> getAllProducts();
    List<Product> getFilteredProducts(List<Integer> list_value_IDs);

    // warehouse related
    List<Warehouse> getWarehouses();

    // supplier related
    List<Supplier> getSuppliers();

    // inventory related
    List<Inventory> getInventory();
    boolean addToInventory(Product product, int amount, LocalDateTime delivered_at, Warehouse warehouse, BigDecimal suppliers_price, Supplier supplier);

    // orders related
    boolean addOrder(Order order);
    boolean addReturnedReason(Order order);

    // Employees related
    Employee getEmployee(String username); // used for employee login

    // Customer related
    Customer getCustomer(String email);
    boolean customerRegistration(Customer customer);
    boolean addReview(Product_review review);

    // manufacturer related
    boolean addManufacturer(Manufacturer manufacturer);
    List<Manufacturer> getManufacturers();

    // order statuses related
    List<Order_status> getAllOrderStatusTypes();
}
