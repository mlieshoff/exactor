package com.exoftware.exactor.command.abbot;

/**
 * Maps to the Abbot xml script element <appletviewer>
 *
 */
public class AbbotAppletViewer extends AbstractAbbotCommand
{
   public static final String APPLET_XML = "  <appletviewer archive=\"\" code=\"\" height=\"\" width=\"\" />";

   protected String getTemplateXML( )
   {
      return APPLET_XML;
   }

   protected ReplacementDefinition[] getReplacementDefinitions( )
   {
      return new ReplacementDefinition[]
            {
               new ReplacementDefinition( 0, "", "archive"),
               new ReplacementDefinition( 1, "", "code"),
               new ReplacementDefinition( 2, "", "height"),
               new ReplacementDefinition( 3, "", "width")
            };
   }
}
