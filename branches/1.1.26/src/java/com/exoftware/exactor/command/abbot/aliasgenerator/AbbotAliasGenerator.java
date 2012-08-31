package com.exoftware.exactor.command.abbot.aliasgenerator;

import com.exoftware.exactor.command.abbot.Util;

import java.io.*;
import java.util.*;
import java.lang.reflect.Method;

/**
 * Experimental.
 *
 */

//todo add to build
public class AbbotAliasGenerator
{
   public static final String ALIAS_TEMPLATE = "Alias         COMMAND        \"AbbotAction \\\"SWING_COMPONENT\\\" \\\"COMPONENT_ACTION\\\" \\\"[0]\\\"\"";
   public static final String ALIAS_PROP_TEMPLATE  = "COMMAND=AbbotAction \\\"SWING_COMPONENT\\\" \\\"COMPONENT_ACTION\\\" \\\"[0]\\\"";
   public static final String METHOD_PREFIX = "action";

   public static Properties loadTesters( ) throws IOException
   {
      Properties props = new Properties( );

      props.load( AbbotAliasGenerator.class.getResourceAsStream( "components.properties" ) );

      return props;
   }

   public static void main( String []args )
   {
      try
      {
         generateAliases( );
      }
      catch ( Throwable e )
      {
         e.printStackTrace( );
         System.exit( 1 );
      }

      System.exit( 0 );
   }

   private static void generateAliases( ) throws IOException, ClassNotFoundException
   {
      Properties     testers           = loadTesters( );
      HashSet        aliasesComposite  = new HashSet( );
      HashSet        aliasesProperties = new HashSet( );

      for ( Iterator iterator = testers.keySet( ).iterator( ); iterator.hasNext( ); )
      {
         String component = ( String ) iterator.next( );
         processClass( component,
                       Util.resolveTester( component ),
                       aliasesComposite,
                       aliasesProperties );
      }
   }

   private static void processClass( String componentClazzName,
                                     String testerClazzName,
                                     HashSet aliasesComposite,
                                     HashSet aliasesEile )
         throws ClassNotFoundException, IOException
   {
      Class       testerClazz   = Class.forName( testerClazzName );
      ArrayList   actionMethods = extractActionMethods( testerClazz );

      for ( Iterator iterator = actionMethods.iterator( ); iterator.hasNext( ); )
      {
         String action = ( String ) iterator.next( );
         String alias = buildAlias( extractSwingComponent( componentClazzName ),
                                    testerClazzName,
                                    action );
         aliasesComposite.add( alias );
         aliasesEile.add( buildAliasProperty( extractSwingComponent( componentClazzName ),
                                    action ) );
      }

      writeAliases( sortAliases( aliasesComposite ), "LoadAbbotAliases.cmp" );
      writeAliases( sortAliases( aliasesEile ), "aliases.properties" );
   }

   private static String buildAliasProperty( String component, String action )
   {
      return component + "." + action.substring( METHOD_PREFIX.length( ) );
   }

   private static void writeAliases( ArrayList sorted, String fileName )
         throws IOException
   {
      FileWriter fileWriter = new FileWriter( fileName );

      for ( Iterator iterator1 = sorted.iterator( ); iterator1.hasNext( ); )
      {
         String s = ( String ) iterator1.next( );
         fileWriter.write( s + "\n" );
      }

      fileWriter.close( );
   }

   private static ArrayList sortAliases( HashSet aliases )
   {
      ArrayList sorted = new ArrayList( aliases );
      Collections.sort( sorted );
      return sorted;
   }

   private static String buildAlias( String component, String testerName, String action )
   {
      String alias = ALIAS_TEMPLATE;

      alias = alias.replaceAll( "COMMAND", component + "." + action.substring( METHOD_PREFIX.length( ) ) );
      alias = alias.replaceAll( "SWING_COMPONENT", testerName );
      alias = alias.replaceAll( "COMPONENT_ACTION", action );
      return alias;
   }

   private static ArrayList extractActionMethods( Class clazz )
   {
      ArrayList   actionMethods = new ArrayList( );
      Method[]    methods = clazz.getMethods( );

      for ( int i = 0; i < methods.length; i++ )
      {
         Method method = methods[i];

         if ( method.getName( ).startsWith( METHOD_PREFIX ) )
            actionMethods.add( method.getName( ) );
      }

      return actionMethods;
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
}
