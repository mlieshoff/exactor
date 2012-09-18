package business;

import junit.framework.TestCase;

public class SimpleBusinessObjectTest extends TestCase
{
    public void testSimpleAddition()
    {
        assertBusinessObject( 5, 2, 3 );
        assertBusinessObject( 7, 3, 4 );
    }

    private void assertBusinessObject( int expected, int num1, int num2 )
    {
        SimpleBusinessObject simpleBusinessObject = new SimpleBusinessObject( num1, num2 );
        assertEquals( expected, simpleBusinessObject.addition() );
    }
}
