/******************************************************************
 * Copyright (c) 2013, Exoftware
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
package com.exoftware.exactor.doc;

import com.exoftware.util.FileUtilities;

import java.util.List;
import java.util.Set;

/**
 * @author Michael Lieshoff
 */
public class WikiDoccer {

    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 1) {
            System.out.println("Usage: WikiDoccer <outfile>  [list of included jars]");
            System.out.println("       WikiDoccer myDocs.txt \"project1.jar,examples.jar\"");
            System.exit(1);
        }
        String[] includeJars = new String[]{};
        if (args != null && args.length == 2) {
            includeJars = args[1].split("[,]");
        }
        String out = new WikiDoccer().doc(new Doccer().transform(includeJars));
        FileUtilities.writeToFile(args[0], out, false);
    }

    private String doc(Set<Doccer.Doc> docs) {
        StringBuilder s = new StringBuilder();
        StringBuilder content = new StringBuilder();
        content.append(h1("Acceptance Test Command Overview"));
        for (Doccer.Doc doc : docs) {
            content.append(h2(doc.name));
            content.append(text(doc.description));
            if (doc.metas.size() > 0) {
                content.append(line("| Parameter | Namespace | Setting | Description |"));
                for (Doccer.Meta meta : doc.metas) {
                    content.append(line(String.format("| %s | %s | %s | %s |", getString(meta.parameterDefinition
                            .getParameterNames()), meta.param.namespace().getSimpleName(), meta.param.type().name(),
                            getString(meta.description))));
                }
                content.append(line(""));
            }
        }
        s.append(content);
        return s.toString();
    }

    private String getString(List<String> list) {
        StringBuilder s = new StringBuilder();
        for (int i = 0, n = list.size(); i < n; i++) {
            s.append(list.get(i));
            if (i < n - 1) {
                s.append(", ");
            }
        }
        return s.toString();
    }

    private String h1(String name) {
        return line(String.format("h1. %s", getString(name)));
    }

    private String h2(String name) {
        return line(String.format("h2. %s", getString(name)));
    }

    private String line(String s) {
        return String.format("%s\n", s);
    }

    private String text(Description description) {
        return line(getString(description));
    }

    private String getString(Description description) {
        return description == null ? "" : getString(description.text());
    }

    private String getString(String s) {
        return s == null ? "" : s;
    }

}
