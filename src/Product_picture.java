package webStore;

public class Product_picture
{
    public int picture_ID, product_ID;
    public String picture_URI;

    public Product_picture(int picture_ID, int product_ID, String picture_URI)
    {
        this.picture_ID = picture_ID;
        this.product_ID = product_ID;
        this.picture_URI = picture_URI;
    }
}
