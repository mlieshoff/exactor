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
package com.exoftware.exactor.command.web;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Type;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Javadoc doclet to generate web commands.
 *
 * @author Brian Swan
 */
class GenerateCommandsDoclet {
    private static final String FS = File.separator;
    private static final String SOURCE_DIR = System.getProperty("user.dir") + FS + "src" + FS + "java" + FS + "com"
            + FS + "exoftware" + FS + "exactor" + FS + "command" + FS + "web" + FS;
    private static final String JAVA_TEMPLATE = "package com.exoftware.exactor.command.web;\n" +
            "\n" +
            "/**\n" +
            " * Command wrapping jwebunit <code>WebTester.{0}( {1} }</code>.\n" +
            " *\n" +
            " * Usage example;\n" +
            " * <pre>\n" +
            " *  <code>\n" +
            " *      {2} {3}\n" +
            " *  </code>\n" +
            " * </pre>\n" +
            " *\n" +
            " * @author Brian Swan\n" +
            " */\n" +
            "public class {2} extends WebCommand\n" +
            "'{'\n" +
            "    /**\n" +
            "     * Execute the command. {4}\n" +
            "     *\n" +
            "     * @throws Exception is an error occurs.\n" +
            "     */\n" +
            "    public void execute() throws Exception\n" +
            "    '{'\n" +
            "        getWebTester().{0}( {5} );\n" +
            "    '}'\n" +
            "'}'";

    public static boolean start(RootDoc root) {
        ClassDoc[] classes = root.classes();
        if (classes.length == 1 && classes[0].name().equals("WebTester")) {
            MethodDoc[] methods = classes[0].methods();
            for (int i = 0; i < methods.length; i++) {
                String methodName = methods[i].name();
                String parameterTypes = getParameterTypes(methods[i].parameters());
                String className = toClassName(methodName);
                String parameterNames = getParameterNames(methods[i].parameters(), " ");
                String docCommenr = getDocComment(methods[i].commentText(), methods[i].parameters());
                String parameterCalls = getParameterCalls(methods[i].parameters());
                String[] replacements = new String[]{methodName, parameterTypes, className, parameterNames, docCommenr,
                        parameterCalls};
                if (methods[i].isPublic() && parameterCalls != null) {
                    createJavaFile(replacements);
                }
            }
        }
        return true;
    }

    private static void createJavaFile(String[] replacements) {
        try {
            FileWriter writer = new FileWriter(SOURCE_DIR + replacements[2] + ".java");
            writer.write(MessageFormat.format(JAVA_TEMPLATE, replacements));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.err.println("Unable to create java file. " + e.getMessage());
        }
    }

    private static String getParameterCalls(Parameter[] parameters) {
        List calls = new ArrayList();
        for (int i = 0; i < parameters.length; i++) {
            Type type = parameters[i].type();
            if (type.typeName().equals("String") && type.dimension().equals("")) {
                calls.add("getParameter( " + i + " ).stringValue()");
            } else if (type.typeName().equals("int") && type.dimension().equals("")) {
                calls.add("getParameter( " + i + " ).intValue()");
            } else {
                return null; // quick exit if we can't deal with the parameter
            }
        }
        return seperateWithChar(calls, ", ");
    }

    private static String getDocComment(String comment, Parameter[] parameters) {
        StringBuffer result = new StringBuffer(comment);
        if (comment.startsWith("Assert")) {
            result.replace(0, "Assert".length(), "Check");
        }
        result.append("\n");
        result.append("     * ");
        switch (parameters.length) {
            case 1:
                result.append("One parameter is expected, ");
                break;
            case 2:
                result.append("Two parameters are expected, ");
                break;
            case 3:
                result.append("Three parameters are expected, ");
                break;
        }
        result.append(getParameterNames(parameters, ", "));
        result.append(".");
        return result.toString();
    }

    private static String getParameterNames(Parameter[] parameters, String separator) {
        List names = new ArrayList();
        for (int i = 0; i < parameters.length; i++) {
            names.add(parameters[i].name());
        }
        return seperateWithChar(names, separator);
    }

    private static String toClassName(String methodName) {
        if (methodName.startsWith("assert")) {
            return "Check" + methodName.substring("assert".length());
        } else if (methodName.startsWith("check")) {
            return "Select" + methodName.substring("check".length());
        } else if (methodName.startsWith("uncheck")) {
            return "Deselect" + methodName.substring("uncheck".length());
        }
        return methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
    }

    private static String getParameterTypes(Parameter[] parameters) {
        List types = new ArrayList();
        for (int i = 0; i < parameters.length; i++) {
            types.add(parameters[i].type().typeName());
        }
        return seperateWithChar(types, ", ");
    }

    private static String seperateWithChar(List types, String s) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < types.size(); i++) {
            result.append((String) types.get(i));
            if (i != types.size() - 1) {
                result.append(s);
            }
        }
        return result.toString();
    }
}
