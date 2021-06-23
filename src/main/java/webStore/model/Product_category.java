package webStore.model;

public class Product_category
{
    public int category_ID;
    public String category_name;
    public Integer parent_category_ID;
    public int number_of_subcategories;

    public Product_category(int category_ID, String category_name, Integer parent_category_ID, int number_of_subcategories)
    {
        this.category_ID = category_ID;
        this.category_name = category_name;
        this.parent_category_ID = parent_category_ID;
        this.number_of_subcategories = number_of_subcategories;
    }

    public Product_category(String category_name, Integer parent_category_ID)
    {
        this.category_name = category_name;
        this.parent_category_ID = parent_category_ID;
    }
}
