package com.exoftware.exactor.command.abbot;

/**
 * Maps to the Abbot xml script element <action>
 */

public class AbbotAction extends AbstractAbbotCommand {
    public static final String ACTION_XML = "<action class=\"\" method=\"\" args=\"\" />";

    public void execute() throws Exception {
        findAndLoadComponent();
        super.execute();
    }

    protected String getTemplateXML() {
        return ACTION_XML;
    }

    protected ReplacementDefinition[] getReplacementDefinitions() {
        return new ReplacementDefinition[]{
                new ReplacementDefinition(0, "", "class"),
                new ReplacementDefinition(1, "", "method"),
                new ReplacementDefinition(2, "", "args")
        };
    }
}
