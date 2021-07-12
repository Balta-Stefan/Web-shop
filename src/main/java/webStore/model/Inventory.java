package webStore.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.sql.Date;

public class Inventory
{
    public int inventory_ID, amount;
    public BigDecimal price;
    public LocalDateTime delivered_at;
    public int available_amount, stored_at;
    public BigDecimal suppliers_price;
    public int product_ID, supplier_ID;
    public Date expiration_date;


    public Inventory() {}

    public Inventory(int inventory_ID, int amount, BigDecimal price, LocalDateTime delivered_at, int available_amount,
			int stored_at, BigDecimal suppliers_price, int product_ID, int supplier_ID, Date expiration_date)
	{
		this.inventory_ID = inventory_ID;
		this.amount = amount;
		this.price = price;
		this.delivered_at = delivered_at;
		this.available_amount = available_amount;
		this.stored_at = stored_at;
		this.suppliers_price = suppliers_price;
		this.product_ID = product_ID;
		this.supplier_ID = supplier_ID;
		this.expiration_date = expiration_date;
	}



	@Override
	public String toString()
	{
		return "Inventory [inventory_ID=" + inventory_ID + ", amount=" + amount + ", price=" + price + ", delivered_at="
				+ delivered_at + ", available_amount=" + available_amount + ", stored_at=" + stored_at
				+ ", suppliers_price=" + suppliers_price + ", product_ID=" + product_ID + ", supplier_ID=" + supplier_ID
				+ ", expiration_date=" + expiration_date + "]";
	}
}
