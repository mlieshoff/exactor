package com.exoftware.exactor.command.abbot;

/**
 * Maps to the Abbot xml script element <terminate>
 */
public class AbbotTerminate extends AbstractAbbotCommand {
    public static final String TERMINATE_XML = "<terminate/>";

    protected String getTemplateXML() {
        return TERMINATE_XML;
    }

    protected ReplacementDefinition[] getReplacementDefinitions() {
        return new ReplacementDefinition[]{};
    }
}
