package com.exoftware.exactor.command.abbot;

/**
 * Maps to the Abbot xml script element <launch>
 *
 */

public class AbbotLaunch extends AbstractAbbotCommand
{
   public static final String LAUNCH_XML = "<launch args=\"\" class=\"\" classpath=\"\" method=\"\" />";

   protected String getTemplateXML( )
   {
      return LAUNCH_XML;
   }

   protected ReplacementDefinition[] getReplacementDefinitions( )
   {
      return new ReplacementDefinition[]
            {
               new ReplacementDefinition( 0, "",     "class"),
               new ReplacementDefinition( 1, "main", "method"),
               new ReplacementDefinition( 2, "[]",   "args"),
               new ReplacementDefinition( 3, ".",    "classpath")
            };
   }
}
