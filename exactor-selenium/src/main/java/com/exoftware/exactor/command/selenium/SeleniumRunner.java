/**
 * ***************************************************************
 * Copyright (c) 2014, Exoftware
 * All rights reserved.
 * <p/>
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * <p/>
 * * Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer.
 * * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials
 * provided with the distribution.
 * * Neither the name of the Exoftware, Exactor nor the names
 * of its contributors may be used to endorse or promote
 * products derived from this software without specific
 * prior written permission.
 * <p/>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * ***************************************************************
 */
package com.exoftware.exactor.command.selenium;

import com.exoftware.exactor.*;
import com.thoughtworks.selenium.*;
import org.openqa.selenium.server.SeleniumServer;

/**
 * @author Kerry Buckley
 * @author Michael Lieshoff
 */
public class SeleniumRunner extends Runner {

    public static final String SELENIUM_COMMAND_PROCESSOR_KEY = "seleniumCommandProcessor";

    private static CommandProcessor commandProcessor = null;

    private static Selenium selenium = null;
    static SeleniumServer server = null;

    public SeleniumRunner(String host, int port, String fileName, String browser, String baseUrl) throws Exception {
        super(fileName);

        server = new SeleniumServer();
        server.start();

        commandProcessor = new HttpCommandProcessor(host, port, browser, baseUrl);

        addListener(new SeleniumListener());

        selenium = new DefaultSelenium(commandProcessor);
        selenium.start(new BrowserConfigurationOptions().setSingleWindow());
    }

    public void run() {
        try {
            super.run();
        } finally {
            selenium.stop();
            server.stop();
        }
    }

    public static class SeleniumListener implements ExecutionSetListener {

        @Override
        public void executionSetStarted(ExecutionSet es) {
            es.getContext().put(SELENIUM_COMMAND_PROCESSOR_KEY, commandProcessor);
        }

        @Override
        public void executionSetEnded(ExecutionSet es) {

        }

        @Override
        public void scriptStarted(Script s) {

        }

        @Override
        public void scriptEnded(Script s) {

        }

        @Override
        public void commandStarted(Command c) {

        }

        @Override
        public void commandEnded(Command c, Throwable t) {

        }

    }
}



