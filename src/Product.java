import java.math.BigDecimal;

public class Product
{
    public int product_ID;
    public String name;
    public int manufacturer_ID;
    public BigDecimal price;
    public int category_ID;
    public double mass;
    public String description, thumbnail;
    public byte warranty_months;

    public Product(int product_ID, String name, int manufacturer_ID, BigDecimal price, int category_ID, double mass, String description, String thumbnail, byte warranty_months)
    {
        this.product_ID = product_ID;
        this.name = name;
        this.manufacturer_ID = manufacturer_ID;
        this.price = price;
        this.category_ID = category_ID;
        this.mass = mass;
        this.description = description;
        this.thumbnail = thumbnail;
        this.warranty_months = warranty_months;
    }

    public Product(String name, int manufacturer_ID, BigDecimal price, int category_ID, double mass, String description, String thumbnail, byte warranty_months)
    {
        this.name = name;
        this.manufacturer_ID = manufacturer_ID;
        this.price = price;
        this.category_ID = category_ID;
        this.mass = mass;
        this.description = description;
        this.thumbnail = thumbnail;
        this.warranty_months = warranty_months;
    }

    @Override
    public String toString()
    {
        return "Product{" +
                "product_ID=" + product_ID +
                ", name='" + name + '\'' +
                '}';
    }
}
