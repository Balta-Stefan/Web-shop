package webStore.model;

public class Manufacturer
{
    public int manufacturer_ID;
    public String name;

    public Manufacturer(int manufacturer_ID, String name)
    {
        this.manufacturer_ID = manufacturer_ID;
        this.name = name;
    }

    public Manufacturer(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "Manufacturer{" +
                "manufacturer_ID=" + manufacturer_ID +
                ", name='" + name + '\'' +
                '}';
    }
}
