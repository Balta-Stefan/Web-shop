package webStore.model;

public class Customer
{
    public int customer_ID;

    public String name, last_name, email, password, phone, address, city, state, zip, shopping_cart;

    public Customer() {}
    
    public Customer(int customer_ID, String name, String last_name, String email, String password, String phone, String shipping_address, String city, String state, String ZIP_code, String shopping_cart)
    {
        this.customer_ID = customer_ID;
        this.name = name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = shipping_address;
        this.city = city;
        this.state = state;
        this.zip = ZIP_code;
        this.shopping_cart = shopping_cart;
    }

    public Customer(String name, String last_name, String email, String password, String phone, String shipping_address, String city, String state, String ZIP_code, String shopping_cart)
    {
        this.name = name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = shipping_address;
        this.city = city;
        this.state = state;
        this.zip = ZIP_code;
        this.shopping_cart = shopping_cart;
    }

    @Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + customer_ID;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((last_name == null) ? 0 : last_name.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((shopping_cart == null) ? 0 : shopping_cart.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((zip == null) ? 0 : zip.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		if (address == null)
		{
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (city == null)
		{
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (customer_ID != other.customer_ID)
			return false;
		if (email == null)
		{
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (last_name == null)
		{
			if (other.last_name != null)
				return false;
		} else if (!last_name.equals(other.last_name))
			return false;
		if (name == null)
		{
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null)
		{
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (phone == null)
		{
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (shopping_cart == null)
		{
			if (other.shopping_cart != null)
				return false;
		} else if (!shopping_cart.equals(other.shopping_cart))
			return false;
		if (state == null)
		{
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (zip == null)
		{
			if (other.zip != null)
				return false;
		} else if (!zip.equals(other.zip))
			return false;
		return true;
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
                ", shipping_address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", ZIP_code='" + zip + '\'' +
                '}';
    }
}
