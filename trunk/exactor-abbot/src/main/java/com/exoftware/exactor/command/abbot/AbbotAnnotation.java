package com.exoftware.exactor.command.abbot;

/**
 * Maps to the Abbot xml script element <annotation>
 */
public class AbbotAnnotation extends AbstractAbbotCommand {
    public static final String ANNOTATION_XML = "<annotation component=\"\" userDismiss=\"\" x=\"\" y=\"\" height=\"\" width=\"\" desc=\"\">XXX</annotation>";

    protected String getTemplateXML() {
        return ANNOTATION_XML;
    }

    protected String buildAbbotXml(String actionXML) throws Exception {
        String xml = super.buildAbbotXml(actionXML);
        return xml.replaceAll("XXX", getParameter(7, "").stringValue());
    }

    protected ReplacementDefinition[] getReplacementDefinitions() {
        return new ReplacementDefinition[] {
                new ReplacementDefinition(0, "", "component"),
                new ReplacementDefinition(1, "", "userDismiss"),
                new ReplacementDefinition(2, "", "x"),
                new ReplacementDefinition(3, "", "y"),
                new ReplacementDefinition(4, "", "height"),
                new ReplacementDefinition(5, "", "width"),
                new ReplacementDefinition(6, "", "desc")
        };
    }
}
