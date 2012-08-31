package com.exoftware.exactor.command.abbot.converter;

import com.exoftware.util.FileCollector;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Utility for converting Abbot xml scripts into Exactor scripts.
 *
 */
public class AbbotToExactorConverter
{
   public static void main( String[] args )
   {
      boolean guessAlias = false;

      if ( !(args.length > 0) )
      {
         System.err.println( "Usage: AbbotToExactorConverter.main() [file or directory to convert] [guessAlias]" );
         System.exit( 1 );
      }

      if ( (args.length == 2) )
      {
         if(args[1].equals( "guessAlias"))
         {
            guessAlias = true;
         }
         else
         {
            System.err.println( "Usage: AbbotToExactorConverter.main() [file or directory to convert] [guessAlias]" );
            System.exit( 1 );
         }
      }

      File[] files = getAbbotFiles( new File( args[0] ) );

      for ( int i = 0; i < files.length; i++ )
      {
         try
         {
            processFile( files[i], guessAlias );
         }
         catch ( Exception e )
         {
            e.printStackTrace( );
            System.exit( 1 );
         }
      }

      System.exit( 0 );
   }

   private static void processFile( File file, boolean guessAlias )
         throws Exception
   {
      String      exactorFileName = extractFileParentDir( file ) + File.separator + extractFileMinusExtension( file ) + ".act";
      System.out.println( "Processing Abbot script " + file.getAbsolutePath( ) + " into Exactor script " + exactorFileName + " ..." );
      String      convertedFile = convertToExactor( file, guessAlias );
      FileWriter  fileWriter = new FileWriter( new File( exactorFileName ), false );

      fileWriter.write( convertedFile );

      fileWriter.close( );
      System.out.println( "Processed Abbot script " + file.getAbsolutePath( ) + " into Exactor script " + exactorFileName );
   }

   private static String extractFileParentDir( File file )
   {
      String parent = file.getParent( );
      if ( parent == null )
      {
         String path = file.getAbsolutePath( );
         parent = path.substring( 0, path.lastIndexOf( File.separator ) );
      }
      return parent;
   }

   private static String extractFileMinusExtension( File file )
   {
      String name = file.getName( );

      return name.substring( 0, name.lastIndexOf( "." ) );
   }


   private static File[] getAbbotFiles( File file )
   {
      if ( file.isFile( ) )
         return new File[]{file};

      return FileCollector.filesWithExtension( file, ".xml" );
   }

   static String convertToExactor( File file, boolean guessAlias ) throws Exception
   {
      String result = "";

      if(guessAlias)
         result += "LoadAbbotAliases\n";

      Document d = new SAXBuilder( ).build( file );

      Element rootElement = d.getRootElement( );

      List children = rootElement.getChildren( );
      for ( Iterator iterator = children.iterator( ); iterator.hasNext( ); )
      {
         Element element = ( Element ) iterator.next( );

         result += "\n";

         result += processElement( element, guessAlias );
      }

      return result;
   }

