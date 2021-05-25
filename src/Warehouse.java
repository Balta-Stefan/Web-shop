public class Warehouse
{
    public int warehouse_ID, capacity, free_capacity;
    public String address, country, state;

    public Warehouse(int warehouse_ID, int capacity, int free_capacity, String address, String country, String state)
    {
        this.warehouse_ID = warehouse_ID;
        this.capacity = capacity;
        this.free_capacity = free_capacity;
        this.address = address;
        this.country = country;
        this.state = state;
    }

    @Override
    public String toString()
    {
        return "Warehouse{" +
                "warehouse_ID=" + warehouse_ID +
                ", capacity=" + capacity +
                ", free_capacity=" + free_capacity +
                ", address='" + address + '\'' +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
