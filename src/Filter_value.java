public class Filter_value
{
    public int filter_value_ID, filter_ID, total_products;
    public String value;

    public Filter_value(int filter_value_ID, int filter_ID, int total_products, String value)
    {
        this.filter_value_ID = filter_value_ID;
        this.filter_ID = filter_ID;
        this.total_products = total_products;
        this.value = value;
    }

    public Filter_value(String value)
    {
        this.value = value;
    }
}
