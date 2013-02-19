package com.exoftware.exactor.command.abbot;

import abbot.finder.AWTHierarchy;
import abbot.script.ComponentReference;

import java.awt.*;

/**
 * Maps to the Abbot xml script element <component>
 */
public class AbbotComponent extends AbstractAbbotCommand {
    public static final String COMPONENT_XML =
            "<component class=\"\"\n" +
                    "              id=\"\"\n" +
                    "              index=\"\"\n" +
                    "              name=\"\"\n" +
                    "              parent=\"\"\n" +
                    "              window=\"\"\n" +
                    "              root=\"\"\n" +
                    "              title=\"\"\n" +
                    "              tag=\"\"\n" +
                    "              icon=\"\"\n" +
                    "              invoker=\"\"\n" +
                    "              borderTitle=\"\"/>";

    public void execute() throws Exception {
        try {
            getAbbotScript().addComponentReference(new ComponentReference(getAbbotScript(), Util.createElement(
                    buildAbbotXml(getTemplateXML()))));
            getScript().getContext().put(SWING_CONTAINER_KEYS, AWTHierarchy.getDefault().getRoots().toArray(
                    new Container[]{}));
        } catch (Throwable throwable) {
            throw new RuntimeException("Abbot Add Component Exception", throwable);
        }
    }

    protected String getTemplateXML() {
        return COMPONENT_XML;
    }

    protected ReplacementDefinition[] getReplacementDefinitions() {
        return new ReplacementDefinition[]{
                new ReplacementDefinition(0, "", "class"),
                new ReplacementDefinition(1, "", "id"),
                new ReplacementDefinition(2, "", "name"),
                new ReplacementDefinition(3, "", "index"),
                new ReplacementDefinition(4, "", "parent"),
                new ReplacementDefinition(5, "", "window"),
                new ReplacementDefinition(6, "", "root"),
                new ReplacementDefinition(7, "", "title"),
                new ReplacementDefinition(8, "", "tag"),
                new ReplacementDefinition(9, "", "icon"),
                new ReplacementDefinition(10, "", "invoker"),
                new ReplacementDefinition(11, "", "borderTitle")
        };
    }
}
