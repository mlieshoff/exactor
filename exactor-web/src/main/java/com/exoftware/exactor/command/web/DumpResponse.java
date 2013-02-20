package com.exoftware.exactor.command.web;

/**
 * Command wrapping jwebunit <code>WebTester.dumpResponse(}</code>.
 * <p/>
 * Usage example;
 * <pre>
 *  <code>
 *      DumpResponse
 *  </code>
 * </pre>
 *
 * @author Brian Swan
 */
public class DumpResponse extends WebCommand {
    /**
     * Execute the command. Dump html of current response to System.out - for debugging purposes.
     *
     * @throws Exception is an error occurs.
     */
    public void execute() throws Exception {
        getWebTester().dumpResponse();
    }
}