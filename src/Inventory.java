import java.math.BigDecimal;
import java.time.LocalDate;

public class Inventory
{
    public int inventory_ID, amount;
    public BigDecimal price;
    public LocalDate delivered_at;
    public int available_amount, stored_at;
    public BigDecimal suppliers_price;
    public int product_ID, supplier_ID;

    public Inventory(int inventory_ID, int amount, BigDecimal price, LocalDate delivered_at, int available_amount, int stored_at, BigDecimal suppliers_price, int product_ID, int supplier_ID)
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
    }
}
