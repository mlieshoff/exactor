/******************************************************************
 * Copyright (c) 2014, Exoftware
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 *   * Redistributions of source code must retain the above
 *     copyright notice, this list of conditions and the following
 *     disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *   * Neither the name of the Exoftware, Exactor nor the names
 *     of its contributors may be used to endorse or promote
 *     products derived from this software without specific
 *     prior written permission.
 *
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
 *****************************************************************/
package com.exoftware.exactor.command.selenium.webdriver;

import com.exoftware.exactor.command.annotated.AnnotatedCommand;
import com.exoftware.exactor.command.annotated.ParameterDefinition;
import com.exoftware.exactor.command.annotated.ParameterType;
import com.exoftware.exactor.command.annotated.Resolver;
import com.exoftware.exactor.command.annotated.resolver.basic.ClassResolver;
import com.exoftware.exactor.command.annotated.resolver.basic.IntegerResolver;
import com.exoftware.exactor.command.annotated.resolver.basic.StringResolver;
import com.exoftware.exactor.command.annotated.resolver.collection.StringSetResolver;
import com.exoftware.exactor.command.selenium.webdriver.resolver.ByResolver;
import com.exoftware.exactor.command.selenium.webdriver.resolver.NavigationResolver;
import com.exoftware.exactor.command.selenium.webdriver.resolver.OptionsResolver;
import com.exoftware.exactor.command.selenium.webdriver.resolver.TargetLocatorResolver;
import com.exoftware.exactor.command.selenium.webdriver.resolver.WebElementResolver;
import com.exoftware.exactor.command.selenium.webdriver.resolver.WebElementsResolver;
import com.exoftware.exactor.doc.Description;

import java.util.List;

/**
 * @author Michael Lieshoff
 */
public enum SeleniumWebDriverParameters implements ParameterDefinition {

    @Description(text = "Defines a browser start command. Example: browserStartCommand=show")
    BROWSER_START_COMMAND(new StringResolver("browserStartCommand")),

    @Description(text = "Defines a browser url. Example: browserUrl=www.google.de")
    BROWSER_URL(new StringResolver("browserUrl")),

    @Description(text = "Defines a by. Example: by=show")
    BY(new ByResolver()),

    @Description(text = "Defines a current url. Example: currentUrl=www.google.de")
    CURRENT_URL(new StringResolver("currentUrl")),

    @Description(text = "Defines keys. Example: keys=this are some keys")
    KEYS(new StringResolver("keys")),

    @Description(text = "Defines a navigation. Example: navigation=")
    NAVIGATION(new NavigationResolver()),

    @Description(text = "Defines options. Example: options=")
    OPTIONS(new OptionsResolver()),

    @Description(text = "Defines a page source. Example: pageSource=www.google.de")
    PAGE_SOURCE(new StringResolver("pageSource")),

    @Description(text = "Defines a server host. Example: serverHost=www.google.de")
    SERVER_HOST(new StringResolver("serverHost")),

    @Description(text = "Defines a server port. Example: serverHost=www.google.de")
    SERVER_PORT(new IntegerResolver("serverPort")),

    @Description(text = "Defines a target locator. Example: targetLocator=")
    TARGET_LOCATOR(new TargetLocatorResolver()),

    @Description(text = "Defines an url. Example: url=www.google.de")
    URL(new StringResolver("url")),

    @Description(text = "Defines a web element. Example: webElement=")
    WEB_ELEMENT(new WebElementResolver()),

    @Description(text = "Defines web elements. Example: webElements=")
    WEB_ELEMENTS(new WebElementsResolver()),

    @Description(text = "Defines a web driver class. Example: webDriverClass=x.y.z.abc.Foo")
    WEB_DRIVER_CLASS(new ClassResolver("webDriverClass")),

    @Description(text = "Defines a windowHandle. Example: windowHandle=handleMe")
    WINDOW_HANDLE(new StringResolver("windowHandle")),

    @Description(text = "Defines a windowHandles. Example: windowHandles=handleMe,handleYou")
    WINDOW_HANDLES(new StringSetResolver("windowHandles"));

    private Resolver _resolver;

    private SeleniumWebDriverParameters(Resolver resolver) {
        _resolver = resolver;
    }

    @Override
    public List<String> getParameterNames() {
        return _resolver.getParameterNames();
    }

    @Override
    public <T> T resolve(ParameterType parameterType, AnnotatedCommand annotatedCommand) {
        return (T) _resolver.resolve(parameterType, annotatedCommand);
    }

}