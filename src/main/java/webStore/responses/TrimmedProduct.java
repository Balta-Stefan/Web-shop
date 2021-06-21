package webStore.responses;

public class TrimmedProduct
{
	public int ID;
	public String name;
	public String thumbnail;
	public String price;
	
	public TrimmedProduct() {}
	
	public TrimmedProduct(int iD, String name, String thumbnail, String price)
	{
		super();
		ID = iD;
		this.name = name;
		this.thumbnail = thumbnail;
		this.price = price;
	}
}
