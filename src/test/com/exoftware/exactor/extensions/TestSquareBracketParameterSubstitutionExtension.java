package com.exoftware.exactor.extensions;

import com.exoftware.exactor.Command;
import com.exoftware.exactor.MockCommand;
import com.exoftware.exactor.Parameter;
import com.exoftware.exactor.Script;
import com.exoftware.exactor.command.Composite;
import junit.framework.TestCase;

public class TestSquareBracketParameterSubstitutionExtension extends TestCase
{
    private Script containingScript;
    private Script compositeScript;
    private Composite composite;
    private Command command;

    protected void setUp() throws Exception
    {
        super.setUp();

        containingScript = new Script();
        compositeScript  = new Script( );
        composite        = new Composite( compositeScript );
        command          = new MockCommand();

        composite.addParameter( new Parameter( "Hello" ) );
        composite.addParameter( new Parameter( "World" ) );


        composite.getCompositeScript().addCommand( command );
        containingScript.addCommand( composite );

        containingScript.getExecutionSet().addListener( new SquareBracketParameterSubstitutionExtension() );
    }

    public void testSubstituteParameters()
    {
        command.addParameter( new Parameter( "[0]" ) );
        command.addParameter( new Parameter( "[1]" ) );

        containingScript.execute();

        assertEquals( "Hello", command.getParameter( 0 ).stringValue() );
        assertEquals( "World", command.getParameter( 1 ).stringValue() );
    }

    public void testSubstituteParameters_MultipleTimes()
    {
        command.addParameter( new Parameter( "[0]" ) );
        command.addParameter( new Parameter( "[0]" ) );

        containingScript.execute();

        assertEquals( "Hello", command.getParameter( 0 ).stringValue() );
        assertEquals( "Hello", command.getParameter( 1 ).stringValue() );
    }

    public void testSubstituteParameters_MultipleTimesInOneParameter()
    {
        command.addParameter( new Parameter( "xxx[0]xxx[1]xxx" ) );

        containingScript.execute();

        assertEquals( "xxxHelloxxxWorldxxx", command.getParameter( 0 ).stringValue() );
    }

    public void testSubstituteParameters_MultipleTimesInOneParameterOneNonExistent()
    {
        command.addParameter( new Parameter( "xxx[2]xxx[0]xxx" ) );

        containingScript.execute();

        assertEquals( "xxx[2]xxxHelloxxx", command.getParameter( 0 ).stringValue() );
    }

    public void testSubstituteParameters_MultipleTimesNotInOrder()
    {
        command.addParameter( new Parameter( "[0]" ) );
        command.addParameter( new Parameter( "[1]" ) );
        command.addParameter( new Parameter( "[0]" ) );

        containingScript.execute();

        assertEquals( "Hello", command.getParameter( 0 ).stringValue() );
        assertEquals( "World", command.getParameter( 1 ).stringValue() );
        assertEquals( "Hello", command.getParameter( 2 ).stringValue() );
    }

    public void testSubstituteWithNonExistantReplacement()
    {
        command.addParameter( new Parameter( "[3]" ) );

        containingScript.execute();

        assertEquals( "[3]", command.getParameter( 0 ).stringValue() );
    }

    public void testSubstituteInvailidReplacementMarker()
    {
        command.addParameter( new Parameter( "[junk]" ) );

        containingScript.execute();

        assertEquals( "[junk]", command.getParameter( 0 ).stringValue() );
    }

    public void testSubstituteOutOfSequenceReplacement()
    {
        command.addParameter( new Parameter( "[1]" ) );

        containingScript.execute();

        assertEquals( "World", command.getParameter( 0 ).stringValue() );
    }

    public void testSubstituteEmbeddedParams()
    {
        command.addParameter( new Parameter( "Hello [1] Hello" ) );

        containingScript.execute();

        assertEquals( "Hello World Hello", command.getParameter( 0 ).stringValue() );
    }

    public void testParametersRemoved()
    {
        
    }
}
