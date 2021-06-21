package webStore.DAO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import webStore.model.Customer;
import webStore.model.Employee;
import webStore.model.Inventory;
import webStore.model.Manufacturer;
import webStore.model.Order;
import webStore.model.Order_status;
import webStore.model.Product;
import webStore.model.Product_category;
import webStore.model.Product_review;
import webStore.model.Supplier;
import webStore.model.Warehouse;
import webStore.responses.FilterValuesOfCategory;
import webStore.responses.ID_string_pair;

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
    List<ID_string_pair> getMainCategories();
    List<ID_string_pair> get_subcategories(int parentID);
    FilterValuesOfCategory get_filters(int categoryID);
    Product_category getCategory(int categoryID);
    Product getProduct(int productID);

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
    Manufacturer getManufacturer(int manufacturerID);

    // order statuses related
    List<Order_status> getAllOrderStatusTypes();
}
