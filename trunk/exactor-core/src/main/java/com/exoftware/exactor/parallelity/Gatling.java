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
package com.exoftware.exactor.parallelity;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Michael Lieshoff
 */
public abstract class Gatling {
    private static Map<Integer, Parallelity> parallels = new ConcurrentHashMap<Integer, Parallelity>();
    private static Map<Parallelity, Integer> executions = new ConcurrentHashMap<Parallelity, Integer>();

    public static void start(Parallelity parallel) {
        register(parallel);
        Thread thread = new Thread(parallel);
        thread.start();
    }

    private static void register(Parallelity parallel) {
        parallels.put(parallel.hashCode(), parallel);
        executions.put(parallel, 0);
    }

    public static void count(Parallelity parallel) {
        executions.put(parallel, executions.get(parallel) + 1);
    }

    public static void stop() {
        synchronized(parallels) {
            for (Map.Entry<Integer, Parallelity> entry : parallels.entrySet()) {
                Parallelity parallel = entry.getValue();
                parallel.stop();
            }
        }
    }

    public static boolean unregisterAll() {
        synchronized(parallels) {
            for (Iterator<Map.Entry<Integer, Parallelity>> iterator = parallels.entrySet().iterator(); iterator
                    .hasNext(); ) {
                if (!iterator.next().getValue().isRunning()) {
                    iterator.remove();
                }
            }
            return parallels.size() == 0;
        }
    }

    public static boolean finished() {
        synchronized(parallels) {
            for (Iterator<Map.Entry<Integer, Parallelity>> iterator = parallels.entrySet().iterator(); iterator
                    .hasNext(); ) {
                if (iterator.next().getValue().isRunning()) {
                    return false;
                }
            }
            return true;
        }
    }

    public static void stats() {
        synchronized(executions) {
            System.out.println("GATLING EXECUTION DUMP");
            for (Map.Entry<Parallelity, Integer> entry : executions.entrySet()) {
                System.out.printf("%s -> %s\n", entry.getKey().getName(), entry.getValue());
            }
        }
    }

}
