package webStore.model;

public class Employee
{
    public int employee_ID;
    public String name, last_name, phone, address, city, state, ZIP, username, password;

    public Employee() {}
    
    public Employee(int employee_ID, String name, String last_name, String phone, String address, String city, String state, String ZIP, String username, String password)
    {
        this.employee_ID = employee_ID;
        this.name = name;
        this.last_name = last_name;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.ZIP = ZIP;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString()
    {
        return "Employee{" +
                "employee_ID=" + employee_ID +
                ", name='" + name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", ZIP='" + ZIP + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
