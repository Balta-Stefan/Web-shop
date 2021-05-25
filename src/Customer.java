public class Customer
{
    public int customer_ID;

    public String name, last_name, email, password, phone, shipping_address, city, state, ZIP_code;

    public Customer(int customer_ID, String name, String last_name, String email, String password, String phone, String shipping_address, String city, String state, String ZIP_code)
    {
        this.customer_ID = customer_ID;
        this.name = name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.shipping_address = shipping_address;
        this.city = city;
        this.state = state;
        this.ZIP_code = ZIP_code;
    }

    public Customer(String name, String last_name, String email, String password, String phone, String shipping_address, String city, String state, String ZIP_code)
    {
        this.name = name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.shipping_address = shipping_address;
        this.city = city;
        this.state = state;
        this.ZIP_code = ZIP_code;
    }

    @Override
    public String toString()
    {
        return "Customer{" +
                "customer_ID=" + customer_ID +
                ", name='" + name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", shipping_address='" + shipping_address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", ZIP_code='" + ZIP_code + '\'' +
                '}';
    }
}
