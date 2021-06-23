package webStore.responses;

public class BuyProduct
{
	public int ID;
	public int quantity;
	public String customer_email;
	
	public BuyProduct() {}

	public BuyProduct(int ID, int quantity, String customer_email)
	{
		this.quantity = quantity;
		this.ID = ID;
		this.customer_email = customer_email;
	}
	
	
}
