public class Product_review
{
    public int review_ID, product_ID, customer_ID;
    public byte grade;
    public String comment;

    public Product_review(int review_ID, byte grade, String comment, int product_ID, int customer_ID)
    {
        this.review_ID = review_ID;
        this.product_ID = product_ID;
        this.customer_ID = customer_ID;
        this.grade = grade;
        this.comment = comment;
    }
    public Product_review(byte grade, String comment, int product_ID, int customer_ID)
    {
        this.product_ID = product_ID;
        this.customer_ID = customer_ID;
        this.grade = grade;
        this.comment = comment;
    }
}
