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

import com.exoftware.exactor.Runner;
import com.exoftware.exactor.command.selenium.webdriver.SeleniumWebDriverRunner;
import com.exoftware.exactor.listener.HtmlOutputListener;
import com.exoftware.exactor.listener.SimpleListener;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;

import static org.junit.Assert.fail;

/**
 * @author Michael Lieshoff
 */
public class SimpleWebDriverTest extends GeneralConfig {

    @Test
    public void shouldRun() throws Exception {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("*** TEST IGNORED BECAUSE NO UI AVAILABLE!!!");
            return;
        }
        try {
            Runner runner = new SeleniumWebDriverRunner(getTest("simple-test.act"), new FirefoxDriver());
            runner.addListener(new SimpleListener());
            runner.addListener(new HtmlOutputListener(new FileWriter(new File(TEMP_DIRECTORY, DEFAULT_HTML)),
                    new FileWriter(new File(TEMP_DIRECTORY, DEFAULT_STYLE_SHEET)),
                    runner.getBaseDir()
            ));
            runner.run();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
