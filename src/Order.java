package webStore;

import java.time.LocalDate;

public class Order
{
    public int order_ID, inventory_ID, amount;
    public LocalDate order_received_at;
    public LocalDate order_delivered_at;
    public String returned_reason;
    public int status_ID, handled_by, ordered_by;

    public Order(int order_ID, int inventory_ID, int amount, LocalDate order_received_at, LocalDate order_delivered_at, String returned_reason, int status_ID, int handled_by, int ordered_by)
    {
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
}
