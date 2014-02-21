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
package com.exoftware.exactor.command.selenium.webdriver.resolver;

import com.exoftware.exactor.command.annotated.AnnotatedCommand;
import com.exoftware.exactor.command.annotated.ParameterType;
import com.exoftware.exactor.command.annotated.resolver.AbstractResolver;
import org.openqa.selenium.By;

/**
 * This class defines a resolver for a by.
 *
 * @author Michael Lieshoff
 */
public class ByResolver extends AbstractResolver<By, AnnotatedCommand> {

    @Override
    public By resolveIntern(AnnotatedCommand command) {
        if (command.hasParameter("by.className")) {
            return By.className(command.getParameterByName("by.className").stringValue());
        } else if (command.hasParameter("by.cssSelector")) {
            return By.cssSelector(command.getParameterByName("by.cssSelector").stringValue());
        } else if (command.hasParameter("by.id")) {
            return By.id(command.getParameterByName("by.id").stringValue());
        } else if (command.hasParameter("by.linkText")) {
            return By.linkText(command.getParameterByName("by.linkText").stringValue());
        } else if (command.hasParameter("by.name")) {
            return By.name(command.getParameterByName("by.name").stringValue());
        } else if (command.hasParameter("by.partialLinkText")) {
            return By.partialLinkText(command.getParameterByName("by.partialLinkText").stringValue());
        } else if (command.hasParameter("by.tagName")) {
            return By.tagName(command.getParameterByName("by.tagName").stringValue());
        } else if (command.hasParameter("by.xpath")) {
            return By.xpath(command.getParameterByName("by.xpath").stringValue());
        }
        return null;
    }

    @Override
    public boolean validate(ParameterType parameterType, AnnotatedCommand command) {
        return true;
    }
}
