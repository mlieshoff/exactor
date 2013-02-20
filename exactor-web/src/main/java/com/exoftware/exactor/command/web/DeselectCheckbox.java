package com.exoftware.exactor.command.web;

/**
 * Command wrapping jwebunit <code>WebTester.uncheckCheckbox( String, String }</code>
 * and <code>WebTester.uncheckCheckbox( String }.
 * <p/>
 * Usage example;
 * <pre>
 *  <code>
 *      DeselectCheckbox checkBoxName [value]
 *  </code>
 * </pre>
 *
 * @author Brian Swan
 */
public class DeselectCheckbox extends WebCommand {
    /**
     * Execute the command.
     * One parameter is expected, checkBoxName. Optionally a value can be supplied.
     *
     * @throws Exception is an error occurs.
     */
    public void execute() throws Exception {
        if (countParameters() == 2) {
            getWebTester().uncheckCheckbox(getParameter(0).stringValue(), getParameter(1).stringValue());
        } else {
            getWebTester().uncheckCheckbox(getParameter(0).stringValue());
        }
    }
}