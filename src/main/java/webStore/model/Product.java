package webStore.model;

import java.math.BigDecimal;
import java.util.List;

public class Product
{
    public int product_ID;
    public String name;
    public int manufacturer_ID;
    public String manufacturer;
    public BigDecimal price;
    public int category_ID;
    public double mass;
    public String description, thumbnail;
    public byte warranty_months;
    public List<String> otherPictureURIs;

    public int quantity; // used when returning customer's cart
    
    public Product() {}
    
    public Product(int product_ID, String name, int manufacturer_ID, BigDecimal price, int category_ID, double mass, String description, String thumbnail, byte warranty_months)
    {
    	// used for displaying products on the front page
    	
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
    
    public Product(int product_ID, String name, int manufacturer_ID, BigDecimal price, int category_ID, double mass, String description, String thumbnail, byte warranty_months, String manufacturer)
    {
    	// used when examining a product
    	
    	this(product_ID, name, manufacturer_ID, price, category_ID, mass, description, thumbnail, warranty_months);
    	this.manufacturer = manufacturer;
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
		return "Product [product_ID=" + product_ID + ", name=" + name + ", manufacturer_ID=" + manufacturer_ID
				+ ", manufacturer=" + manufacturer + ", price=" + price + ", category_ID=" + category_ID + ", mass="
				+ mass + ", description=" + description + ", thumbnail=" + thumbnail + ", warranty_months="
				+ warranty_months + ", otherPictureURIs=" + otherPictureURIs + ", quantity=" + quantity + "]";
	}

	

}
