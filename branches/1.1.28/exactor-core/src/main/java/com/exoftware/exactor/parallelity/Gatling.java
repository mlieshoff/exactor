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

import com.exoftware.util.Require;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This class manages parallel command executions.
 *
 * @author Michael Lieshoff
 */
class Gatling {
    private static Set<Parallelity> parallels = new HashSet<Parallelity>();

    private Gatling() {
        // avoid construction.
    }

    /**
     * Starts a parallelity.
     *
     * @param parallel the parallelity to start.
     */
    static void start(Parallelity parallel) {
        register(parallel);
        Thread thread = new Thread(parallel);
        thread.start();
    }

    private static void register(Parallelity parallelity) {
        Require.condition(parallelity != null, "parallelity cannot be null!");
        synchronized (parallels) {
            if (parallels.contains(parallelity)) {
                throw new IllegalArgumentException(String.format("%s (%s) is already registered!",
                        parallelity.getName(), parallelity.hashCode()));
            }
            parallels.add(parallelity);
        }
    }

    /**
     * Stops all parallelities.
     */
    static void stop() {
        synchronized (parallels) {
            for (Parallelity parallelity : parallels) {
                parallelity.stop();
            }
        }
    }

    /**
     * Unregisters all stopped parallelities.
     *
     * @return true if all parallelities unregistered, false if some are already running.
     */
    static boolean unregisterAll() {
        synchronized (parallels) {
            for (Iterator<Parallelity> iterator = parallels.iterator(); iterator.hasNext(); ) {
                if (!iterator.next().isRunning()) {
                    iterator.remove();
                }
            }
            return parallels.size() == 0;
        }
    }

    /**
     * Checks if all parallelities are finished.
     *
     * @return true all parallelities are finished, false some are already running.
     */
    static boolean finished() {
        synchronized (parallels) {
            for (Iterator<Parallelity> iterator = parallels.iterator(); iterator.hasNext(); ) {
                Parallelity parallelity = iterator.next();
                if (!parallelity.wasStarted() || parallelity.isRunning()) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Stats the gatlings state to an informative string.
     *
     * @return an informative string about gatlings state.
     */
    static String stats() {
        synchronized (parallels) {
            StringBuilder s = new StringBuilder();
            s.append("GATLING EXECUTION DUMP\n");
            for (Parallelity parallelity : parallels) {
                s.append(String.format("%s -> %s\n", parallelity.getName(), parallelity.getTurns()));
            }
            return s.toString();
        }
    }

}
