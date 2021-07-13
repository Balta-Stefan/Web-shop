package webStore.responses;

public class BuyProduct
{
	public int productID;
	public int quantity;
	
	public BuyProduct() {}

	public BuyProduct(int ID, int quantity)
	{
		this.quantity = quantity;
		this.productID = ID;
	}
	
	
}
