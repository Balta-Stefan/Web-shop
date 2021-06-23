package webStore.model;

public class Supplier
{
    public int supplier_ID;
    public String name;
    public String phone;
    public String email;
    public String website;

    public Supplier(int supplier_ID, String name, String phone, String email, String website)
    {
        this.supplier_ID = supplier_ID;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.website = website;
    }

    @Override
    public String toString()
    {
        return "Supplier{" +
                "supplier_ID=" + supplier_ID +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}
