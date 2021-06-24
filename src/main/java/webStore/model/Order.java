package webStore.model;

import java.time.LocalDateTime;

public class Order
{
    public int order_ID, inventory_ID, amount;
    public LocalDateTime order_received_at;
    public LocalDateTime order_delivered_at;
    public String returned_reason;
    public int status_ID, ordered_by;
    public Integer handled_by;
    
    public Order(int order_ID, int inventory_ID, int amount, LocalDateTime order_received_at, LocalDateTime order_delivered_at,
			String returned_reason, int status_ID, int handled_by, int ordered_by)
	{
		// used when retrieving data from the database
    	
		this.order_ID = order_ID;
		this.inventory_ID = inventory_ID;
		this.amount = amount;
		this.order_received_at = order_received_at;
		this.order_delivered_at = order_delivered_at;
		this.returned_reason = returned_reason;
		this.status_ID = status_ID;
		this.handled_by = handled_by;
		this.ordered_by = ordered_by;
	}


    public Order(int inventory_ID, int amount, LocalDateTime order_received_at, int status_ID, int ordered_by)
    {
    	// used when creating new orders
    	
        this.inventory_ID = inventory_ID;
        this.amount = amount;
        this.order_received_at = order_received_at;
        this.status_ID = status_ID;
        this.ordered_by = ordered_by;
    }


	@Override
	public String toString()
	{
		return "Order [order_ID=" + order_ID + ", inventory_ID=" + inventory_ID + ", amount=" + amount
				+ ", order_received_at=" + order_received_at + ", order_delivered_at=" + order_delivered_at
				+ ", returned_reason=" + returned_reason + ", status_ID=" + status_ID + ", handled_by=" + handled_by
				+ ", ordered_by=" + ordered_by + "]";
	}
}
