package com.exoftware.exactor.command.abbot;

import abbot.finder.Hierarchy;
import abbot.script.ComponentReference;
import abbot.script.Resolver;
import abbot.script.Step;
import abbot.tester.ComponentTester;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.awt.Component;
import java.io.*;
import java.util.Collection;

/**
 * Utility class containing methods used by the various Exactor Abbot commands.
 */

public class Util
{
    /**
     * Create a JDom element from an XML string.
     *
     * @param xml
     * @return JDom element
     * @throws Exception
     */
    public static Element createElement( String xml ) throws Exception
    {
        StringReader reader = new StringReader( xml );
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build( reader );
        Element el = doc.getRootElement();

        return el;
    }

    /**
     * Read content from file.
     *
     * @param fileName
     * @return file content
     * @throws IOException
     */
    public static String getFileContent( String fileName ) throws IOException
    {
        FileReader fReader = null;
        BufferedReader reader = null;
        StringBuffer result = new StringBuffer();

        try
        {
            fReader = new FileReader( fileName );
            reader = new BufferedReader( fReader );
            String line = reader.readLine();
            while ( line != null )
            {
                result.append( line + "\n" );
                line = reader.readLine();
            }
        }
        finally
        {
            if ( reader != null )
                reader.close();
            if ( fReader != null )
                reader.close();
        }

        return result.toString();
    }

    static public String resolveTester( String className ) throws ClassNotFoundException
    {
        MyStep step = createStep();

        return step.resolveTester( className ).getClass().getName();
    }

    private static MyStep createStep()
    {
        return new MyStep();
    }

    private static class MyStep extends Step
    {
        public MyStep()
        {
            super( new Resolver()
            {
                public ComponentReference getComponentReference( Component comp )
                {
                    return null;
                }

                public ComponentReference addComponent( Component comp )
                {
                    return null;
                }

                public void addComponentReference( ComponentReference ref )
                {

                }

                public Collection getComponentReferences()
                {
                    return null;
                }

                public ComponentReference getComponentReference( String refid )
                {
                    return null;
                }

                public Hierarchy getHierarchy()
                {
                    return null;
                }

                public ClassLoader getContextClassLoader()
                {
                    return null;
                }

                public File getDirectory()
                {
                    return null;
                }

                public void setProperty( String name, Object value )
                {

                }

                public Object getProperty( String name )
                {
                    return null;
                }

                public String getContext( Step step )
                {
                    return null;
                }
            }, "" );
        }

        protected ComponentTester resolveTester( String className ) throws ClassNotFoundException
        {
            return super.resolveTester( className );
        }

        protected void runStep() throws Throwable
        {

        }

        public String getXMLTag()
        {
            return null;
        }

        public String getUsage()
        {
            return null;
        }

        public String getDefaultDescription()
        {
            return null;
        }
    }
}
