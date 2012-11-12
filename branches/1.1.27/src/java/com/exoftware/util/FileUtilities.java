package com.exoftware.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtilities
{
    public static String[] linesFromFile( File file )
    {
        List result = new ArrayList();
        try
        {
            FileReader fileReader = new FileReader( file );
            BufferedReader reader = new BufferedReader( fileReader );
            String line = reader.readLine();
            while( line != null )
            {
                result.add( line );
                line = reader.readLine();
            }
            reader.close();
            fileReader.close();
        }
        catch( FileNotFoundException e )
        {
            throw new RuntimeException( "Unable to find file, " + file.getAbsolutePath() );
        }
        catch( IOException e )
        {
            throw new RuntimeException( "An error occurred reading file, " + file.getAbsolutePath(), e );
        }

        return (String[]) result.toArray( new String[0] );
    }


    public static String getFileContent( String fileName ) throws IOException
    {
        FileReader fReader = null;
        BufferedReader reader = null;
        StringBuffer result = new StringBuffer();

        try
        {
            fReader = new FileReader( fileName );
            reader = new BufferedReader( fReader );
            while( reader.ready() )
            {
                result.append( (char) reader.read() );
            }
        }
        finally
        {
            if( reader != null )
                reader.close();
            if( fReader != null )
                fReader.close();
        }

        return result.toString();
    }

    public static String mergeDynamicLines( String baseContents, String mergeContent, int[] replacements )
    {
        String[] baseLines = baseContents.split( "\n" );
        String[] mergeLines = mergeContent.split( "\n" );

        doReplacements( baseLines, mergeLines, replacements );

        return rebuildContent( baseLines );
    }

    private static String rebuildContent( String[] lines )
    {
        String result = "";

        for( int i = 0; i < lines.length; i++ )
        {
            result += lines[i] + "\n";
        }

        return result;
    }

    private static void doReplacements( String[] baselines, String[] mergeLines, int[] replacements )
    {
        for( int i = 0; i < replacements.length; i++ )
        {
            int replacement = replacements[i];

            baselines[replacement] = mergeLines[replacement];
        }
    }

    public static void writeToFile( String fileName,
                                    String contents,
                                    boolean append )
            throws Exception
    {
        File file = new File( fileName );

        if( !file.exists() )
        {
            if( !file.getParentFile().exists() )
            {
                file.getParentFile().mkdirs();
            }
        }
        FileWriter fileWriter = null;

        try
        {
            fileWriter = new FileWriter( file, append );
            fileWriter.write( contents );
            fileWriter.flush();
        }
        finally
        {
            if( fileWriter != null )
                fileWriter.close();
        }
    }

    public static String resolveLocation( String location, String currentDirectory )
    {
        if( location.startsWith( "." ) )
        {
            if( !currentDirectory.endsWith( File.separator ) )
                currentDirectory += File.separator;

            return currentDirectory + location;
        }
        else
        {
            return location;
        }
    }
}