   static String processElement( Element element, boolean guessAlias ) throws Exception
   {
      String result = "";

      if ( element.getName( ).equals( "action" ) )
      {
         if ( guessAlias && !(checkForAnAlias( element ).equals( "" )) )
         {
            String alias  = checkForAnAlias( element );

            String value = element.getAttribute( "args" ).getValue( );
            value = value.replaceAll( "\"", "\\\\\"" );

            result += alias +
                         " \"" + value + "\"";
         }
         else
         {
            result += AbbotToExactorConverter.convertAbbotXmlToExactorScript( element,
                                                                              "AbbotAction",
                                                                              new String[]{
                                                                                 "class",
                                                                                 "method",
                                                                                 "args"} );
         }
      }
      else if ( element.getName( ).equals( "assert" ) )
      {
         result += AbbotToExactorConverter.convertAbbotXmlToExactorScript( element,
                                                                           "AbbotAssert",
                                                                           new String[]{
                                                                              "class",
                                                                              "method",
                                                                              "args",
                                                                              "value",
                                                                              "component",
                                                                              "invert"} );
      }
      else if ( element.getName( ).equals( "wait" ) )
      {
         result += AbbotToExactorConverter.convertAbbotXmlToExactorScript( element,
                                                                           "AbbotWait",
                                                                           new String[]{
                                                                              "class",
                                                                              "method",
                                                                              "args",
                                                                              "invert",
                                                                              "component",
                                                                              "timeout",
                                                                              "value"} );
      }
      else if ( element.getName( ).equals( "launch" ) )
      {
         element.setAttribute( "classpath",
                               element.getAttribute( "classpath" ).getValue( ).replace( '\\', '/' ) );

         result += AbbotToExactorConverter.convertAbbotXmlToExactorScript( element,
                                                                           "AbbotLaunch",
                                                                           new String[]{
                                                                              "class",
                                                                              "method",
                                                                              "args",
                                                                              "classpath"} );
      }
      else if ( element.getName( ).equals( "component" ) )
      {
         result += AbbotToExactorConverter.convertAbbotXmlToExactorScript( element,
                                                                           "AbbotComponent",
                                                                           new String[]{
                                                                              "class",
                                                                              "id",
                                                                              "name",
                                                                              "index",
                                                                              "parent",
                                                                              "window",
                                                                              "root",
                                                                              "title",
                                                                              "tag",
                                                                              "icon",
                                                                              "invoker",
                                                                              "borderTitle"} );
      }
      else if ( element.getName( ).equals( "terminate" ) )
      {
         result += AbbotToExactorConverter.convertAbbotXmlToExactorScript( element,
                                                                           "AbbotTerminate",
                                                                           new String[]{} );
      }
      else if ( element.getName( ).equals( "appletviewer" ) )
      {
         result += AbbotToExactorConverter.convertAbbotXmlToExactorScript( element,
                                                                           "AbbotAppletViewer",
                                                                           new String[]{
                                                                              "archive",
                                                                              "code",
                                                                              "height",
                                                                              "width"} );
      }
      else if ( element.getName( ).equals( "annotation" ) )
      {
         String converted = AbbotToExactorConverter.convertAbbotXmlToExactorScript( element,
                                                                                    "AbbotAnnotation",
                                                                                    new String[]{
                                                                                       "component",
                                                                                       "userDismiss",
                                                                                       "x",
                                                                                       "y",
                                                                                       "height",
                                                                                       "width",
                                                                                       "desc"} );
         String text = element.getText( );

         text = text.replaceAll( "\n", "" );

         converted += ( " \"" + text + "\"" );

         result += converted;
      }
      else if ( element.getName( ).equals( "sequence" ) )
      {
         List children = element.getChildren( );
         for ( Iterator iterator = children.iterator( ); iterator.hasNext( ); )
         {
            result += "\n";

            result += processElement( ( Element ) iterator.next( ), guessAlias );
         }
      }
      else if ( element.getName( ).equals( "event" ) )
      {
         result += AbbotToExactorConverter.convertAbbotXmlToExactorScript( element,
                                                                           "AbbotEvent",
                                                                           new String[]{
                                                                              "component",
                                                                              "kind",
                                                                              "type",
                                                                              "x",
                                                                              "y",
                                                                              "keyCode",
                                                                              "modifiers"} );
      }
      else
      {
         System.out.println( "Can't convert: " + element.getName( ) );
      }

      return result;
   }

   private static String checkForAnAlias( Element element ) throws IOException, ClassNotFoundException
   {
      InputStream       resourceAsStream = AbbotToExactorConverter.class.getResourceAsStream( "aliases.properties" );
      Properties        properties = new Properties( );

      properties.load( resourceAsStream );

      if ( element.getAttribute( "class" ) != null )
      {
         String className  = element.getAttribute( "class" ).getValue( );
         String methodName = element.getAttribute( "method" ).getValue( );
         String potentialAlias = createPotentialAlias(className, methodName);

         if(properties.getProperty( potentialAlias ) != null)
            return  potentialAlias;
      }

      return "";
   }

   private static String createPotentialAlias( String className, String methodName )
   {
      String clazzName = extractSwingComponent( className );
      String action = methodName.substring( "action".length() );

      return clazzName + "." + action;
   }
   private static String extractSwingComponent( String fullyQualifiedClazzName )
   {
      String[] strings = fullyQualifiedClazzName.split( "[.]" );
      String   clazzName = strings[strings.length - 1];

      if ( clazzName.endsWith( "Tester" ) )
         return clazzName.substring( 0, clazzName.length( ) - 6 );
      else
         return clazzName;
   }

   static String convertAbbotXmlToExactorScript( Element element, String scriptAction, String[] attribs )
   {
      String result = scriptAction + " ";

      for ( int i = 0; i < attribs.length; i++ )
      {
         String attrib = attribs[i];

         result += writeAttribute( element, attrib );
      }

      return result.trim( );
   }

   private static String writeAttribute( Element element, String attributeName )
   {
      String result = "";
      Attribute attribute = element.getAttribute( attributeName );
      if ( attribute != null )
      {
         String value = attribute.getValue( );

         value = value.replaceAll( "\"", "\\\\\"" );

         result += "\"" +
               value +
               "\" ";
      }
      else
      {
         result += "\"\" ";
      }

      return result;
   }


}
