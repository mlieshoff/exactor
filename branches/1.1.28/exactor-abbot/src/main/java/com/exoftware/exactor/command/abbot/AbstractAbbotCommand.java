package com.exoftware.exactor.command.abbot;

import abbot.finder.AWTHierarchy;
import abbot.finder.TestHierarchy;
import abbot.script.ComponentReference;
import abbot.script.InvalidScriptException;
import abbot.script.Script;
import abbot.script.Step;
import abbot.script.StepRunner;
import com.exoftware.exactor.command.swing.AbstractSwingCommand;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import java.awt.*;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;

/**
 * Abstract class for all of the Exactor commands that map to the various Abbot XML elements.
 */
public abstract class AbstractAbbotCommand extends AbstractSwingCommand {
// ------------------------------ FIELDS ------------------------------

    public static final String ABBOT_SCRIPT_KEY = "AbbotScript";
    public static final String ABBOT_STEP_RUNNER_KEY = "AbbotStepRunner";

// -------------------------- OTHER METHODS --------------------------

    /**
     * Execute Abbot command.
     *
     * @throws Exception
     */
    public void execute() throws Exception {
        try {
            getStepRunner().run(createStep(getAbbotScript(), buildAbbotXml(getTemplateXML())));
        } catch (Throwable throwable) {
            throw new RuntimeException("Abbot Run Exception: ", throwable);
        }
    }

    protected Step createStep(Script script, String str) throws InvalidScriptException, IOException {
        return Step.createStep(script, str);
    }

    Script getAbbotScript() {
        if (getScript().getContext().get(ABBOT_SCRIPT_KEY) == null) {
            TestHierarchy testHierarchy = new TestHierarchy(true);
            Script abbotScript = new Script(testHierarchy);
            AWTHierarchy.setDefault(testHierarchy);
            abbotScript.setAWTMode(false);
            System.setProperty("abbot.robot.verify", "false");
            getScript().getContext().put(ABBOT_SCRIPT_KEY, abbotScript);
        }
        return (Script) getScript().getContext().get(ABBOT_SCRIPT_KEY);
    }

    protected String buildAbbotXml(String abbotXML) throws Exception {
        return createAbbotXML(abbotXML, getReplacementDefinitions());
    }

    protected String createAbbotXML(String abbotXML, ReplacementDefinition[] replacementDefinitions) throws Exception {
        Element element = Util.createElement(abbotXML);
        for (int i = 0; i < replacementDefinitions.length; i++) {
            ReplacementDefinition replacementDefinition = replacementDefinitions[i];
            String paramValue = getParameter(replacementDefinition.exactorParameterPosition, replacementDefinition
                    .defaultParameterValue).stringValue();
            doReplacementIfPresent(element, replacementDefinition.xmlAttribute, paramValue);
        }
        return toString(element);
    }

    protected void doReplacementIfPresent(Element abbotXML, String attribName, String attribValue) {
        if (attribValue.length() != 0) {
            abbotXML.setAttribute(attribName, attribValue);
        } else {
            abbotXML.removeAttribute(attribName);
        }
    }

    private String toString(Element element) throws IOException {
        XMLOutputter xmlOutputter = new XMLOutputter();
        StringWriter stringWriter = new StringWriter();
        xmlOutputter.output(element, stringWriter);
        return stringWriter.toString();
    }

    protected abstract ReplacementDefinition[] getReplacementDefinitions();

    protected abstract String getTemplateXML();

    protected void findAndLoadComponent() {
        try {
            Component component = findComponent(extractComponentName());
            if (component != null) {
                ComponentReference componentReference = new ComponentReference(getAbbotScript(), component);
                getAbbotScript().addComponentReference(componentReference);
            }
        } catch (RuntimeException e) {
            // we ignore, because Abbot might be able to loadTesters it on its own from
            // added components
        }
    }

    private String extractComponentName() {
        return getParameter(2).stringValue().split(",")[0];
    }

    StepRunner getStepRunner() {
        if (getScript().getContext().get(ABBOT_STEP_RUNNER_KEY) == null) {
            getScript().getContext().put(ABBOT_STEP_RUNNER_KEY, new StepRunner(AWTHierarchy.getDefault()));
        }
        return (StepRunner) getScript().getContext().get(ABBOT_STEP_RUNNER_KEY);
    }


    protected Container[] getContainers() {
        Script abbotScript = (Script) getScript().getContext().get(ABBOT_SCRIPT_KEY);
        Collection roots = abbotScript.getHierarchy().getRoots();
        return (Container[]) roots.toArray(new Container[]{});
    }
    // -------------------------- INNER CLASSES --------------------------

    class ReplacementDefinition {
        int exactorParameterPosition = -1;
        String defaultParameterValue = "";
        String xmlAttribute = "";

        public ReplacementDefinition(int exactorParameterPosition, String defaultParameterValue, String xmlAttribute) {
            this.exactorParameterPosition = exactorParameterPosition;
            this.defaultParameterValue = defaultParameterValue;
            this.xmlAttribute = xmlAttribute;
        }
    }
}