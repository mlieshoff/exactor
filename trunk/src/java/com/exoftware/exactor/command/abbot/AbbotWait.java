package com.exoftware.exactor.command.abbot;

/**
 * Maps to the Abbot xml script element <wait>
 *
 */

public class AbbotWait extends AbstractAbbotCommand
{
   public static final String WAIT_XML = "<wait args=\"\" class=\"\" invert=\"\" method=\"\" component=\"\" timeout=\"\" value=\"\" />";

   protected String getTemplateXML( )
   {
      return WAIT_XML;
   }

   protected ReplacementDefinition[] getReplacementDefinitions( )
   {
      return new ReplacementDefinition[]
            {
               new ReplacementDefinition( 0, "",     "class"),
               new ReplacementDefinition( 1, "",     "method"),
               new ReplacementDefinition( 2, "",     "args"),
               new ReplacementDefinition( 3, "",     "invert"),
               new ReplacementDefinition( 4, "",     "component"),
               new ReplacementDefinition( 5, "",     "timeout"),
               new ReplacementDefinition( 6, "",     "value")
            };
   }
}
