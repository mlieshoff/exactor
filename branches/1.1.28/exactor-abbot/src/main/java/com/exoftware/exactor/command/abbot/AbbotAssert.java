package com.exoftware.exactor.command.abbot;

/**
 * Maps to the Abbot xml script element <assert>
 */
public class AbbotAssert extends AbstractAbbotCommand {
    public static final String ASSERT_XML = "<assert args=\"\" class=\"\" method=\"\" value=\"\" component=\"\" invert=\"\"/>";

    public void execute() throws Exception {
        findAndLoadComponent();
        super.execute();
    }

    protected String getTemplateXML() {
        return ASSERT_XML;
    }

    protected ReplacementDefinition[] getReplacementDefinitions() {
        return new ReplacementDefinition[]{
                new ReplacementDefinition(0, "", "class"),
                new ReplacementDefinition(1, "", "method"),
                new ReplacementDefinition(2, "", "args"),
                new ReplacementDefinition(3, "", "value"),
                new ReplacementDefinition(4, "", "component"),
                new ReplacementDefinition(5, "", "invert")
        };
    }
}
