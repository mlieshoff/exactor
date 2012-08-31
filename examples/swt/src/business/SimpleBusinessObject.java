package business;

public class SimpleBusinessObject
{
    private int num1;
    private int num2;

    public SimpleBusinessObject( int num1, int num2 )
    {
        this.num1 = num1;
        this.num2 = num2;
    }

    public int addition()
    {
        return num1 + num2;
    }
}
