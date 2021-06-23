package webStore.model;

public class Category_filter
{
    public int filter_ID, category_ID;
    public String filter;

    public Category_filter(int filter_ID, int category_ID, String filter)
    {
        this.filter_ID = filter_ID;
        this.category_ID = category_ID;
        this.filter = filter;
    }

    public Category_filter(String filter)
    {
        this.filter = filter;
    }
}
