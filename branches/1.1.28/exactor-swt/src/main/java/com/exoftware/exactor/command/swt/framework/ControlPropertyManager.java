/******************************************************************
 * Copyright (c) 2004, Exoftware
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
package com.exoftware.exactor.command.swt.framework;

import org.eclipse.swt.widgets.Widget;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class ControlPropertyManager {
    private Widget control;
    private Method method;
    private Object result;

    protected abstract String getMethodName();

    protected abstract Object[] getObjectParameters();

    public ControlPropertyManager(Widget control) {
        this.control = control;
    }

    private void invokeMethod() throws IllegalAccessException, InvocationTargetException {
        result = getMethod().invoke(getControl(), getObjectParameters());
    }

    void useReflectionToGetOrSetProperty() {
        try {
            createAndInvokeMethod();
        } catch (NoSuchMethodException ignored) {
            // ignore
        } catch (SecurityException e) {
            throw e;
        }
    }

    private void createAndInvokeMethod() throws NoSuchMethodException {
        setMethod(createMethod(getMethodName(), getClassParameters()));
        invokeMethodCatchingException();
    }

    Class[] getClassParameters() {
        return ClassParameterBuilder.buildParameterClasses(getObjectParameters());
    }


    private void invokeMethodCatchingException() {
        try {
            invokeMethod();
        } catch (IllegalAccessException e) {
            // ignore
        } catch (IllegalArgumentException e) {
            // ignore
        } catch (InvocationTargetException e) {
            // ignore
        }
    }

    private Class getControlClass() {
        return control.getClass();
    }

    private Widget getControl() {
        return control;
    }

    private Method createMethod(String methodName, Class[] classParameters) throws NoSuchMethodException {
        return getControlClass().getMethod(methodName, classParameters);
    }

    private void setMethod(Method method) {
        this.method = method;
    }

    private Method getMethod() {
        return method;
    }

    Object getMethodResult() {
        return result;
    }

    String convertBooleanToString(boolean booleanProperty) {
        return booleanProperty ? "true" : "false";
    }

}
