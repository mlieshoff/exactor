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

import com.exoftware.exactor.command.annotated.AnnotatedCommand;
import com.exoftware.exactor.command.annotated.Param;
import com.exoftware.exactor.command.annotated.ParameterDefinition;
import com.exoftware.util.ClassFinder;

import java.lang.reflect.Field;
import java.util.*;

/**
 * This class defines a doc generator for commands.
 *
 * @author Michael Lieshoff
 */
public class Doccer {
    private static final String CLASSPATH = System.getProperty( "java.class.path" );
    private Map<Class<? extends Enum>, Map<String, Meta>> definitions = new HashMap<Class<? extends Enum>, Map<String,
            Meta>>();

    public Set<Command> doc() throws NoSuchFieldException {
        Set<Command> data = new TreeSet<Command>();
        Set<WrappedClass> commands = filterClasses().get(new WrappedClass(AnnotatedCommand.class));
        for(WrappedClass wrappedClass : commands) {
            Class<?> command = wrappedClass.clazz;
            Set<Meta> metas = new TreeSet<Meta>();
            for (Field field : command.getDeclaredFields()) {
                Param param = field.getAnnotation(Param.class);
                if (param != null) {
                    metas.add(getMeta(param));
                }
            }
            data.add(new Command(command.getSimpleName(), command.getAnnotation(Description.class), metas));
        }
        return data;
    }

    static class Command implements Comparable<Command> {
        String name;
        Description description;
        Set<Meta> metas;

        Command(String name, Description description, Set<Meta> metas) {
            this.name = name;
            this.description = description;
            this.metas = metas;
        }

        @Override
        public int compareTo(Command o) {
            return name.compareTo(o.name);
        }
    }

    static class WrappedClass implements Comparable<WrappedClass> {

        Class<?> clazz;

        WrappedClass(Class<?> clazz) {
            this.clazz = clazz;
        }

        @Override
        public int compareTo(WrappedClass wrappedClass) {
            return clazz.getName().compareTo(wrappedClass.clazz.getName());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            WrappedClass that = (WrappedClass) o;

            if (clazz != that.clazz) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            return clazz.hashCode();
        }
    }

    public static void main(String[] args) {
        try {
            new Doccer().doc();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<WrappedClass, Set<WrappedClass>> filterClasses() {
        Map<WrappedClass, Set<WrappedClass>> result = new TreeMap<WrappedClass, Set<WrappedClass>>();
        Set<String> classnames = ClassFinder.getClassnamesFromPath(CLASSPATH);
        for (String classname : classnames) {
            try {
                Class<?> clazz = Class.forName(classname);
                if (clazz.isEnum()) {
                    for (Class<?> cls : clazz.getInterfaces()) {
                        if (cls == ParameterDefinition.class) {
                            putIn(result, ParameterDefinition.class, clazz);
                            break;
                        }
                    }
                } else {
                    if (isCommand(clazz)) {
                        putIn(result, AnnotatedCommand.class, clazz);
                    }
                }
            } catch (Throwable t) {
                // ignore
            }
        }
        return result;
    }

    private void putIn(Map<WrappedClass, Set<WrappedClass>> result, Class<?> type, Class<?> clazz) {
        WrappedClass wrappedType = new WrappedClass(type);
        Set<WrappedClass> set = result.get(wrappedType);
        if (set == null) {
            set = new TreeSet<WrappedClass>();
            result.put(wrappedType, set);
        }
        set.add(new WrappedClass(clazz));
    }

    private boolean isCommand(Class<?> clazz) {
        if (clazz == null || clazz == Object.class) {
            return false;
        } else if (clazz == AnnotatedCommand.class) {
            return true;
        } else {
            return isCommand(clazz.getSuperclass());
        }
    }

    private Meta getMeta(Param param) throws NoSuchFieldException {
        Map<String, Meta> params = definitions.get(param.namespace());
        if (params == null) {
            params = new HashMap<String, Meta>();
            definitions.put(param.namespace(), params);
        }
        Meta meta = params.get(param.name());
        if (meta == null) {
            Class<? extends Enum> enumClass = param.namespace();
            for (Enum enm : enumClass.getEnumConstants()) {
                if (enm.name().equals(param.name())) {
                    Field enmField = enm.getClass().getField(((Enum) enm).name());
                    ParameterDefinition parameterDefinition = (ParameterDefinition) enm;
                    meta = new Meta(param, parameterDefinition, enmField.getAnnotation(Description.class));
                    params.put(enm.name(), meta);
                }
            }
        }
        return meta;
    }

    static class Meta implements Comparable<Meta> {
        Param param;
        ParameterDefinition parameterDefinition;
        Description description;

        Meta(Param param, ParameterDefinition parameterDefinition, Description description) {
            this.param = param;
            this.parameterDefinition = parameterDefinition;
            this.description = description;
        }

        @Override
        public int compareTo(Meta o) {
            String s1 = param.namespace().getSimpleName() + param.name();
            String s2 = o.param.namespace().getSimpleName() + o.param.name();
            return s1.compareTo(s2);
        }
    }
}
