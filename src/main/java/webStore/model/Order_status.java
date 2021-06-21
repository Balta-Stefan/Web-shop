package webStore.model;

public class Order_status
{
    public int status_ID;
    public String status_type;

    public Order_status(int status_ID, String status_type)
    {
        this.status_ID = status_ID;
        this.status_type = status_type;
    }

    @Override
    public String toString()
    {
        return "Order_status{" +
                "status_ID=" + status_ID +
                ", status_type='" + status_type + '\'' +
                '}';
    }
}
